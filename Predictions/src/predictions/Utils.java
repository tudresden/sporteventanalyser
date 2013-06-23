package predictions;

public class Utils {

	public static final String FIELD_AREA_OWN_TEAM = "FIELD_AREA_OWN_TEAM";
	public static final String FIELD_AREA_MIDDLE = "FIELD_AREA_MIDDLE";
	public static final String FIELD_AREA_OPPONENTS = "FIELD_AREA_OPPONENTS";

	public static final String getFieldArea(int x, boolean onOpponentSide) {
		final int totalWidth = 2 * 52500;

		// TODO determine boolean onOpponentSide

		// left field side
		if (x < totalWidth / 3 - totalWidth / 2) {
			return onOpponentSide ? FIELD_AREA_OPPONENTS : FIELD_AREA_OWN_TEAM;
		}
		// right field side
		else if (x > 2 * totalWidth / 3 - totalWidth / 2) {
			return onOpponentSide ? FIELD_AREA_OPPONENTS : FIELD_AREA_OWN_TEAM;
		}
		// middle of field
		else {
			return FIELD_AREA_MIDDLE;
		}

	}
}
