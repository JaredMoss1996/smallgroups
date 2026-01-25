CREATE TABLE IF NOT EXISTS churches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    denomination VARCHAR(255),
    address VARCHAR(500),
    city VARCHAR(255),
    state VARCHAR(50),
    zip_code VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255),
    provider VARCHAR(50) DEFAULT 'local',
    enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(50) DEFAULT 'USER',
    home_church_id BIGINT,
    approved_for_group_creation BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (home_church_id) REFERENCES churches(id)
);

CREATE TABLE IF NOT EXISTS small_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    church_name VARCHAR(255) NOT NULL,
    denomination VARCHAR(255),
    location VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    latitude DOUBLE,
    longitude DOUBLE,
    description VARCHAR(1000),
    age_group VARCHAR(50),
    gender VARCHAR(50),
    type VARCHAR(100),
    meeting_day VARCHAR(50),
    meeting_time VARCHAR(50),
    childcare_included BOOLEAN DEFAULT FALSE,
    handicap_accessible BOOLEAN DEFAULT FALSE,
    contact_name VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    current_size INTEGER,
    max_size INTEGER,
    image_url VARCHAR(2048),
    creator_id BIGINT,
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES small_groups(id) ON DELETE CASCADE,
    UNIQUE (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    church_id BIGINT NOT NULL,
    message TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    reviewed_by BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
);
