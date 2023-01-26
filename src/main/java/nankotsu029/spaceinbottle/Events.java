package nankotsu029.spaceinbottle;

import nankotsu029.spaceinbottle.SystemCore;
import nankotsu029.spaceinbottle.Utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {
    private SystemCore plugin;

    public Events(SystemCore plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (event.getItem().getItemMeta() == null) {
                return;
            }
            if (event.getItem().getItemMeta().equals(ItemManager.CreateCustomItem(Material.LINGERING_POTION, 1, "JoinPortalSpace", "右クリックで、自分のポータルベースに移動", "シフト+右クリックで周囲のエンティティを連れて、自分のポータルベースに移動", false, false).getItemMeta())) {
                if (player.isSneaking()) {
                    if (player.getWorld() == Bukkit.getWorld("PortalSpaceWorld")) {
                        player.performCommand("PortalSpace LeaveWith");
                    } else {
                        player.performCommand("PortalSpace JoinWith");
                    }
                } else {
                    if (player.getWorld() == Bukkit.getWorld("PortalSpaceWorld")) {
                        player.performCommand("PortalSpace Leave");
                    } else {
                        player.performCommand("PortalSpace Join");
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
