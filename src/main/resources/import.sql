DELETE FROM clearing_costs;
DELETE FROM users;

-- ===========================
-- INSERT Clearing Costs
-- ===========================
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4001, 'US', 5);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4002, 'GR', 15);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4004, 'DK', 12);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4005, 'IT', 7);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4006, 'AE', 9);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4007, 'IR', 6);
INSERT INTO clearing_costs(id, country_code, cost) VALUES (4003, 'OTHERS', 10);

-- ===========================
-- INSERT ADMIN Users
-- ===========================
INSERT INTO Users (role, username, password) VALUES ( 'ADMIN', 'admin1', 'adminPass');

-- ===========================
-- INSERT NON-ADMIN Users
-- ===========================
INSERT INTO users (role, username, password) VALUES ('USER', 'user1', 'userPass1');
INSERT INTO users (role, username, password) VALUES ('USER', 'user2', 'userPass2');