package com.core.wolfbadger.antiTeaming;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: MayoDwarf
 * Date: 6/8/14
 * Time: 8:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main extends JavaPlugin implements Listener {
    API api;
    private FileConfiguration config;
    public HashMap<UUID, UUID> versing = new HashMap<UUID, UUID>();
    public void onEnable() {
        api = new API(this);
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                if(api.inMatch.size() >= 2 && versing.size() >= 2) {
                for(UUID ids : api.inMatch) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        Player p = Bukkit.getPlayer(ids);
                        if(api.inMatch.contains(p.getUniqueId())) {
                        if(!p.getUniqueId().equals(players.getUniqueId()) && !versing.get(p.getUniqueId()).equals(players.getUniqueId())) {
                            p.hidePlayer(players);
                            UUID id = versing.get(p.getUniqueId());
                            Player p2 = Bukkit.getPlayer(id);
                            p2.hidePlayer(players);
                            if(!players.hasPermission("AntiTeaming.Admin")) {
                            players.hidePlayer(p2);
                            players.hidePlayer(p);
                            }
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                if(api.count.size() >= 1) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(api.count.containsKey(p.getUniqueId())) {
                        api.count.put(p.getUniqueId(), api.count.get(p.getUniqueId())-1);
                        if(api.count.get(p.getUniqueId()) <= 0) {
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(players);
                                players.showPlayer(p);
                                api.count.remove(p.getUniqueId());
                                versing.remove(p.getUniqueId());
                                api.inMatch.remove(p.getUniqueId());
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 20);
    }
    public void onDisable() {}
    @EventHandler
    public void onFight(EntityDamageByEntityEvent evt) {
        Entity e = evt.getEntity();
        Entity damager = evt.getDamager();
        if(damager instanceof Player && e instanceof Player) {
            Player p1 = (Player) e;
            Player p2 = (Player) damager;
            api.setVersing(p1, p2);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent evt) {
        Player p = evt.getEntity();
        if(versing.containsKey(p.getUniqueId())) {
            UUID s = versing.get(p.getUniqueId());
            Player p2 = Bukkit.getPlayer(s);
            versing.remove(p.getUniqueId());
            versing.remove(p2.getUniqueId());
            api.inMatch.remove(p.getUniqueId());
            api.inMatch.remove(p2.getUniqueId());
            api.count.remove(p.getUniqueId());
            api.count.remove(p2.getUniqueId());
            for(Player players : Bukkit.getOnlinePlayers()) {
                p.showPlayer(players);
                p2.showPlayer(players);
                players.showPlayer(p);
                players.showPlayer(p2);
            }
        }
    }
}
