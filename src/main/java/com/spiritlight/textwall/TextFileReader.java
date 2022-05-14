package com.spiritlight.textwall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFileReader {

    static List<String> readTextFile(String directory) {
        List<String> text = new ArrayList<>();
        try {
            File file = new File(directory);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                List<String> l = safeChatSize(line);
                MainMod.messages.addAll(l);
            }
            fr.close();
        } catch (IOException ex) {
            message.send("§cAn error has occurred: " + ex.getClass().getCanonicalName() + " (Check console for more information)");
            ex.printStackTrace();
        }
        return text;
    }

    // 11pm coding be like
    private static List<String> safeChatSize(String message) {
        List<String> l = new ArrayList<>();
        if(message.length() < 255)
            return Collections.singletonList(message);
        int len = message.length();
        int ml = (256 - MainMod.prefix.length());
        // int mi = len / ml - (len % ml == 0 ? 0 : 1);
        int sc = 255;
        char[] c = {'.', ',', ' '}; // by priority
        while(len > (ml - MainMod.prefix.length())) {
            int m;
            int n = 256;
            int[] in = {-1, -1, -1};
            for(int x=0; x<c.length; x++) {
                m = message.indexOf(c[x]); // acq int ind
                if(ml - m < n && m < ml) { // we want close to 0 as possible
                    in[x] = m;
                }
                n = m;
            }
            for (int i : in) {
                if (i != -1) {
                    sc = i;
                    break;
                }
            }
            l.add(message.substring(0, sc));
            message = message.substring(sc);
        }
        return l;
    }
}