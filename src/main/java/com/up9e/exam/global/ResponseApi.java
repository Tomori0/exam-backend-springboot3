package com.up9e.exam.global;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApi<T> {
    private Integer status;
    private String statusText;
    private T data;

    public ResponseApi(Integer status, T data, String statusText) {
        this.status = status;
        this.data = data;
        this.statusText = statusText;
    }

    public ResponseApi(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseApi(T data) {
        this.status = 200;
        this.data = data;
    }

    public ResponseApi(Integer status, String statusText) {
        this.status = status;
        this.statusText = statusText;
    }
}
