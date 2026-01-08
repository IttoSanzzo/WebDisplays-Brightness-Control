package io.github.ittosanzzo.webdisplays_brightness_control.config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ModConfigs {
	public static void Register(ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.Spec);
	}
}
