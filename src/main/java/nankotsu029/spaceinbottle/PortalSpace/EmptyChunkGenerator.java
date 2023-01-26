package nankotsu029.spaceinbottle.PortalSpace;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import javax.annotation.Nonnull;
import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {
    @Override
    public void generateNoise(@Nonnull WorldInfo worldInfo, @Nonnull Random random, int chunkX, int chunkZ, @Nonnull ChunkGenerator.ChunkData chunkData){
        for (int x = 0;x < 16;x++) {
            for (int z = 0; z < 16;z++) {
                for (int y = 0;y < 10;y++) {
                    chunkData.setBlock(x,y,z, Material.AIR);
                }
            }
        }
    }
}
