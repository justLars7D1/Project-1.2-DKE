package ui.terrains;

public abstract class HeightsGenerator {

    protected float gridX;
    protected float gridZ;

    protected HeightsGenerator() {}

    public void setGridX(int gridX) {
        this.gridX = gridX * Terrain.getSIZE();
    }

    public void setGridZ(int gridZ) {
        this.gridZ = gridZ * Terrain.getSIZE();
    }

    public abstract float generateHeight(float x, float z);

}
