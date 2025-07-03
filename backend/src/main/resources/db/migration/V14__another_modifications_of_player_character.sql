ALTER TABLE character
    ADD COLUMN avatar VARCHAR(255),
    ADD COLUMN character_class VARCHAR(50),
    ALTER COLUMN player_controlled TYPE VARCHAR(255);

ALTER TABLE campaign
    ALTER COLUMN description TYPE TEXT;
