import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class Point {
	private enum RuleType {
		classic,
		cities,
		coral
	}

	private record Rule(List<Integer> aliveRules, List<Integer> deadRules) { }

	private final ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private final int numStates = 6;
	private final EnumMap<RuleType, Rule> rules = new EnumMap<>(RuleType.class);
	private final RuleType currentRuleType = RuleType.classic;
	
	public Point() {
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();

		rules.put(RuleType.classic, new Rule(List.of(2, 3), List.of(3)));
		rules.put(RuleType.cities, new Rule(List.of(2, 3, 4, 5), List.of(4, 5, 6, 7, 8)));
		rules.put(RuleType.coral, new Rule(List.of(4, 5, 6, 7, 8), List.of(3)));
	}

	public void clicked() {
		currentState=(++currentState)%numStates;	
	}
	
	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState(SimulationMode mode) {
		switch (mode) {
			case GameOfLife:
				int neighborsAlive = countNeighbors();
				final var currentRule = rules.get(currentRuleType);

				if (currentState == 1 && !currentRule.aliveRules.contains(neighborsAlive))
					nextState = 0;
				else if (currentState == 0 && currentRule.deadRules.contains(neighborsAlive))
					nextState = 1;
				else
					nextState = currentState;
				break;
			case Rain:
				if (currentState > 0)
					nextState = currentState - 1;
				if (currentState == 0 && !neighbors.isEmpty() && neighbors.get(0).currentState > 0)
					nextState = 6;
				break;
		}


	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	public int countNeighbors() {
		return (int)neighbors.stream().filter(Point::isAlive).count();
	}

	public boolean isAlive() {
		return currentState == 1;
	}

	public void drop() {
		final int percentBound = 100;
		final int dropChance = 5;

		if ((new Random()).nextInt(percentBound) < dropChance)
			nextState = 6;
	}
}
