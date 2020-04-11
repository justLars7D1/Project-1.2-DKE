package ui.shaders;

import ui.entities.Camera;
import ui.entities.PlayerCamera;
import ui.entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ui.toolbox.Maths;

import java.util.List;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "./src/main/java/ui/shaders/terrainVertexShader.shader";
    private static final String FRAGMENT_FILE = "./src/main/java/ui/shaders/terrainFragmentShader.shader";
    private static final int MAX_LIGHTS = 4;

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    private int[] locationLightPosition;
    private int[] locationLightColor;
    private int[] locationAttenuation;
    private int locationShineDamper;
    private int locationReflectivity;

    private int locationPlane;

    private int locationSkyColor;

    private int locationBackgroundTexture;
    private int locationrTexture;
    private int locationgTexture;
    private int locationbTexture;
    private int locationBlendMap;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        this.locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        this.locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        this.locationViewMatrix = super.getUniformLocation("viewMatrix");
        this.locationShineDamper = super.getUniformLocation("shineDamper");
        this.locationReflectivity = super.getUniformLocation("reflectivity");
        this.locationSkyColor = super.getUniformLocation("skyColor");
        this.locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        this.locationrTexture = super.getUniformLocation("rTexture");
        this.locationgTexture = super.getUniformLocation("gTexture");
        this.locationbTexture = super.getUniformLocation("bTexture");
        this.locationBlendMap = super.getUniformLocation("blendMap");
        this.locationPlane = super.getUniformLocation("plane");

        this.locationLightPosition = new int[MAX_LIGHTS];
        this.locationLightColor = new int[MAX_LIGHTS];
        this.locationAttenuation = new int[MAX_LIGHTS];

        for (int i = 0; i < MAX_LIGHTS; i++) {
            this.locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            this.locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            this.locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }

    }

    public void loadClipPlane(Vector4f plane) {
        super.loadVector(this.locationPlane, plane);
    }

    public void connectTextureUnits() {
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationrTexture, 1);
        super.loadInt(locationgTexture, 2);
        super.loadInt(locationbTexture, 3);
        super.loadInt(locationBlendMap, 4);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(this.locationSkyColor, new Vector3f(r,g,b));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(this.locationTransformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(this.locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(this.locationViewMatrix, matrix);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(this.locationShineDamper, damper);
        super.loadFloat(this.locationReflectivity, reflectivity);

    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < 4; i++) {
            if (i < lights.size()) {
                super.loadVector(this.locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(this.locationLightColor[i], lights.get(i).getColor());
                super.loadVector(this.locationAttenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(this.locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(this.locationLightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(this.locationAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }
}
