package com.genedu.lecturecontent.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class PgVectorConfig {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Bean("pgVectorStore")
    public VectorStore vectorStore(
            JdbcTemplate jdbcTemplate,
            @Qualifier("openAiEmbeddingModel")
            EmbeddingModel embeddingModel
    )
    {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .dimensions(1536)
                .vectorTableName("lecture_content_embeddings") // Optional: defaults to "vectors"
                .build();
    }

    @Bean("jdbcTemplatePgVector")

    public JdbcTemplate jdbcTemplatePgVector(
            @Qualifier("pgVectorDataSource")
            javax.sql.DataSource pgVectorDataSource
    ) {
        return new JdbcTemplate(pgVectorDataSource);
    }

    @Bean("pgVectorDataSource")
    public javax.sql.DataSource pgVectorDataSource() {
        return DataSourceBuilder.create()
                .url(dataSourceUrl)
                .username("admin")
                .password("admin")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}