create table cuenta(
    id UUID primary key ,
    owner_id varchar not null,
    saldo numeric(19,2) default 0 not null check (saldo>=0),
    moneda varchar(3) not null,
    created_at TIMESTAMPTZ not null default now()
);