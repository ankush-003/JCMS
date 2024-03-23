create table account
(
    id      int            not null primary key default unique_rowid(),
    balance numeric(19, 2) not null,
    name    varchar(128)   not null,
    type    varchar(25)    not null
);

CREATE TABLE user
(
    id      int               NOT NULL primary key default unique_rowid(),
    name    varchar(128)      NOT NULL,
    email   varchar(128)      UNIQUE NOT NULL
);