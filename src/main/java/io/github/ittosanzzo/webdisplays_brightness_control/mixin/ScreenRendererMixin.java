package io.github.ittosanzzo.webdisplays_brightness_control.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.ittosanzzo.webdisplays_brightness_control.WebDisplaysBrightnessControl;
import io.github.ittosanzzo.webdisplays_brightness_control.config.ClientConfig;
import net.montoyo.wd.client.renderers.ScreenRenderer;

@Mixin(ScreenRenderer.class)
public class ScreenRendererMixin {

	static {
		WebDisplaysBrightnessControl.Logger.info("[WDBC]: ScreenRendererMixin LOADED");
	}

	@Redirect(method = "render(Lnet/montoyo/wd/entity/ScreenBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))

	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$modifyShaderColor(float r, float g, float b, float a) {
		float brightness = ClientConfig.Brightness.get().floatValue();
		// WebDisplaysBrightnessControl.Logger.error("BRIGHTNESS IS {}", brightness);

		RenderSystem.setShaderColor(r * brightness, g * brightness, b * brightness, a);
	}
}
