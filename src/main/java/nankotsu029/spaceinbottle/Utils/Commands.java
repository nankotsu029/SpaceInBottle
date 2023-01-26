package nankotsu029.spaceinbottle.Utils;

import nankotsu029.spaceinbottle.PortalSpace.EmptyChunkGenerator;
import nankotsu029.spaceinbottle.PortalSpace.PlayerPortalSpace;
import nankotsu029.spaceinbottle.PortalSpace.PortalData;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static nankotsu029.spaceinbottle.PortalSpace.PortalData.*;
import static nankotsu029.spaceinbottle.SystemCore.plugin;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("CreatePortalSpaceWorld")) {
            WorldCreator wc = new WorldCreator("PortalSpaceWorld");
            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.FLAT);
            wc.generator(new EmptyChunkGenerator());
            World PBWorld = wc.createWorld();
            PBWorld.setPVP(false);
            PBWorld.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        } else if (command.getName().equalsIgnoreCase("PortalSpace")) {
            if (args.length == 0) {
                player.sendMessage("§b-----------------------------------------------------");
                player.sendMessage("§e/PortalSpace §7- §aPortalSpaceコマンドを表示");
                player.sendMessage("§e/PortalSpace Join §7- §a自分のポータルベースに移動");
                player.sendMessage("§e/PortalSpace JoinWith §7- §a周囲のエンティティを連れて、自分のポータルベースに移動");
                player.sendMessage("§e/PortalSpace Leave §7- §aポータルベースから元のいた場所に移動");
                player.sendMessage("§e/PortalSpace LeaveWith §7- §a周囲のエンティティを連れて、ポータルベースから元のいた場所に移動");
                player.sendMessage("§e/PortalSpace Visit 'playerName' §7- §aplayerNameのポータルベースに移動");
                player.sendMessage("§b-----------------------------------------------------");
                return true;
            }

            if (!new File(plugin.getDataFolder().getParentFile().getParent(), "PortalSpaceWorld").exists()) {
                player.sendMessage("§cワールドが存在しません。初めに§e'/CreatePortalSpaceWorld'§cを入力してください");
                return true;
            }

            if (args[0].length() >= 1) {
                if (args[0].equalsIgnoreCase("Join")) {
                    PortalData.spaceFileExists();
                    if (spaceData.get(player.getUniqueId().toString()) == null){
                        try {
                            PlayerPortalSpace.AddPlayerSpaceData(player);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            PlayerPortalSpace.WarpPlayerSpace(player);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("JoinWith")) {
                    Location outsideLoc = player.getLocation();
                    player.performCommand("PortalSpace Join");
                    for (Entity e:outsideLoc.getWorld().getNearbyEntities(outsideLoc,3,3,3)) {
                        if ((e instanceof LivingEntity)&&(!(e instanceof Player))) {
                            e.teleport(player);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("Leave")) {
                    /*File outSideLocationFile = new File(plugin.getDataFolder().getParent(),"PlayerOutSideLocation.yml");
                    if (!outSideLocationFile.exists()) {
                        try {
                            outSideLocationFile.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }*/ //↑コードなくても機能する
                    boolean ExitsData = false;
                    if (outSideLocationData.get(player.getUniqueId().toString()) == null) {
                        PlayerPortalSpace.LeaveSpace(player,ExitsData);
                    } else {
                        ExitsData = true;
                        PlayerPortalSpace.LeaveSpace(player,ExitsData);
                    }
                } else if (args[0].equalsIgnoreCase("LeaveWith")) {
                    Location spaceLoc = player.getLocation();
                    player.performCommand("PortalSpace Leave");
                    for (Entity e:spaceLoc.getWorld().getNearbyEntities(spaceLoc,3,3,3)) {
                        if ((e instanceof LivingEntity)&&(!(e instanceof Player))) {
                            e.teleport(player);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("Visit")) {
                    if (args[1].length() == 0) {
                        player.sendMessage("§e/PortalSpace Visit 'playerName'");
                        return true;
                    }
                    PortalData.spaceFileExists();
                    boolean ExitsPlayer = false;
                    Player target = null;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equals(args[1])) {
                            ExitsPlayer = true;
                            target = Bukkit.getPlayer(args[1]);
                        }
                    }
                    if (!ExitsPlayer){
                        player.sendMessage(args[1]+"さんは存在しないもしくは、オンラインではありません。");
                        return true;
                    }
                    if (spaceData.get(target.getUniqueId().toString()) == null) {
                        player.sendMessage(target+"さんのポータルベースは存在しません。");
                    } else {
                        try {
                            PlayerPortalSpace.VisitPlayerSpace(player,target);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("ChangeWorld")) {
            if (args.length == 0) {
                player.sendMessage("§e/ChangeWorld 'worldName'");
                return true;
            }
            boolean ExitsWorld = false;

            if (args[0].length() >= 1) {
                for (World w: Bukkit.getWorlds()) {
                    if (w.getName().equals(args[0])) {
                        ExitsWorld = true;
                    }
                }
                if (!ExitsWorld) {
                    player.sendMessage("§e"+args[0]+"は存在しません");
                    return true;
                }
                World world = Bukkit.getWorld(args[0]);
                Location spawnLoc = world.getSpawnLocation();
                Location location = new Location(world,spawnLoc.getX(),spawnLoc.getY(),spawnLoc.getZ());
                player.teleport(location);
            }
        } else if (command.getName().equalsIgnoreCase("getPortalSpaceItem")) {
            player.getInventory().setItem(0,ItemManager.CreateCustomItem(Material.LINGERING_POTION,1,"JoinPortalSpace","右クリックで、自分のポータルベースに移動","シフト+右クリックで周囲のエンティティを連れて、自分のポータルベースに移動",false,false));
        }
        return true;
    }
}
