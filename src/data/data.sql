INSERT INTO dish (name, dish_type) VALUES
                                       ('Salade fraîche','START'),
                                       ('Poulet grillé','MAIN'),
                                       ('Gâteau au chocolat','DESSERT'),
                                        ('Riz au legume','MAIN'),
                                        ('Salade de fruits' , 'DESSERT');

INSERT INTO ingredient (name, price, category, required_quantity) VALUES
                                                                      ('Laitue',800,'VEGETABLE',1),
                                                                      ('Tomate',600,'VEGETABLE',2),
                                                                      ('Poulet',4500,'ANIMAL',0.5),
                                                                      ('Chocolat',3000,'OTHER',NULL),
                                                                      ('Beurre',2500,'DAIRY',NULL);

INSERT INTO dish_ingredient VALUES
                                (1,1),(1,2),(2,3),(3,4),(3,5);
select ingredient.name
 from dish_ingredient inner join ingredient on public.dish_ingredient.ingredient_id= ingredient.id where dish_id=1  ;

INSERT INTO stock_movement (id_ingredient, quantity, type, unit, creation_datetime) VALUES
                                                                                        (1, 5.0,  'IN',  'KG', '2024-01-05 08:00'),
                                                                                        (1, 0.2,  'OUT', 'KG', '2024-01-06 12:00'),

                                                                                        (2, 4.0,  'IN',  'KG', '2024-01-05 08:00'),
                                                                                        (2, 0.15, 'OUT', 'KG', '2024-01-06 12:00'),

                                                                                        (3, 10.0, 'IN',  'KG', '2024-01-04 09:00'),
                                                                                        (3, 1.0,  'OUT', 'KG', '2024-01-06 13:00'),

                                                                                        (4, 3.0,  'IN',  'KG', '2024-01-05 10:00'),
                                                                                        (4, 0.3,  'OUT', 'KG', '2024-01-06 14:00'),

                                                                                        (5, 2.5,  'IN',  'KG', '2024-01-05 10:00'),
                                                                                        (5, 0.2,  'OUT', 'KG', '2024-01-06 14:00');

