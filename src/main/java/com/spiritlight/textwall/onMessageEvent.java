package com.spiritlight.textwall;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onMessageEvent {
    @SubscribeEvent
    public void onMessage(ClientChatEvent event) {
        if(MainMod.awaitMessage && !event.getMessage().equals("/tw stop")) {
            TextComponentString s = new TextComponentString("Added " + event.getMessage() + " to message list.");
            s.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tw removeMessageFromList " + event.getMessage())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to remove this phrase from list!"))));
            MainMod.messages.add(event.getMessage());
            event.setCanceled(true);
        }
    }
}