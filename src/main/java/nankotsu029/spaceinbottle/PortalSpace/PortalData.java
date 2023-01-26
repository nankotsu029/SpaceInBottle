package nankotsu029.spaceinbottle.PortalSpace;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static nankotsu029.spaceinbottle.SystemCore.plugin;

public class PortalData {
    public static FileConfiguration spaceData;
    public static FileConfiguration outSideLocationData;

    public static void spaceFileExists() {
        File spaceFile = new File(plugin.getDataFolder().getParent(),"SpaceNumber.yml");
        if (!spaceFile.exists()) {
            try {
                //spaceFile.createNewFile();//コードなくても機能する
                spaceData.set("shared space",1);
                spaceData.save(spaceFile);
                PlayerPortalSpace.SharedSpace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            spaceData = YamlConfiguration.loadConfiguration(spaceFile);
        }
    }
}
