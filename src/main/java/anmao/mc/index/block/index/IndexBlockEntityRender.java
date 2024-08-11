package anmao.mc.index.block.index;

import anmao.mc.amlib.render.RenderCube;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class IndexBlockEntityRender implements BlockEntityRenderer<IndexBlockEntity> {
    public IndexBlockEntityRender(BlockEntityRendererProvider.Context context){
    }
    @Override
    public void render(@NotNull IndexBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.25,0.25,0.25);
        Matrix4f matrix4f = pPoseStack.last().pose();
        RenderCube.renderEndGatewayCube(matrix4f, pBuffer,0.5F);
        pPoseStack.popPose();
    }
}
