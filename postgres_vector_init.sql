-- Ensure the vector extension is available
CREATE EXTENSION IF NOT EXISTS vector;

-- Create the lecture_embeddings table with a vector column for embeddings
CREATE TABLE IF NOT EXISTS lecture_content_vectors(
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    metadata JSON,
    embedding VECTOR(768)
);