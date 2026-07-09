CREATE TABLE IF NOT EXISTS training_types (
                                              id BIGINT PRIMARY KEY,
                                              name VARCHAR(50) NOT NULL UNIQUE
    );

INSERT INTO training_types (id, name) VALUES
                                               (1, 'YOGA'),
                                               (2, 'FITNESS'),
                                               (3, 'STRENGTH_TRAINING'),
                                               (4, 'CARDIO')
    ON CONFLICT (id) DO NOTHING;
