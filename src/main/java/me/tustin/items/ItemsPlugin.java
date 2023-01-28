package me.tustin.items;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.suggestion.SuggestionContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionResolver;
import me.tustin.items.command.MainCommand;
import me.tustin.items.listeners.BlockListener;
import me.tustin.items.listeners.FarmingListener;
import me.tustin.items.listeners.FishingListener;
import me.tustin.items.listeners.ShopListener;
import me.tustin.items.model.ItemProperty;
import me.tustin.items.module.DefaultModule;
import me.tustin.items.task.ArmorRunnable;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ItemsPlugin extends JavaPlugin {

	private Economy econ;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		setupEconomy();
		BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);
		manager.registerArgument(ItemProperty.class, (s, a) -> ItemProperty.valueOf(a.toUpperCase(Locale.ROOT)));
		manager.registerSuggestion(ItemProperty.class, (s, c) -> Arrays.stream(ItemProperty.values()).map(ItemProperty::name).collect(Collectors.toList()));

		Injector injector = Guice.createInjector(new DefaultModule(this, econ));
		Bukkit.getPluginManager().registerEvents(injector.getInstance(BlockListener.class), this);
		Bukkit.getPluginManager().registerEvents(injector.getInstance(FishingListener.class), this);
		Bukkit.getPluginManager().registerEvents(injector.getInstance(ShopListener.class), this);
		Bukkit.getPluginManager().registerEvents(injector.getInstance(FarmingListener.class), this);

		ArmorRunnable runnable = injector.getInstance(ArmorRunnable.class);
		runnable.runTaskTimerAsynchronously(this,30, getConfig().getInt("Configurations.cooldown")* 20L);

		manager.registerCommand(injector.getInstance(MainCommand.class));
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
}
