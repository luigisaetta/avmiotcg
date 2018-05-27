package oracle.avmcg;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class AVMMQTTSubscriber implements MqttCallback
{
	private MqttClient client = null;

	private static final int MYQOS = 1;
	private static final int SLEEP_TIME = 10000;
	private static final String clientId = "javagw1";

	// le due successive forse vale la pena spostarle in un file di properties
	private static final String broker = "tcp://localhost:1883";
	// using wildcard here
	private static final String IN_TOPIC = "devices/+/msg";

	private static MemoryPersistence persistence = new MemoryPersistence();
	private static MqttConnectOptions connOpts = new MqttConnectOptions();

	private static final int MIN_LENGTH = 76;

	public AVMMQTTSubscriber()
	{
		try
		{
			// then connect to the MQTT broker
			System.out.println("Connecting to MQTT broker: " + broker);

			client = new MqttClient(broker, clientId, persistence);

			connOpts.setCleanSession(true);
			connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

			client.connect(connOpts);

			System.out.println("Connected...");

			client.setCallback(this);

			// subscriptions to MQTT topics
			client.subscribe(IN_TOPIC, MYQOS);

		} catch (MqttException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.exit(-1);
		}

	}

	/**
	 * function for MqttCallback interface
	 */
	@Override
	public void connectionLost(Throwable cause)
	{ // Called when the client lost the connection to the broker
		System.out.println("Lost connection to broker...");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception
	{
		String sMessage = new String(message.getPayload());

		System.out.println("Received a msg on topic: " + topic);
		System.out.println("Message: " + sMessage);

		String s = null;
		try
		{
			// to solve the problem with + in string
			s = new String(sMessage.getBytes("UTF-8"));

			System.out.println("..........");
			System.out.println("input request s: " + s);

			if (isPayloadOK(s))
			{
				ParserDati pdd = new ParserDati();

				// encapsulate data in pdd
				pdd.parseAVM(s);

			} else
			{
				// malformed input msg
				System.out.println("Malformed request...");
			}
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token)
	{

	}

	private boolean isPayloadOK(String s)
	{
		if (s != null && s.length() >= MIN_LENGTH)
			return true;
		else
			return false;
	}
}
