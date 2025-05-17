CREATE SEQUENCE user_id_seq;

CREATE TABLE users
(
    id            BIGINT PRIMARY KEY    DEFAULT nextval('user_id_seq'),
    name          VARCHAR(500) NOT NULL,
    date_of_birth DATE         NOT NULL,
    password      VARCHAR(500) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    enabled       BOOLEAN      NOT NULL DEFAULT true
);

CREATE INDEX idx_user_name ON users (name);
CREATE INDEX idx_user_date_of_birth ON users (date_of_birth);


CREATE SEQUENCE email_data_id_seq;

CREATE TABLE email_data
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('email_data_id_seq'),
    user_id BIGINT       NOT NULL REFERENCES users (id),
    email   VARCHAR(200) NOT NULL UNIQUE
);



CREATE SEQUENCE phone_data_id_seq;

CREATE TABLE phone_data
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('phone_data_id_seq'),
    user_id BIGINT      NOT NULL REFERENCES users (id),
    phone   VARCHAR(13) NOT NULL UNIQUE
);

CREATE SEQUENCE account_id_seq;

CREATE TABLE account
(
    id                    BIGINT PRIMARY KEY DEFAULT nextval('account_id_seq'),
    user_id               BIGINT         NOT NULL REFERENCES users (id) UNIQUE,
    balance               NUMERIC(19, 2) NOT NULL CHECK (balance >= 0),
    initial_balance_limit NUMERIC(19, 2) NOT NULL CHECK (initial_balance_limit >= 0)
);