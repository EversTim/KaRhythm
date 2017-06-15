# Adding user foreign key to patterns

# --- !Ups

ALTER TABLE patterns
ADD COLUMN username text REFERENCES users(username);

UPDATE patterns
SET username = 'public'
WHERE username IS NULL;

ALTER TABLE patterns
ALTER COLUMN username SET NOT NULL;

# --- !Downs

ALTER TABLE patterns
DROP COLUMN username;