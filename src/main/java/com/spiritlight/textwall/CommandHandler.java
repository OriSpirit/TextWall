package com.spiritlight.textwall;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class CommandHandler extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @ParametersAreNonnullByDefault
    @Override
    public String getName() {
        return "tw";
    }

    @ParametersAreNonnullByDefault
    @Override
    public String getUsage(ICommandSender sender) {
        return "/tw";
    }

    @ParametersAreNonnullByDefault
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            message.send("/tw record - Starts recording messages");
            message.send("/tw stop - Stops recording messages");
            message.send("/tw clear - Clears messages");
            message.send("/tw load - Loads message from text files (local)");
            message.send("/tw send [delay] - Sends messages with delay in ms");
        } else switch(args[0]) {
            case "record":
                MainMod.awaitMessage = true;
                MainMod.messages = new ArrayList<>();
                message.send("Started recording message, use /tw stop to stop. Send any message to record.");
                break;
            case "stop":
                MainMod.awaitMessage = false;
                message.send("Stopped recording message, use /tw send <delay/no args> to send");
                //message.send("");
                break;
            case "clear":
                MainMod.messages = new ArrayList<>();
                message.send("Cleared message list");
                break;
            case "load":
                MainMod.messages = new ArrayList<>();
                if (args.length == 1) {
                    message.send("You must supply a file path.");
                    break;
                }
                String[] arr = Arrays.copyOfRange(args, 1, args.length);
                String fileDestination = String.join(" ", arr);
                fileDestination = fileDestination.trim();
                message.send("Opening file " + fileDestination);
                MainMod.messages = TextFileReader.readTextFile(fileDestination);
                break;
            case "send":
                if(MainMod.messages.isEmpty()) {
                    message.send("Unable to send empty message.");
                    break;
                }
                int delay;
                try {
                    delay = Integer.parseInt(args[1]);
                } catch (IndexOutOfBoundsException|NumberFormatException ignored) {
                    delay = 0;
                }
                message.send("Sending message!");
                int finalDelay = delay;
                CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < MainMod.messages.size(); i++) {
                        Minecraft.getMinecraft().player.sendChatMessage(MainMod.messages.get(i));
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
                break;
            case "removeMessageFromList":
                try {
                    String[] str = Arrays.copyOfRange(args, 1, args.length);
                    String st2 = String.join(" ", str);
                    MainMod.messages.remove(st2);
                    message.send("Removed message " + st2 + " from list!");
                } catch (IndexOutOfBoundsException ignored) {}
            default:
                break;
        }
    }
}
