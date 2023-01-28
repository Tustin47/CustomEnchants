package me.tustin.items.task;

import me.tustin.items.ItemsPlugin;
import me.tustin.items.model.ItemProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorRunnable extends BukkitRunnable {

	private final JavaPlugin plugin;
	private final List<PotionEffect> potions;

	@Inject
	public ArmorRunnable(JavaPlugin plugin) {
		this.plugin = plugin;
		this.potions = new ArrayList<>();

		plugin.getConfig().getStringList("Configuration.effects").stream().forEach(it -> {
			String[] splited = it.split(":");
			potions.add(new PotionEffect(PotionEffectType.getByName(splited[0]), 10, Integer.parseInt(splited[1])-1, false, false));
		});
	}

	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			ItemStack[] armor = player.getInventory().getArmorContents();

			int index = -1;
			boolean none = false;

			for(ItemStack item : armor) {
				if(item == null){
					none = true;
					break;
				}
				if(!ItemProperty.ARMOR_EFFECT.have(plugin, item)) {
					none = true;
					break;
				}
				int value = ItemProperty.ARMOR_EFFECT.get(plugin, item);
				if(index == -1) index = value;
				if(index != value) {
					none = true;
					break;
				}
			}

			if(none) continue;

			PotionEffect effect = potions.get(index-1);
			Bukkit.getScheduler().runTask(plugin, new Runnable(){
				@Override
				public void run() {
					player.addPotionEffect(effect);
				}
			});
		}
	}
}
