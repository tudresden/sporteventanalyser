package de.core;

public class HeatMapGrid
{

	private int[] grid;
	private float widthResolution;
	private float heightResolution;
	private int numberOfCellsForWidth;

	public HeatMapGrid(int numberOfCellsForWidth, int fieldWidthInMM, int fieldHeightInMM)
	{

		this.numberOfCellsForWidth = numberOfCellsForWidth;
		this.widthResolution = fieldWidthInMM / numberOfCellsForWidth;
		int numberOfCellsForHeight = Math.round((float) fieldHeightInMM / widthResolution);
		this.heightResolution = fieldHeightInMM / numberOfCellsForHeight;
		this.grid = new int[numberOfCellsForWidth * numberOfCellsForHeight];

		// System.out.println("width " + numberOfCellsForWidth + "--- height " + numberOfCellsForHeight);
	}

	public int getGridCellFromPosition(int y, int x)
	{

		y += 33960;
		if (y < 0)
			y = 0;

		if (x < 0)
			x = 0;

		int column = (int) (y / widthResolution);
		int row = (int) (x / heightResolution);
		// System.out.println("[" + row + "," + column + "]");

		return row * numberOfCellsForWidth + column;
	}

	public void incrementCellValue(int y, int x)
	{

		grid[this.getGridCellFromPosition(y, x)]++;
	}

	public int getGridSize()
	{
		return grid.length;
	}

	public int getCell(int nr)
	{
		return grid[nr];
	}

	public int getSum()
	{

		int sum = 0;
		for (int i = 0; i < grid.length; i++)
			sum += grid[i];
		return sum;
	}

	public void drawGrid()
	{

		System.out.println("\n-------------------\n");
		for (int x = 0; x < grid.length / numberOfCellsForWidth; x++)
		{

			String line = new String();
			for (int y = 0; y < numberOfCellsForWidth; y++)
			{

				if (grid[x * numberOfCellsForWidth + y] != 0)
					line = line.concat(Integer.toString(grid[x * numberOfCellsForWidth + y]));
				else
					line = line.concat(" ");
				line = line.concat(" | ");
			}
			System.out.println(line + "\n");
		}

	}
}
