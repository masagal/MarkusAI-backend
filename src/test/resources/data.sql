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

INSERT INTO users (id, email, name, is_admin) VALUES (1, 'stinker@whitehouse.gov','Stinker McGee', true);
INSERT INTO users (id, email, name, is_admin) VALUES (2, 'asparagus666@gmail.com','Asparagus Germondsen', false);