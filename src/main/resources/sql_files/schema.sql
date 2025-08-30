DROP TABLE IF EXISTS user_accounts;

CREATE TABLE user_accounts(
    user_id VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (user_id)
);