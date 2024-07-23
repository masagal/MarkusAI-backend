INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','testUser', false);
INSERT INTO requests (user_id) VALUES (1);
INSERT INTO requests (user_id) VALUES (1);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (2, 2, 5);