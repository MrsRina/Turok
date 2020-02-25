package com.oldturok.turok.module.modules.render;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.event.events.ChunkEvent;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.TurokMod;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.lwjgl.opengl.GL11.*;

@Module.Info(name = "ChunkFinder", description = "Highlights newly generated chunks", category = Module.Category.TUROK_RENDER)
public class ChunkFinder extends Module {

    private Setting<Integer> yOffset = register(Settings.i("Y Offset", 0));
    private Setting<Boolean> relative = register(Settings.b("Relative", true));
    private Setting<Boolean> saveNewChunks = register(Settings.b("Save New Chunks", false));
    private Setting<SaveOption> saveOption = register(Settings.enumBuilder(SaveOption.class).withValue(SaveOption.extraFolder).withName("Save Option").withVisibility(aBoolean -> saveNewChunks.getValue()).build());
    private Setting<Boolean> saveInRegionFolder = register(Settings.booleanBuilder("In Region").withValue(false).withVisibility(aBoolean -> saveNewChunks.getValue()).build());
    private Setting<Boolean> alsoSaveNormalCoords = register(Settings.booleanBuilder("Save Normal Coords").withValue(false).withVisibility(aBoolean -> saveNewChunks.getValue()).build());

    private LastSetting lastSetting = new LastSetting();
    private PrintWriter logWriter;

    static ArrayList<Chunk> chunks = new ArrayList<>();

    private static boolean dirty = true;
    private int list = GL11.glGenLists(1);

    @Override
    public void onWorldRender(RenderEvent event) {
        if (dirty) {
            GL11.glNewList(list, GL11.GL_COMPILE);

            glPushMatrix();
            glEnable(GL_LINE_SMOOTH);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_TEXTURE_2D);
            glDepthMask(false);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);
            glLineWidth(1.0F);
            for (Chunk chunk : chunks) {
                double posX = chunk.x * 16;
                double posY = 0;
                double posZ = chunk.z * 16;

                glColor3f(.6f, .1f, .2f);

                glBegin(GL_LINE_LOOP);
                glVertex3d(posX, posY, posZ);
                glVertex3d(posX + 16, posY, posZ);
                glVertex3d(posX + 16, posY, posZ + 16);
                glVertex3d(posX, posY, posZ + 16);
                glVertex3d(posX, posY, posZ);
                glEnd();
            }
            glDisable(GL_BLEND);
            glDepthMask(true);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glPopMatrix();
            glColor4f(1, 1, 1, 1);

            GL11.glEndList();
            dirty = false;
        }

        double x = mc.getRenderManager().renderPosX;
        double y = relative.getValue() ? 0 : -mc.getRenderManager().renderPosY;
        double z = mc.getRenderManager().renderPosZ;
        GL11.glTranslated(-x, y + yOffset.getValue(), -z);
        GL11.glCallList(list);
        GL11.glTranslated(x, -(y + yOffset.getValue()), z);
    }

    @Override
    protected void onDisable() {
        logWriterClose();
        chunks.clear();
    }

    @EventHandler
    public Listener<ChunkEvent> listener = new Listener<>(event -> {
        if (!event.getPacket().isFullChunk()) {
            chunks.add(event.getChunk());
            dirty = true;
            if(saveNewChunks.getValue()) {
                saveNewChunk(event.getChunk());
            }
        }
    });

    public void saveNewChunk(Chunk chunk) {
        saveNewChunk(testAndGetLogWriter(), getNewChunkInfo(chunk));
    }
    
    private String getNewChunkInfo(Chunk chunk) {
        String rV = String.format("%d,%d,%d", System.currentTimeMillis(), chunk.x, chunk.z);
        if(alsoSaveNormalCoords.getValue()){
            rV += String.format(",%d,%d", chunk.x * 16 + 8, chunk.z * 16 + 8);
        }
        return rV;
    }
    
    private PrintWriter testAndGetLogWriter() {
        if(lastSetting.testChangeAndUpdate()) {
            logWriterClose();
            logWriterOpen();
        }
        return logWriter;
    }
    
    private void logWriterOpen() {
        String filepath = getPath().toString();
        try {
            logWriter = new PrintWriter(new BufferedWriter(new FileWriter(filepath, true)), true);
            String head = "timestamp,ChunkX,ChunkZ";
            if (alsoSaveNormalCoords.getValue()) {
                head += ",x coordinate,z coordinate";
            }
            logWriter.println(head);
        } catch (Exception e) {
            e.printStackTrace();
            TurokMod.log.error("some exception happened when trying to start the logging -> " + e.getMessage());
            Command.sendChatMessage("onLogStart: " + e.getMessage());
        }
    }

    private Path getPath() {
        File file = null;
        int dimension = mc.player.dimension;

        if (mc.isSingleplayer()) {
            try {
                file = mc.getIntegratedServer().getWorld(dimension).getChunkSaveLocation();
            } catch (Exception e) {
                e.printStackTrace();
                TurokMod.log.error("some exception happened when getting canonicalFile -> " + e.getMessage());
                Command.sendChatMessage("onGetPath: " + e.getMessage());
            }

            if (file.toPath().relativize(mc.gameDir.toPath()).getNameCount() != 2) {
                file = file.getParentFile();
            }

        } else {
            file = makeMultiplayerDirectory().toFile();
        }

        if(dimension != 0) {
            file = new File(file, "DIM" + dimension);
        }
        
        if(saveInRegionFolder.getValue()) {
            file = new File(file, "region");
        }
        
        file = new File(file, "newChunkLogs");


        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        file = new File(file, mc.getSession().getUsername() + "_" + date + ".csv"); // maybe dont safe the name actually. But I also dont want to make another option...

        Path rV = file.toPath();
        try {
            if(!Files.exists(rV)) {
                Files.createDirectories(rV.getParent());
                Files.createFile(rV);
            }
        } catch (IOException e) {
            e.printStackTrace();
            TurokMod.log.error("some exception happened when trying to make the file -> " + e.getMessage());
            Command.sendChatMessage("onCreateFile: " + e.getMessage());
        }
        return rV;
    }
    
    private Path makeMultiplayerDirectory(){
        File rV = Minecraft.getMinecraft().gameDir;
        String folderName;
        switch(saveOption.getValue()){
                case liteLoaderWdl:
                    folderName = mc.getCurrentServerData().serverName;
                    
                    rV = new File(rV, "saves");
                    rV = new File(rV, folderName);
                    break;
                case nhackWdl:
                    folderName = getNHackInetName();
                    
                    rV = new File(rV, "config");
                    rV = new File(rV, "wdl-saves");
                    rV = new File(rV, folderName);

                    if (!rV.exists()) {
                        Command.sendChatMessage("nhack wdl directory doesnt exist: " + folderName);
                        Command.sendChatMessage("creating the directory now. It is recommended to update the ip");
                    }
                    break;
                default:
                    folderName = mc.getCurrentServerData().serverName + "-" + mc.getCurrentServerData().serverIP;
                    if (SystemUtils.IS_OS_WINDOWS) {
                        folderName = folderName.replace(":", "_");
                    }

                    rV = new File(rV, "Turok_NewChunks");
                    rV = new File(rV, folderName);
            }
        
        return rV.toPath();
    }

    private String getNHackInetName() {
        String folderName = mc.getCurrentServerData().serverIP;
        if (SystemUtils.IS_OS_WINDOWS) {
            folderName = folderName.replace(":", "_");
        }
        if (hasNoPort(folderName)) {
            folderName += "_25565";
        }
        return folderName;
    }

    private boolean hasNoPort(String ip) {
        if(!ip.contains("_")) {
            return true;
        }

        String[] sp = ip.split("_");
        String ending = sp[sp.length - 1];
        if (!isInteger(ending)) {
            return true;
        }
        return false;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
    
    private void logWriterClose() {
        if(logWriter != null) {
            logWriter.close();
            logWriter = null;
        }
    }
    
    private void saveNewChunk(PrintWriter log, String data) {
        log.println(data);
    }



    @EventHandler
    private Listener<net.minecraftforge.event.world.ChunkEvent.Unload> unloadListener = new Listener<>(event -> dirty = chunks.remove(event.getChunk()));

    @Override
    public void destroy() {
        GL11.glDeleteLists(1, 1);
    }

    private enum SaveOption {
        extraFolder, liteLoaderWdl, nhackWdl
    }
    
    private class LastSetting {

        SaveOption lastSaveOption;
        boolean lastInRegion;
        boolean lastSaveNormal;
        int dimension;
        String ip;

        public boolean testChangeAndUpdate() {
            if (testChange()) {
                update();
                return true;
            }
            return false;
        }

        public boolean testChange() {
            if (saveOption.getValue() != lastSaveOption) {
                return true;
            }
            if (saveInRegionFolder.getValue() != lastInRegion) {
                return true;
            }
            if (alsoSaveNormalCoords.getValue() != lastSaveNormal) {
                return true;
            }
            if(dimension != mc.player.dimension) {
                return true;
            }
            if(!mc.getCurrentServerData().serverIP.equals(ip)) {
                return true;
            }
            return false;
        }

        private void update() {
            lastSaveOption = saveOption.getValue();
            lastInRegion = saveInRegionFolder.getValue();
            lastSaveNormal = alsoSaveNormalCoords.getValue();
            dimension = mc.player.dimension;
            ip = mc.getCurrentServerData().serverIP;
        }
    }
}
