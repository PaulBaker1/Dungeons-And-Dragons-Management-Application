ALTER TABLE player_log RENAME TO character_log;
ALTER TABLE player_log_magic_items RENAME TO character_log_magic_items;

ALTER TABLE player_log_magic_items
    RENAME TO character_log_magic_items;

ALTER TABLE character_log_magic_items
    RENAME COLUMN player_log_id TO character_log_id;

ALTER TABLE character_log_magic_items
    ADD COLUMN magic_item TEXT; -- if missing

CREATE TABLE player_log_magic_items (
                                        player_log_id BIGINT NOT NULL,
                                        magic_item TEXT,
                                        FOREIGN KEY (player_log_id) REFERENCES player_log(id) ON DELETE CASCADE
);


select status from campaign
