package project12.physicsengine;

public abstract class Vector {

    protected double[] coords;

    protected Vector(double ... coords) {
        this.coords = coords;
    }

    public void normalize() {
        double normalizationFactor = 0;
        for (double coord: coords) {
            normalizationFactor += Math.pow(coord, 2);
        }
        normalizationFactor = Math.sqrt(normalizationFactor);

        if (normalizationFactor != 0) {
            for (int i = 0; i < coords.length; i++) {
                coords[i] /= normalizationFactor;
            }
        } else {
            System.out.println("Normalization failed!");
        }

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("(");
        for (int i = 0; i < coords.length; i++) {
            res.append(coords[i]);
            if (i != coords.length-1) res.append(" ");
        }
        res.append(")");
        return res.toString();
    }

}
