-- Insert products
INSERT INTO products (name) VALUES ( 'Blue whiteboard markers'),
                                       ('Green whiteboard markers'),
                                       ('Orange whiteboard markers'),
                                       ('Red whiteboard markers'),
                                       ('Oatly litres'),
                                       ('Black whiteboard markers'),
                                       ('Yellow whiteboard markers'),
                                       ('Purple whiteboard markers'),
                                       ('Erasers'),
                                       ('Whiteboard cleaner');

-- Insert inventory items
INSERT INTO inventory_items (id, product_id, quantity) VALUES ('1', 1, 4),
                                                              ('2', 2, 6),
                                                              ('3', 3, 6),
                                                              ('4', 4, 6),
                                                              ('5', 5, 5),
                                                              ('6', 6, 8),
                                                              ('7', 7, 7),
                                                              ('8', 8, 10),
                                                              ('9', 9, 15),
                                                              ('10', 10, 12);

-- Insert users
INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','Adam Adamsson', 'false'),
                                                 ('admin@admin.com', 'Erik Eriksson', 'true'),
                                                 ('user1@domain.com', 'Ludvig Ludvigsson', 'false'),
                                                 ('user2@domain.com', 'Mathangi Mathangisson', 'false'),
                                                 ('user3@domain.com', 'Simon Simonsson', 'false'),
                                                 ('user4@domain.com', 'Alek Aleksson', 'false'),
                                                 ('user5@domain.com', 'Supipi Supipisson', 'false'),
                                                 ('user6@domain.com', 'Nelson Nelsonsson', 'false'),
                                                 ('user7@domain.com', 'Ivan Ivansson', 'false'),
                                                 ('user8@domain.com', 'Harald Haraldsson', 'true');

-- Insert requests
INSERT INTO requests (user_id, is_approved) VALUES (1, 'false'),
                                                   (1, 'false'),
                                                   (2, 'true'),
                                                   (3, 'false'),
                                                   (4, 'true'),
                                                   (5, 'false'),
                                                   (6, 'true'),
                                                   (7, 'false'),
                                                   (8, 'true'),
                                                   (9, 'false');

-- Insert request products
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10),
                                                                       (2, 2, 5),
                                                                       (3, 3, 8),
                                                                       (4, 4, 10),
                                                                       (5, 5, 7),
                                                                       (6, 6, 12),
                                                                       (7, 7, 9),
                                                                       (8, 8, 11),
                                                                       (9, 9, 4),
                                                                       (10, 10, 6);

-- Insert more products
INSERT INTO products (name) VALUES ('Whiteboard erasers'),
                                       ('Highlighters'),
                                       ('Permanent markers'),
                                       ('Notebook A4'),
                                       ('Notebook A5'),
                                       ('Printer paper'),
                                       ('Sticky notes'),
                                       ('Paper clips'),
                                       ('Stapler'),
                                       ('Staples');

-- Insert more inventory items
INSERT INTO inventory_items (id, product_id, quantity) VALUES ('11', 11, 20),
                                                              ('12', 12, 15),
                                                              ('13', 13, 30),
                                                              ('14', 14, 25),
                                                              ('15', 15, 22),
                                                              ('16', 16, 50),
                                                              ('17', 17, 40),
                                                              ('18', 18, 35),
                                                              ('19', 19, 10),
                                                              ('20', 20, 5);

-- Insert more users
INSERT INTO users (email, name, is_admin) VALUES ('user9@domain.com', 'User Nine', false),
                                                 ('user10@domain.com', 'User Ten', false),
                                                 ('user11@domain.com', 'User Eleven', false),
                                                 ('user12@domain.com', 'User Twelve', false),
                                                 ('user13@domain.com', 'User Thirteen', false),
                                                 ('user14@domain.com', 'User Fourteen', false),
                                                 ('user15@domain.com', 'User Fifteen', false),
                                                 ('user16@domain.com', 'User Sixteen', false),
                                                 ('user17@domain.com', 'User Seventeen', false),
                                                 ('user18@domain.com', 'User Eighteen', false);

-- Insert more requests
INSERT INTO requests (user_id, is_approved) VALUES (10, true),
                                                   (11, false),
                                                   (12, true),
                                                   (13, false),
                                                   (14, true),
                                                   (15, false),
                                                   (16, true),
                                                   (17, false),
                                                   (18, true),
                                                   (19, false);

-- Insert more request products
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 11, 10),
                                                                       (2, 12, 5),
                                                                       (3, 13, 8),
                                                                       (4, 14, 10),
                                                                       (5, 15, 7),
                                                                       (6, 16, 12),
                                                                       (7, 17, 9),
                                                                       (8, 18, 11),
                                                                       (9, 19, 4),
                                                                       (10, 20, 6);
