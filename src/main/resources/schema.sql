DROP TABLE IF EXISTS user_accounts;

CREATE TABLE user_accounts (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    balance  DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (username)
);
