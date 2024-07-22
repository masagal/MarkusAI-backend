-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

INSERT INTO products (id, name) VALUES (1, 'Blue whiteboard markers'),
                                        (2, 'Green whiteboard markers'),
                                        (3, 'Orange whiteboard markers'),
                                        (4, 'Red whiteboard markers'),
                                        (5, 'Oatly litres');

INSERT INTO inventory_items (id, product_id, quantity) VALUES ('1', 1, 4),
                                                         ('2', 2, 6),
                                                         ('3', 3, 6),
                                                         ('4', 4, 6),
                                                         ('5', 5, 5);