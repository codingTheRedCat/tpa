package net.taigamc.tpa;

import org.bukkit.configuration.Configuration;

import java.text.ParseException;

public class TPAConfigLoader {

    public static void save(TPAConfig config, Configuration target) {
        target.set("messages.request-other-sent", config.messages.requestOtherSent);
        target.set("messages.request-self-sent", config.messages.requestSelfSent);
        target.set("messages.tp-other-to-you-success", config.messages.tpOtherToYouSuccess);
        target.set("messages.tp-you-to-other-success", config.messages.tpYouToOtherSuccess);
        target.set("messages.tpa-other-request", config.messages.tpaOtherRequest);
        target.set("messages.tpa-self-request", config.messages.tpaSelfRequest);
        target.set("messages.request-cooldown", config.messages.requestCooldown);
        target.set("messages.no-request", config.messages.noRequest);
        target.set("messages.player-not-found", config.messages.playerNotFound);
        target.set("messages.no-player-arg", config.messages.noPlayerArg);
        target.set("messages.too-much-args", config.messages.tooMuchArgs);
        target.set("messages.no-permission", config.messages.noPermission);
        target.set("messages.sender-is-not-player", config.messages.senderIsNotPlayer);

        target.set("request-timeout", DurationFormat.formatDuration(config.requestTimeout));
    }

    public static TPAConfig load(Configuration from) {
        final TPAConfig config = new TPAConfig();
        config.messages.requestOtherSent = from.getString("messages.request-other-sent");
        config.messages.requestSelfSent = from.getString("messages.request-self-sent");
        config.messages.tpOtherToYouSuccess = from.getString("messages.tp-other-to-you-success");
        config.messages.tpYouToOtherSuccess = from.getString("messages.tp-you-to-other-success");
        config.messages.tpaOtherRequest = from.getStringList("messages.tpa-other-request");
        config.messages.tpaSelfRequest = from.getStringList("messages.tpa-self-request");
        config.messages.requestCooldown = from.getString("messages.request-cooldown");
        config.messages.noRequest = from.getString("messages.no-request");
        config.messages.playerNotFound = from.getString("messages.player-not-found");
        config.messages.noPlayerArg = from.getString("messages.messages.no-player-args");
        config.messages.tooMuchArgs = from.getString("messages.too-much-args");
        config.messages.noPermission = from.getString("messages.no-permission");
        config.messages.senderIsNotPlayer = from.getString("messages.sender-is-not-player");

        try {
            config.requestTimeout = DurationFormat.parseDuration(from.getString("request-timeout"));
        } catch (ParseException e) {
            throw new RuntimeException("Wrong duration format at config.yml:request-timeout use XdXhXmXs you can skip units to first non zero");
        }

        return config;
    }
}
