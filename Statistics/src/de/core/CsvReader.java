package de.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader
{
	public static ArrayList<String[]> get(String filename)
	{
		ArrayList<String[]> arrayList = new ArrayList<String[]>();

		try
		{
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			while ((strLine = br.readLine()) != null)
			{
				arrayList.add(strLine.split(","));
			}

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		return arrayList;
	}
}
