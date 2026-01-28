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


