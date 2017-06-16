# Some new sounds

# --- !Ups

INSERT INTO sounds(name, file_location)
    VALUES
        ('Windows Battery Critical', 'Windows Battery Critical.wav'),
        ('Windows Battery Low', 'Windows Battery Low.wav'),
        ('Windows Critical Stop', 'Windows Critical Stop.wav');

# --- !Downs

DELETE FROM sounds
    WHERE name = 'Windows Battery Critical'
    OR name = 'Windows Battery Low'
    OR name = 'Windows Critical Stop';