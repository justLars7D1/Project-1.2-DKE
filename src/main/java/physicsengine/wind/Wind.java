package physicsengine.wind;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Wind {

    private Directions direction;
    private int velocity;

    public Wind(){
        this.direction = Directions.randomDirection();
        int min = 0;
        int max = 130;
        this.velocity = 0;
    }

    public enum Directions{
        EAST,
        WEST,
        NORTH,
        SOUTH;

        private static final List<Directions> DIR = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = DIR.size();
        private static final Random RANDOM = new Random();

        public static Directions randomDirection()  {
            return DIR.get(RANDOM.nextInt(SIZE));}
    }

    public Directions getDirection() {
        return direction;
    }

    public int getVelocity() {
        return velocity;
    }

}
