import java.util.Random;

public class Point {
    static final int CAR_TYPE = 1;
    static final int EMPTY_TYPE = 0;

    static int MAX_POS;

    static final int MIN_VELOCITY = 0;
    static final int MAX_VELOCITY = 5;

    static final int SLOW_DOWN_CHANCE = 10;

    boolean isActionDone;

    int type;
    boolean moved;

    Point next;

    int velocity = 1;

    public void move() {
        if(type == CAR_TYPE && next.type == EMPTY_TYPE && !next.moved && !moved) {
            type = EMPTY_TYPE;
            next.type = CAR_TYPE;

            moved = true;
            next.moved = true;

            next.velocity = velocity;
        }
    }

    public void accelerate() {
        if(type == CAR_TYPE && !isActionDone && velocity < MAX_VELOCITY)  {
            velocity += 1;
            isActionDone = true;
        }
    }

    public void move_random() {
        if(type == CAR_TYPE && !isActionDone && should_slow_down()) {
            velocity -= 1;
            isActionDone = true;
        }
    }

    public void set_next(int x, int y, Point[][] board) {
        if (type == CAR_TYPE) {
            next = null;
            for (int i = 1; i < velocity; i++) {
                if(board[(x + i) % MAX_POS][y].type == CAR_TYPE) {
                    velocity = i;
                    next = board[(x + i) % MAX_POS][y];
                }
            }
            if(next == null) {
                next = board[(x + velocity) % MAX_POS][y];
            }
        }
    }

    public void reset() {
        isActionDone = false;
        moved = false;
    }

    public void clicked() {
        if(type == EMPTY_TYPE) {
            type = CAR_TYPE;
            velocity = 1;
        } else {
            clear();
        }
    }

    public void clear() {
        type = EMPTY_TYPE;
        velocity = 0;
    }

    public boolean should_slow_down() {
        return velocity > MIN_VELOCITY && new Random().nextInt(100) < SLOW_DOWN_CHANCE;
    }

}

