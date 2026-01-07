insert into Dish (name,dish_type) values ( 'Salade fraîche' , 'START'),
                                            ( 'Poulet grillé' , 'MAIN'),
                                            ( 'Riz aux légumes' , 'MAIN'),
                                            ( 'Gâteau au chocolat' , 'DESSERT'),
                                            ( 'Salade de fruits' , 'DESSERT');

insert into ingredient (name, price, category, id_dish) values ('Laitue' ,800.00 , 'VEGETABLE' , 1),
                                                               ('Tomate' , 600.00 , 'VEGETABLE' , 1),
                                                               ('Poulet' , 4500.00 , 'ANIMAL' ,2),
                                                               ('Chocolat' , 3000.00 , 'OTHER' , 4),
                                                               ('Beurre' , 2500.00 , 'DAIRY' , 4);

UPDATE ingredient SET required_quantity = 1   WHERE name = 'Laitue';
UPDATE ingredient SET required_quantity = 2   WHERE name = 'Tomate';
UPDATE ingredient SET required_quantity = 0.5 WHERE name = 'Poulet';
UPDATE ingredient SET required_quantity = NULL WHERE name = 'Chocolat';
UPDATE ingredient SET required_quantity = NULL WHERE name = 'Beurre';


