-- For created_at (set once on insert)
ALTER TABLE character
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- For updated_at (needs trigger)
ALTER TABLE character
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

-- Create update trigger function
CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Attach trigger to table
CREATE TRIGGER update_character_updated_at
    BEFORE UPDATE ON character
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();


ALTER TABLE character DROP COLUMN creation_at;
ALTER TABLE character DROP COLUMN update_at;

select * from character;

UPDATE character
SET created_at = '2025-04-01 10:00:00',
    updated_at = '2025-04-05 16:00:00'
WHERE id = 3;  -- Eldrin Veyl

UPDATE character
SET created_at = '2025-03-15 14:45:00',
    updated_at = '2025-03-20 09:30:00'
WHERE id = 2;  -- Bob

SELECT id, character_name, created_at, updated_at FROM character;

select * from campaign;

select * from session;

select * from character;

SELECT
    status,
    COUNT(*) AS count
FROM
    campaign
GROUP BY
    status
ORDER BY
    count DESC;
