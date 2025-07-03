CREATE TABLE combatant_actions (
                                   combatant_id BIGINT NOT NULL,
                                   action TEXT,
                                   CONSTRAINT pk_combatant_actions PRIMARY KEY (combatant_id, action),
                                   CONSTRAINT fk_combatant_actions FOREIGN KEY (combatant_id) REFERENCES combatant(id) ON DELETE CASCADE
);

CREATE TABLE combatant_special_abilities (
                                             combatant_id BIGINT NOT NULL,
                                             special_ability TEXT,
                                             CONSTRAINT pk_combatant_special_abilities PRIMARY KEY (combatant_id, special_ability),
                                             CONSTRAINT fk_combatant_special_abilities FOREIGN KEY (combatant_id) REFERENCES combatant(id) ON DELETE CASCADE
);