package oracle.avmcg;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class AVMMQTTSubscriber implements MqttCallback
{
	// le due variabili conviene impostarle come variabili di ambiente
	// nella shell di lancio
	private static final String SEC_PWD = System.getenv("SEC_PWD");
	private static final String SEC_FILE = System.getenv("SEC_FILE");

	private static IoTGatewayClient gwClient = new IoTGatewayClient(SEC_FILE, SEC_PWD);
	
	private MqttClient client = null;

	private static final int MYQOS = 1;
	private static final String clientId = "javagw1";

	// le due successive forse vale la pena spostarle in un file di properties
	private static final String broker = "tcp://localhost:1883";
	// using wildcard here
	private static final String IN_TOPIC = "devices/avm/msg";

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

		System.out.println("..........");
		System.out.println("Received a msg on topic: " + topic);
		System.out.println("Message: " + sMessage);

		String s = null;
		try
		{
			// to solve the problem with + in string
			s = new String(sMessage.getBytes("UTF-8"));

			if (isPayloadOK(s))
			{
				ParserDati pdd = new ParserDati();

				// encapsulate data in pdd
				pdd.parseAVM(s);

				// add send to Oracle IoT
				// send to Oracle IoT CS the msg
				gwClient.send(pdd);
				
				// send to Traccar
				TraccarClient.sendToVServer(pdd);

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
