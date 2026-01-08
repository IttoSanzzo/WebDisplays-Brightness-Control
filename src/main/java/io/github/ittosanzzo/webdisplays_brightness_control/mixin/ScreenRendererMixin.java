package io.github.ittosanzzo.webdisplays_brightness_control.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.ittosanzzo.webdisplays_brightness_control.WebDisplaysBrightnessControl;
import io.github.ittosanzzo.webdisplays_brightness_control.config.ClientConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.montoyo.wd.client.renderers.ScreenRenderer;
import net.montoyo.wd.entity.ScreenBlockEntity;

@Mixin(ScreenRenderer.class)
public class ScreenRendererMixin {

	static {
		WebDisplaysBrightnessControl.Logger.debug("[WDBC]: ScreenRendererMixin LOADED");
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"), remap = false)
	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$applyBrightness(float r, float g, float b, float a) {
		// WebDisplaysBrightnessControl.Logger.debug("[WDBC]: APPLY");
		float brightness = ClientConfig.Brightness.get().floatValue();

		RenderSystem.setShaderColor(brightness, brightness, brightness, 1.0f);
	}

	@Inject(method = "render", at = @At("RETURN"), remap = false)
	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$resetBrightness(ScreenBlockEntity te, float partialTick,
			PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
		// WebDisplaysBrightnessControl.Logger.debug("[WDBC]: RESET");
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
}