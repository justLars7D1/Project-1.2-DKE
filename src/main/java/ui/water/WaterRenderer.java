package ui.water;

import java.util.List;

import ui.models.RawModel;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ui.renderEngine.Loader;
import ui.renderEngine.Window;
import ui.toolbox.Maths;
import ui.entities.Camera;

public class WaterRenderer {

	private static final String DUDV_MAP = "game/terrain/waterDUDV";
	private static final float WAVE_SPEED = 0.03f;

	private RawModel quad;
	private WaterShader shader;

	private int dudvTexture;
	private WaterFrameBuffers fbos;

	private float moveFactor;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.fbos = fbos;
		this.shader = shader;
		this.dudvTexture = loader.loadTexture(DUDV_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera) {
		prepareRender(camera);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getNumVertices());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * Window.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL30.glActiveTexture(GL30.GL_TEXTURE1);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL30.glActiveTexture(GL30.GL_TEXTURE2);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, dudvTexture);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
