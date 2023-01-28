package me.tustin.items.listeners;

import me.tustin.items.model.ItemProperty;
import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FishingListener implements Listener {

	@Inject private JavaPlugin plugin;

	@EventHandler public void onFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		ItemStack tool = player.getInventory().getItemInMainHand();

		if(tool == null)return;
		if(!(event.getCaught() instanceof Item))return;
		if (ItemProperty.DOUBLE_DROP.have(plugin, tool)) {
			int value = ItemProperty.DOUBLE_DROP.get(plugin, tool);
			int random = ThreadLocalRandom.current().nextInt(99)+1;

			if(random <= value) {
				player.getInventory().addItem(((Item) event.getCaught()).getItemStack());
			}
		}

	}
}
