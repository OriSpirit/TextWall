package com.spiritlight.textwall;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class CommandHandler extends CommandBase {
    static String fileDestination = "";
    private boolean saf = false;

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull String getName() {
        return "tw";
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull String getUsage(ICommandSender sender) {
        return "/tw";
    }

    @ParametersAreNonnullByDefault
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            message.send("/tw record - Starts recording messages");
            message.send("/tw stop - Stops recording messages");
            message.send("/tw clear - Clears messages");
            message.send("/tw list - Lists all recorded messages stored");
            message.send("/tw load - Loads message from text files (local)");
            message.send("/tw reload - Reloads current loaded file");
            message.send("/tw send [delay] - Sends messages with delay in ms");
            message.send("/tw setprefix [prefix] - Sets prefix before message sent");
            message.send("protip: use ${space} for space in the ending");
            message.send("Prefix defined? " + (MainMod.prefix.equals("") ? false : "true: '" + MainMod.prefix + "'"));
            message.send("Path defined? " + (fileDestination.equals("") ? false : "true: '" + fileDestination + "'"));
        } else switch(args[0]) {
            case "setprefix":
                if(args.length == 1) {
                    message.send("§aCleared prefix.");
                    MainMod.prefix = "";
                    ConfigSpirit.writeConfig();
                    break;
                }
                String[] msg = Arrays.copyOfRange(args, 1, args.length);
                String s = String.join(" ", msg).replace("${space}", " ");
                if(s.length() > 120) {
                    message.send("Prefix cannot be longer than 120 characters.");
                    break;
                }
                MainMod.prefix = s;
                message.send("§aPrefix set as §b" + s);
                ConfigSpirit.writeConfig();
                break;
            case "record":
                MainMod.awaitMessage = true;
                MainMod.messages = new ArrayList<>();
                message.send("§aStarted recording message, use /tw stop to stop. Send any message to record.");
                break;
            case "stop":
                MainMod.awaitMessage = false;
                message.send("§aStopped recording message, use /tw send <delay/no args> to send");
                //message.send("");
                break;
            case "clear":
                MainMod.messages = new ArrayList<>();
                message.send("§aCleared message list");
                break;
            case "load":
                MainMod.messages = new ArrayList<>();
                if (args.length == 1) {
                    message.send("§cYou must supply a file path.");
                    break;
                }
                String[] arr = Arrays.copyOfRange(args, 1, args.length);
                fileDestination = String.join(" ", arr).trim();
                message.send("§aOpening file " + fileDestination);
                TextFileReader.readTextFile(fileDestination);
                ConfigSpirit.writeConfig();
                break;
            case "list":
                for(int i=0; i<MainMod.messages.size(); i++) {
                    TextComponentString st = new TextComponentString("§a[" + i + "] §b" + MainMod.messages.get(i));
                    st.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tw removeMessageFromList " + MainMod.messages.get(i))).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to remove this phrase from list!"))));
                    message.send(st);
                }
                break;
            case "reload":
                if(fileDestination.length() >= 1) {
                    TextFileReader.readTextFile(fileDestination);
                    message.send("Reloaded text file!");
                }
                else
                    message.send("§cFile destination must be set first.");
                break;
            case "send":
                if(saf) {
                    message.send("Please wait for current message session to end.");
                    break;
                }
                saf = true;
                if(MainMod.messages.isEmpty()) {
                    message.send("§cUnable to send empty message.");
                    break;
                }
                int delay;
                try {
                    delay = Integer.parseInt(args[1]);
                } catch (IndexOutOfBoundsException|NumberFormatException ignored) {
                    delay = 0;
                }
                message.send("§aSending message!");
                if(MainMod.messages.size() >= 10) {
                    message.send("Your message:delay is too large, it has been set to 200ms.");
                    message.send("Consider lowering your message count or increase the delay as you can get kicked for spamming.");
                    delay = 200;
                }
                int finalDelay = delay;
                CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < MainMod.messages.size(); i++) {
                        Minecraft.getMinecraft().player.sendChatMessage(MainMod.prefix + MainMod.messages.get(i));
                        if(finalDelay != 0) {
                            try {
                                //noinspection BusyWait
                                Thread.sleep(finalDelay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                saf = false;
                break;
            case "removeMessageFromList":
                try {
                    String[] str = Arrays.copyOfRange(args, 1, args.length);
                    String st2 = String.join(" ", str);
                    MainMod.messages.remove(st2);
                    message.send("§aRemoved message " + st2 + " from list!");
                } catch (IndexOutOfBoundsException ignored) {}
                break;
            default:
                message.send("§cUnknown command, try /tw");
                break;
        }
    }
}
