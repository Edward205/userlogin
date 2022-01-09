package com.elchologamer.userlogin.command.base;

import com.elchologamer.userlogin.UserLogin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler extends BaseCommand {

    private final UserLogin plugin = UserLogin.getPlugin();
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CommandHandler(String name) {
        super(name);
    }

    @Override
    public boolean run(CommandSender sender, String label, String[] args) {
        if (args.length == 0) return false;

        for (SubCommand subCommand : subCommands) {
            if (!subCommand.getName().equals(args[0])) continue;

            // Check that player has permission
            String perm = subCommand.getPermission();
            if (perm != null && !sender.hasPermission(perm)) {
                sender.sendMessage(plugin.getLang().getMessage("commands.errors.no_permission"));
                return true;
            }

            return subCommand.run(sender, label, getSubArgs(args));
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            for (SubCommand subCommand : subCommands) {
                options.add(subCommand.getName());
            }
        } else {
            for (SubCommand subCommand : subCommands) {
                if (!subCommand.getName().equals(args[0])) continue;

                options = subCommand.tabComplete(sender, label, getSubArgs(args));
                break;
            }
        }

        // Filter out list
        options.removeIf(s -> !s.startsWith(args[args.length - 1]));

        return options;
    }

    private String[] getSubArgs(String[] args) {
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subArgs;
    }

    public void add(SubCommand subCommand) {
        subCommands.add(subCommand);
    }
}