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

CREATE table stock_mouvement (
                            id SERIAL primary key ,
                            id_ingredient INTEGER REFERENCES ingredient(id),
                            quantity numeric not null ,
                            mouvement_type varchar(10)not null , --IN | OUT
                            unit unit_type,
                            creation_datetime timestamp
)