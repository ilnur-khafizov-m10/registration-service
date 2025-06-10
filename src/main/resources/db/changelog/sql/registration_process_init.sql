-- db/changelog/sql/registration_process_init.sql

-- Создаем новую таблицу для процессов регистрации
CREATE TABLE registration_process (
    process_id VARCHAR(255) PRIMARY KEY,
    business_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    process_version VARCHAR(50) NOT NULL,
    current_state VARCHAR(100) NOT NULL,
    variables JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
