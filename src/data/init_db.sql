--cree la database
create database mini_dish_db;

--cree l'user
create user "mini_dish_db_manager" with password '123456';

-- Privileges sur la base de donne
GRANT connect on database mini_dish_db to mini_dish_db_manager;

GRANT REFERENCES ON TABLE ingredient TO mini_dish_db_manager;


GRANT USAGE, SELECT ON SEQUENCE dish_id_seq TO mini_dish_db_manager;


ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO mini_dish_db_manager;




-- Privileges sur le schema public
grant usage on schema public to mini_dish_db_manager;

-- permettre la creation des tables
grant create on schema public to mini_dish_db_manager;

-- donner les droit CRUD sur tous les tables
grant select , insert , update , delete on all tables in schema public to mini_dish_db_manager;
--Ajout de la colonne required_quantity;
ALTER TABLE ingredient
ADD COLUMN IF NOT EXISTS required_quantity DOUBLE PRECISION;

