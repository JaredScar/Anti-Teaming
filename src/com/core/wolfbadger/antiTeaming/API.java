package com.core.wolfbadger.antiTeaming;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: MayoDwarf
 * Date: 6/6/14
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class API {
    Main m;
    public API(Main m) {
        this.m = m;
    }
    public HashSet<UUID> inMatch = new HashSet<UUID>();
    public HashMap<UUID, Integer> count = new HashMap<UUID, Integer>();
    public void setVersing(Player starter, Player p) {
        m.versing.put(starter.getUniqueId(), p.getUniqueId());
        m.versing.put(p.getUniqueId(), starter.getUniqueId());
        inMatch.add(starter.getUniqueId());
        inMatch.add(p.getUniqueId());
        count.put(p.getUniqueId(), m.getConfig().getInt("Time"));
        count.put(starter.getUniqueId(), m.getConfig().getInt("Time"));
        if(!m.versing.containsKey(starter.getUniqueId())) {
        for(Player players : Bukkit.getOnlinePlayers()) {
            if(!players.getUniqueId().equals(starter.getUniqueId()) && !players.getUniqueId().equals(p.getUniqueId())) {
            starter.hidePlayer(players);
            if(!players.hasPermission("AntiTeaming.Admin")) {
            players.hidePlayer(starter);
            players.hidePlayer(p);
            }
            p.hidePlayer(players);
                }
            }
        }
    }
}
