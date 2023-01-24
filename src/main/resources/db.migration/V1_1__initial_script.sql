create table if not exists currency
(
    id             bigint       not null
        constraint currency_pk
            primary key,
    code           varchar(4)   not null,
    abbreviation   varchar(4)   not null,
    name           varchar      not null,
    name_eng       varchar      not null,
    name_bel       varchar      not null,
    quot_name      varchar      not null,
    quot_name_eng  varchar      not null,
    quot_name_bel  varchar,
    name_multi     varchar,
    name_multi_eng varchar,
    name_multi_bel varchar,
    scale          bigint       not null,
    periodicity    integer      not null,
    date_start     timestamp(6) not null,
    date_end       timestamp(6) not null,
    parent_id      bigint       not null
);

alter table currency
    owner to postgres;

create unique index if not exists currency_id_uindex
    on currency (id);

create table if not exists rate
(
    id            bigserial
        constraint rate_pk
            primary key,
    date          date             not null,
    abbreviation  varchar(4)       not null,
    scale         bigint           not null,
    name          varchar,
    official_rate double precision not null,
    cur_id        bigint           not null
);

alter table rate
    owner to postgres;

create unique index if not exists rate_id_uindex
    on rate (id);

create table if not exists users
(
    chat_id       bigint      not null
        constraint users_pk
            primary key,
    first_name    varchar(20),
    last_name     varchar(20),
    user_name     varchar(20) not null,
    registered_at timestamp(6)
);

alter table users
    owner to postgres;

create unique index if not exists users_chat_id_uindex
    on users (chat_id);

