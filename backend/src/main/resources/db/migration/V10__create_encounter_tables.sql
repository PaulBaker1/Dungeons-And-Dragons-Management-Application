CREATE TABLE encounter (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           notes TEXT,
                           session_id BIGINT NOT NULL,
                           total_xp INT NOT NULL DEFAULT 0,
                           challenge_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_encounter_session FOREIGN KEY (session_id) REFERENCES session(id) ON DELETE CASCADE
);

CREATE TABLE combatant (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           hp INT NOT NULL,
                           xp INT NOT NULL,
                           cr DOUBLE PRECISION NOT NULL,
                           initiative INT,
                           is_template BOOLEAN NOT NULL DEFAULT false,
                           encounter_id BIGINT,
                           CONSTRAINT fk_combatant_encounter FOREIGN KEY (encounter_id) REFERENCES encounter(id) ON DELETE CASCADE
);


