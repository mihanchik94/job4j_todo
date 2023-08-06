CREATE TABLE categories (
   id SERIAL PRIMARY KEY,
   name TEXT not null unique
);