CREATE TABLE player_log (
                            id BIGSERIAL PRIMARY KEY,
                            session_id BIGINT NOT NULL,
                            player_name VARCHAR(255) NOT NULL,
                            character_name VARCHAR(255) NOT NULL,
                            class_and_level VARCHAR(50) NOT NULL,
                            faction VARCHAR(50),
                            inspiration_used BOOLEAN NOT NULL,
                            death_saves_failed INT NOT NULL,
                            gold_earned NUMERIC(19,2) NOT NULL,
                            downtime_gained INT NOT NULL,
                            renown INT NOT NULL,
                            custom_notes TEXT,
                            CONSTRAINT fk_session
                                FOREIGN KEY (session_id) REFERENCES session(id) ON DELETE CASCADE
);

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'player_log' AND column_name IN ('id', 'session_id', 'player_name',
                                                    'character_name', 'class_and_level',
                                                   'faction', 'inspiration_used',
                                                   'death_saves_failed', 'gold_earned',
                                                   'downtime_gained', 'renown', 'custom_notes');

CREATE TABLE player_log_magic_items (
                                        player_log_id BIGINT NOT NULL,
                                        magic_item VARCHAR(255) NOT NULL,
                                        PRIMARY KEY (player_log_id, magic_item),
                                        CONSTRAINT fk_player_log
                                            FOREIGN KEY (player_log_id)
                                                REFERENCES player_log (id)
                                                ON DELETE CASCADE
);
