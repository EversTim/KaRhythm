# Addition of users table

# --- !Ups

CREATE TABLE users (
    user_id uuid NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    email text NOT NULL UNIQUE,
    password text NOT NULL,
);

# --- !Downs

DROP TABLE users;