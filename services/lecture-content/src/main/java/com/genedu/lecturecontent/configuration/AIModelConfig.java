package com.genedu.lecturecontent.configuration;

import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AIModelConfig {
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
                .apiKey("AIzaSyB8Njl9vYU89SM9JLBGN_8WZUtRbStxO3I")
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai")
                .completionsPath("/chat/completions")
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi) // Replace with your OpenAI API key
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .model("gemini-2.5-flash")
                                .temperature(0.7) // Set the temperature for response variability
                                .maxTokens(10000)  // Set the maximum number of tokens in the response
                                .build()
                )
                .build();
    }

    @Bean("openAiAudioSpeechModel")
    public OpenAiAudioSpeechModel openAiAudioSpeechModel() {
        OpenAiAudioApi openAiApi = OpenAiAudioApi.builder()
                .apiKey("AIzaSyB8Njl9vYU89SM9JLBGN_8WZUtRbStxO3I")
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai")
                .build();

        OpenAiAudioSpeechOptions openAiAudioSpeechOptions = OpenAiAudioSpeechOptions.builder()
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f) // Set the speed of the speech
                .model("gemini-2.5-flash-preview-tts")
                .build();

        return new OpenAiAudioSpeechModel(
                openAiApi,
                openAiAudioSpeechOptions
        );
    }
}
