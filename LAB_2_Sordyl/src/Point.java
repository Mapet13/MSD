public class Point {

	public Point nNeighbor;
	public Point wNeighbor;
	public Point eNeighbor;
	public Point sNeighbor;
	public float nVel;
	public float eVel;
	public float wVel;
	public float sVel;
	public float pressure;

	public static Integer[] types = {0, 1, 2};
	int type;

	int sinInput;

	public Point() {
		clear();
		type = 0;
	}

	public void clicked() {
		pressure = 1;
	}
	
	public void clear() {
		nVel = 0;
		eVel = 0;
		sVel = 0;
		wVel = 0;
		pressure = 0;
	}

	public void updateVelocity() {
		if (type == 0) {
			nVel = nVel - (nNeighbor.pressure - pressure);
			sVel = sVel - (sNeighbor.pressure - pressure);
			eVel = eVel - (eNeighbor.pressure - pressure);
			wVel = wVel - (wNeighbor.pressure - pressure);
		}
	}

	public void updatePresure() {
		if (type == 2) {
			sinInput += 30;
			double radians = Math.toRadians(sinInput);
			pressure = (float)(Math.sin(radians));
		} else {
			final var cs = 0.5;
			pressure -= cs *
					(   getCheckedVelocity(nVel, nNeighbor)
					  + getCheckedVelocity(sVel, sNeighbor)
					  + getCheckedVelocity(eVel, eNeighbor)
					  + getCheckedVelocity(wVel, wNeighbor)
					);
		}
	}

	public float getPressure() {
		return pressure;
	}

	private float getCheckedVelocity(float velocity, Point neighbor) {
		return (neighbor.type != 1)
				? velocity
				: 0;
	}
}