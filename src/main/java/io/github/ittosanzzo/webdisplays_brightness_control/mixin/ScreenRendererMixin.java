package io.github.ittosanzzo.webdisplays_brightness_control.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.ittosanzzo.webdisplays_brightness_control.WebDisplaysBrightnessControl;
import net.montoyo.wd.client.renderers.ScreenRenderer;

@Mixin(ScreenRenderer.class)
public class ScreenRendererMixin {

	static {
		WebDisplaysBrightnessControl.Logger.info("[WDBC]: ScreenRendererMixin LOADED");
	}

	// @ModifyArgs(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "INVOKE", target =
	// "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	// @Redirect(method =
	// "render(Lnet/montoyo/wd/entity/ScreenBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
	// at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))

	// @SuppressWarnings("unused")
	// private void webdisplaysBrightnessControl$modifyShaderColor(float r, float g, float b, float a) {
	// float brightness = ClientConfig.Brightness.get().floatValue();
	// WebDisplaysBrightnessControl.Logger.error("BRIGHTNESS IS {}", brightness);

	// RenderSystem.setShaderColor(r * brightness, g * brightness, b * brightness, a);
	// }
}
