package ui.guis;

import org.joml.Matrix4f;

import ui.shaders.ShaderProgram;
import ui.shaders.StaticShader;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "guiVertexShader.shader";
	private static final String FRAGMENT_FILE = "guiFragmentShader.shader";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(GuiShader.class.getResourceAsStream(VERTEX_FILE), GuiShader.class.getResourceAsStream(FRAGMENT_FILE));
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
