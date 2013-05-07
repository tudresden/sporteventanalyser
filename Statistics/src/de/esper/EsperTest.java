package de.esper;

import java.util.Random;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEngineDefaults;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import de.core.Entity;

public class EsperTest
{
	private EPRuntime cepRT;
	private EPAdministrator cepAdm;

	private static Random generator = new Random();

	public EsperTest()
	{
		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();

		// set time format
		cepConfig.getEngineDefaults().getTimeSource().setTimeSourceType(ConfigurationEngineDefaults.TimeSourceType.NANO);

		// We register Ticks as objects the engine will have to handle
		cepConfig.addEventType("StockEntity", Entity.class.getName());
		// We setup the engine
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);

		cepRT = cep.getEPRuntime();

		// We register an EPL statement
		this.cepAdm = cep.getEPAdministrator();
		// EPStatement cepStatement = cepAdm.createEPL("select * from " + "StockTick(symbol='AAPL').win:length(2) " +
		// "having avg(price) > 6.0");

	}

	public void getAllFromSensorId(int id, int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "StockEntity().win:time(" + timeFrame + ") where symbol='" + id + "'");
		cepStatement.addListener(new CEPListener());
	}

	public void getTimedFromSensorId(int id, int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "StockEntity().win:ext_timed(timeStamp," + timeFrame + ") where symbol='" + id + "'");
		cepStatement.addListener(new CEPListener());
	}

	public void getAllFromSensorIdPerSecond(int id, int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "StockEntity().win:time_batch(" + timeFrame + ") where symbol='" + id + "'");
		cepStatement.addListener(new CEPListener());
	}

	public EPRuntime getCepRT()
	{
		return cepRT;
	}

	public void test()
	{
		// We generate a few ticks...
		for (int i = 0; i < 5000000; i++)
		{
			GenerateRandomTick(cepRT);
		}
	}

	public static void GenerateRandomTick(EPRuntime cepRT)
	{
		double price = (double) generator.nextInt(10);
		long timeStamp = System.currentTimeMillis();
		String symbol = "AAPL";
		Tick tick = new Tick(symbol, price, timeStamp);
		// System.out.println("Sending tick:" + tick);
		cepRT.sendEvent(tick);
	}
}
