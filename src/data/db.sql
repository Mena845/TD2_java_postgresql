-- Création de la base de données
CREATE DATABASE mini_dish_db;

-- Création de l'utilisateur
CREATE USER mini_dish_db_manager WITH PASSWORD '1234567';

-- Donner tous les privilèges sur la base
GRANT ALL PRIVILEGES ON DATABASE mini_dish_db TO mini_dish_db_manager;

-- 4 - Se connecter a la base
\c mini_dish_db
