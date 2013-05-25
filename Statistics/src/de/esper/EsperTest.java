package de.esper;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEngineDefaults;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import de.core.Entity;
import de.core.Event;
import de.core.EventBallPossession;
import de.core.Player;

public class EsperTest
{
	private EPRuntime cepRT;
	private EPAdministrator cepAdm;

	public EsperTest()
	{
		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();

		// set time format
		cepConfig.getEngineDefaults().getTimeSource().setTimeSourceType(ConfigurationEngineDefaults.TimeSourceType.NANO);

		// We register Ticks as objects the engine will have to handle
		cepConfig.addEventType("Event", Event.class.getName());
		cepConfig.addEventType("Entity", Entity.class.getName());
		cepConfig.addEventType("Player", Player.class.getName());
		cepConfig.addEventType("EventBallPossession", EventBallPossession.class.getName());

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
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "Event().win:time(" + timeFrame + "milliseconds) where id=" + id);
		cepStatement.addListener(new CEPListener());
	}

	public void getTimedFromSensorId(int id, int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "Event().win:ext_timed(timeStamp," + timeFrame + "milliseconds) where id=" + id);
		cepStatement.addListener(new CEPListener());
	}

	public void getAllFromSensorIdPerMillisecond(int id, int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "Event().win:time_batch(" + timeFrame + "milliseconds) where id=" + id);
		cepStatement.addListener(new CEPListener());
	}

	public void ballPossession(int timeFrame)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "EventBallPossession().win:time_batch(" + timeFrame + "milliseconds)");
		cepStatement.addListener(new BallPossessionListener());
	}

	public EPRuntime getCepRT()
	{
		return cepRT;
	}
}
