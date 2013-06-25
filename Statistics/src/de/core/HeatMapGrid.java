package de.core;

public class HeatMapGrid
{

	private int[] grid;
	private float widthResolution;
	private float heightResolution;
	private int widthInCells;
	private int heightInCells;
	private int fieldYmin;
	private int fieldYmax;
	private int fieldXmin;
	private int fieldXmax;
	private int yMinNegativeAbs;
	private int xMinNegativeAbs;
	private int cellOfLastUpdate;

	public HeatMapGrid(HeatMapInit heatMapInit)
	{

		fieldYmin = Config.GAMEFIELDMINY;
		fieldYmax = Config.GAMEFIELDMAXY;
		fieldXmin = Config.GAMEFIELDMINX;
		fieldXmax = Config.GAMEFIELDMAXX;

		widthInCells = heatMapInit.widthInCells;
		heightInCells = heatMapInit.heightInCells;
		widthResolution = heatMapInit.widthResolution;
		heightResolution = heatMapInit.heightResolution;

		yMinNegativeAbs = heatMapInit.yMinNegativeAbs;
		xMinNegativeAbs = heatMapInit.xMinNegativeAbs;

		this.grid = new int[widthInCells * heightInCells];
	}

	public int getGridCellFromPosition(int y, int x)
	{

		if (fieldYmin <= y && y <= fieldYmax && fieldXmin <= x && x <= fieldXmax)
		{

			y += yMinNegativeAbs;
			x += xMinNegativeAbs;
			int column = (int) (y / widthResolution);
			int row = (int) (x / heightResolution);

			return row * widthInCells + column;
		}

		else
			return -1;
	}

	public void incrementCellValue(int y, int x)
	{
		int cellNr = this.getGridCellFromPosition(y, x);
		grid[cellNr]++;
		cellOfLastUpdate = cellNr;
	}

	public int getGridSize()
	{
		return grid.length;
	}

	public int getCell(int nr)
	{
		return grid[nr];
	}

	public void setCell(int nr, int value)
	{
		grid[nr] = value;
	}

	public int getSum()
	{
		int sum = 0;
		for (int i : grid)
			sum += i;

		return sum;
	}

	public int[] getScaledHeatmap(int upperBound)
	{
		int maxValue = 0;
		int gridSize = grid.length;

		for (int i : grid)
		{
			if (i > maxValue)
			{
				maxValue = i;
			}
		}

		int[] scaledGrid = new int[gridSize];
		System.out.println(maxValue);
		if (maxValue > upperBound)
		{
			float factor = (float) maxValue / upperBound;
			System.out.println(factor);
			for (int i = 0; i < gridSize; i++)
			{
				scaledGrid[i] = Utils.fastFloor(grid[i] / factor);
			}
		}
		return scaledGrid;
	}

	public void drawGrid(boolean scaled, int upperBound)
	{
		int[] scaledGrid = grid.clone();
		if (scaled)
			scaledGrid = getScaledHeatmap(upperBound);
		System.out.println("\n-------------------\n");
		String line;
		int index = 0;
		for (int x = 0; x < heightInCells; x++)
		{
			line = "";
			for (int y = 0; y < widthInCells; y++)
			{
				if (scaledGrid[index] != 0)
					if (scaledGrid[index] < 10)
						line = line.concat(" " + Integer.toString(scaledGrid[index]));
					else
						line = line.concat(Integer.toString(scaledGrid[index]));
				else
					line = line.concat("--");
				line = line.concat(" | ");
				index++;
			}
			System.out.println(line + "\n");
		}
	}

	public void drawFieldWithLastPosition()
	{
		System.out.println("\n-------------------\n");
		String line;
		int index = 0;
		for (int x = 0; x < heightInCells; x++)
		{
			line = "";
			for (int y = 0; y < widthInCells; y++)
			{
				if (index == cellOfLastUpdate)
					line = line.concat("X");
				else
					line = line.concat(" ");
				line = line.concat(" | ");
				index++;
			}
			System.out.println(line + "\n");
		}
	}
}