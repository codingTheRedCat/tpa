package net.taigamc.tpa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class TPACommand implements TabExecutor {

    private final TPAPlugin plugin;

    public TPACommand(TPAPlugin plugin) {
        this.plugin = plugin;
        final PluginCommand cmd = plugin.getCommand("tpa");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tpa.request.other")) {
            sender.sendRichMessage(plugin.config.get().messages.noPermission, TagResolver.builder()
                    .tag("permission", Tag.selfClosingInserting(Component.text("tpa.request.other")))
                    .build());
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendRichMessage(plugin.config.get().messages.senderIsNotPlayer);
            return true;
        }
        final Player p = (Player) sender;
        if (args.length < 1) {
            sender.sendRichMessage(plugin.config.get().messages.noPlayerArg);
            return true;
        }
        if (args.length > 1) {
            sender.sendRichMessage(plugin.config.get().messages.tooMuchArgs);
            return true;
        }
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendRichMessage(plugin.config.get().messages.playerNotFound, TagResolver.builder()
                    .tag("player", Tag.selfClosingInserting(Component.text(args[0])))
                    .build());
            return true;
        }
        try {
            plugin.sendRequest(p, target, TPARequest.TO_OTHER);
        } catch (TaskExistException e) {
            sender.sendRichMessage(plugin.config.get().messages.requestCooldown, TagResolver.builder()
                    .tag("player", Tag.selfClosingInserting(Component.text(target.getName())))
                    .tag("remaining-time", Tag.selfClosingInserting(Component.text(DurationFormat.formatDuration(plugin.config.get().requestTimeout))))
                    .build());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return null;
        return Collections.emptyList();
    }
}
