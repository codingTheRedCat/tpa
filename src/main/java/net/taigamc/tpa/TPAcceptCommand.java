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

public class TPAcceptCommand implements TabExecutor {

    private final TPAPlugin plugin;

    public TPAcceptCommand(TPAPlugin plugin) {
        this.plugin = plugin;
        final PluginCommand cmd = plugin.getCommand("tpaccept");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tpa.accept")) {
            sender.sendRichMessage(plugin.config.get().messages.noPermission, TagResolver.builder()
                    .tag("permission", Tag.selfClosingInserting(Component.text("tpa.accept")))
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
            plugin.tpAccept(p, target);
        } catch (NoRequestException e) {
            p.sendRichMessage(plugin.config.get().messages.noRequest, TagResolver.builder()
                    .tag("player", Tag.selfClosingInserting(Component.text(target.getName())))
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
