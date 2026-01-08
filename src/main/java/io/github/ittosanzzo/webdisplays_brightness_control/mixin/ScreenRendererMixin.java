package io.github.ittosanzzo.webdisplays_brightness_control.mixin;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import static com.mojang.math.Axis.XN;
import static com.mojang.math.Axis.XP;
import static com.mojang.math.Axis.YN;
import static com.mojang.math.Axis.YP;
import static com.mojang.math.Axis.ZP;

import io.github.ittosanzzo.webdisplays_brightness_control.WebDisplaysBrightnessControl;
import io.github.ittosanzzo.webdisplays_brightness_control.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.client.renderers.ScreenRenderer;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.entity.ScreenData;
import net.montoyo.wd.utilities.math.Vector3f;
import net.montoyo.wd.utilities.math.Vector3i;

@Mixin(ScreenRenderer.class)
public class ScreenRendererMixin implements BlockEntityRenderer<ScreenBlockEntity> {

	static {
		WebDisplaysBrightnessControl.Logger.info("[WDBC]: ScreenRendererMixin LOADED");
	}

	private final Vector3f mid = new Vector3f();
	private final Vector3i tmpi = new Vector3i();
	private final Vector3f tmpf = new Vector3f();

	// @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))

	// @SuppressWarnings("unused")
	// private void webdisplaysBrightnessControl$modifyShaderColor(float r, float g,
	// float b, float a) {
	// float brightness = ClientConfig.Brightness.get().floatValue();
	// // WebDisplaysBrightnessControl.Logger.error("BRIGHTNESS IS {}", brightness);

	// RenderSystem.setShaderColor(r * brightness, g * brightness, b * brightness,
	// a);
	// }

	@Override
	public void render(ScreenBlockEntity te, float partialTick, @NotNull PoseStack poseStack,
			@NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (!te.isLoaded())
			return;

		RenderSystem.disableBlend();

		for (int i = 0; i < te.screenCount(); i++) {
			ScreenData scr = te.getScreen(i);
			if (scr.browser == null) {
				double dist = WebDisplays.PROXY.distanceTo(te,
						Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition());
				if (dist <= WebDisplays.INSTANCE.loadDistance2 * 16)
					scr.createBrowser(te, true);
				else
					continue;
			}

			tmpi.set(scr.side.right);
			tmpi.mul(scr.size.x);
			tmpi.addMul(scr.side.up, scr.size.y);
			tmpf.set(tmpi);
			mid.set(0.5, 0.5, 0.5);
			mid.addMul(tmpf, 0.5f);
			tmpf.set(scr.side.left);
			mid.addMul(tmpf, 0.5f);
			tmpf.set(scr.side.down);
			mid.addMul(tmpf, 0.5f);

			poseStack.pushPose();
			poseStack.translate(mid.x, mid.y, mid.z);

			switch (scr.side) {
			case BOTTOM -> poseStack.mulPose(XP.rotation(90.f + 49.8f));
			case TOP -> poseStack.mulPose(XN.rotation(90.f + 49.8f));
			case NORTH -> poseStack.mulPose(YN.rotationDegrees(180.f));
			case SOUTH -> {
			}
			case WEST -> poseStack.mulPose(YN.rotationDegrees(90.f));
			case EAST -> poseStack.mulPose(YP.rotationDegrees(90.f));
			}

			if (scr.doTurnOnAnim) {
				long lt = System.currentTimeMillis() - scr.turnOnTime;
				float ft = ((float) lt) / 100.0f;

				if (ft >= 1.0f) {
					ft = 1.0f;
					scr.doTurnOnAnim = false;
				}

				poseStack.scale(ft, ft, 1.0f);
			}

			if (!scr.rotation.isNull)
				poseStack.mulPose(ZP.rotationDegrees(scr.rotation.angle));

			float sw = ((float) scr.size.x) * 0.5f - 2.f / 16.f;
			float sh = ((float) scr.size.y) * 0.5f - 2.f / 16.f;

			if (scr.rotation.isVertical) {
				float tmp = sw;
				sw = sh;
				sh = tmp;
			}

			float brightness = ClientConfig.Brightness.get().floatValue();
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder builder = tesselator.getBuilder();
			RenderSystem.enableDepthTest();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
			RenderSystem._setShaderTexture(0, ((MCEFBrowser) scr.browser).getRenderer().getTextureID());
			RenderSystem.setShaderColor(brightness,brightness,brightness, 1.0f);
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			builder.vertex(poseStack.last().pose(), -sw, -sh, 0.505f).uv(0.f, 1.f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			builder.vertex(poseStack.last().pose(), sw, -sh, 0.505f).uv(1.f, 1.f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			builder.vertex(poseStack.last().pose(), sw, sh, 0.505f).uv(1.f, 0.f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			builder.vertex(poseStack.last().pose(), -sw, sh, 0.505f).uv(0.f, 0.f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			tesselator.end();
			RenderSystem.disableDepthTest();
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

			//banana

			poseStack.popPose();
		}
	}
}