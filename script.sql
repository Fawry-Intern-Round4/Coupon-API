DROP TABLE IF EXISTS consumption;
DROP TABLE IF EXISTS coupon;
CREATE TABLE coupon
(
    id               BIGINT                       NOT NULL AUTO_INCREMENT,
    code             VARCHAR(255)                 NOT NULL,
    remaining_usages INT                          NOT NULL,
    expiry_date      DATE                         NOT NULL,
    value            DECIMAL(38, 2)               NOT NULL,
    active           BIT                          NOT NULL,
    type             enum ('FIXED', 'PERCENTAGE') null,
    PRIMARY KEY (id)
);
create table consumption
(
    id     BIGINT                       NOT NULL AUTO_INCREMENT,
    actual_discount  DECIMAL(38, 2)   not null,
    consumption_date TIMESTAMP       not null,
    coupon_id        BIGINT          not null,
    customer_email   VARCHAR(255)    not null,
    order_id         BIGINT          not null,
    PRIMARY KEY (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);