package com.jhh;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    static final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    static final String MODEL_NAME = "gpt-4o-mini"; // "gpt-4o";
    static final double TEMPERATURE = 0.4;
    static OpenAiService service;


    static String getOpenAiResult(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        Long startTime = System.currentTimeMillis();
        ChatCompletionRequest request = generatePrompt(systemPrompt, chatPrompt);
        ChatCompletionResult result = service.createChatCompletion(request);
        Long endTime = System.currentTimeMillis();
        System.out.printf("Prompt consumed %d tokens total (%ds)%n", result.getUsage().getTotalTokens(), (endTime-startTime)/1000);
        return result.getChoices().get(0).getMessage().getContent();
    }

    static String testKeywordFiltering(String keyword){

        // 일단 번역부터
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates english to korean."),
                new ChatMessage(SYSTEM, "Translation result should look natural")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Welcome to today's episode of our financial news podcast, where we delve into the latest developments in the S&P 500. "),
                new ChatMessage(ASSISTANT, "오늘의 팟캐스트에 오신 걸 환영합니다! 오늘은 가장 최근의 S&P 500 근황에 대해 알아보겠습니다."),
                new ChatMessage(USER, "A Farewell To Arms"),
                new ChatMessage(ASSISTANT, "무기여 잘 있거라"),
                new ChatMessage(USER, keyword)
        );

        String resultScript = getOpenAiResult(systemPrompt, chatPrompt);
        saveResultToFile(keyword, resultScript, "keywordFilter");
        return resultScript;

    }

    static String testCastCreation(String keyword){

        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are the host of the podcast."),
                new ChatMessage(SYSTEM, "Your job is to make a podcast script about what happened recently. It is best if you say things based on real news"),
                new ChatMessage(SYSTEM, "script should only contain what you have to say (no markdowns or background musics)"),
                new ChatMessage(SYSTEM, "script should be " + 10 + " minutes long."),
                new ChatMessage(SYSTEM, "Put '.\n@' at the end of each sentence"),
                new ChatMessage(SYSTEM, "Answer in " + "official" + " manner."),
                new ChatMessage(SYSTEM, "The script should be in english")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Make a podcast script about " + keyword)
        );


        String resultScript = getOpenAiResult(systemPrompt, chatPrompt);

        saveResultToFile(keyword, resultScript, "castCreation");
        return resultScript;
    }

    static String testTranslation(String keyword, String script){
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates english to korean."),
                new ChatMessage(SYSTEM, "Translation result should look natural."),
                new ChatMessage(SYSTEM, "'@' means end of the sentence; you should leave it be.")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Welcome to today's episode of our financial news podcast, where we delve into the latest developments in the S&P 500. @"),
                new ChatMessage(ASSISTANT, "오늘의 팟캐스트에 오신 걸 환영합니다! 오늘은 가장 최근의 S&P 500 근황에 대해 알아보겠습니다.@"),
                new ChatMessage(USER, "A Farewell To Arms"),
                new ChatMessage(ASSISTANT, "무기여 잘 있거라"),
                new ChatMessage(USER, "Welcome! @ This is a test script! @ Thank you! @"),
                new ChatMessage(ASSISTANT, "환영합니다! @ 이건 테스트용 스크립트입니다! @ 감사합니다! @"),
                new ChatMessage(USER, script)
        );


        String resultScript = getOpenAiResult(systemPrompt, chatPrompt);

        saveResultToFile(keyword, resultScript, "translation");
        return resultScript;
    }


    static void saveResultToFile(String keyword, String result, String testDescription){
        String dirName = String.format("%.2f", TEMPERATURE).replace('.', '_');
        File dir = new File(dirName);
        if(!dir.exists()){
            dir.mkdir();
        }

        String filename = keyword.replace(" ", "-").replace('\"', '-') + "-" + testDescription;
        File file = new File(dirName, filename + ".txt");
        int i = 1;
        while(file.exists()){
            file = new File(dirName, filename + "(" + i + ").txt");
            i++;
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(result);
            System.out.println("File written successfully: " + file.getAbsolutePath());
            writer.close();
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

        service = new OpenAiService(apiKey, Duration.ofSeconds(30));

        System.out.print("Type in keyword: ");
        Scanner sc = new Scanner(System.in);
        String keyword = sc.nextLine();

        String castResult = "", translationResult = "", filterResult = "";
        castResult = testCastCreation(keyword);
        translationResult = testTranslation(keyword, castResult);
//        filterResult = testKeywordFiltering(keyword);
        System.out.println(castResult);
        System.out.println(translationResult);
        System.out.println(filterResult);

    }

}