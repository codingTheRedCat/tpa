package net.taigamc.tpa;

import java.time.Duration;
import java.util.List;

public class TPAConfig {

    public static class Messages {

        public String requestOtherSent = "<aqua>Sent request to <yellow><player></yellow> to teleport you to them";

        public String requestSelfSent = "<aqua>Sent request to <yellow><player></yellow> to teleport them to you";

        public String tpOtherToYouSuccess = "<aqua>Teleported <yellow><player></yellow> to you";

        public String tpYouToOtherSuccess = "<aqua>Teleported to <yellow><player></yellow>";

        public List<String> tpaOtherRequest = List.of(
                "<green><yellow><player></yellow> wants to teleport to you. <gold><accept><hover:show_text:'<gold>ACCEPT REQUEST'>[ACCEPT]</hover></accept> (<timeout>)"
        );

        public List<String> tpaSelfRequest = List.of(
                "<green><yellow><player></yellow> wants to teleport you to themself. <gold><accept><hover:show_text:'<gold>ACCEPT REQUEST'>[ACCEPT]</hover></accept></gold> (<timeout>)"
        );

        public String requestCooldown = "<red>You can not send a request to <yellow><player></yellow> because you have already sent one. Please wait <yellow><remaining-time></yellow> for it to expire";

        public String noRequest = "<red>Player <yellow><player></yellow> has not sent a request to you or it has expired";
        public String playerNotFound = "<red>Player '<player>' is not on line";

        public String noPlayerArg = "<red>To few arguments: <player> is required";

        public String tooMuchArgs = "<red>To much arguments";

        public String noPermission = "<red>You need permission <permission> to do that";

        public String senderIsNotPlayer = "<red>You must be a player to do this";
    }

    public Messages messages = new Messages();

    public Duration requestTimeout = Duration.ofSeconds(60);
}
