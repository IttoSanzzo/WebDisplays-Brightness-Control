package io.github.ittosanzzo.webdisplays_brightness_control.mixin;

import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.ittosanzzo.webdisplays_brightness_control.config.ClientConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.montoyo.wd.client.renderers.MinePadRenderer;

@Mixin(MinePadRenderer.class)
public class MinePadRendererMixin {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"), remap = false)
	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$redirectShader(Supplier<?> shader) {
		float brightness = ClientConfig.MinePadBrightness.get().floatValue();
		RenderSystem.setShaderColor(brightness, brightness, brightness, 1.0f);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableDepthTest()V", ordinal = 0), remap = false)
	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$disableDepthTest() {
		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(GL11.GL_ALWAYS);
	}

	@Inject(method = "render", at = @At("RETURN"), remap = false)
	@SuppressWarnings("unused")
	private void webdisplaysBrightnessControl$resetBrightness(PoseStack stack, ItemStack itemStack, float handSideSign,
			float swingProgress, float equipProgress, MultiBufferSource multiBufferSource, int packedLight,
			CallbackInfoReturnable<Boolean> cir) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
