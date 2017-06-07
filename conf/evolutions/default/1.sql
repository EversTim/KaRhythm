# First setup

# --- !Ups

CREATE TABLE patterns (
    pattern_id SERIAL PRIMARY KEY,
    name text NOT NULL
);

CREATE TABLE sounds (
    sound_id SERIAL PRIMARY KEY,
    name text NOT NULL,
    file_location text NOT NULL
);

CREATE TABLE tracks (
    track_id SERIAL PRIMARY KEY,
    name text NOT NULL,
    pattern_id int references patterns(pattern_id),
    sound_id int references sounds(sound_id)
);

INSERT INTO sounds(name, file_location) VALUES ('Windows Ding', 'Windows Ding.wav');
INSERT INTO sounds(name, file_location) VALUES ('Windows Error', 'Windows Error.wav');
INSERT INTO sounds(name, file_location) VALUES ('Windows Default', 'Windows Default.wav');

# --- !Downs

DROP TABLE tracks;
DROP TABLE sounds;
DROP TABLE patterns;