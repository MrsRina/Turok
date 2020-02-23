package com.oldturok.turok;

import com.oldturok.turok.client.TurokClient;
import com.oldturok.turok.client.TurokCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(
        modid = "turok",
        version = "0.1",
        acceptedMinecraftVersions = "[1.12.2]"
)
public class Turok
{
    public static TurokClient client;
    public static TurokCommands commands;

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(client = new TurokClient());
        MinecraftForge.EVENT_BUS.register(commands = new TurokCommands());
    }
}
