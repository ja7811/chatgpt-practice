package com.jhh.tester;

import com.jhh.CommonUtil;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

import static com.jhh.Globals.*;
import static com.jhh.Globals.chatGPTService;

public class CastParsingTester {
    public String testParsing(String keyword, String script){
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a sentence parser."),
                new ChatMessage(SYSTEM, "You should wrap each sentence using <>")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Hello everyone, This is a test!"),
                new ChatMessage(ASSISTANT, "<Hello everyone, This is a test!>"),
                new ChatMessage(USER, "In conclusion, the LCK continues to be a thrilling league filled with talent, competition, and passionate fans. The current season has been nothing short of exciting, with teams like T1, Gen.G, and DRX leading the charge@ As we move forward, it will be interesting to see how the standings evolve and which teams will rise to the occasion during the playoffs"),
                new ChatMessage(ASSISTANT, "<In conclusion, the LCK continues to be a thrilling league filled with talent, competition, and passionate fans.> <[>The current season has been nothing short of exciting, with teams like T1, Gen.G, and DRX leading the charge@ As we move forward, it will be interesting to see how the standings evolve and which teams will rise to the occasion during the playoffs>"),
                new ChatMessage(USER, script)
        );


        String resultScript = chatGPTService.getResult(systemPrompt, chatPrompt);
        CommonUtil.saveResultToFile(keyword, resultScript, "parse", chatGPTService.TEMPERATURE);
        return resultScript;
    }
}
