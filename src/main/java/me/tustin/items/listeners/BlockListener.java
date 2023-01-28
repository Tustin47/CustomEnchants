package me.tustin.items.listeners;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.tustin.items.ItemsPlugin;
import me.tustin.items.model.ItemProperty;
import me.tustin.items.utils.MapUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BlockListener implements Listener {

	private final JavaPlugin plugin;
	private final Map<Material, Material> furnace;
	private final Economy economy;

	@Inject public BlockListener(JavaPlugin plugin, Economy economy) {
		this.economy = economy;
		this.plugin = plugin;
		this.furnace = MapUtils.get(plugin.getConfig().getStringList("Configuration.furnace").stream().map(it -> {
				String[] splited = it.split(":");
					return new MapUtils.Pair(Material.matchMaterial(splited[0]), Material.matchMaterial(splited[1]));
				}).collect(Collectors.toList()));
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		BlockState state = event.getBlock().getState();
		ItemStack tool = player.getInventory().getItemInMainHand();

		List<ItemStack> drops = new ArrayList<>(event.getBlock().getDrops(tool, player));
		event.setCancelled(true);
		Material type = block.getType();
		if (ItemProperty.RADIUS.have(plugin, tool)) {
			int diameter = ( ItemProperty.RADIUS.get(plugin, tool) - 1 ) / 2;
			for(int x = -diameter; x <= diameter; x++)
				for(int z = -diameter; z <= diameter; z++){
					Block current = block.getLocation().clone().add(x, 0.0, z).getBlock();
					Material material = current.getType();
					if(!(current.getState().getBlockData() instanceof Ageable))continue;
					Ageable state1 = (Ageable) current.getState().getBlockData();
					if (state1.getAge() != state1.getMaximumAge())continue;
					if (!(x == 0 && z == 0)) {
						drops.addAll(current.getDrops(tool, player));
						current.setType(Material.AIR);
						if(ItemProperty.REPLANT.have(plugin, tool))current.setType(material);
					}
				}
		}

		if(ItemProperty.CUBIC.have(plugin, tool)) {
			int diameter = ( ItemProperty.CUBIC.get(plugin, tool) - 1 ) / 2;
			for(int x = -diameter; x<=diameter; x++)
				for(int y = -diameter; y<=diameter; y++)
					for(int z = -diameter; z<=diameter; z++) {
						if (x == 0 && y == 0 && z == 0) continue;
						Block current = block.getLocation().clone().add(x, y, z).getBlock();
						drops.addAll(current.getDrops(tool, player));
						current.setType(Material.AIR);
					}
		}

		if(ItemProperty.BURN.have(plugin, tool)) {
			block.setType(Material.AIR);
			for(ItemStack stack : drops) {
				Material type2 = stack.getType();
				if(furnace.containsKey(type2)) {
					stack.setType(furnace.get(type2));
				}
			}
		}

		block.setType(Material.AIR);

		System.out.println(state.toString());
		if(ItemProperty.REPLANT.have(plugin, tool) && !block.getType().isSolid()) {
			block.setType(type);
		}

		if(ItemProperty.SELL.have(plugin, tool)) {
			for(ItemStack stack : drops){
				ShopItem shopItem = EconomyShopGUIHook.getShopItem(stack);
				double price = EconomyShopGUIHook.getItemSellPrice(shopItem, stack, player);

				double multiplier = ShopListener.getSellPrice(plugin, player);
				economy.depositPlayer(player, price*multiplier);
			}
		} else if(ItemProperty.ABSORPTION.have(plugin, tool)) {
			for(ItemStack stack : drops)player.getInventory().addItem(stack);
		} else {
			for(ItemStack stack : drops)player.getWorld().dropItemNaturally(block.getLocation(), stack);
		}
	}
}
