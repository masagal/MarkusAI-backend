-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

INSERT INTO products (id, name, image_url) VALUES (default, 'Whiteboard Marker - Blue Foray - Round Tip - 2.5mm', 'https://www.officedepot.se/-/media/commerce/sweden/original/24/09/2409541.jpg?bc=white&h=700&w=700&hash=16CA712FB6D8978B81276B105CB5F18440F3BA43'),
                                        (default, 'Whiteboard Marker - Green Foray - Round Tip - 2.5mm', 'https://www.officedepot.se/-/media/commerce/sweden/original/24/09/2409543.jpg?bc=white&h=700&w=700&hash=7CD8E897291B46509D027D0D431209B5F418D1F3'),
                                        (default, 'Whiteboard Marker - Black Foray - Round Tip - 2.5mm', 'https://www.officedepot.se/-/media/commerce/sweden/original/24/09/2409544.jpg?bc=white&h=700&w=700&hash=6E00C954EB4BB8FDC51C61B21C292FE871C41265'),
                                        (default, 'Whiteboard Marker - Red Foray - Round Tip - 2.5mm', 'https://www.officedepot.se/-/media/commerce/sweden/original/24/09/2409542.jpg?bc=white&h=700&w=700&hash=D5FCFA7BE98D3D68AA873BCB601AD71591D8417D'),
                                        (default, 'Oatly Oat Milk - Ekologisk KRAV - 1 Liter', 'https://www.officedepot.se/-/media/commerce/sweden/original/85/52/8552280.jpg?bc=white&h=700&w=700&hash=AF1447B2FEBD538F51821ECB37B58BB4F0A4AA9B'),
                                        (default, 'Sproud Pea Milk - Barista - 1 Liter', 'https://www.officedepot.se/-/media/commerce/sweden/original/85/56/8556368.jpg?bc=white&h=700&w=700&hash=EDAC0B3FCEDE7E2391778EFBB0D163715FEC833B'),
                                        (default, 'Milk - Arla Long-Life UHT - 1 Liter', 'https://www.officedepot.se/-/media/commerce/sweden/original/28/29/2829602.jpg?bc=white&h=700&w=700&hash=9398326587CD165647C96D8E993B9789B1C0FC5E'),
                                        (default, 'Storage Box - SmartStore Classic - 0.3 Litre Capacity', 'https://www.officedepot.se/-/media/commerce/sweden/original/23/62/2362000.jpg?bc=white&h=700&w=700&hash=86E99DDBEF98F273E1AB2873883831392BC172D6'),
                                        (default, 'Thin Folder - Elastic Bands - Forest Green', 'https://www.officedepot.se/-/media/commerce/sweden/original/26/69/2669873.jpg?bc=white&h=700&w=700&hash=F187379D1F5DA9D0E26980B6C8C74C359AC45283'),
                                        (default, 'Thin Folder - Elastic Bands - Plain Orange', 'https://www.officedepot.se/-/media/commerce/sweden/original/26/69/2669926.jpg?bc=white&h=700&w=700&hash=6DAA8D6611C8BFC17B4B5A4F78D7CCFD314581F8'),
                                        (default, 'Thin Folder - Elastic Bands - Bright Pink', 'https://www.officedepot.se/-/media/commerce/sweden/original/26/69/2669927.jpg?bc=white&h=700&w=700&hash=051F7AADB55CDC537295422AB90B91E3ECB0D16F'),
                                        (default, 'Ring Binder - Blue', 'https://www.officedepot.se/-/media/commerce/sweden/original/20/68/2068230.jpg?bc=white&h=700&w=700&hash=DE59F8FA5742026B23E68C56BDCBE0370E78840B'),
                                        (default, 'Ink Cartridge - Fits Canon Inkjet - Black Large', 'https://cf-images.dustin.eu/cdn-cgi/image/format=auto,quality=75,width=1080,,fit=contain/image/d2000010011270138/canon-bl%C3%A4ck-svart-pg-540l-300-sidor.jpeg'),
                                        (default, 'Gilford Toner - Fits HP Laser Printers - Black', 'https://cf-images.dustin.eu/cdn-cgi/image/format=auto,quality=75,width=1080,,fit=contain/image/d200001002203379/gilford-toner-svart-203x-32k-cf540x-alternativ-till-cf540x.jpg'),
                                        (default, 'Brother Toner - Fits Brother Laser Printers - Black', 'https://cf-images.dustin.eu/cdn-cgi/image/format=auto,quality=75,width=828,,fit=contain/image/d2000010011177098/brother-trumma-svart-hl-l6300.png');

INSERT INTO inventory_items (id, product_id, quantity, location) VALUES ('1', 1, 4, 'supply closet'),
                                                         ('2', 2, 6, 'supply closet'),
                                                         ('3', 3, 6, 'meeting room oregano'),
                                                         ('4', 4, 6, 'meeting room pepper'),
                                                         ('5', 5, 5, 'core team fridge'),
                                                         ('6', 6, 0, 'developer fridge'),
                                                         ('7', 7, 0, 'developer fridge'),
                                                         ('8', 8, 14, 'auxiliary storage'),
                                                         ('9', 9, 0, 'paper cupboard'),
                                                         ('10', 10, 25, 'paper cupboard'),
                                                         ('11', 11, 20, 'paper cupboard'),
                                                         ('12', 12, 1, 'paper cupboard'),
                                                         ('13', 13, 3, 'box in the JSFS room'),
                                                         ('14', 14, 2, 'box in the JSFS room'),
                                                         ('15', 15, 4, 'box in the JSFS room');

INSERT INTO users (email, name, is_admin) VALUES ('test@test.no','testUser', false);
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('admin@tolpuddle.tech', 'Stinker McGee', true, 'user_2jbOj40LQGG8Y8RhHMgGO763eRq', 'https://files.worldwildlife.org/wwfcmsprod/images/HERO_harbor_seal_on_ice/hero_small/41yzw17euy_Harbor_Seal_on_Ice_close_0357_6_11_07.jpg');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('regular@tolpuddle.tech', 'Simon Kendall', false, 'user_2jhcwIX4Gn6eY7PU7jBhaJcbOy6', 'https://ca.slack-edge.com/TA01UCHBN-U06SQBABJ85-d1786941fd20-512');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('cypress+clerk_test@tolpuddle.tech', 'cypress admin', true, 'user_2jjMkCNC2oaUopPuZPwhtBlqdOA', 'https://akns-images.eonline.com/eol_images/Entire_Site/2020107/rs_1200x1200-201107114351-1200-kamala-harris.cm.11720.jpg?fit=around%7C1080:1080&output-quality=90&crop=1080:1080;center,top');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('mathi.admin@gmail.com', 'Math math', true, 'user_2jm6AYThCDfPqMlHBO8jhwyPLMI', 'https://ca.slack-edge.com/TA01UCHBN-U06TG2P5L80-9509cd9eb3a9-512');
INSERT INTO users (email, name, is_admin, clerk_id, image_url) VALUES ('alwaysthere@gmail.com', 'NonAdmin math', false, 'user_2jjIzGq8PO6mkRaF8aVabp4OVXR', 'https://www.gulflive.com/resizer/qP4Km5tGSOFi-Fr05NSxZSirjAs=/1280x0/smart/advancelocal-adapter-image-uploads.s3.amazonaws.com/image.gulflive.com/home/gulf-media/width2048/img/mississippi-press-entertainment/photo/s5x032-0877-9jpg-81525108288c26be.jpg');

INSERT INTO requests (user_id, is_approved) VALUES (1, false);
INSERT INTO requests (user_id, is_approved) VALUES (1, true);
INSERT INTO requests (user_id, is_approved) VALUES (3, false);
INSERT INTO requests (user_id, is_approved) VALUES (3, true);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (1, 1, 10);
INSERT INTO request_products (product_id, request_id, quantity) VALUES (2, 2, 5);
INSERT INTO orders (user_id, status, approved_date, request_id) VALUES (5, 'PENDING', '2024-07-26T13:00:00', 2);