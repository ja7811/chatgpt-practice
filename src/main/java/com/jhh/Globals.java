package com.jhh;

import com.theokanning.openai.completion.chat.ChatMessageRole;

public class Globals {
    public static final String USER = ChatMessageRole.USER.value();
    public static final String SYSTEM = ChatMessageRole.SYSTEM.value();
    public static final String ASSISTANT = ChatMessageRole.ASSISTANT.value();

    public static ChatGPTService chatGPTService;

    static {
        System.out.println("init Globals");
        chatGPTService = new ChatGPTService();
    }
}
