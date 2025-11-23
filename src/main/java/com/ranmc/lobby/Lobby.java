package com.ranmc.lobby;

import java.util.ArrayList;

import cc.baka9.catseedlogin.bukkit.CatSeedLoginAPI;
import cc.baka9.catseedlogin.bukkit.event.CatSeedPlayerLoginEvent;
import cc.baka9.catseedlogin.bukkit.event.CatSeedPlayerRegisterEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class Lobby extends JavaPlugin implements Listener {

    private static ItemStack item;

	@Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§dLobby By Ranica.");
        Bukkit.getPluginManager().registerEvents(this, this);

        item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c❆ §e§l选择服务器 §c❆");
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add("§7点击打开菜单");
        meta.setLore(Lore);
        item.setItemMeta(meta);
    }
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(4, item.clone());
		player.setGameMode(GameMode.SPECTATOR);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.getPlayer().chat("/cd");
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		event.getPlayer().chat("/cd");
	}

    @EventHandler
    public void onCatSeedPlayerLoginEvent(CatSeedPlayerLoginEvent event) {
        Player player = event.getPlayer();
        player.chat("/cd");
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onCatSeedPlayerRegisterEvent(CatSeedPlayerRegisterEvent event) {
        Player player = event.getPlayer();
        player.chat("/cd");
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (CatSeedLoginAPI.isLogin(player.getName()) &&
                (to.getX() != from.getX() || to.getZ() != from.getZ()) &&
                player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
            player.chat("/cd");
        }
    }
}
