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