-- SwissKit Database Initialization Script

-- Email Settings Table
CREATE TABLE IF NOT EXISTS swiss_kit_setting_email
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    smtp_address VARCHAR(255) NOT NULL,
    smtp_port    INTEGER      NOT NULL,
    need_tls     INTEGER      NOT NULL DEFAULT 0,
    need_ssl     INTEGER      NOT NULL DEFAULT 0,
    from_address VARCHAR(255)
);

-- Excel Complex Split Config Table
CREATE TABLE IF NOT EXISTS complex_split_config
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    task_id      VARCHAR(255) NOT NULL,
    field_name   VARCHAR(255) NOT NULL,
    sheet_name   VARCHAR(255) NOT NULL,
    header_index INTEGER      NOT NULL,
    column_index INTEGER      NOT NULL
);

-- Email Address Book Table
CREATE TABLE IF NOT EXISTS email_address_book
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    email_address VARCHAR(255) NOT NULL,
    nickname      VARCHAR(255),
    tags          JSON
);

-- Email Tag Table
CREATE TABLE IF NOT EXISTS email_tag
(
    id  INTEGER PRIMARY KEY AUTO_INCREMENT,
    tag VARCHAR(255) NOT NULL UNIQUE
);


