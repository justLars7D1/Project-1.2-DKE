package edu.dke.project12.physicsengine;

class EulerSolver implements PhysicsEngine {

    private double stepSize;

    public EulerSolver() {
        this.stepSize = 0.01;
    }

    public void set_step_size(double h) {
        this.stepSize = h;
    }

}
