# Creating tables, adding defaults/tests

# --- !Ups

CREATE TABLE patterns (
    pattern_id SERIAL PRIMARY KEY,
    name text NOT NULL
);

CREATE TABLE sounds (
    sound_id SERIAL PRIMARY KEY,
    name text UNIQUE NOT NULL,
    file_location text UNIQUE NOT NULL
);

CREATE TABLE tracks (
    track_id SERIAL PRIMARY KEY,
    name text NOT NULL,
    data text NOT NULL,
    pattern_id integer REFERENCES patterns(pattern_id),
    sound_id integer REFERENCES sounds(sound_id)
);

INSERT INTO sounds(name, file_location) VALUES ('Windows Ding', 'Windows Ding.wav');
INSERT INTO sounds(name, file_location) VALUES ('Windows Error', 'Windows Error.wav');
INSERT INTO sounds(name, file_location) VALUES ('Windows Default', 'Windows Default.wav');

INSERT INTO patterns(name) VALUES ('default'), ('test1'), ('test2');

INSERT INTO tracks(name, data, pattern_id, sound_id)
    VALUES
        ('d1', '0101', 1, 1), ('d2', '1010', 1, 2),
        ('t11', '0101', 2, 1), ('t12', '1010', 2, 2), ('t13', '1111', 2, 3),
        ('t21', '10101', 3, 1), ('t22', '01010', 3, 2), ('t23', '11110', 3, 3);

# --- !Downs

DROP TABLE tracks;
DROP TABLE sounds;
DROP TABLE patterns;