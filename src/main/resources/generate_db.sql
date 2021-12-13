CREATE DATABASE kp_db

create table users
(
    id       integer default nextval('users_id_seq'::regclass) not null
        constraint "USERS_pkey"
            primary key,
    login    varchar                                           not null,
    password integer                                           not null,
    name     varchar                                           not null,
    surname  varchar                                           not null,
    type     varchar                                           not null,
    birthday date                                              not null,
    outlet   integer
        constraint users_outlets_id_fk
            references outlets
);

alter table users
    owner to postgres;

create unique index users_login_uindex
    on users (login);

create table countries
(
    id   serial
        constraint countries_pk
            primary key,
    name varchar not null
);

alter table countries
    owner to postgres;

create table actions
(
    id                serial
        constraint actions_pk
            primary key,
    user_id           integer
        constraint actions_users_id_fk
            references users
            on update cascade on delete cascade,
    descriptionaction varchar,
    time              timestamp
);

alter table actions
    owner to postgres;

create unique index actions_id_uindex
    on actions (id);

create table techmessages
(
    id           serial
        constraint techmessages_pk
            primary key,
    questiondate timestamp,
    user_id      integer
        constraint techmessages_users_id_fk
            references users
            on update cascade on delete cascade,
    question     varchar,
    answerdate   timestamp,
    admin_id     integer
        constraint techmessages_users_id_fk_2
            references users
            on update cascade on delete cascade,
    answer       varchar
);

alter table techmessages
    owner to postgres;

create unique index techmessages_id_uindex
    on techmessages (id);

create table sales
(
    id         serial
        constraint sales_pk
            primary key,
    product_id integer
        constraint sales_products_id_fk
            references products
            on update cascade on delete cascade,
    percent    integer,
    outlet_id  integer
        constraint sales_outlets_id_fk
            references outlets
            on update cascade on delete cascade
);

alter table sales
    owner to postgres;

create unique index sales_id_uindex
    on sales (id);

create table purchases
(
    id        serial
        constraint purchases_pk
            primary key,
    time      date,
    outlet_id integer
        constraint purchases_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    user_id   integer
        constraint purchases_users_id_fk
            references users
            on update cascade on delete cascade
);

alter table purchases
    owner to postgres;

create unique index purchases_id_uindex
    on purchases (id);

create table providers
(
    id           serial
        constraint providers_pk
            primary key,
    name         varchar not null,
    address      varchar not null,
    country      integer not null,
    deliverydays integer
);

alter table providers
    owner to postgres;

create table "Purchase_products"
(
    "Purchase_id" bigint not null
        constraint purchase_products_purchases_id_fk
            references purchases
            on update cascade on delete cascade,
    products_key  bigint not null
        constraint purchase_products_products_id_fk
            references products
            on update cascade on delete cascade,
    products      integer,
    constraint purchase_products_pk
        primary key ("Purchase_id", products_key)
);

alter table "Purchase_products"
    owner to postgres;

create table producttypes
(
    id   integer default nextval('"productTypes_id_seq"'::regclass) not null
        constraint producttypes_pk
            primary key,
    name varchar                                                    not null
);

alter table producttypes
    owner to postgres;

create table products
(
    id                  serial
        constraint products_pk
            primary key,
    name                varchar          not null,
    type                integer          not null,
    purchaseprice       double precision not null,
    sellingprice        double precision not null,
    manufacturedate     date             not null,
    manufacturercountry integer          not null,
    shelflife           date             not null,
    provider_id         integer
        constraint products_providers_id_fk
            references providers
            on update cascade on delete cascade
);

alter table products
    owner to postgres;

create table "productRequests"
(
    id         serial
        constraint productrequests_pk
            primary key,
    starttime  date not null,
    accepttime date,
    outlet_id  integer
        constraint productrequests_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    user_id    integer
        constraint productrequests_users_id_fk
            references users
            on update cascade on delete cascade
);

alter table "productRequests"
    owner to postgres;

create unique index productrequests_id_uindex
    on "productRequests" (id);

create table "ProductRequest_productMap"
(
    "ProductRequest_id" bigint not null
        constraint productrequest_productmap_productrequests_id_fk
            references "productRequests"
            on update cascade on delete cascade,
    productmap_key      bigint not null
        constraint productrequest_productmap_products_id_fk
            references products
            on update cascade on delete cascade,
    productmap          bigint,
    constraint productrequest_productmap_pk
        primary key ("ProductRequest_id", productmap_key)
);

alter table "ProductRequest_productMap"
    owner to postgres;

create table "ProductProvider_productMap"
(
    "ProductProvider_id" bigint not null
        constraint providers_productmap_providers_id_fk
            references providers
            on update cascade on delete cascade,
    productmap_key       bigint not null
        constraint providers_productmap_products_id_fk
            references products
            on update cascade on delete cascade,
    productmap           integer,
    constraint providers_productmap_pkey
        primary key ("ProductProvider_id", productmap_key)
);

alter table "ProductProvider_productMap"
    owner to postgres;

create table outlets
(
    id      serial
        constraint outlets_pk
            primary key,
    name    varchar not null,
    address varchar not null,
    country integer not null
);

alter table outlets
    owner to postgres;

create table "Outlet_currentAssortment"
(
    "Outlet_id"           bigint not null
        constraint outlet_currentassortment_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    currentassortment_key bigint not null
        constraint outlet_currentassortment_products_id_fk
            references products
            on update cascade on delete cascade,
    currentassortment     integer,
    constraint outlet_currentassortment_pk
        primary key ("Outlet_id", currentassortment_key)
);

alter table "Outlet_currentAssortment"
    owner to postgres;

create table "Outlet_currentStorage"
(
    "Outlet_id"        bigint not null
        constraint outlet_currentstorage_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    currentstorage_key bigint not null
        constraint outlet_currentstorage_products_id_fk
            references products
            on update cascade on delete cascade,
    currentstorage     integer,
    constraint outlet_currentstorage_pk
        primary key ("Outlet_id", currentstorage_key)
);

alter table "Outlet_currentStorage"
    owner to postgres;

create table "Outlet_standardAssortment"
(
    "Outlet_id"            bigint not null
        constraint outlet_standardassortment_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    standardassortment_key bigint not null
        constraint outlet_standardassortment_products_id_fk
            references products
            on update cascade on delete cascade,
    standardassortment     integer,
    constraint outlet_standardassortment_pk
        primary key ("Outlet_id", standardassortment_key)
);

alter table "Outlet_standardAssortment"
    owner to postgres;

create table "Outlet_standardStorage"
(
    "Outlet_id"         bigint not null
        constraint outlet_standardstorage_outlets_id_fk
            references outlets
            on update cascade on delete cascade,
    standardstorage_key bigint not null
        constraint outlet_standardstorage_products_id_fk
            references products
            on update cascade on delete cascade,
    standardstorage     integer,
    constraint outlet_standardstorage_pk
        primary key ("Outlet_id", standardstorage_key)
);

alter table "Outlet_standardStorage"
    owner to postgres;

