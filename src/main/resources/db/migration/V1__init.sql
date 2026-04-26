-- pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================================
-- 도서 임베딩
-- =========================================
CREATE TABLE p_book_embedding (
                                  id UUID PRIMARY KEY,
                                  book_id VARCHAR(255) NOT NULL,
                                  vector VECTOR(1536),
                                  profile_text VARCHAR(255),

                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  created_by UUID,
                                  updated_at TIMESTAMP,
                                  updated_by UUID,
                                  deleted_at TIMESTAMP,
                                  deleted_by UUID
);

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
    book_id             VARCHAR(255),
    keyword_original    JSONB,
    keyword_normalized  JSONB,
    sentiment           VARCHAR(50),
    summary_text        TEXT,

    -- BaseEntity 필드 (실제 BaseEntity 구성에 맞게 조정하세요)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP,
    updated_by UUID,
    deleted_at TIMESTAMP,
    deleted_by UUID,

    CONSTRAINT pk_p_report_analysis PRIMARY KEY (id)
);

-- 조회 성능을 위한 인덱스
CREATE INDEX idx_p_report_analysis_report_id ON p_report_analysis (report_id);
CREATE INDEX idx_p_report_analysis_book_id   ON p_report_analysis (book_id);
CREATE INDEX idx_p_report_analysis_sentiment ON p_report_analysis (sentiment);

-- jsonb 내부 검색을 위한 GIN 인덱스 (키워드로 검색할 경우 필요)
CREATE INDEX idx_p_report_analysis_keyword_original   ON p_report_analysis USING GIN (keyword_original);
CREATE INDEX idx_p_report_analysis_keyword_normalized ON p_report_analysis USING GIN (keyword_normalized);

-- =========================================
-- 유저 임베딩
-- =========================================
CREATE TABLE p_user_embedding (
                                  id UUID PRIMARY KEY,
                                  user_id UUID NOT NULL,
                                  vector VECTOR(1536),
                                  profile_text VARCHAR(255),

                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  created_by UUID,
                                  updated_at TIMESTAMP,
                                  updated_by UUID,
                                  deleted_at TIMESTAMP,
                                  deleted_by UUID
);

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
CREATE TABLE p_user_action (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL,
                               book_id UUID,
                               action_type VARCHAR(20),

                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               created_by UUID,
                               updated_at TIMESTAMP,
                               updated_by UUID,
                               deleted_at TIMESTAMP,
                               deleted_by UUID
);

-- =========================================
-- Index
-- =========================================
CREATE INDEX idx_report_analysis_report_id
    ON p_report_analysis(report_id);

CREATE INDEX idx_book_embedding_book_id
    ON p_book_embedding(book_id);

CREATE INDEX idx_user_embedding_user_id
    ON p_user_embedding(user_id);

CREATE INDEX idx_user_action_user_id
    ON p_user_action(user_id);

CREATE INDEX idx_user_action_book_id
    ON p_user_action(book_id);

-- =========================================
-- pgvector similarity search index
-- =========================================
CREATE INDEX idx_book_embedding_vector
    ON p_book_embedding
    USING ivfflat (vector vector_cosine_ops)
    WITH (lists = 100);

CREATE INDEX idx_user_embedding_vector
    ON p_user_embedding
    USING ivfflat (vector vector_cosine_ops)
    WITH (lists = 100);