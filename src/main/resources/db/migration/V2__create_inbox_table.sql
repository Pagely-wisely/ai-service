-- V2__create_inbox_table.sql
CREATE TABLE inbox (
                       event_id        VARCHAR(255)    NOT NULL,
                       topic           VARCHAR(255)    NOT NULL,
                       payload         TEXT            NOT NULL,
                       status          VARCHAR(50)     NOT NULL DEFAULT 'RECEIVED',
                       created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
                       processed_at    TIMESTAMP,

                       CONSTRAINT pk_inbox PRIMARY KEY (event_id)
);

CREATE INDEX idx_inbox_status ON inbox (status);
CREATE INDEX idx_inbox_created_at ON inbox (created_at);