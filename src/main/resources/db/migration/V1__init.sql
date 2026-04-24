-- pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- =========================================
-- 도서 전체 리뷰 요약
-- =========================================
CREATE TABLE p_book_report_summary (
                                       id UUID PRIMARY KEY,
                                       book_id UUID NOT NULL,
                                       summary_text VARCHAR(255),
                                       report_count INT DEFAULT 0,

                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       created_by UUID,
                                       updated_at TIMESTAMP,
                                       updated_by UUID,
                                       deleted_at TIMESTAMP,
                                       deleted_by UUID
);

-- =========================================
-- 독후감 키워드
-- =========================================
CREATE TABLE p_report_keyword (
                                  id UUID PRIMARY KEY,
                                  report_id UUID NOT NULL,
                                  keyword_original VARCHAR(255),
                                  keyword_normalized VARCHAR(255),

                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  created_by UUID,
                                  updated_at TIMESTAMP,
                                  updated_by UUID,
                                  deleted_at TIMESTAMP,
                                  deleted_by UUID
);

-- =========================================
-- 도서 임베딩
-- =========================================
CREATE TABLE p_book_embedding (
                                  id UUID PRIMARY KEY,
                                  book_id UUID NOT NULL,
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
-- 독후감 분석
-- =========================================
CREATE TABLE p_report_analysis (
                                   id UUID PRIMARY KEY,
                                   report_id UUID NOT NULL,
                                   sentiment VARCHAR(20),

                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   created_by UUID,
                                   updated_at TIMESTAMP,
                                   updated_by UUID,
                                   deleted_at TIMESTAMP,
                                   deleted_by UUID
);

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
CREATE INDEX idx_book_report_summary_book_id
    ON p_book_report_summary(book_id);

CREATE INDEX idx_report_keyword_report_id
    ON p_report_keyword(report_id);

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