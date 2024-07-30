-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

INSERT INTO products (id, name) VALUES (default, 'Blue whiteboard markers'),
                                        (default, 'Green whiteboard markers'),
                                        (default, 'Orange whiteboard markers'),
                                        (default, 'Red whiteboard markers'),
                                        (default, 'Oatly litres'),
                                        (default, 'Low-fat milk'),
                                        (default, 'SaltBot MacBook Air'),
                                        (default, 'Split-palm mechanical keyboard'),
                                        (default, 'Split-palm mechanical keyboard');

INSERT INTO inventory_items (id, product_id, quantity, location) VALUES ('1', 1, 4, 'supply closet'),
                                                         ('2', 2, 6, 'supply closet'),
                                                         ('3', 3, 6, 'meeting room oregano'),
                                                         ('4', 4, 6, 'meeting room pepper'),
                                                         ('5', 5, 5, 'core team fridge'),
                                                         ('6', 6, 0, 'developer fridge'),
                                                         ('7', 7, 1, 'alek put it somewhere'),
                                                         ('8', 8, 1, 'markus has one'),
                                                         ('9', 9, 1, 'in the JFS room');

INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','testUser', false);
INSERT INTO users (email, name, is_admin, clerk_id) VALUES ('admin@tolpuddle.tech', 'simon admin', true, 'user_2jbOj40LQGG8Y8RhHMgGO763eRq');
INSERT INTO users (email, name, is_admin, clerk_id) VALUES ('regular@tolpuddle.tech', 'simon regular', false, 'user_2jhcwIX4Gn6eY7PU7jBhaJcbOy6');
INSERT INTO users (email, name, is_admin, clerk_id) VALUES ('cypress+clerk_test@tolpuddle.tech', 'cypress admin', true, 'user_2jjMkCNC2oaUopPuZPwhtBlqdOA');
INSERT INTO users (email, name, is_admin, clerk_id) VALUES ('mathi.admin@gmail.com', 'Math math', true, 'user_2jm6AYThCDfPqMlHBO8jhwyPLMI');
INSERT INTO users (email, name, is_admin, clerk_id) VALUES ('alwaysthere@gmail.com', 'NonAdmin math', false, 'user_2jjIzGq8PO6mkRaF8aVabp4OVXR');

INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (3, false);
INSERT INTO requests (user_id, is_approved) VALUES (3, true);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (2, 2, 5);
INSERT INTO orders (user_id, status, approved_date, request_id) VALUES (5, 'PENDING', '2024-07-26T13:00:00', 2);