package me.tustin.items.command;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import me.tustin.items.model.ItemProperty;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

@Command("customenchants")
public class MainCommand extends BaseCommand {

	@Inject private JavaPlugin plugin;

	@SubCommand("set")
	public void set(CommandSender sender, ItemProperty property, int value) {
		if(!(sender instanceof Player player))return;
		ItemStack item = player.getInventory().getItemInMainHand();
		property.set(plugin, item, value);
		player.sendMessage("§7Tu as appliqué une propriété à ton item.");
	}

	@SubCommand("info")
	public void info(CommandSender sender) {
		if(!(sender instanceof Player player))return;
		ItemStack item = player.getInventory().getItemInMainHand();
		player.sendMessage("§6Propriétées de l'item :");
		for(ItemProperty property : ItemProperty.values()) {
			if(property.have(plugin, item)) {
				player.sendMessage(" §7- " + property.name() + " : " + property.get(plugin, item));
			}
		}
	}
}
