-- Core Session Table
CREATE TABLE session (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         objective VARCHAR(1000),
                         summary VARCHAR(4000),
                         status VARCHAR(50),
                         campaign_id BIGINT NOT NULL,
                         session_date TIMESTAMP,
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP
);

-- Agenda Items
CREATE TABLE session_agenda_item (
                                     id BIGSERIAL PRIMARY KEY,
                                     session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
                                     content TEXT NOT NULL,
                                     dm_only BOOLEAN DEFAULT FALSE,
                                     sort_order INT DEFAULT 0
);

-- Encounters
CREATE TABLE session_encounter (
                                   id BIGSERIAL PRIMARY KEY,
                                   session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
                                   name VARCHAR(255),
                                   location VARCHAR(255),
                                   estimated_difficulty VARCHAR(50),
                                   outcome TEXT
);

-- Notes
CREATE TABLE session_note (
                              id BIGSERIAL PRIMARY KEY,
                              session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
                              type VARCHAR(50),
                              content TEXT
);

-- Decisions
CREATE TABLE session_decision (
                                  id BIGSERIAL PRIMARY KEY,
                                  session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
                                  decision_point TEXT,
                                  choice_made TEXT,
                                  consequences TEXT
);

-- Key Items
CREATE TABLE key_item_tracker (
                                  id BIGSERIAL PRIMARY KEY,
                                  session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
                                  item_name VARCHAR(255),
                                  description TEXT,
                                  acquired BOOLEAN DEFAULT FALSE,
                                  location_found VARCHAR(255),
                                  used_in TEXT
);

ALTER TABLE campaign ADD COLUMN version BIGINT DEFAULT 0;

SELECT table_name, column_name, data_type, column_default, is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
ORDER BY table_name, ordinal_position;

