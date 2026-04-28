-- pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS book_vector_store (
                                                 id uuid PRIMARY KEY,
                                                 content text,
                                                 metadata json,
                                                 embedding vector(1536)
);

CREATE TABLE IF NOT EXISTS user_vector_store (
                                                 id uuid PRIMARY KEY,
                                                 content text,
                                                 metadata json,
                                                 embedding vector(1536)
);

CREATE INDEX ON book_vector_store USING HNSW (embedding vector_cosine_ops);
CREATE INDEX ON user_vector_store USING HNSW (embedding vector_cosine_ops);

-- =========================================
-- 독후감 분석
-- =========================================
CREATE TABLE p_report_analysis
(
    id                  UUID        NOT NULL DEFAULT gen_random_uuid(),
    report_id           UUID        NOT NULL,
    user_id             UUID        NOT NULL,  -- 추가
    book_id             VARCHAR(255),
    book_title          VARCHAR(255),          -- 추가
    book_category       VARCHAR(100),          -- 추가
    author              VARCHAR(255),          -- 추가
    keyword_original    JSONB,
    keyword_normalized  JSONB,
    sentiment           VARCHAR(50),
    summary_text        TEXT,

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by  UUID,
    updated_at  TIMESTAMP,
    updated_by  UUID,
    deleted_at  TIMESTAMP,
    deleted_by  UUID,

    CONSTRAINT pk_p_report_analysis PRIMARY KEY (id)
);

CREATE INDEX idx_p_report_analysis_user_id      ON p_report_analysis (user_id);
CREATE INDEX idx_p_report_analysis_book_id      ON p_report_analysis (book_id);
CREATE INDEX idx_p_report_analysis_sentiment    ON p_report_analysis (sentiment);

CREATE INDEX idx_p_report_analysis_keyword_original   ON p_report_analysis USING GIN (keyword_original);
CREATE INDEX idx_p_report_analysis_keyword_normalized ON p_report_analysis USING GIN (keyword_normalized);

-- =========================================
-- AI 로그
-- =========================================
CREATE TABLE p_ai_log (
                          id UUID PRIMARY KEY,
                          question VARCHAR(255),
                          content VARCHAR(255),
                          purpose VARCHAR(255),

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by UUID,
                          updated_at TIMESTAMP,
                          updated_by UUID,
                          deleted_at TIMESTAMP,
                          deleted_by UUID
);

-- =========================================
-- 유저 활동
-- =========================================
CREATE TABLE p_user_liked_book (
                                   id          UUID        NOT NULL,
                                   user_id     UUID        NOT NULL,
                                   book_id     VARCHAR(255) NOT NULL,

    -- 도서 정보 반정규화
                                   title       VARCHAR(255),
                                   author      VARCHAR(255),
                                   category    VARCHAR(100),
                                   description TEXT,

                                   CONSTRAINT pk_user_liked_book PRIMARY KEY (id)
);

CREATE TABLE p_user_search_history (
                                       id             UUID         NOT NULL,
                                       user_id        UUID         NOT NULL,

    -- 검색 후 클릭한 도서 (nullable)
                                       book_id        VARCHAR(255),

    -- 도서 정보 반정규화
                                       title          VARCHAR(255),
                                       author         VARCHAR(255),
                                       category       VARCHAR(100),

                                       search_keyword VARCHAR(255),
                                       searched_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT pk_user_search_history PRIMARY KEY (id)
);
-- =========================================
-- Index
-- =========================================
CREATE INDEX idx_report_analysis_report_id
    ON p_report_analysis(report_id);

CREATE INDEX idx_user_liked_book_user_id ON p_user_liked_book (user_id);
CREATE INDEX idx_user_liked_book_book_id ON p_user_liked_book (book_id);
CREATE INDEX idx_user_search_history_user_id     ON p_user_search_history (user_id);
CREATE INDEX idx_user_search_history_searched_at ON p_user_search_history (searched_at);
