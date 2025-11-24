package com.ranmc.lobby;

import java.util.ArrayList;
import java.util.List;

import cc.baka9.catseedlogin.bukkit.CatSeedLoginAPI;
import cc.baka9.catseedlogin.bukkit.event.CatSeedPlayerLoginEvent;
import cc.baka9.catseedlogin.bukkit.event.CatSeedPlayerRegisterEvent;
import com.viaversion.viaversion.api.Via;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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

    private static void sendDialog(Player player) {
        if (Via.getAPI().getPlayerVersion(player) < 771) return;
        if (CatSeedLoginAPI.isRegister(player.getName())) {
            player.showDialog(Dialog.create(builder -> builder
                    .empty()
                    .base(DialogBase.builder(Component.text("登陆账号 : " + player.getName()))
                            .canCloseWithEscape(false)
                            .inputs(List.of(
                                    DialogInput.text("password", Component.text("登陆密码")).build()
                            )).build()
                    )
                    .type(DialogType.notice(ActionButton.builder(Component.text("确认"))
                            .action(DialogAction.customClick((response, audience) -> {
                                String pwd = response.getText("password");
                                player.chat("/l " + pwd);
                            }, ClickCallback.Options.builder().lifetime(ClickCallback.DEFAULT_LIFETIME).build())).build()
                    ))));
        } else {
            player.showDialog(Dialog.create(builder -> builder
                    .empty()
                    .base(DialogBase.builder(Component.text("注册账号 : " + player.getName()))
                            .canCloseWithEscape(false)
                            .inputs(List.of(
                                    DialogInput.text("password", Component.text("密码")).build(),
                                    DialogInput.text("password2", Component.text("重复密码")).build()
                            )).build()
                    )
                    .type(DialogType.notice(ActionButton.builder(Component.text("确认"))
                            .action(DialogAction.customClick((response, audience) -> {
                                String pwd = response.getText("password");
                                String pwd2 = response.getText("password2");
                                player.chat("/reg " + pwd + " " + pwd2);
                            }, ClickCallback.Options.builder().lifetime(ClickCallback.DEFAULT_LIFETIME).build())).build()
                    ))));
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (!CatSeedLoginAPI.isLogin(player.getName())) {
                sendDialog(player);
            }
        }, 20);
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(4, item.clone());
		player.setGameMode(GameMode.SPECTATOR);
        sendDialog(player);
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
