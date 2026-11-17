create table orders (
    id          uuid primary key,
    customer_id varchar(64)  not null,
    currency    varchar(3)   not null,
    total_cents bigint       not null,
    status      varchar(16)  not null,
    created_at  timestamp with time zone not null
);

create index idx_orders_customer on orders (customer_id, created_at desc);
