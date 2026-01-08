package io.github.ittosanzzo.webdisplays_brightness_control.config;

import io.github.ittosanzzo.webdisplays_brightness_control.WebDisplaysBrightnessControl;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WebDisplaysBrightnessControl.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

	private static final ForgeConfigSpec.Builder Builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec Spec;

	public static final ForgeConfigSpec.DoubleValue Brightness;

	static {
		Brightness = Builder.comment(
			"Defines the bright level for WebDisplays in percent (0 = 0%, 0.5 = 50%, 1 = 100%)"
		).defineInRange("brightness", 0.5, 0.0, 1.0);

		Spec = Builder.build();
	}
}
