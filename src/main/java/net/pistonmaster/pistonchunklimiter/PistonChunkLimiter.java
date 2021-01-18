package net.pistonmaster.pistonchunklimiter;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PistonChunkLimiter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.RED + "Loading config");
        saveDefaultConfig();

        getLogger().info(ChatColor.RED + "Registering listener");
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info(ChatColor.RED + "Done! :D");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Chunk chunk = block.getChunk();

        if (block.getType() == Material.CHEST
                && getAmountOf(chunk, "CHEST") >= getConfig().getInt("chests-per-chunk")) {
            event.setBuild(false);
            player.sendMessage(format(getConfig().getString("chest-message")));
        } else if (block.getType().toString().contains("SHULKER_BOX")
                && getAmountOf(chunk, "SHULKER_BOX") >= getConfig().getInt("shulkers-per-chunk")) {
            event.setBuild(false);
            player.sendMessage(format(getConfig().getString("shulker-message")));
        }
    }

    public static int getAmountOf(final Chunk chunk, final String material) {
        final int minX = chunk.getX() << 4;
        final int minZ = chunk.getZ() << 4;
        final int maxX = minX | 15;
        final int maxY = chunk.getWorld().getMaxHeight();
        final int maxZ = minZ | 15;
        int amount = 0;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = 0; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (chunk.getBlock(x, y, z).getType().toString().contains(material)) {
                        ++amount;
                    }
                }
            }
        }

        return amount;
    }

    private String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}

