CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   description TEXT not null,
   created TIMESTAMP not null,
   done BOOLEAN not null
);