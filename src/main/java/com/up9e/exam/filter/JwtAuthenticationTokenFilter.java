package com.up9e.exam.filter;

import com.up9e.exam.constant.ErrorEnum;
import com.up9e.exam.global.ExamUserDetails;
import com.up9e.exam.global.BusinessException;
import com.up9e.exam.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import java.util.List;

@Log4j2
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    private final String tokenHeader;

    private final String tokenHead;

    private final List<String> whiteList;

    public JwtAuthenticationTokenFilter(UserDetailsService userDetailsService,
                                        @Value("${jwt.tokenHeader}") String tokenHeader,
                                        @Value("${jwt.tokenHead}") String tokenHead,
                                        @Value("${jwt.white}") List<String> whiteList) {
        this.userDetailsService = userDetailsService;
        this.tokenHeader = tokenHeader;
        this.tokenHead = tokenHead;
        this.whiteList = whiteList;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain chain) {
        if (!whiteList.contains(request.getRequestURI())) {
            // 从请求头中获取token
            String authHeader = request.getHeader(this.tokenHeader);
            // token前面的"Bearer "需要截取
            String authToken = authHeader.substring(this.tokenHead.length());
            //验证token,获取token中的email
            Claims claims = JwtUtils.verifyJwt(authToken);
            if (claims == null) {
                throw new BusinessException(ErrorEnum.ERROR_TOKEN);
            }
            String email = claims.get("email", String.class);
            if (JwtUtils.isExpired(authToken)) {
                authToken = JwtUtils.generateToken(email);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                response.setHeader("Authorization", authToken);
            }
            // 校验该token是否过期
            log.info("email: {}", email);
            log.info("token verification succeeded, checking email:{}", email);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                ExamUserDetails userDetails = (ExamUserDetails) this.userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user:{}", email);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

}