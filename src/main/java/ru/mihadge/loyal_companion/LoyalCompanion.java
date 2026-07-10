package ru.mihadge.loyal_companion;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import ru.mihadge.loyal_companion.event.CatRescueHandler;
import ru.mihadge.loyal_companion.event.ParrotRescueHandler;
import ru.mihadge.loyal_companion.event.WolfRescueHandler;

@Mod(LoyalCompanion.MODID)
public class LoyalCompanion {

    public static final String MODID = "loyal_companion";

    public static final Logger LOGGER = LogUtils.getLogger();



    public LoyalCompanion(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new WolfRescueHandler());
        NeoForge.EVENT_BUS.register(new CatRescueHandler());
        NeoForge.EVENT_BUS.register(new ParrotRescueHandler());

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO FROM SERVER STARTING");
    }
}