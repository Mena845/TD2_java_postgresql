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

create type movement_type as enum ('IN','OUT');
CREATE TYPE unit_type AS ENUM ('KG');


CREATE table stock_movement (
                            id SERIAL primary key ,
                            id_ingredient int,
                            quantity numeric (10 ,2) ,
                            unit unit_type,
                            type movement_type,
                            creation_datetime timestamp with time zone,
                            CONSTRAINT fk_stock_ingredient
                                FOREIGN KEY (id_ingredient)
                                    REFERENCES ingredient(id)
);

create table order (
    id serial primary key ,
    reference varchar(255),
    creation_dateTime timestamp with time zone
)