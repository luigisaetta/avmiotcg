package oracle.avmcg;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class AVMMQTTSubscriber implements MqttCallback
{
	// le due variabili sono impostate come variabili di ambiente
	// nella shell di lancio
	private static final String SEC_PWD = System.getenv("SEC_PWD");
	private static final String SEC_FILE = System.getenv("SEC_FILE");

	// Access config
	private static MyConfig config = MyConfig.getInstance();

	private static IoTGatewayClient gwClient = new IoTGatewayClient(SEC_FILE, SEC_PWD);

	private MqttClient client = null;

	private static final int MYQOS = 1;
	private static final String clientId = "javagw1";

	// MQTT broker
	private static final String broker = "tcp://" + config.getMqttBrokerHost() + ":" + config.getMqttBrokerPort();
	// using wildcard here
	private static final String IN_TOPIC = config.getInputTopic();

	private static MemoryPersistence persistence = new MemoryPersistence();
	private static MqttConnectOptions connOpts = new MqttConnectOptions();

	MessageParser processor = MessageParserFactory.createProcessor(config.getMsgType());
	
	private static final int MIN_LENGTH = config.getMsgMinLength();

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

	/*
	 * (non-Javadoc) The msg is processed in this function
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
	 * org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception
	{
		String sMessage = new String(message.getPayload());

		System.out.println("..........");
		System.out.println("Received a msg on topic: " + topic);
		System.out.println("Message: " + sMessage);

		// to solve the problem with + in string
		String sOut = encodeUTF8(sMessage);
		
		if (isPayloadOK(sOut))
		{
			OBD2Message msg = (OBD2Message) processor.process(sOut);

			// add send to Oracle IoT
			// send to Oracle IoT CS the msg
			gwClient.send(msg);

			// send to Traccar
			if (config.isTraccarEnabled())
				TraccarClient.sendToVServer(msg);

		} else
		{
			// malformed input msg
			System.out.println("Malformed request...");
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token)
	{

	}

	private String encodeUTF8(String sInput)
	{
		String sOutput = null;

		try
		{
			sOutput = new String(sInput.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return sOutput;
	}

	private boolean isPayloadOK(String s)
	{
		if (s != null && s.length() >= MIN_LENGTH)
			return true;
		else
			return false;
	}
}
