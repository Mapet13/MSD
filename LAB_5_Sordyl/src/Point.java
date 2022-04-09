import java.util.ArrayList;
import java.util.Comparator;

public class Point {

	public ArrayList<Point> neighbors;
	public static Integer []types ={0,1,2,3};
	public int type;
	public int staticField;
	public boolean isPedestrian;
	public boolean blocked = false;

	public Point() {
		type=0;
		staticField = 100000;
		neighbors = new ArrayList<>();
	}
	
	public void clear() {
		staticField = 100000;
	}

	public boolean calcStaticField() {
		if(type == 1 || type == 2)
			return false;

		return neighbors
				.stream()
				.map(p -> p.staticField)
				.min(Integer::compare)
				.filter(smallestValue -> staticField > smallestValue + 1)
				.stream().findAny()
				.map(smallestValue -> staticField = smallestValue + 1)
				.isPresent();
	}
	
	public void move() {
		if(isPedestrian && !found_exit() && !blocked) {
			neighbors
					.stream()
					.filter(n -> !n.isPedestrian && n.type == 0)
					.min(Comparator.comparing(n -> n.staticField))
					.ifPresent(n -> {
						n.isPedestrian = true;
						n.blocked = true;
						isPedestrian = false;
					});
		}
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	private boolean found_exit() {
		if (neighbors.stream().anyMatch(n -> n.type == 2 )) {
			type = 0;
			isPedestrian = false;
			return true;
		}
		return false;
	}
}