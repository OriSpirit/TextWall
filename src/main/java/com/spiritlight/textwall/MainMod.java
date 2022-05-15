package com.spiritlight.textwall;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = MainMod.MODID, name = MainMod.NAME, version = MainMod.VERSION)
public class MainMod
{
    public static final String MODID = "textwall";
    public static final String NAME = "Textwalls";
    public static final String VERSION = "1.0";
    static boolean awaitMessage = false;
    static List<String> messages = new ArrayList<>();
    static String prefix = "";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigSpirit.getConfig();
        if(CommandHandler.fileDestination.length() != 0) {
            TextFileReader.readTextFile(CommandHandler.fileDestination);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new CommandHandler());
        MinecraftForge.EVENT_BUS.register(new onMessageEvent());
    }

}
