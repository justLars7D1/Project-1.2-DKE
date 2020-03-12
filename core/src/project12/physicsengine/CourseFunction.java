package project12.physicsengine;

/**
 * Represents a function of the course
 */
public class CourseFunction implements Function2d {

    //TODO: Maybe change add this to the function: 0.1 * |x| * |y|

    //  We have a function in the format of h(x,y) =  a *sin(b(x-c)) * sin(d(y-e)) + f
    //  Where a, b, c, d, e, f and g are user specified variables with the following definitions
    //  Constant    |                       Definition
    //      a       |   The amplitude of the function (scaling the height)
    //      b       |   The period scaling of the function in the x direction (period = 2*pi / b)
    //      c       |   Shifting the function to the right in the x direction
    //      d       |   The period scaling of the function in the y direction (period = 2*pi / b)
    //      e       |   Shifting the function to the right in the y direction
    //      f       |   Shifting the function higher in the z direction

    /**
     * Amplitude
     */
    private double a = 0.4;
    /**
     * Period scaling factor x
     */
    private double b = 1;
    /**
     * Shifting x factor
     */
    private double c = 0;
    /**
     * Period scaling factor y
     */
    private double d = 1;
    /**
     * Shifting y factor
     */
    private double e = 0;
    /**
     * Shifting z factor
     */
    private double f = 1;

    /**
     * Constructor
     * @param a Amplitude
     * @param b Period scaling factor x
     * @param c Shifting x factor
     * @param d Period scaling factor y
     * @param e Shifting y factor
     * @param f Shifting z factor
     */
    public CourseFunction(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    /**
     * Default constructor
     */
    public CourseFunction() {
    }

    /**
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    @Override
        public double evaluate(Vector2d p) {
            double x = p.get_x();
            double y = p.get_y();
            //return a * Math.sin(b * (x - c)) * Math.sin(d * (y - e)) + f;
            return 1;
        }

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    @Override
        public Vector2d gradient(Vector2d p) {
            double z = evaluate(p);
            double zphx = evaluate(new Vector2d(p.get_x()+ACCURACYGRADIENTFACTOR, p.get_y()));
            double zphy = evaluate(new Vector2d(p.get_x(), p.get_y()+ACCURACYGRADIENTFACTOR));
            return new Vector2d((zphx-z)/ACCURACYGRADIENTFACTOR, (zphy-z)/ACCURACYGRADIENTFACTOR);
        }

}
