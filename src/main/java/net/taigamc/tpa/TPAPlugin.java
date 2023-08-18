package net.taigamc.tpa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class TPAPlugin extends JavaPlugin {
    private Map<UUID, Map<UUID, TPARequest>> playersRequests = new HashMap<>();

    public AtomicReference<TPAConfig> config = new AtomicReference<>();

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        loadConfig();

        getCommand("tpareloadconfig").setExecutor((s, c, l, a) -> {
            this.loadConfig();
            return true;
        });
        new TPACommand(this);
        new TPASelfCommand(this);
        new TPAcceptCommand(this);
    }

    public void loadConfig() {
        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                throw new RuntimeException("Could not make plugin directory");
            }
        }
        final File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration fConfig = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            TPAConfigLoader.save(config.get(), fConfig);
            try {
                fConfig.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException("can not save config", e);
            }
            return;
        }
        config.set(TPAConfigLoader.load(fConfig));
    }

    public void sendRequest(Player from, Player to, int type) throws TaskExistException {
        if (type < 0 || type > 1) throw new IllegalArgumentException("type must be 0 | 1");
        Map<UUID, TPARequest> requests = this.playersRequests.computeIfAbsent(to.getUniqueId(), k -> new HashMap<>());
        if (requests.containsKey(from.getUniqueId())) throw new TaskExistException();
        final BukkitTask task = scheduleRequestTimeout(from, to);
        requests.put(from.getUniqueId(), new TPARequest(type, task));
        switch (type) {
            case 0 -> sendRequestMessage(to, from.getName(), this.config.get().messages.tpaOtherRequest);
            case 1 -> sendRequestMessage(to, from.getName(), this.config.get().messages.tpaSelfRequest);
        }
        switch (type) {
            case 0 -> sendSendConfirm(from, to.getName(), this.config.get().messages.requestOtherSent);
            case 1 -> sendSendConfirm(from, to.getName(), this.config.get().messages.requestSelfSent);
        }
    }

    private void sendRequestMessage(Player to, String from, List<String> messages) {
        messages.forEach(msg -> to.sendRichMessage(msg, TagResolver.builder()
                .tag("player", Tag.selfClosingInserting(Component.text(from)))
                .tag("timeout", Tag.selfClosingInserting(Component.text(DurationFormat.formatDuration(config.get().requestTimeout))))
                .tag("accept", Tag.styling(builder -> builder.clickEvent(ClickEvent.runCommand("/tpaccept " + from))))
                .build()));
    }

    private void sendSendConfirm(Player from, String to, String msg) {
        from.sendRichMessage(msg, TagResolver.builder()
                .tag("player", Tag.selfClosingInserting(Component.text(to)))
                .tag("timeout", Tag.selfClosingInserting(Component.text(DurationFormat.formatDuration(this.config.get().requestTimeout))))
                .build());
    }

    private BukkitTask scheduleRequestTimeout(Player from, Player to) {
        final UUID fromId = from.getUniqueId();
        final UUID toId = to.getUniqueId();
        return getServer().getScheduler().runTaskLater(this, () -> {
            final Map<UUID, TPARequest> pr = this.playersRequests.get(toId);
            if (pr == null) return;
            pr.remove(fromId);
        }, this.config.get().requestTimeout.toMillis() / 50);
    }

    public void tpAccept(Player to, Player from) throws NoRequestException {
        final Map<UUID, TPARequest> pr = this.playersRequests.get(to.getUniqueId());
        final TPARequest r;
        if (pr == null || (r = pr.remove(from.getUniqueId())) == null) throw new NoRequestException();
        r.task.cancel();
        final Player tpWho;
        final Player tpTo;
        if (r.type == TPARequest.TO_OTHER) {
            tpWho = from;
            tpTo = to;
        } else {
            tpWho = to;
            tpTo = from;
        }
        tpWho.teleport(tpTo);
        tpWho.sendRichMessage(this.config.get().messages.tpYouToOtherSuccess, TagResolver.builder()
                .tag("player", Tag.selfClosingInserting(Component.text(tpTo.getName())))
                .build());
        tpTo.sendRichMessage(this.config.get().messages.tpOtherToYouSuccess, TagResolver.builder()
                .tag("player", Tag.selfClosingInserting(Component.text(tpWho.getName())))
                .build());
    }

}
