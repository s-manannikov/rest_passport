create table passport(
    id serial primary key,
    series varchar(10),
    number varchar(10),
    expirydate date
);