package it.myke.creditcards;

import com.google.common.base.Stopwatch;
import it.myke.creditcards.command.ATMCommand;
import it.myke.creditcards.command.BanknoteCommand;
import it.myke.creditcards.command.CreditCardCommand;
import it.myke.creditcards.disk.DefNameSpacedKeys;
import it.myke.creditcards.disk.ItemStore;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.inventory.InventoryManager;
import it.myke.creditcards.listener.ATMListener;
import it.myke.creditcards.object.CardConfigStep;
import it.myke.creditcards.papi.CardPlaceholders;
import it.myke.creditcards.task.CardTypesLoader;
import it.myke.creditcards.timer.PaymentLimitTimer;
import it.myke.creditcards.utils.ATMUtils;
import it.myke.creditcards.utils.BanknoteUtils;
import it.myke.creditcards.utils.Filter;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Plugin extends JavaPlugin {

    @Getter private Economy economy;
    @Getter private ItemStore itemStore;
    @Getter private BanknoteUtils banknoteUtils;
    @Getter private ATMUtils atmUtils;
    @Getter private InventoryManager inventoryManager;
    @Getter public HashMap<UUID, Double> depositLimits;
    @Getter public HashMap<UUID, Double> withdrawLimits;
    @Getter public HashMap<UUID, Double> paymentLimits;
    @Getter public HashMap<String, CardConfigStep> cardConfigStep;
    @Getter private CardTypesLoader cardTypesLoader;
    @Getter public ArrayList<String> cardConfigPlayers;
    private HashMap<String, FileConfiguration> configs;



    @Override
    public void onEnable() {
        if(!setupEconomy()) fatalError("Vault not enabled properly! Check for an economy plug-in or the vault itself.", new Exception("Vault missing!"));

        System.out.println("\n=========== CreditCards Loading ===========");
        Stopwatch stopwatch = Stopwatch.createStarted();


        // Saving & loading the default configs to the plugin folder.
        this.saveDefaultConfig();
        this.depositLimits = new HashMap<>();
        this.withdrawLimits = new HashMap<>();
        this.paymentLimits = new HashMap<>();

        this.configs = new HashMap<>();
        this.cardConfigStep = new HashMap<>();

        this.cardConfigPlayers = new ArrayList<>();

        this.configs.put("lang.yml", initializeCustom("lang.yml"));
        this.configs.put("atms.yml", initializeCustom("atms.yml"));
        this.configs.put("creditcards.yml", initializeCustom("creditcards.yml"));
        this.configs.put("banknotes.yml", initializeCustom("banknotes.yml"));
        new Lang().init(this.getLangConfig());

        // Disk
        this.itemStore = new ItemStore(this);
        new DefNameSpacedKeys(this);

        // Utils
        this.banknoteUtils = new BanknoteUtils(this);
        this.atmUtils = new ATMUtils();
        atmUtils.loadATMsLocation(this);

        // Tasks
        this.cardTypesLoader = new CardTypesLoader(getConfig().getConfigurationSection("card-types"));
        this.inventoryManager = new InventoryManager(this);

        // Timer
        new PaymentLimitTimer(this).runTaskTimer(this, getConfig().getInt("payment-rate-tick"), 0);

        // Commands
        new ATMCommand(this);
        new BanknoteCommand(this);
        new CreditCardCommand(this);

        // Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new ATMListener(this), this);

        //Add PlaceHolderApi support


        stopwatch.stop();
        System.out.println("Enabled in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
        System.out.println("===========================================\n");

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            this.getLogger().info("PlaceholderAPI plugin detected. You can use our Expansion! (Name: CreditCards)");
            this.getLogger().warning("Server reloads can break the Placeholders System! Restart only.");
            new CardPlaceholders(this).register();
        }


        //Filter for creditcard
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new Filter() {

            @Override
            public Result filter(LogEvent event) {

                if (event.getMessage().getFormattedMessage().toLowerCase().contains("/creditcard confirm")) {
                    return Result.DENY;

                }
                return Result.NEUTRAL;
            }
        });


    }


    public FileConfiguration getLangConfig() {
        return configs.get("lang.yml");
    }

    public FileConfiguration getCreditcardConfig() {
        return configs.get("creditcards.yml");
    }

    public FileConfiguration getAtmConfig() {
        return configs.get("atms.yml");
    }

    public FileConfiguration getBanknoteConfig() {
        return configs.get("banknotes.yml");
    }




    private YamlConfiguration initializeCustom(String config) {
        try {
            File conf = new File(this.getDataFolder(), config);
            if(!conf.exists())
                this.saveResource(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), config));
    }


    public void fatalError(String message, Exception ex) {
        if (ex != null) {
            ex.printStackTrace();
        }
        consoleRedError(message);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignored) { }
        setEnabled(false);
        Bukkit.shutdown();
    }

    public void consoleRedError(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] " + message);
    }


    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }




}
