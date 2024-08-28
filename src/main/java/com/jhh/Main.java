package com.jhh;

import com.jhh.tester.CastCreationTester;
import com.jhh.tester.CastParsingTester;
import com.jhh.tester.KeywordFilteringTester;
import com.jhh.tester.TranslationTester;
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
    public static void main(String[] args){
        CastCreationTester castCreationTester = new CastCreationTester();
        TranslationTester translationTester = new TranslationTester();
        CastParsingTester castParsingTester = new CastParsingTester();
        KeywordFilteringTester keywordFilteringTester = new KeywordFilteringTester();

        Scanner sc = new Scanner(System.in);
        System.out.print("Type in keyword: ");
        String keyword = sc.nextLine();

        String castResult = "", translationresult = "", keywordResult = "", parsedResult = "";

//        keyword = keywordFilteringTester.testKeywordFiltering(keyword);
        castResult = castCreationTester.testCastCreation(keyword);
//        parsedResult = castParsingTester.testParsing(keyword, castResult);
        translationresult = translationTester.testTranslation(keyword, castResult);
//        translationresult = translationTester.testKoreanToEnglish(keyword, keyword);

        System.out.println(castResult);
        System.out.println("\n\n");
        System.out.println(parsedResult);
        System.out.println("\n\n");
        System.out.println(translationresult);
    }

}