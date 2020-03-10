package project12.physicsengine.test;

import project12.physicsengine.Function2d;
import project12.physicsengine.Vector2d;

public class TestEngine {
    public static void main(String[] args) {

        Function2d f = new Function2d() {
            @Override
            public double evaluate(Vector2d p) {
                return Math.sin(p.get_x())*Math.sin(p.get_y());
            }

            @Override
            public Vector2d gradient(Vector2d p) {
                double h = 0.000000000000000000000000000000000001;
                double zphx = evaluate(new Vector2d(p.get_x()+h, p.get_y()));
                double zphy = evaluate(new Vector2d(p.get_x(), p.get_y()+h));
                double z = evaluate(p);
                return new Vector2d((zphx-z)/h, (zphy-z)/h);
            }
        };

        System.out.println(f.gradient(new Vector2d(0, 0)));

    }
}
