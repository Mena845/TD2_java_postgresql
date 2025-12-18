CREATE TYPE ingredient_category AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
    );

CREATE TYPE dish_type_enum AS ENUM (
    'START',
    'MAIN',
    'DESSERT'
    );


-- La table dish
CREATE TABLE Dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      dish_type dish_type_enum NOT NULL
);


-- La table ingredient
CREATE TABLE Ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price NUMERIC(10,2) NOT NULL,
                            category ingredient_category NOT NULL,
                            id_dish INT,
                            CONSTRAINT fk_dish
                                FOREIGN KEY (id_dish)
                                    REFERENCES Dish(id)
                                    ON DELETE SET NULL
);

INSERT INTO Dish (id, name, dish_type) VALUES
                                           (1, 'Salade fraîche', 'START'),
                                           (2, 'Poulet grillé', 'MAIN'),
                                           (3, 'Riz aux légumes', 'MAIN'),
                                           (4, 'Gâteau au chocolat', 'DESSERT'),
                                           (5, 'Salade de fruits', 'DESSERT');


INSERT INTO Ingredient (id, name, price, category, id_dish) VALUES
                                                                (1, 'Laitue', 800.00, 'VEGETABLE', 1),
                                                                (2, 'Tomate', 600.00, 'VEGETABLE', 1),
                                                                (3, 'Poulet', 4500.00, 'ANIMAL', 2),
                                                                (4, 'Chocolat', 3000.00, 'OTHER', 4),
                                                                (5, 'Beurre', 2500.00, 'DAIRY', 4);
 select id , Dish.name from Dish

