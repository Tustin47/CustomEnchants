package me.tustin.items.model;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public enum ItemProperty {

	SELL,
	REPLANT,
	RADIUS,
	ABSORPTION,

	CUBIC,
	BURN,

	ARMOR_EFFECT,
	MULTIPLIER,
	DOUBLE_DROP;


	public boolean have(JavaPlugin plugin, ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if(meta == null)return false;
		return meta.getPersistentDataContainer().has(new NamespacedKey(plugin, this.name()), PersistentDataType.INTEGER);
	}

	public int get(JavaPlugin plugin, ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		assert meta != null;
		return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, this.name()), PersistentDataType.INTEGER);
	}

	public ItemStack set(JavaPlugin plugin, ItemStack stack, int value) {
		ItemMeta meta = stack.getItemMeta();
		assert meta != null;
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(new NamespacedKey(plugin, this.name()), PersistentDataType.INTEGER, value);
		stack.setItemMeta(meta);
		return stack;
	}
}
