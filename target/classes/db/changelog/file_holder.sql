CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;

CREATE TABLE file_holder(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    file_name VARCHAR NOT NULL,
    file_hash VARCHAR NOT NULL
);