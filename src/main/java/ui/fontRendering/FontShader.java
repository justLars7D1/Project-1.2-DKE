package ui.fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;
import ui.shaders.ShaderProgram;

public class FontShader extends ShaderProgram {

	//To add on IntelIJ: src/main/java/
	private static final String VERTEX_FILE = "fontVertex.txt";
	private static final String FRAGMENT_FILE = "fontFragment.txt";
	
	private int location_colour;
	private int location_translation;
	
	public FontShader() {
		super(FontShader.class.getResourceAsStream(VERTEX_FILE), FontShader.class.getResourceAsStream(FRAGMENT_FILE));
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector(location_translation, translation);
	}


}
