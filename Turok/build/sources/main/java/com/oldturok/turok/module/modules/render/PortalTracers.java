package com.oldturok.turok.module.modules.render;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.event.events.ChunkEvent;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.ArrayList;

// @Module.Info(name = "PortalTracers", category = Module.Category.TUROK_RENDER)
public class PortalTracers extends Module {

    private Setting<Integer> range = register(Settings.i("Range", 5000));
    private ArrayList<BlockPos> portals = new ArrayList<>();

    @EventHandler
    private Listener<ChunkEvent> loadListener = new Listener<>(event -> {
        Chunk chunk = event.getChunk();
        portals.removeIf(blockPos -> blockPos.getX() / 16 == chunk.x && blockPos.getZ() / 16 == chunk.z);

        for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
            if (storage != null) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (storage.get(x, y, z).getBlock() instanceof BlockPortal) {
                                int px = chunk.x * 16 + x;
                                int py = storage.yBase + y;
                                int pz = chunk.z * 16 + z;
                                portals.add(new BlockPos(px, py, pz));
                                y += 6;
                            }
                        }
                    }
                }
            }
        }
    });


    @Override
    public void onWorldRender(RenderEvent event) {
        portals.stream().filter(blockPos -> mc.player.getDistance(blockPos.x, blockPos.y, blockPos.z) <= range.getValue())
                .forEach(blockPos -> Tracers.drawLine(blockPos.x - mc.getRenderManager().renderPosX, blockPos.y - mc.getRenderManager().renderPosY, blockPos.z - mc.getRenderManager().renderPosZ, 0, 0.6f, 0.3f, 0.8f, 1));
    }
}
