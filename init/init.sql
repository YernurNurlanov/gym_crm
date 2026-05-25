CREATE TABLE IF NOT EXISTS training_types (
                                              training_type_id BIGINT PRIMARY KEY,
                                              training_type_name VARCHAR(50) NOT NULL UNIQUE
    );

INSERT INTO training_types (training_type_id, training_type_name) VALUES
                                               (1, 'YOGA'),
                                               (2, 'FITNESS'),
                                               (3, 'STRENGTH_TRAINING'),
                                               (4, 'CARDIO')
    ON CONFLICT (training_type_id) DO NOTHING;
