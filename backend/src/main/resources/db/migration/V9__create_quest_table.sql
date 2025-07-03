CREATE TABLE quest (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       status VARCHAR(50) NOT NULL,         -- Values: PLANNED, ACTIVE, COMPLETED, FAILED
                       priority VARCHAR(50) NOT NULL,         -- Values: LOW, NORMAL, HIGH, CRITICAL
                       visible_to_players BOOLEAN NOT NULL,
    -- Using a join table for tags (see below)
                       linked_session_id BIGINT,              -- Optional FK to session(id)
                       campaign_id BIGINT NOT NULL,           -- Required FK to campaign(id)
                       completed_at TIMESTAMP,                -- For finished quests (optional)
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_quest_session FOREIGN KEY (linked_session_id) REFERENCES session(id) ON DELETE SET NULL,
                       CONSTRAINT fk_quest_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id) ON DELETE CASCADE
);

-- Create a join table for quest tags
CREATE TABLE quest_tags (
                            quest_id BIGINT NOT NULL,
                            tag VARCHAR(100) NOT NULL,
                            PRIMARY KEY (quest_id, tag),
                            CONSTRAINT fk_quest_tags FOREIGN KEY (quest_id) REFERENCES quest(id) ON DELETE CASCADE
);
