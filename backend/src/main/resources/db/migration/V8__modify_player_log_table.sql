-- 1) Rename "message" to "player_name"
ALTER TABLE player_log
    RENAME COLUMN message TO player_name;

-- 2) Add a "character_name" column (optional: make it NOT NULL if required)
ALTER TABLE player_log ADD COLUMN character_name VARCHAR(255);

-- 3) Optionally add an "updated_at" column if you plan to maintain timestamps
ALTER TABLE player_log ADD COLUMN updated_at TIMESTAMP(6);

-- 4) (Optional) Create a table to store magic items found by a player
--    if you want to keep them as an element collection.
--    You can remove this if you’re not storing magic items.
CREATE TABLE IF NOT EXISTS player_log_magic_items (
                                                      player_log_id BIGINT NOT NULL,
                                                      magic_item VARCHAR(255) NOT NULL
);

ALTER TABLE player_log_magic_items
    ADD CONSTRAINT fk_player_log_magic_items
        FOREIGN KEY (player_log_id) REFERENCES player_log(id) ON DELETE CASCADE;

ALTER TABLE player_log
    ADD is_critical BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE player_log
    ADD file_url VARCHAR(255);
