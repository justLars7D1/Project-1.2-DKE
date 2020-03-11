package project12.physicsengine;

//TODO: Program this method
class EulerSolver implements PhysicsEngine {

    /**
     * The time step size
     */
    private double stepSize;

    /**
     * Constructor
     * @param stepSize The time step size
     */
    public EulerSolver(double stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * Constructor without arguments
     */
    public EulerSolver() {
        this.stepSize = 0.01;
    }

    /**
     * Set the step size
     * @param h The step size
     */
    public void set_step_size(double h) {
        this.stepSize = h;
    }

}
