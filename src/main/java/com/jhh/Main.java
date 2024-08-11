package com.jhh;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String USER = ChatMessageRole.USER.value();
    static final String SYSTEM = ChatMessageRole.SYSTEM.value();
    static final String MODEL_NAME = "gpt-4o-mini"; // "gpt-4o";
    static final double TEMPERATURE = 0.4;



    static String testKeywordFiltering(String keyword, OpenAiService service){

        return null;

    }

    static String testCastCreation(String keyword, OpenAiService service){

        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are the host of the podcast."),
                new ChatMessage(SYSTEM, "Your job is to make a podcast script about what happened recently. It is best if you say things based on real news"),
                new ChatMessage(SYSTEM, "script should only contain what you have to say (no markdowns or background musics)"),
                new ChatMessage(SYSTEM, "script should be " + 1 + " minutes long."),
                new ChatMessage(SYSTEM, "Put '\n' at the end of each sentence"),
                new ChatMessage(SYSTEM, "Answer in " + "official" + " manner."),
                new ChatMessage(SYSTEM, "The script should be in english")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Make a podcast script about " + keyword)
        );


        ChatCompletionRequest request = generatePrompt(systemPrompt, chatPrompt);
        ChatCompletionResult result = service.createChatCompletion(request);

        return result.getChoices().get(0).getMessage().getContent();

    }

    static String testTranslation(String script, OpenAiService service){
        return null;
    }


    static void saveResultToFile(String keyword, String result){
        String dirName = String.format("%.2f", TEMPERATURE).replace('.', '_');
        File dir = new File(dirName);
        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dirName, keyword + ".txt");
        int i = 1;
        while(file.exists()){
            file = new File(dirName, keyword + "(" + i + ").txt");
            i++;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(result);
            System.out.println("File written successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public static ChatCompletionRequest generatePrompt(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(systemPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model(MODEL_NAME)
                .messages(promptMessage)
                .temperature(TEMPERATURE)
                .build();
    }

    public static void main(String[] args) {

        String apiKey = "";

        try {
            apiKey = new String(Files.readAllBytes(Paths.get("key.txt"))).trim();
            System.out.println("API Key: " + apiKey);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

        System.out.print("Type in keyword: ");
        Scanner sc = new Scanner(System.in);
        String keyword = sc.nextLine();

        String result = testCastCreation(keyword, service);
        // String result = testTranslation("", service);
        // String result = testKeywordFiltering(keyword, service);
        System.out.println(result);
        System.out.println();
        saveResultToFile(keyword, result);
    }

}