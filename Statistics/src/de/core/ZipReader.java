package de.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

public class ZipReader
{
	private FileInputStream fs;
	private ZipInputStream zs;
	private DataInputStream in;
	private BufferedReader br;
	private String source;

	public ZipReader(String source) throws IOException
	{
		this.source = source;
		initReader();
	}

	private void initReader() throws IOException
	{
		fs = new FileInputStream(source);
		zs = new ZipInputStream(fs);
		in = new DataInputStream(zs);
		br = new BufferedReader(new InputStreamReader(in));
	}

	public String readRawNextLine()
	{
		try
		{
			String strLine;

			if ((strLine = br.readLine()) != null)
			{
				return strLine;
			}
			else
			{
				in.close();
			}
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public String[] readNextLine()
	{
		final String string = readRawNextLine();
		if (string != null)
		{
			return string.replace("\"", "").split("\\s*,\\s*");
		}
		return null;
	}

	public String[] readNextCsvLine()
	{
		final String string = readRawNextLine();
		if (string != null)
		{
			return string.replace("\"", "").split("\\s*;\\s*");
		}
		return null;
	}
}
