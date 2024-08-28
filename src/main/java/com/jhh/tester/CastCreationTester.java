package com.jhh.tester;

import com.jhh.CommonUtil;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

import static com.jhh.Globals.*;

public class CastCreationTester {

    public String testCastCreation(String keyword){
        List<ChatMessage> systemPrompt = List.of(

                new ChatMessage(SYSTEM, "You are the host of the podcast."),
                new ChatMessage(SYSTEM, "Your job is to make a podcast script about what happened recently. It is best if you say things based on real news"),
                new ChatMessage(SYSTEM, "Script should only contain what you have to say (no markdowns or background musics)"),
                new ChatMessage(SYSTEM, "Seperate each sentence using '@'."),
                new ChatMessage(SYSTEM, "Answer in " + "official" + " manner."),
                new ChatMessage(SYSTEM, "Answer in english.")



//                 new ChatMessage(SYSTEM, "You are the host of the podcast."), // 역할 부여
//                new ChatMessage(SYSTEM, "Answer should only contain what you have to say (no markdowns or background musics)"), // 형식 지정
//                new ChatMessage(SYSTEM, "You should add " + "@" + " at each end of sentences!!!!!!!"), // 문장 분리
//                new ChatMessage(SYSTEM, "Answer in " + "casual" + " manner."), // 격식 설정 (official, casual)
//                new ChatMessage(SYSTEM, "The answer should be in " + "english") // 언어 설정 (English, Spanish, Japanese, ...)

        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Make a " + 3 + " minute podcast script about " + keyword + "; Use around " +  3 * 130 + " words.")
        );


        String resultScript = chatGPTService.getResult(systemPrompt, chatPrompt);

        CommonUtil.saveResultToFile(keyword, resultScript, "castCreation", chatGPTService.TEMPERATURE);
        return resultScript;
    }
}
