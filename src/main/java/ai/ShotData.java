package ai;

import physicsengine.Vector3d;

public class ShotData {

    private Vector3d shotVector;
    private double distanceToTarget;
    private Vector3d calculatedPosition;

    public ShotData(Vector3d shotVector, double distanceToTarget, Vector3d calculatedPosition) {
        this.shotVector = shotVector;
        this.distanceToTarget = distanceToTarget;
        this.calculatedPosition = calculatedPosition;
    }

    public Vector3d getShotVector() {
        return shotVector;
    }

    public double getDistanceToTarget() {
        return distanceToTarget;
    }

    public Vector3d getCalculatedPosition() {
        return calculatedPosition;
    }

    @Override
    public String toString() {
        return "ShotData{" +
                "shotVector=" + shotVector +
                ", distanceToTarget=" + distanceToTarget +
                ", calculatedPosition=" + calculatedPosition +
                '}';
    }
}
