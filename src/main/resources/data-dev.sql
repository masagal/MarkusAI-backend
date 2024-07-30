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
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('admin@tolpuddle.tech', 'Stinker McGee', true, 'user_2jbOj40LQGG8Y8RhHMgGO763eRq', 'https://files.worldwildlife.org/wwfcmsprod/images/HERO_harbor_seal_on_ice/hero_small/41yzw17euy_Harbor_Seal_on_Ice_close_0357_6_11_07.jpg');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('regular@tolpuddle.tech', 'Simon Kendall', false, 'user_2jhcwIX4Gn6eY7PU7jBhaJcbOy6', 'https://ca.slack-edge.com/TA01UCHBN-U06SQBABJ85-d1786941fd20-512');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('cypress+clerk_test@tolpuddle.tech', 'cypress admin', true, 'user_2jjMkCNC2oaUopPuZPwhtBlqdOA', 'https://akns-images.eonline.com/eol_images/Entire_Site/2020107/rs_1200x1200-201107114351-1200-kamala-harris.cm.11720.jpg?fit=around%7C1080:1080&output-quality=90&crop=1080:1080;center,top');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('mathi.admin@gmail.com', 'Math math', true, 'user_2jm6AYThCDfPqMlHBO8jhwyPLMI', 'https://ca.slack-edge.com/TA01UCHBN-U06TG2P5L80-9509cd9eb3a9-512');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('alwaysthere@gmail.com', 'NonAdmin math', false, 'user_2jjIzGq8PO6mkRaF8aVabp4OVXR', 'https://www.gulflive.com/resizer/qP4Km5tGSOFi-Fr05NSxZSirjAs=/1280x0/smart/advancelocal-adapter-image-uploads.s3.amazonaws.com/image.gulflive.com/home/gulf-media/width2048/img/mississippi-press-entertainment/photo/s5x032-0877-9jpg-81525108288c26be.jpg');

INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (3, false);
INSERT INTO requests (user_id, is_approved) VALUES (3, true);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (2, 2, 5);
INSERT INTO orders (user_id, status, approved_date, request_id) VALUES (5, 'PENDING', '2024-07-26T13:00:00', 2);