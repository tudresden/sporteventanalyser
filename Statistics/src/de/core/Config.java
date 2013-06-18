package de.core;

import java.util.concurrent.ConcurrentHashMap;

public class Config
{
	private ConcurrentHashMap<Integer, Entity> entityList;

	public final static float BALLPOSSESSIONTHRESHOLD = 1000f; // 1000mm = 1m

	public static final int GAMEFIELDMINX = -50;
	public static final int GAMEFIELDMAXX = 52489;
	public static final int GAMEFIELDMINY = -33960;
	public static final int GAMEFIELDMAXY = 33965;

	public static final int GOALONEMINX = 22579;
	public static final int GOALONEMAXX = 29899;
	public static final int GOALTWOMINX = 22560;
	public static final int GOALTWOMAXX = 29880;

	public final static long GAMESTARTTIMESTAMP = 10753295594424116L;
	public final static double TIMEFACTOR = Math.pow(10, 12); // pico seconds

	public final static int[] PLAYERIDS = { 47, 49, 19, 53, 23, 57, 59, 63, 65, 67, 69, 71, 73, 75 };
	public final static int[] PLAYERSENSORIDS = { 47, 16, 49, 88, 19, 52, 53, 54, 23, 24, 57, 58, 59, 28, 63, 64, 65, 66, 67, 68, 69, 38, 71, 40, 73, 74, 75, 44 };
	public final static int[] BALLIDS = { 4, 8, 10 };

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
		entityList.put(13, new Goalkeeper(13, "GELB", "Nick Gertje", 29, 1, 13, 14, 97, 98));

		entityList.put(47, new Player(47, "GELB", "Dennis Dotterweich", 21, PlayingPosition.LB, 0, 47, 16));
		entityList.put(16, entityList.get(47));

		entityList.put(49, new Player(49, "GELB", "Niklas Waelzlein", 19, PlayingPosition.DF, 1, 49, 88));
		entityList.put(88, entityList.get(49));

		entityList.put(19, new Player(19, "GELB", "Wili Sommer", 23, PlayingPosition.RB, 1, 19, 52));
		entityList.put(52, entityList.get(19));

		entityList.put(53, new Player(53, "GELB", "Philipp Harlass", 24, PlayingPosition.LM, 0, 53, 54));
		entityList.put(54, entityList.get(53));

		entityList.put(23, new Player(23, "GELB", "Roman Hartleb", 22, PlayingPosition.CM, 1, 23, 24));
		entityList.put(24, entityList.get(23));

		entityList.put(57, new Player(57, "GELB", "Erik Engelhardt", 21, PlayingPosition.RM, 1, 57, 58));
		entityList.put(58, entityList.get(57));

		entityList.put(59, new Player(59, "GELB", "Sandro Schneider", 27, PlayingPosition.CF, 0, 59, 28));
		entityList.put(28, entityList.get(59));

		// Team ROT
		entityList.put(61, new Goalkeeper(61, "ROT", "Leon Krapf", 26, 0, 61, 62, 99, 100));
		//
		entityList.put(63, new Player(63, "ROT", "Kevin Baer", 18, PlayingPosition.LWB, 0, 63, 64));
		entityList.put(64, entityList.get(63));
		//
		entityList.put(65, new Player(65, "ROT", "Luca Ziegler", 29, PlayingPosition.LB, 1, 65, 66));
		entityList.put(66, entityList.get(65));
		//
		entityList.put(67, new Player(67, "ROT", "Ben Müller", 26, PlayingPosition.DM, 1, 67, 68));
		entityList.put(68, entityList.get(67));
		//
		entityList.put(69, new Player(69, "ROT", "Vale Reitstetter", 22, PlayingPosition.RWB, 1, 69, 38));
		entityList.put(38, entityList.get(69));
		//
		entityList.put(71, new Player(71, "ROT", "Christopher Lee", 23, PlayingPosition.RB, 1, 71, 40));
		entityList.put(40, entityList.get(71));
		//
		entityList.put(73, new Player(73, "ROT", "Leon Heinze", 27, PlayingPosition.RM, 0, 73, 74));
		entityList.put(74, entityList.get(73));
		//
		entityList.put(75, new Player(75, "ROT", "Leo Langhans", 21, PlayingPosition.LM, 1, 75, 44));
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
