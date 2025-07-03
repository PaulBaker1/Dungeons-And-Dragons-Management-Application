-- Create the table if it doesn't already exist (with only a primary key)
CREATE TABLE IF NOT EXISTS prep_item (
                                         id BIGSERIAL PRIMARY KEY
);

-- Add columns if they do not exist already
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS title VARCHAR(255) NOT NULL;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS type VARCHAR(50) NOT NULL;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS visible_to_players BOOLEAN NOT NULL;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS session_id BIGINT;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS campaign_id BIGINT NOT NULL;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE prep_item ADD COLUMN IF NOT EXISTS description TEXT;

-- Add the foreign key constraint for session if not exists
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints
            WHERE constraint_name = 'fk_prep_item_session'
              AND table_name = 'prep_item'
        ) THEN
            ALTER TABLE prep_item
                ADD CONSTRAINT fk_prep_item_session FOREIGN KEY (session_id) REFERENCES session(id) ON DELETE CASCADE;
        END IF;
    END $$;

-- Add the foreign key constraint for campaign if not exists
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints
            WHERE constraint_name = 'fk_prep_item_campaign'
              AND table_name = 'prep_item'
        ) THEN
            ALTER TABLE prep_item
                ADD CONSTRAINT fk_prep_item_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id) ON DELETE CASCADE;
        END IF;
    END $$;
