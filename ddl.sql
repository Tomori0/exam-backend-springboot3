create table public.exam_user
(
    id              integer generated always as identity (maxvalue 9999999)
        constraint exam_user_pk
            primary key,
    email           varchar(64)             not null
        constraint exam_user_pk2
            unique,
    nickname        varchar(16)             not null,
    birthday        date                    not null,
    password        varchar(64)             not null,
    avatar          varchar(256),
    last_login_time timestamp,
    create_time     timestamp default now() not null,
    salt            varchar(64)             not null
);

comment on column public.exam_user.id is '用户id';

comment on column public.exam_user.email is '邮箱';

comment on column public.exam_user.nickname is '用户名';

comment on column public.exam_user.birthday is '生日';

comment on column public.exam_user.password is '密码';

comment on column public.exam_user.avatar is '头像';

comment on column public.exam_user.last_login_time is '最后登录时间';

comment on column public.exam_user.create_time is '账号创建时间';

alter table public.exam_user
    owner to postgres;

create table public.exam_category
(
    category_id   integer generated always as identity (maxvalue 999999)
        constraint exam_category_pk
            primary key,
    category_name varchar(64)             not null
        constraint exam_category_pk2
            unique,
    create_user   varchar(64)             not null,
    create_time   timestamp default now() not null,
    update_user   varchar(64)             not null,
    update_time   timestamp default now() not null
);

comment on column public.exam_category.category_id is '类型id';

comment on column public.exam_category.category_name is '类型名';

comment on column public.exam_category.create_user is '创建者';

comment on column public.exam_category.create_time is '创建时间';

comment on column public.exam_category.update_user is '更新者';

comment on column public.exam_category.update_time is '更新时间';

alter table public.exam_category
    owner to postgres;

create table public.exam_info
(
    info_id     varchar(17)                                          not null
        constraint exam_info_pk
            primary key,
    exam_code   varchar(64)                                          not null
        constraint exam_info_pk2
            unique,
    exam_title  varchar(64)                                          not null,
    category_id integer                                              not null,
    create_user varchar(64)                                          not null,
    create_time timestamp default now()                              not null,
    update_user varchar(64)                                          not null,
    update_time timestamp default now()                              not null,
    exam_logo   varchar(128),
    total_score integer   default 0                                  not null,
    expire_time time      default '02:00:00'::time without time zone not null
);

comment on column public.exam_info.info_id is '信息id';

comment on column public.exam_info.exam_code is '考试代码';

comment on column public.exam_info.exam_title is '试题标题';

comment on column public.exam_info.category_id is '分类id';

comment on column public.exam_info.create_user is '创建者';

comment on column public.exam_info.create_time is '创建时间';

comment on column public.exam_info.update_user is '更新者';

comment on column public.exam_info.exam_logo is '试题logo';

comment on column public.exam_info.total_score is '出题总分';

alter table public.exam_info
    owner to postgres;

create table public.exam_tag
(
    tag_id      integer generated always as identity (maxvalue 999999),
    tag_name    varchar(16) not null,
    tag_color   varchar(16) not null,
    create_user varchar(64) not null,
    create_time timestamp   not null
);

comment on column public.exam_tag.tag_id is '标签id';

comment on column public.exam_tag.tag_name is '标签名';

comment on column public.exam_tag.tag_color is '标签颜色';

comment on column public.exam_tag.create_user is '创建者';

comment on column public.exam_tag.create_time is '创建时间';

alter table public.exam_tag
    owner to postgres;

create table public.exam_tag_info_relation
(
    realtion_id integer not null,
    tag_id      integer not null,
    info_id     integer not null
);

comment on column public.exam_tag_info_relation.realtion_id is '关系id';

comment on column public.exam_tag_info_relation.tag_id is '标签id';

alter table public.exam_tag_info_relation
    owner to postgres;

create table public.exam_question
(
    question_id     varchar(17)             not null
        constraint exam_question_pk
            primary key,
    info_id         varchar(17)             not null,
    question_head   text                    not null,
    question_img    varchar(128),
    question_type   integer                 not null,
    question_body   text,
    question_answer text                    not null,
    question_score  integer   default 0     not null,
    create_user     varchar(64)             not null,
    create_time     timestamp default now() not null,
    update_user     varchar(64)             not null,
    update_time     timestamp default now() not null
);

comment on column public.exam_question.question_id is '试题id';

comment on column public.exam_question.info_id is '信息id';

comment on column public.exam_question.question_head is '试题问题';

comment on column public.exam_question.question_img is '试题图片';

comment on column public.exam_question.question_type is '试题类型';

comment on column public.exam_question.question_body is '试题体';

comment on column public.exam_question.question_answer is '试题答案';

comment on column public.exam_question.question_score is '试题分值';

comment on column public.exam_question.create_user is '创建者';

comment on column public.exam_question.create_time is '创建时间';

comment on column public.exam_question.update_user is '更新着';

comment on column public.exam_question.update_time is '更新时间';

alter table public.exam_question
    owner to postgres;

create index exam_question_info_id_index
    on public.exam_question (info_id);

create table public.ai_chat_info
(
    chat_id     varchar(17)             not null
        constraint ai_chat_info_pk
            primary key,
    email       varchar(64)             not null,
    create_user varchar(64)             not null,
    create_time timestamp default now() not null,
    update_user varchar(64)             not null,
    update_time timestamp               not null,
    summary     varchar(4000)           not null
);

comment on column public.ai_chat_info.chat_id is 'ai聊天id';

comment on column public.ai_chat_info.email is '用户邮箱';

comment on column public.ai_chat_info.create_user is '创建者';

comment on column public.ai_chat_info.create_time is '创建时间';

comment on column public.ai_chat_info.update_user is '更新着';

comment on column public.ai_chat_info.update_time is '更新时间';

alter table public.ai_chat_info
    owner to postgres;

create index ai_chat_info_email_index
    on public.ai_chat_info (email);

create table public.ai_chat_history
(
    history_id  varchar(17)             not null
        constraint ai_chat_history_pk
            primary key,
    chat_id     varchar(17)             not null,
    role        varchar(10)             not null,
    content     text                    not null,
    create_user varchar(64)             not null,
    create_time timestamp default now() not null
);

comment on table public.ai_chat_history is 'ai聊天记录';

comment on column public.ai_chat_history.history_id is '历史id';

comment on column public.ai_chat_history.chat_id is '聊天ai';

comment on column public.ai_chat_history.role is '角色';

comment on column public.ai_chat_history.content is '内容';

comment on column public.ai_chat_history.create_user is '创建者';

comment on column public.ai_chat_history.create_time is '创建时间';

alter table public.ai_chat_history
    owner to postgres;

create index ai_chat_history_chat_id_index
    on public.ai_chat_history (chat_id);

