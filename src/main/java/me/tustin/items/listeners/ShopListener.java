package me.tustin.items.listeners;

import com.google.common.collect.Lists;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Transaction;
import me.tustin.items.model.ItemProperty;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class ShopListener implements Listener {

	private static final List<Transaction.Type> SELL_TYPES = Arrays.asList(Transaction.Type.SELL_SCREEN, Transaction.Type.SELL_GUI_SCREEN, Transaction.Type.SELL_ALL_SCREEN, Transaction.Type.SELL_ALL_COMMAND);
	private final JavaPlugin plugin;
	private Economy economy;

	@Inject
	public ShopListener(JavaPlugin plugin, Economy economy) {
		this.plugin = plugin;
		this.economy = economy;
	}

	@EventHandler()
	public void onSell(PreTransactionEvent event) {
		if(!SELL_TYPES.contains(event.getTransactionType()))return;
		Player player = event.getPlayer();

		double multiplier = getSellPrice(plugin, player);
		economy.depositPlayer(player, event.getPrice() * multiplier-event.getPrice());
	}

	public static double getSellPrice(JavaPlugin plugin, Player player) {

		ItemStack[] armor = player.getInventory().getArmorContents();
		double multiplier = -1;

		for(ItemStack stack : armor) {
			if(stack == null)return 1;
			if(!ItemProperty.MULTIPLIER.have(plugin, stack))return 1;
			int current = ItemProperty.MULTIPLIER.get(plugin, stack);
			if(multiplier == -1)multiplier = current;
			if(multiplier != current)return 1;
		}

		return multiplier*0.01;
	}
}
