package de.core;

import java.util.Random;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class exampleMain
{
	static int count = 1;

	public static class Tick
	{
		String symbol;
		Double price;
		long timeStamp;

		public Tick(String s, double p, long t)
		{
			symbol = s;
			price = p;
			// timeStamp = new Date(t);
			timeStamp = t;
		}

		public double getPrice()
		{
			return price;
		}

		public String getSymbol()
		{
			return symbol;
		}

		public long getTimeStamp()
		{
			return timeStamp;
		}

		@Override
		public String toString()
		{
			return "Price: " + price.toString() + " time: " + timeStamp;
		}
	}

	private static Random generator = new Random();

	public static void GenerateRandomTick(EPRuntime cepRT)
	{

		double price = (double) generator.nextInt(10);
		long timeStamp = System.currentTimeMillis();
		String symbol = "AAPL";
		Tick tick = new Tick(symbol, count, timeStamp);
		// System.out.println("Sending tick:" + tick);
		cepRT.sendEvent(tick);
		count++;
	}

	public static class CEPListener implements UpdateListener
	{

		public void update(EventBean[] newData, EventBean[] oldData)
		{
			System.out.println("Event received: " + newData[0].getUnderlying());
		}
	}

	public static void main(String[] args)
	{

		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("StockTick", Tick.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
		EPRuntime cepRT = cep.getEPRuntime();

		EPAdministrator cepAdm = cep.getEPAdministrator();
		// EPStatement cepStatement = cepAdm.createEPL("select * from " +
		// "StockTick(symbol='AAPL').win:ext_timed(StockTick.timeStamp,1msec)");
		// EPStatement cepStatement = cepAdm.createEPL("select * from " + "StockTick(symbol='AAPL')");
		// EPStatement cepStatement = cepAdm.createEPL("select * from StockTick(symbol='AAPL').win:length(4) having count(*) > 2");

		EPStatement cepStatement = cepAdm.createEPL("select * from StockTick(symbol='AAPL').win:length(4).std:lastevent()");

		cepStatement.addListener(new CEPListener());

		// We generate a few ticks...
		for (int i = 0; i < 10000; i++)
		{
			GenerateRandomTick(cepRT);
		}
	}
}