-- Drop tables in reverse order of dependency to avoid foreign key errors
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS token_blocklist;
DROP TABLE IF EXISTS wallets;
DROP TABLE IF EXISTS user_accounts;

-- Create the user_accounts table for identity and profile information
CREATE TABLE user_accounts (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    last_login_timestamp DATETIME NULL,
    PRIMARY KEY (username)
);

CREATE TABLE users (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    last_login_timestamp DATETIME NULL,
    PRIMARY KEY (username)
);

-- Create the wallets table for financial data
CREATE TABLE wallets (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_username VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL,
    last_updated_at DATETIME NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_username) REFERENCES users(username)
);

-- Create the transactions table to log all financial events
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT NOT NULL,
    wallet_id BIGINT NOT NULL,
    type VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    timestamp DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);

-- Create the token blocklist table to store invalidated JWTs
CREATE TABLE token_blocklist (
    id BIGINT AUTO_INCREMENT NOT NULL,
    token VARCHAR(512) NOT NULL UNIQUE,
    expiry_date DATETIME NOT NULL,
    PRIMARY KEY (id)
);

