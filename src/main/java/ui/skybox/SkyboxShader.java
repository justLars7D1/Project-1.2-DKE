package ui.skybox;

import org.joml.Matrix4f;

import ui.entities.Camera;
import ui.entities.PlayerCamera;

import org.joml.Vector3f;
import ui.renderEngine.Window;
import ui.shaders.ShaderProgram;
import ui.toolbox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/main/java/ui/skybox/skyboxVertexShader.shader";
	private static final String FRAGMENT_FILE = "src/main/java/ui/skybox/skyboxFragmentShader.shader";

	public static final float ROTATE_SPEED = 0.5f;
	private float curRotation;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix = matrix.m30(0);
		matrix = matrix.m31(0);
		matrix = matrix.m32(0);
		curRotation += ROTATE_SPEED * Window.getFrameTimeSeconds();
		matrix = matrix.rotateY((float) Math.toRadians(curRotation));
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
	}

	public void loadForColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r, g, b));
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
