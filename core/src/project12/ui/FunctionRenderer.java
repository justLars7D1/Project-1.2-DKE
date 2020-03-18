package project12.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import project12.gameelements.PuttingCourse;
import project12.physicsengine.Vector2d;
import project12.physicsengine.functions.Function2d;

public class FunctionRenderer {

    private ModelBatch modelBatch;

    public ModelInstance[] rectInstance;

    private Function2d f;

    public FunctionRenderer(PuttingCourse course) {
        this.f = course.get_height();
        create(course.get_start_position(), course.get_flag_position());
    }

    private static int colSize = 25;
    private void create(Vector2d startPoint, Vector2d endPoint) {

        int minX = (int) Math.min(startPoint.get_x()/colSize, endPoint.get_x()/colSize) - 1;
        int maxX = (int) Math.max(startPoint.get_x()/colSize, endPoint.get_x()/colSize) + 1;
        int minY = (int) Math.min(startPoint.get_y()/colSize, endPoint.get_y()/colSize) - 1;
        int maxY = (int) Math.max(startPoint.get_y()/colSize, endPoint.get_y()/colSize) + 1;

        modelBatch = new ModelBatch();
        ModelBuilder modelBuilder = new ModelBuilder();

        rectInstance = new ModelInstance[(Math.abs(maxX-minX)+1)*(Math.abs(maxY-minY)+1)];
        int count = 0;
        for(int i = minX; i <= maxX; i++) {
            for(int j = minY; j <= maxY; j++) {
                modelBuilder.begin();
                MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
                buildTerrain(builder, i, j);
                Model mod = modelBuilder.end();
                rectInstance[count++] = new ModelInstance(mod, i*colSize, 0, j*colSize);
            }
        }

    }

    public void render(PerspectiveCamera camera, Environment environment) {
        modelBatch.begin(camera);
        for(int i=0;i<rectInstance.length;i++) {
            modelBatch.render(rectInstance[i], environment);
        }
        modelBatch.end();
    }

    /**
     * Credits to Samuele for this function. We understand how it works, but we weren't the ones who originally came up with it.
     * Although we modified it, the idea is the same.
     * @param b The builder of the mesh
     * @param gridCol The column of the grid in the field
     * @param gridRow The row of the grid in the field
     */
    public void buildTerrain(MeshPartBuilder b, int gridCol, int gridRow) {
        Vector3 pos1,pos2,pos3,pos4;
        Vector3 nor1,nor2,nor3,nor4;
        MeshPartBuilder.VertexInfo v1,v2,v3,v4;
        for(int i=-colSize+(colSize*gridCol);i<=colSize+(colSize*gridCol);i++){
            for(int k=-colSize+(colSize*gridRow);k<=colSize+(colSize*gridRow);k++){
                pos1 = new Vector3 (i,(float)(f.evaluate(i, k)), k);
                pos2 = new Vector3 (i,(float)(f.evaluate(i, k+1)),k+1);
                pos3 = new Vector3 (i+1,(float)(f.evaluate(i+1, k+1)),k+1);
                pos4 = new Vector3 (i+1,(float)(f.evaluate(i+1, k)),k);

                nor1 = (new Vector3((float)-f.partialDerivativeX(i, k),1,0).add(new Vector3(0,1,(float)-f.partialDerivativeY(i, k))));
                nor2 = (new Vector3((float)-f.partialDerivativeX(i, k),1,0).add(new Vector3(0,1,(float)-f.partialDerivativeY(i, k+1))));
                nor3 = (new Vector3((float)-f.partialDerivativeX(i+1, k+1),1,0).add(new Vector3(0,1,(float)-f.partialDerivativeY(i+1, k+1))));
                nor4 = (new Vector3((float)-f.partialDerivativeX(i+1, k),1,0).add(new Vector3(0,1,(float)-f.partialDerivativeY(i, k))));

                v1 = new MeshPartBuilder.VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(0.0f, 0.0f);
                v2 = new MeshPartBuilder.VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.5f);
                v3 = new MeshPartBuilder.VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.5f, 0.0f);
                v4 = new MeshPartBuilder.VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(0.5f, 0.5f);

                b.rect(v1, v2, v3, v4);
            }
        }

    }

    public void dispose() {
        modelBatch.dispose();
    }

}
