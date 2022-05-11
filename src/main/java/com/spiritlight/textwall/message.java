package com.spiritlight.textwall;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class message {
    public static void send(String s) {
        try {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(s));
        } catch (NullPointerException ignored) {}
    }

    public static void send(TextComponentString s) {
        try {
            Minecraft.getMinecraft().player.sendMessage(s);
        } catch (NullPointerException ignored) {}
    }
}
