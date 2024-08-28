package com.jhh.tester;

import com.jhh.CommonUtil;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

import static com.jhh.Globals.*;

public class TranslationTester {
    public String testTranslation(String keyword, String script){
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates given script to korean."),
                new ChatMessage(SYSTEM, "Translate the input, sentence by sentence."),
                new ChatMessage(SYSTEM, "Each sentence in output should correspond with each sentences in input.")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "A Farewell To Arms"),
                new ChatMessage(ASSISTANT, "무기여 잘 있거라"),
                new ChatMessage(USER, "Your job is to translate the input.@ Read input sentence by sentence and translate.@ Have you understood?@"),
                new ChatMessage(ASSISTANT, "당신의 역할은 입력을 번역하는 겁니다.@ 입력을 한 문장씩 읽고 각각 번역하세요.@ 이해하셨나요?@"),
                new ChatMessage(USER, script)
        );


        String resultScript = chatGPTService.getResult(systemPrompt, chatPrompt);

        CommonUtil.saveResultToFile(keyword, resultScript, "translation", chatGPTService.TEMPERATURE);
        return resultScript;
    }

    public String testKoreanToEnglish(String keyword, String script) {
        String memberLanguage = "spanish";

        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates given script to " + memberLanguage), // TODO 회원 기능 연동
                new ChatMessage(SYSTEM, "Translation result should look natural."),
                new ChatMessage(SYSTEM, "The number of sentences of the translation result should be EQUAL to the original script."),
                new ChatMessage(SYSTEM, "'@' means end of the sentence; you should leave it be.")

        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "오늘의 팟캐스트에 오신 걸 환영합니다, 오늘은 가장 최근의 S&P 500 근황에 대해 알아보겠습니다."),
                new ChatMessage(ASSISTANT, "Welcome to today's episode of our financial news podcast, where we delve into the latest developments in the S&P 500. "),
                new ChatMessage(USER, "무기여 잘 있거라"),
                new ChatMessage(ASSISTANT, "A Farewell To Arms"),
                new ChatMessage(USER, "올림픽"),
                new ChatMessage(ASSISTANT, "オリンピック"),
                new ChatMessage(USER, "최근 자바스크립트 근황"),
                new ChatMessage(ASSISTANT, "Recent JavaScript updates"),
                new ChatMessage(USER, "coffee"),
                new ChatMessage(ASSISTANT, "coffee"),
                new ChatMessage(USER, "커피 맛있게 마시는 법"),
                new ChatMessage(ASSISTANT, "Cómo disfrutar del café"),
                new ChatMessage(USER, script)
        );

        String resultScript = chatGPTService.getResult(systemPrompt, chatPrompt);

        CommonUtil.saveResultToFile(keyword, resultScript, "translation", chatGPTService.TEMPERATURE);
        return resultScript;
    }
}
