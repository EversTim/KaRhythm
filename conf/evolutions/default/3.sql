# Addition of users table

# --- !Ups

CREATE TABLE users (
    user_id uuid NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    username text NOT NULL UNIQUE,
    password text NOT NULL
);

INSERT INTO users(username, password)
    VALUES
        ('public', crypt('PasswordOfPublicUserNoOneEverNeedsToFindOutAbout', gen_salt('bf', 8))),
        ('admin', crypt('admin', gen_salt('bf', 8)));

# --- !Downs

DROP TABLE users;
