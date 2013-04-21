package de.tudresden.inf.rn.mobilis.sea.client.proxy;


public class SportEventAnalyserProxy {

	private ISportEventAnalyserOutgoing _bindingStub;

	public SportEventAnalyserProxy(ISportEventAnalyserOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}

	public ISportEventAnalyserOutgoing getBindingStub() {
		return _bindingStub;
	}

	public void EventNotification(String toJid, int Sender, long Timestamp,
			int PositionX, int PositionY, int PositionZ, int Velocity,
			int Acceleration, int VelocityX, int VelocityY, int VelocityZ,
			int AccelerationX, int AccelerationY, int AccelerationZ) {
		if (null == _bindingStub)
			return;

		Event out = new Event(Sender, Timestamp, PositionX, PositionY,
				PositionZ, Velocity, Acceleration, VelocityX, VelocityY,
				VelocityZ, AccelerationX, AccelerationY, AccelerationZ);
		out.setTo(toJid);

		_bindingStub.sendXMPPBean(out);
	}

}