package ui.textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean hasTransparancy = false;
    private boolean useFakeLighting = false;

    private int numRows = 1;

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public boolean isHasTransparancy() {
        return hasTransparancy;
    }

    public void setHasTransparancy(boolean hasTransparancy) {
        this.hasTransparancy = hasTransparancy;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public int getID() {
        return this.textureID;
    }

}
