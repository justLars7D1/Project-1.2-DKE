package ui.toolbox;

import ui.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ui.renderEngine.Window;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.identity();
        matrix = matrix.translate(translation);
        matrix = matrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
        matrix = matrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
        matrix = matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
        matrix = matrix.scale(scale);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, int rx, int ry, int rz, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.identity();
        matrix = matrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
        matrix = matrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
        matrix = matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
        matrix = matrix.translate(new Vector3f(translation.x, translation.y, 0));
        matrix = matrix.scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.identity();
        matrix = matrix.translate(new Vector3f(translation.x, translation.y, 0));
        matrix = matrix.scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.identity();
        matrix = matrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        matrix = matrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        Vector3f position = camera.getPosition();
        Vector3f negativeCameraPosition = new Vector3f(-position.x, -position.y, -position.z);
        matrix = matrix.translate(negativeCameraPosition);
        return matrix;
    }

    public static Matrix4f createProjectionMatrix(float fov, float near_plane, float far_plane) {

        float aspectRatio = (float) Window.getWidth() / (float) Window.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustum_length = far_plane - near_plane;

        Matrix4f matrix = new Matrix4f();
        matrix = matrix.m00(xScale);
        matrix = matrix.m11(yScale);
        matrix = matrix.m22(-((far_plane + near_plane) / frustum_length));
        matrix = matrix.m23(-1);
        matrix = matrix.m32(-((2 * near_plane * far_plane) / frustum_length));
        matrix = matrix.m33(0);

        return matrix;

    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

}
