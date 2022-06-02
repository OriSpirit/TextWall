package com.spiritlight.textwall;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader {

    public static void readTextFile(String directory) {
        try {
            File file = new File(directory);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            MainMod.messages.clear();
            String line;
            message.send("Parsing data...");
            while ((line = br.readLine()) != null) {
                safeChatSize(line);
            }
            message.send("Successfully imported " + MainMod.messages.size() + " lines.");
            fr.close();
        } catch (IOException ex) {
            message.send("§cAn error has occurred: " + ex.getClass().getCanonicalName() + " (Check console for more information)");
            ex.printStackTrace();
        }
    }

    // 11pm coding be like
    private static void safeChatSize(@NotNull String message) {
        final int ml = (255 - MainMod.prefix.length());
        System.out.println("Operating with length " + message.length());
        String t = message;
        if(t.length() < ml && !t.equals("")) {
            MainMod.messages.add(message);
            return;
        }
        char[] c = {'.', ',', ' '}; // by priority
        while(t.length() > ml) {
            int temp = 255 - MainMod.prefix.length();
            int[] in = {-1, -1, -1};
            String tmp = t.substring(0, 255);
            for(int x=0; x<c.length; x++) {
                in[x] = tmp.lastIndexOf(c[x]);
            }
            for (int i : in) {
                if (i > 0) {
                    temp = i;
                    break;
                }
            }
            if(!t.substring(0, temp+1).equals(""))
                MainMod.messages.add(t.substring(0, temp+1));
            t = t.substring(temp+1).trim();
        }
        MainMod.messages.add(t);
    }
}