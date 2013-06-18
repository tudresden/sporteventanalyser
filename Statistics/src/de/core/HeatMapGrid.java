package de.core;


public class HeatMapGrid
{

	private int[] grid;
	private float widthResolution;
	private float heightResolution;
	private int numberOfCellsForWidth;
	private long lastUpdateTimeStamp = 0L;
	private int cellOfLastUpdate;
	private int fieldYmin;
	private int fieldYmax;
	private int fieldXmin;
	private int fieldXmax;

	public HeatMapGrid(int numberOfCellsForWidth, int fieldYmin, int fieldYmax, int fieldXmin, int fieldXmax)
	{

		int fieldWidthInMM = fieldYmax + Math.abs(fieldYmin);
		int fieldHeightInMM = fieldXmax + Math.abs(fieldXmin);
		this.numberOfCellsForWidth = numberOfCellsForWidth;
		this.widthResolution = fieldWidthInMM / numberOfCellsForWidth;
		int numberOfCellsForHeight = Math.round((float) fieldHeightInMM / widthResolution);
		this.heightResolution = fieldHeightInMM / numberOfCellsForHeight;
		this.grid = new int[numberOfCellsForWidth * numberOfCellsForHeight];
		this.fieldYmin = fieldYmin;
		this.fieldYmax = fieldYmax;
		this.fieldXmin = fieldXmin;
		this.fieldXmax = fieldXmax;
	}

	public int getGridCellFromPosition(int y, int x)
	{

		if (fieldYmin <= y
			&& y <= fieldYmax
			&& fieldXmin <= x
			&& x <= fieldXmax)
		{
			if (fieldYmin < 0)
				y -= fieldYmin;
			if (fieldXmin < 0)
				x -= fieldXmin;
			int column = (int) (y / widthResolution);
			int row = (int) (x / heightResolution);
			// System.out.println("[" + row + "," + column + "]");

			return row * numberOfCellsForWidth + column;
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
		for (int i = 0; i < grid.length; i++)
			sum += grid[i];
		return sum;
	}
	
	public int[] getScaledHeatmap(int upperBound)
	{		
		int maxValue = 0;
		for (int i = 0; i<grid.length; i++)
		{
			if (grid[i] > maxValue)
			{
				maxValue = grid[i];
			}
		}

		int[] scaledGrid = new int[grid.length];
		if (maxValue > upperBound)
		{
			float factor = maxValue / upperBound;
			for (int i = 0; i<grid.length; i++)
			{
				scaledGrid[i] = Math.round(grid[i] / factor);
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
		for (int x = 0; x < scaledGrid.length / numberOfCellsForWidth; x++)
		{

			String line = new String();
			for (int y = 0; y < numberOfCellsForWidth; y++)
			{

				if (scaledGrid[x * numberOfCellsForWidth + y] != 0)
					if (scaledGrid[x * numberOfCellsForWidth + y] < 10)
						line = line.concat(" " + Integer.toString(scaledGrid[x * numberOfCellsForWidth + y]));
					else
						line = line.concat(Integer.toString(scaledGrid[x * numberOfCellsForWidth + y]));
				else
					line = line.concat("--");
				line = line.concat(" | ");
			}
			System.out.println(line + "\n");
		}
	}
	
	public void drawFieldWithLastPosition()
	{
		System.out.println("\n-------------------\n");
		for (int x = 0; x < grid.length / numberOfCellsForWidth; x++)
		{
			String line = new String();
			for (int y = 0; y < numberOfCellsForWidth; y++)
			{
				if (x * numberOfCellsForWidth + y == cellOfLastUpdate)
					line = line.concat("X");
				else
					line = line.concat(" ");
				line = line.concat(" | ");
			}
			System.out.println(line + "\n");
		}
	}

	public long getLastUpdateTimeStamp()
	{
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(long lastUpdateTimeStamp)
	{
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}
	
	public int getWidth()
	{
		return numberOfCellsForWidth;
	}
}