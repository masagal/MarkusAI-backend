-- Insert products
INSERT INTO products (name)
VALUES ('Blue whiteboard markers'),
       ('Green whiteboard markers'),
       ('Orange whiteboard markers'),
       ('Red whiteboard markers'),
       ('Oatly litres'),
       ('Black whiteboard markers'),
       ('Yellow whiteboard markers'),
       ('Purple whiteboard markers'),
       ('Erasers'),
       ('Whiteboard cleaner'),
       ('Sticky notes');

-- Insert inventory items
INSERT INTO inventory_items (id, product_id, quantity, location)
VALUES ('1', 1, 4, 'supply closet'),
       ('2', 2, 6, 'supply closet'),
       ('3', 3, 6, 'supply closet'),
       ('4', 4, 6, 'supply closet'),
       ('5', 5, 5, 'developer fridge'),
       ('6', 6, 8, 'alek has it'),
       ('7', 7, 7, 'markus took it'),
       ('8', 8, 10, 'conference room oregano'),
       ('9', 9, 15, 'conference room oregano'),
       ('10', 10, 12, 'conference room pepper');

-- Insert users
INSERT INTO users (email, name, is_admin, clerk_id)
VALUES ('admin@tolpuddle.tech', 'simon admin', 'true', 'user_2jbOj40LQGG8Y8RhHMgGO763eRq'),
       ('mathi.admin@gmail.com', 'Math math', 'true', 'user_2jm6AYThCDfPqMlHBO8jhwyPLMI'),
       ('regular@tolpuddle.tech', 'simon regular', 'false', 'user_2jhcwIX4Gn6eY7PU7jBhaJcbOy6');

-- Insert requests
INSERT INTO requests (user_id, is_approved)
VALUES (1, 'false'),
       (2, 'false'),
       (3, 'true');

-- Insert request products
INSERT INTO request_products (product_id, request_id, quantity)
VALUES (1, 1, 10),
       (2, 2, 5),
       (3, 3, 8);

