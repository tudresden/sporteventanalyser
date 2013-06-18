package de.core;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class Esper
{
	private EPRuntime cepRT;
	private EPAdministrator cepAdm;

	public Esper()
	{
		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();

		// set time format
		// cepConfig.getEngineDefaults().getTimeSource().setTimeSourceType(ConfigurationEngineDefaults.TimeSourceType.NANO);

		// register Ticks as objects the engine will have to handle
		cepConfig.addEventType("Event", Event.class.getName());
		cepConfig.addEventType("Entity", Entity.class.getName());
		cepConfig.addEventType("Player", Player.class.getName());
		cepConfig.addEventType("EventBallPossession", EventBallPossession.class.getName());

		// setup the engine
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);

		cepRT = cep.getEPRuntime();

		// register an EPL statement
		this.cepAdm = cep.getEPAdministrator();
	}

	public EPRuntime getCepRT()
	{
		return cepRT;
	}

	public void sendEvent(Object object)
	{
		getCepRT().sendEvent(object);
	}

	public void getAllEventsFromSensorId(int id, UpdateListener updateListener)
	{
		EPStatement cepStatement = cepAdm.createEPL("SELECT * FROM " + "Event() WHERE sender=" + id);
		cepStatement.addListener(updateListener);
	}

	public void getAllFromSensorId(int id, int timeFrame, UpdateListener updateListener)
	{
		EPStatement cepStatement = cepAdm.createEPL("select * from " + "Event().win:time(" + timeFrame + "milliseconds) where sender=" + id);
		cepStatement.addListener(updateListener);
	}
}
