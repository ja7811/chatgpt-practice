package com.jhh;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    public static void saveResultToFile(String keyword, String result, String testDescription, double temperature){
        String dirName = String.format("%.2f", temperature).replace('.', '_');
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

}
