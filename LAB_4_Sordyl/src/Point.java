import java.util.Arrays;

public class Point {
    enum Type {
        EMPTY(0),
        CAR_SLOW(1),
        CAR_REGULAR(2),
        CAR_FAST(3),
        GRASS(5);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type fromInt(int value) {
            Type xd = Arrays.stream(Type.values()).filter(type -> type.value == value).findAny().orElse(EMPTY);
            return xd;
        }

        public boolean is_car() {
            return this == CAR_FAST || this == CAR_SLOW || this == CAR_REGULAR;
        }

        public int getMaxVelocity() {
            return switch(this) {
                case CAR_FAST -> 7;
                case CAR_REGULAR -> 5;
                case CAR_SLOW -> 3;
                default -> 0;
            };
        }
    };

    static int MAX_POS;

    static final int MIN_VELOCITY = 0;

    static final int SLOW_DOWN_CHANCE = 10;

    boolean isActionDone;

    Type type;
    boolean moved;

    Point next;

    int velocity = 1;

    public Point(Type type) {
        this.type = type;
    }

    public void move() {
        if(type.is_car() && next.type == Type.EMPTY && !next.moved && !moved) {
            next.type = type;
            type = Type.EMPTY;

            moved = true;
            next.moved = true;

            next.velocity = velocity;
        }
    }

    public void accelerate() {
        if(type.is_car() && !isActionDone && velocity < type.getMaxVelocity())  {
            velocity += 1;
            isActionDone = true;
        }
    }


    public void set_next(int x, int y, Point[][] board) {
            next = null;
            for (int i = 1; i < velocity && next == null; i++) {
                if(y != getRightLane(board) && shouldBackToRight(x, board)) {
                    next = board[(x + i) % MAX_POS][y + 1];
                }
                else if(board[(x + i) % MAX_POS][y].type.is_car()) {
                    if(velocity != type.getMaxVelocity() && isEnoughSpaceForOvertaking(x, board)) {
                        next = board[(x + i) % MAX_POS][y - 1];
                        velocity += 1;
                    } else {
                        velocity = i;
                        next = board[(x + i) % MAX_POS][y];
                    }
                }
            }
            if(next == null) {
                next = board[(x + velocity) % MAX_POS][y];
            }
    }

    private boolean checkPrevLane(int x, Point[][] board, int laneOffset, int start) {
        for (int i = start; i < velocity; ++i) {
            if(board[Math.floorMod(x - i, MAX_POS)][getRightLane(board) - laneOffset].type.is_car()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNextLane(int x, Point[][] board, int laneOffset, int start) {
        for (int i = start; i < velocity; ++i) {
            if(board[Math.floorMod(x + i, MAX_POS)][getRightLane(board) - laneOffset].type.is_car()) {
                return false;
            }
        }
        return true;
    }

    private boolean isEnoughSpaceForOvertaking(int x, Point[][] board) {
        return checkPrevLane(x, board, 0, 1) && checkPrevLane(x, board, 1, 0) && checkNextLane(x, board, 1, 0);
    }

    public void reset() {
        isActionDone = false;
        moved = false;
    }

    public void clicked(Type type) {
        this.type = type;
        velocity = type.getMaxVelocity();
    }

    public void clear() {
        type = Type.EMPTY;
        velocity = 0;
    }

    public int getRightLane(Point[][] board) {
        return (board[0].length / 2);
    }

    private boolean shouldBackToRight(int x, Point[][] board) {
        return checkPrevLane(x, board, 0, 0) && checkPrevLane(x, board, 1, 1) && checkNextLane(x, board, 0, 0);
    }

}

