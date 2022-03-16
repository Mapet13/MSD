import java.util.Random;

public class Point {

    static final int CAR_TYPE = 1;
    static final int EMPTY_TYPE = 0;

    static final int MIN_VELOCITY = 0;
    static final int MAX_VELOCITY = 5;

    static final int SLOW_DOWN_CHANCE = 10;

    public static int MAX_POS;

    int type;
    //Point next;
    boolean moved;

    Point nextCar;

    int velocity = MIN_VELOCITY;

    int position;
    int y;

    Point(int position, int y) {
        this.position = position;
        this.y = y;
    }

    public void move(Point[][] board) {
        if(type != CAR_TYPE || moved)
            return;

        do_bonus_action();

        Point next = board[(position + velocity) % MAX_POS][y];

        type = EMPTY_TYPE;
        next.type = CAR_TYPE;

        moved = true;
        next.moved = true;
    }

    public void clicked() {
        type = CAR_TYPE;
        velocity = 1;
    }

    public void clear() {
        type = EMPTY_TYPE;
        velocity = 0;
    }

    private boolean rand_slow_down() {
        return new Random().nextInt(100) < SLOW_DOWN_CHANCE;
    }

    private void do_bonus_action() {
        if(velocity > MIN_VELOCITY && rand_slow_down())
            velocity -= 1;
        else if(nextCar != null && Math.abs(position - nextCar.position) < velocity )
            velocity = Math.abs(position - nextCar.position);
        else if(velocity < MAX_VELOCITY)
            velocity += 1;
    }
}

