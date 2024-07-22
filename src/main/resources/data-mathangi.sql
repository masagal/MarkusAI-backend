INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','testUser', false);
INSERT INTO request (user_id) VALUES (1);
INSERT INTO request (user_id) VALUES (1);
INSERT INTO request_product (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_product (product_id, request_id, quantity) VALUES (2, 2, 5);