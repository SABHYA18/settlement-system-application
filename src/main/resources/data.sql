-- Disable foreign key checks temporarily to ensure clean insertion
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM wallets;
DELETE FROM users;

-- 1. Insert into user_accounts (Matches @Table(name = "user_accounts"))
INSERT INTO users (username, password, name, phone_number)
VALUES
('Broda@18', '$2a$10$R/x.4g.c5z3.uW.zY8.e..sS7/1/4F.b.3Y.d.9Q.a.w.c', 'Broda', '9876543210'),
('Raven@11', '$2a$10$R/x.4g.c5z3.uW.zY8.e..sS7/1/4F.b.3Y.d.9Q.a.w.c', 'Raven', '9876543211'),
('Psyn@07', '$2a$10$R/x.4g.c5z3.uW.zY8.e..sS7/1/4F.b.3Y.d.9Q.a.w.c', 'Psychonoice', '9876543212'),
('Ut@20', '$2a$10$R/x.4g.c5z3.uW.zY8.e..sS7/1/4F.b.3Y.d.9Q.a.w.c', 'Ut', '9876543213'),
('Vortex@08', '$2a$10$R/x.4g.c5z3.uW.zY8.e..sS7/1/4F.b.3Y.d.9Q.a.w.c', 'Vortex', '9876012345');

-- 2. Insert into wallets (Matches @Table(name = "wallets"))
-- user_username must match the username values above
INSERT INTO wallets (user_username, balance, last_updated_at)
VALUES
('Broda@18', 5000.00, NOW()),
('Raven@11', 4500.00, NOW()),
('Psyn@07', 4800.00, NOW()),
('Ut@20', 4000.00, NOW()),
('Vortex@08', 3500.00, NOW());

SET FOREIGN_KEY_CHECKS = 1;