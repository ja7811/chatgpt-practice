package com.jhh;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTService {
    public final String MODEL_NAME = "gpt-4o"; // "gpt-4o-mini";
    public final double TEMPERATURE = 0.4;
    OpenAiService service;

    {
        String apiKey = "";

        try {
            apiKey = new String(Files.readAllBytes(Paths.get("key.txt"))).trim();
            System.out.println("API Key: " + apiKey);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        service = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    public String getResult(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        Long startTime = System.currentTimeMillis();
        ChatCompletionRequest request = generatePrompt(systemPrompt, chatPrompt);
        ChatCompletionResult result = service.createChatCompletion(request);
        Long endTime = System.currentTimeMillis();
        System.out.printf("Prompt consumed %d tokens total (%ds)%n", result.getUsage().getTotalTokens(), (endTime-startTime)/1000);
        return result.getChoices().get(0).getMessage().getContent();
    }

    private ChatCompletionRequest generatePrompt(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(systemPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model(MODEL_NAME)
                .messages(promptMessage)
                .temperature(TEMPERATURE)
                .build();
    }


}
