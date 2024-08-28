package com.jhh.tester;

import com.jhh.CommonUtil;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

import static com.jhh.Globals.*;
public class KeywordFilteringTester {

    public String testKeywordFiltering(String keyword){

        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates given input to english.")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Welcome to today's episode of our financial news podcast, where we delve into the latest developments in the S&P 500. "),
                new ChatMessage(ASSISTANT, "오늘의 팟캐스트에 오신 걸 환영합니다! 오늘은 가장 최근의 S&P 500 근황에 대해 알아보겠습니다."),
                new ChatMessage(USER, "A Farewell To Arms"),
                new ChatMessage(ASSISTANT, "무기여 잘 있거라"),
                new ChatMessage(USER, "기원 전 2000년"),
                new ChatMessage(ASSISTANT, "B.C. 2000"),
                new ChatMessage(USER, keyword)
        );

        String result = chatGPTService.getResult(systemPrompt, chatPrompt);
        System.out.println(keyword + "->" + result);
//        CommonUtil.saveResultToFile(keyword, result, "keywordFilter", chatGPTService.TEMPERATURE);
        return result;
    }
}
