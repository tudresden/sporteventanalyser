package de.core;

import java.awt.Point;

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
	private int yOffset;
	private int xOffset;
	private int cellOfLastUpdate;
	private Point positionOfLastUpdate = new Point();

	public int getCellOfLastUpdate()
	{
		return cellOfLastUpdate;
	}

	public void setCellOfLastUpdate(int cellOfLastUpdate)
	{
		this.cellOfLastUpdate = cellOfLastUpdate;
	}

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

		yOffset = heatMapInit.yOffset;
		xOffset = heatMapInit.xOffset;

		this.grid = new int[widthInCells * heightInCells];
	}

	public int getGridCellIndexFromPosition(int y, int x)
	{

		if (fieldYmin <= y && y <= fieldYmax && fieldXmin <= x && x <= fieldXmax)
		{

			x += xOffset;
			y += yOffset;
			int column = (int) (y / widthResolution);
			int row = (int) (x / heightResolution);
			// int row = (int) (y / heightResolution);
			// int column = (int) (x / widthResolution);

			return row * widthInCells + column;
		}

		else
			return -1;
	}

	public Point getGridCellCoordinatesFromPosition(int y, int x)
	{

		if (fieldYmin <= y && y <= fieldYmax && fieldXmin <= x && x <= fieldXmax)
		{
			x += xOffset;
			y += yOffset;
			int column = (int) (y / widthResolution);
			int row = (int) (x / heightResolution);

			return new Point(row, column);
		}

		else
			return null;
	}

	public void incrementCellValue(int y, int x)
	{
		Point cellCoordinates = this.getGridCellCoordinatesFromPosition(y, x);
		int cellNr = cellCoordinates.x * widthInCells + cellCoordinates.y;
		grid[cellNr]++;
		cellOfLastUpdate = cellNr;
		positionOfLastUpdate.x = x;
		positionOfLastUpdate.y = y;
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

	public Point getPositionOfLastUpdate()
	{
		return positionOfLastUpdate;
	}

	public void setPositionOfLastUpdate(Point positionOfLastUpdate)
	{
		this.positionOfLastUpdate = positionOfLastUpdate;
	}
}