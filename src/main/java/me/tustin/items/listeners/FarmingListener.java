package me.tustin.items.listeners;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.tustin.items.model.ItemProperty;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.swing.text.html.parser.Entity;

public class FarmingListener implements Listener {

	@Inject
	private JavaPlugin plugin;

	@Inject
	private Economy economy;

	@EventHandler
	public void onKill(EntityDeathEvent event) {

		LivingEntity ent = event.getEntity();
		Player player = ent.getKiller();
		if(player == null)return;

		ItemStack tool = player.getInventory().getItemInMainHand();

		if(ItemProperty.SELL.have(plugin, tool)) {
			for(ItemStack stack : event.getDrops()){
				ShopItem shopItem = EconomyShopGUIHook.getShopItem(stack);
				if(shopItem == null)continue;
				double price = EconomyShopGUIHook.getItemSellPrice(shopItem, stack, player);

				double multiplier = ShopListener.getSellPrice(plugin, player);
				economy.depositPlayer(player, price * multiplier);
			}
			event.getDrops().clear();
		}
	}
}
