package io.github.ittosanzzo.webdisplays_brightness_control;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.github.ittosanzzo.webdisplays_brightness_control.config.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WebDisplaysBrightnessControl.MODID)
public class WebDisplaysBrightnessControl {
	public static final String MODID = "webdisplays_brightness_control";
	public static final Logger Logger = LogUtils.getLogger();

	public WebDisplaysBrightnessControl(FMLJavaModLoadingContext context) {
		ModConfigs.Register(context);
		// Logger.info("[WDBC]: Main Started");
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {

		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			// Logger.info("HELLO FROM CLIENT SETUP");
		}
	}
}
