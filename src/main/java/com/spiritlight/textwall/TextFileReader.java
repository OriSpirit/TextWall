package com.spiritlight.textwall;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
                text.add(line);
            }
            fr.close();
        } catch (IOException ex) {
            message.send("§cAn error has occurred: " + ex.getClass().getCanonicalName() + " (Check console for more information)");
            ex.printStackTrace();
        }
        return text;
    }
}