package de.core;

import java.util.concurrent.ConcurrentHashMap;

public class Config
{
	private ConcurrentHashMap<Integer, Entity> entityList;

	public final static double TIMEFACTOR = Math.pow(10, 9); // picoseconds to milliseconds

	public final static float BALLPOSSESSIONTHRESHOLD = 1000f; // 1000mm = 1m

	// first half 30min, 4sec
	public final static long GAMESTARTTIMESTAMPA = 10753295594424116L;
	// public final static long GAMESTOPTIMESTAMPA = 12557295594424116L; // from DEBS - NOT CORRECT!
	public final static long GAMESTOPTIMESTAMPA = 12553295594424116L;

	public final static long GAMESTARTTIMESTAMPB = 13086639146403495L;
	// public final static long GAMESTOPTIMESTAMPB = 14879639146403495L; // from DEBS - NOT CORRECT!
	public final static long GAMESTOPTIMESTAMPB = 14886639146403495L;

	public final static long DATAPUSHINTERVAL = 1000; // in ms

	public static final int GAMEFIELDMINX = -50;
	public static final int GAMEFIELDMAXX = 52489;
	public static final int GAMEFIELDMINY = -33960;
	public static final int GAMEFIELDMAXY = 33965;

	public static final int GOALONEMINX = 22579;
	public static final int GOALONEMAXX = 29899;
	public static final int GOALTWOMINX = 22560;
	public static final int GOALTWOMAXX = 29880;

	public static final int GOALONEY = 33941;
	public static final int GOALTWOY = -33968;

	public final static int[] PLAYERIDS = { 47, 49, 19, 53, 23, 57, 59, 63, 65, 67, 69, 71, 73, 75, 13, 61 };
	public final static int[] PLAYERSENSORIDS = { 47, 16, 49, 88, 19, 52, 53, 54, 23, 24, 57, 58, 59, 28, 63, 64, 65, 66, 67, 68, 69, 38, 71, 40, 73, 74, 75, 44, 13, 14, 61, 62 };
	public final static int[] BALLIDS = { 4, 8, 10 };

	public static final HeatMapInit heatMapInit = Utils.calculateHeatMapInit();
	public static final int heatMapWidthInCells = 13;

	// public static final int heatMapHeightInCells = 13;

	public Config()
	{
		entityList = new ConcurrentHashMap<Integer, Entity>();
		createEntityList();
	}

	private void createEntityList()
	{
		// key = left Leg, Leg 0 = left, 1 = right
		// Player(int id, String team, String name, int age, PlayingPosition playingPosition, int preferedFoot, int leftFootID, int
		// rightFootID)

		// Team GELB
		entityList.put(13, new Goalkeeper(13, Team.GELB, "Nick Gertje", 13, 14, 97, 98));
		entityList.put(14, entityList.get(13));

		entityList.put(47, new Player(47, Team.GELB, "Dennis Dotterweich", PlayingPosition.LB, 47, 16));
		entityList.put(16, entityList.get(47));

		entityList.put(49, new Player(49, Team.GELB, "Niklas Waelzlein", PlayingPosition.DF, 49, 88));
		entityList.put(88, entityList.get(49));

		entityList.put(19, new Player(19, Team.GELB, "Wili Sommer", PlayingPosition.RB, 19, 52));
		entityList.put(52, entityList.get(19));

		entityList.put(53, new Player(53, Team.GELB, "Philipp Harlass", PlayingPosition.LM, 53, 54));
		entityList.put(54, entityList.get(53));

		entityList.put(23, new Player(23, Team.GELB, "Roman Hartleb", PlayingPosition.CM, 23, 24));
		entityList.put(24, entityList.get(23));

		entityList.put(57, new Player(57, Team.GELB, "Erik Engelhardt", PlayingPosition.RM, 57, 58));
		entityList.put(58, entityList.get(57));

		entityList.put(59, new Player(59, Team.GELB, "Sandro Schneider", PlayingPosition.CF, 59, 28));
		entityList.put(28, entityList.get(59));

		// Team ROT
		entityList.put(61, new Goalkeeper(61, Team.ROT, "Leon Krapf", 61, 62, 99, 100));
		entityList.put(62, entityList.get(61));
		//
		entityList.put(63, new Player(63, Team.ROT, "Kevin Baer", PlayingPosition.LWB, 63, 64));
		entityList.put(64, entityList.get(63));
		//
		entityList.put(65, new Player(65, Team.ROT, "Luca Ziegler", PlayingPosition.LB, 65, 66));
		entityList.put(66, entityList.get(65));
		//
		entityList.put(67, new Player(67, Team.ROT, "Ben Müller", PlayingPosition.DM, 67, 68));
		entityList.put(68, entityList.get(67));
		//
		entityList.put(69, new Player(69, Team.ROT, "Vale Reitstetter", PlayingPosition.RWB, 69, 38));
		entityList.put(38, entityList.get(69));
		//
		entityList.put(71, new Player(71, Team.ROT, "Christopher Lee", PlayingPosition.RB, 71, 40));
		entityList.put(40, entityList.get(71));
		//
		entityList.put(73, new Player(73, Team.ROT, "Leon Heinze", PlayingPosition.RM, 73, 74));
		entityList.put(74, entityList.get(73));
		//
		entityList.put(75, new Player(75, Team.ROT, "Leo Langhans", PlayingPosition.LM, 75, 44));
		entityList.put(44, entityList.get(75));

		// Balls
		entityList.put(4, new Ball(4));
		entityList.put(8, new Ball(8));
		entityList.put(10, new Ball(10));
		entityList.put(12, new Ball(12));

	}

	public ConcurrentHashMap<Integer, Entity> getEntityList()
	{
		return entityList;
	}
}
