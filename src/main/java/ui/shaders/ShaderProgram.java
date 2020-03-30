package ui.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        this.vertexShaderID = loadShader(vertexFile, GL30.GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentFile, GL30.GL_FRAGMENT_SHADER);
        this.programID = GL30.glCreateProgram();
        GL30.glAttachShader(programID, this.vertexShaderID);
        GL30.glAttachShader(programID, this.fragmentShaderID);
        bindAttributes();
        GL30.glLinkProgram(programID);
        GL30.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL30.glGetUniformLocation(programID, uniformName);
    }

    public void start() {
        GL30.glUseProgram(programID);
    }

    public void stop() {
        GL30.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL30.glDetachShader(programID, this.vertexShaderID);
        GL30.glDetachShader(programID, this.fragmentShaderID);
        GL30.glDeleteShader(this.vertexShaderID);
        GL30.glDeleteShader(this.fragmentShaderID);
        GL30.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL30.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        GL30.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        GL30.glUniform1i(location, value);
    }

    protected void loadVector(int location, Vector4f vector) {
        GL30.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL30.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, Vector2f vector) {
        GL30.glUniform2f(location, vector.x, vector.y);
    }


    protected void loadBoolean(int location, boolean value) {
        float toLoad = (value) ? 1 : 0;
        GL30.glUniform1f(location, toLoad);
    }
    protected void loadMatrix(int location, Matrix4f matrix) {
        GL30.glUniformMatrix4fv(location, false, matrix.get(matrixBuffer));
    }


    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Couldn't read shader file!");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL30.glCreateShader(type);
        GL30.glShaderSource(shaderID, shaderSource);
        GL30.glCompileShader(shaderID);
        if (GL30.glGetShaderi(shaderID, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            System.out.println(GL30.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Couldn't compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }

}
