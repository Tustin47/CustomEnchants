package me.tustin.items.module;

import com.google.inject.AbstractModule;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultModule extends AbstractModule {

	private final JavaPlugin plugin;
	private final Economy economy;

	public DefaultModule(JavaPlugin plugin, Economy economy) {
		this.plugin = plugin;
		this.economy = economy;
	}

	@Override
	protected void configure() {
		bind(JavaPlugin.class).toInstance(plugin);
		bind(Economy.class).toInstance(economy);
	}
}
