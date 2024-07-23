-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

INSERT INTO products (id, name) VALUES (default, 'Blue whiteboard markers'),
                                        (default, 'Green whiteboard markers'),
                                        (default, 'Orange whiteboard markers'),
                                        (default, 'Red whiteboard markers'),
                                        (default, 'Oatly litres');

INSERT INTO inventory_items (id, product_id, quantity) VALUES ('1', 1, 4),
                                                         ('2', 2, 6),
                                                         ('3', 3, 6),
                                                         ('4', 4, 6),
                                                         ('5', 5, 5);

INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','testUser', false);
INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (2, 2, 5);