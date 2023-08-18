package net.taigamc.tpa;

import org.bukkit.scheduler.BukkitTask;

public class TPARequest {

    public static int TO_OTHER = 0;

    public static int TO_SELF = 1;

    public int type;

    public BukkitTask task;

    public TPARequest(int type, BukkitTask task) {
        this.type = type;
        this.task = task;
    }
}
