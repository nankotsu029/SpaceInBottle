package nankotsu029.spaceinbottle;

import nankotsu029.spaceinbottle.PortalSpace.PortalData;
import nankotsu029.spaceinbottle.Utils.Commands;
import nankotsu029.spaceinbottle.Utils.ItemManager;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class SystemCore extends JavaPlugin {
    public static Plugin plugin;
    public static World mainWorld;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        PortalData.spaceData = YamlConfiguration.loadConfiguration(new File(this.getDataFolder().getParent(),"SpaceNumber.yml"));
        PortalData.outSideLocationData = YamlConfiguration.loadConfiguration(new File(this.getDataFolder().getParent(),"PlayerOutSideLocation.yml"));
        getCommand("CreatePortalSpaceWorld").setExecutor(new Commands());
        getCommand("ChangeWorld").setExecutor(new Commands());
        getCommand("PortalSpace").setExecutor(new Commands());
        getCommand("getPortalSpaceItem").setExecutor(new Commands());
        new Events(this);
        File PBWorldFile = new File(this.getDataFolder().getParentFile().getParent(),"PortalSpaceWorld");
        if (PBWorldFile.exists()) {
            new WorldCreator("PortalSpaceWorld").environment(World.Environment.NORMAL).createWorld();
        }
        try {
            getWorldProperty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setCustomRecipes();
        Bukkit.getLogger().info("Plugin : Space-in-bottleが有効になりました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        File spaceFile = new File(this.getDataFolder().getParent(),"SpaceNumber.yml");
        File outSideLocFile = new File(this.getDataFolder().getParent(),"PlayerOutSideLocation.yml");
        if (spaceFile.exists()) {
            try {
                PortalData.spaceData.save(spaceFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (outSideLocFile.exists()) {
            try {
                PortalData.outSideLocationData.save(outSideLocFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Bukkit.getLogger().info("Plugin : Space-in-bottleが無効になりました。");
    }

    private void setCustomRecipes(){
        ItemStack portalSpaceItem = ItemManager.CreateCustomItem(Material.LINGERING_POTION,1,"JoinPortalSpace","右クリックで、自分のポータルベースに移動","シフト+右クリックで周囲のエンティティを連れて、自分のポータルベースに移動",false,false);
        NamespacedKey portalSpaceKey = new NamespacedKey(this,"portalSpace");
        ShapedRecipe portalSpaceRecipe = new ShapedRecipe(portalSpaceKey,portalSpaceItem);
        portalSpaceRecipe.shape(
                "ctf",
                "ses",
                "ddd");
        portalSpaceRecipe.setIngredient('c',Material.CHEST);
        portalSpaceRecipe.setIngredient('t',Material.CRAFTING_TABLE);
        portalSpaceRecipe.setIngredient('f',Material.FURNACE);
        portalSpaceRecipe.setIngredient('s',Material.COBBLESTONE);
        portalSpaceRecipe.setIngredient('e',Material.ENDER_PEARL);
        portalSpaceRecipe.setIngredient('d',Material.DIRT);
        Bukkit.getServer().addRecipe(portalSpaceRecipe);
    }

    private void getWorldProperty() throws IOException {
        FileInputStream fis =  new FileInputStream(new File(this.getDataFolder().getParentFile().getParent(),"server.properties"));;
        Properties serverProperties = new Properties();
        serverProperties.load(fis);
        mainWorld = Bukkit.getWorld(serverProperties.getProperty("level-name"));
    }
}
