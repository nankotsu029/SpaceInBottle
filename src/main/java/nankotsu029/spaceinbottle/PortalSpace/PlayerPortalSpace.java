package nankotsu029.spaceinbottle.PortalSpace;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static nankotsu029.spaceinbottle.SystemCore.mainWorld;
import static nankotsu029.spaceinbottle.SystemCore.plugin;
import static nankotsu029.spaceinbottle.PortalSpace.PortalData.spaceData;
import static nankotsu029.spaceinbottle.PortalSpace.PortalData.outSideLocationData;

public class PlayerPortalSpace {
    private static World world;
    public static void AddPlayerSpaceData(Player player) throws IOException {
        File spaceFile = new File(plugin.getDataFolder().getParent(),"SpaceNumber.yml");
        spaceData = YamlConfiguration.loadConfiguration(spaceFile);
        int dataSize = spaceData.getKeys(false).size();
        spaceData.set(player.getUniqueId().toString(),dataSize+1);
        spaceData.save(spaceFile);

        int spaceNum = (int) spaceData.get(player.getUniqueId().toString());
        CreateSpaceArea(spaceNum);
        WarpPlayerSpace(player);
    }

    public static void WarpPlayerSpace(Player player) throws IOException {
        int spaceNum = (int) spaceData.get(player.getUniqueId().toString());
        world = Bukkit.getWorld("PortalSpaceWorld");
        if (player.getWorld() != world) {
            outSideLocationData.set(player.getUniqueId().toString(),player.getLocation());
            outSideLocationData.save(new File(plugin.getDataFolder().getParent(),"PlayerOutSideLocation.yml"));
        }
        Location spaceLoc = new Location(world,50*spaceNum-25,5,25);
        player.teleport(spaceLoc);
        player.sendMessage("拠点にワープしました。");
    }

    public static void VisitPlayerSpace(Player player,Player target) throws IOException {
        world = Bukkit.getWorld("PortalSpaceWorld");
        int spaceNum = (int) spaceData.get(target.getUniqueId().toString());
        if (player.getWorld() != world) {
            outSideLocationData.set(player.getUniqueId().toString(),player.getLocation());
            outSideLocationData.save(new File(plugin.getDataFolder().getParent(),"PlayerOutSideLocation.yml"));
        }
        Location visitLoc = new Location(world,50*spaceNum-25,5,25);
        player.teleport(visitLoc);
        player.sendMessage(target+"さんの拠点にワープしました");
        player.setGameMode(GameMode.ADVENTURE);
        target.sendMessage(player+"さんがあなたの拠点に訪れました");
    }

    public static void LeaveSpace(Player player,boolean ExitsData) {
        if (ExitsData) {
            player.teleport((Location) outSideLocationData.get(player.getUniqueId().toString()));
        } else {
            Location outSideLoc = mainWorld.getSpawnLocation();
            player.teleport(outSideLoc);
        }
    }

    public static void SharedSpace() {
        Bukkit.getLogger().info("共有拠点の作成開始");
        CreateSpaceArea(1);
        //ここに共有スペースを自動で作って欲しい
        //家、チェスト、池
        Bukkit.getLogger().info("共有拠点の作成完了");
    }

    public static void CreateSpaceArea(int spaceNum) {
        world = Bukkit.getWorld("PortalSpaceWorld");

        int startX = 50 * spaceNum - 47;
        int finishX = 50 * spaceNum + 1;
        int pointX = 50 * spaceNum;

        //地面
        for (int x = pointX-50;x < pointX+1;x++) {
            for (int z = 0;z < 50;z++) {
                for (int y = 0;y < 4;y++) {
                    if (y == 0) {
                        world.getBlockAt(x,y,z).setType(Material.BEDROCK);
                    } else if (y == 3) {
                        world.getBlockAt(x,y,z).setType(Material.GRASS_BLOCK);
                    } else {
                        world.getBlockAt(x,y,z).setType(Material.DIRT);
                    }
                }
            }
        }
        //天井
        for (int x = pointX-50;x<pointX+1;x++) {
            for (int z = 0;z<50;z++) {
                for (int y = 50;y<51;y++) {
                    world.getBlockAt(x,y,z).setType(Material.BARRIER);
                }
            }
        }
        //zが大きく変わる二つの中でxが小さい方
        for (int x = startX - 3; x < startX; x++) {
            for (int z = 0; z < 50; z++) {
                for (int y = 1; y < 50; y++) {
                    if (x == startX - 3) {
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    } else {
                        world.getBlockAt(x, y, z).setType(Material.WHITE_CONCRETE);
                    }
                }
            }
        }
        //zが大きく変わる二つの中でxが大きい方
        for (int x = finishX - 3; x < finishX; x++) {
            for (int z = 0; z < 50; z++) {
                for (int y = 1; y < 50; y++) {
                    if (x == finishX-1) {
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    } else {
                        world.getBlockAt(x, y, z).setType(Material.WHITE_CONCRETE);
                    }
                }
            }
        }
        //xが大きく変わる二つの中でzが小さい方
        for (int x = pointX - 50; x < pointX; x++) {
            for (int z = 0; z < 3; z++) {
                for (int y = 1; y < 50; y++) {
                    if ((z == 0) || (x == pointX-50)) {
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    } else {
                        world.getBlockAt(x, y, z).setType(Material.WHITE_CONCRETE);
                    }
                }
            }
        }
        //xが大きく変わるな二つの中でzが大きい方
        for (int x = pointX - 50; x < pointX; x++) {
            for (int z = 47; z < 50; z++) {
                for (int y = 1; y < 50; y++) {
                    if ((z == 49)||(x == pointX-50)) {
                        world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    } else {
                        world.getBlockAt(x, y, z).setType(Material.WHITE_CONCRETE);
                    }
                }
            }
        }
    }
}
