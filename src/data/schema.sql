CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100),
                      dish_type VARCHAR(20)
);

CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100),
                            price DOUBLE PRECISION,
                            category VARCHAR(20),
                            required_quantity DOUBLE PRECISION
);
alter table ingredient drop column required_quantity;

CREATE TABLE dish_ingredient (
                                 dish_id INTEGER REFERENCES dish(id),
                                 ingredient_id INTEGER REFERENCES ingredient(id),
                                 PRIMARY KEY (dish_id, ingredient_id)
);
