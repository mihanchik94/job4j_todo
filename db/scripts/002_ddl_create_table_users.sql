CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   login TEXT not null unique,
   password TEXT not null
   );