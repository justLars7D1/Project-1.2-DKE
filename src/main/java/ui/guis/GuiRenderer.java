package ui.guis;

import ui.models.RawModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import ui.renderEngine.Loader;
import ui.toolbox.Maths;

import java.util.List;

public class GuiRenderer {

    private final RawModel quad;
    private GuiShader shader;

    public GuiRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis) {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        for(GuiTexture guiTexture: guis) {
            GL30.glActiveTexture(GL30.GL_TEXTURE0);
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, guiTexture.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(guiTexture.getPosition(),
                    guiTexture.getRotX(), guiTexture.getRotY(), guiTexture.getRotZ(), guiTexture.getScale());
            shader.loadTransformation(matrix);
            GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, quad.getNumVertices());
        }
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}
