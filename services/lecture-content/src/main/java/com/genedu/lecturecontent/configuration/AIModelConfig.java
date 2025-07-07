package com.genedu.lecturecontent.configuration;

import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AIModelConfig {

    private final String OPENAI_API_KEY = "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";
    private final String OPENAI_BASE_URL = "https://api.openai.com";
    private final String OPENAI_COMPLETIONS_PATH = "/v1/chat/completions";
    private final String OPENAI_EMBEDDING_PATH = "/v1/embeddings";
    private final String OPENAI_CHAT_MODEL = "gpt-4.1-nano-2025-04-14";
    private final String OPENAI_EMBEDDING_MODEL = "text-embedding-3-small";
    private final String OPENAI_AUDIO_MODEL = "gpt-4o-mini-tts";

    @Bean
    public ChatClient openAiChatClient(
            @Qualifier("openAiChatModel")
            OpenAiChatModel openAiChatModel
    ) {
        return ChatClient.create(
                openAiChatModel
        );
    }

    @Bean("openAiChatMemory")
    public ChatMemory openAiChatMemory() {
        return new InMemoryChatMemory();
    }


    @Bean("openAiChatModel")
    public OpenAiChatModel openAiChatModel() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(OPENAI_API_KEY)
                .baseUrl(OPENAI_BASE_URL)
                .completionsPath(OPENAI_COMPLETIONS_PATH)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi) // Replace with your OpenAI API key
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .model(OPENAI_CHAT_MODEL)
                                .temperature(0.7) // Set the temperature for response variability
                                .maxTokens(10000)  // Set the maximum number of tokens in the response
                                .build()
                )
                .build();
    }

    @Bean("openAiAudioSpeechModel")
    public OpenAiAudioSpeechModel openAiAudioSpeechModel() {
        OpenAiAudioApi openAiApi = OpenAiAudioApi.builder()
                .apiKey(OPENAI_API_KEY)
                .baseUrl(OPENAI_BASE_URL)
                .build();

        OpenAiAudioSpeechOptions openAiAudioSpeechOptions = OpenAiAudioSpeechOptions.builder()
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .speed(1.0f) // Set the speed of the speech
                .model(OPENAI_AUDIO_MODEL) // Specify the model for text-to-speech
                .build();

        return new OpenAiAudioSpeechModel(
                openAiApi,
                openAiAudioSpeechOptions
        );
    }

    @Bean("openAiEmbeddingModel")
    public OpenAiEmbeddingModel openAiEmbeddingModel() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(OPENAI_API_KEY)
                .baseUrl(OPENAI_BASE_URL)
                .embeddingsPath(OPENAI_EMBEDDING_PATH)
                .build();

        OpenAiEmbeddingOptions openAiEmbeddingOptions = OpenAiEmbeddingOptions.builder()
                .model(OPENAI_EMBEDDING_MODEL) // Specify the embedding model
                .dimensions(1536)
                .build();

        return new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                openAiEmbeddingOptions
        );
    }
}
