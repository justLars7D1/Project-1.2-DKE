package ui.terrains;

import physicsengine.functions.Function2d;

public class FunctionalHeightsGenerator extends HeightsGenerator {

    private final Function2d func;

    public FunctionalHeightsGenerator(Function2d func) {
        this.func = func;
    }

    @Override
    public float generateHeight(float x, float z) {
        x += gridX; z += gridZ;
        return function(x, z);
    }

    public float normalGenerateHeight(float x, float z) {
        return function(x, z);
    }

    private float function(float x, float z) {
        return (float) (func.evaluate(x, z));
    }

}
