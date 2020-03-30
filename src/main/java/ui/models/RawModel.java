package ui.models;

public class RawModel {

    private int vaoID;
    private int numVertices;

    public RawModel(int vaoID, int numVertices) {
        this.vaoID = vaoID;
        this.numVertices = numVertices;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getNumVertices() {
        return numVertices;
    }

    @Override
    public String toString() {
        return "ui.models.RawModel{" +
                "vaoID=" + vaoID +
                ", numVertices=" + numVertices +
                '}';
    }
}
