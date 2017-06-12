# Adding empty sound to database

# --- !Ups

INSERT INTO sounds (name, file_location) VALUES ('Empty', 'Empty.wav');

# --- !Downs

DELETE FROM sounds WHERE name = 'Empty' AND file_location = 'Empty.wav';