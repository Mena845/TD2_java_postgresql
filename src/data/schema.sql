create type ingredient_category AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
);

create type dish_type_enum AS ENUM (
    'START',
    'MAIN',
    'DESSERT'
);

create table Dish (
    id serial primary key ,
    name varchar(200) not null ,
    dish_type dish_type_enum
);

create table Ingredient (
    id serial primary key ,
    name varchar(200) not null ,
    price numeric (10,2),
    category ingredient_category not null ,
    id_dish int ,
    constraint fk_dish foreign key (id_dish) references Dish(id) on delete set null
);


