package oracle.avmcg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyConfig
{
	// static variable instance (Singleton)
	private static MyConfig instance = null;
	
	Properties prop = new Properties();
	
	// flag to enable/disable send to Traccar
	private boolean traccarEnabled = false;
	private String traccarUrl = "http://visual-server:5144";
	

	// Base URI the Grizzly HTTP server will listen on
	// changed to 0.0.0.0 in order to make it OK for Docker
	private String baseUri = "http://0.0.0.0:8080/avm/";
	
	private boolean mqttBrokerEnabled = false;
	private String mqttBrokerHost = "mqtt-broker";
	private int mqttBrokerPort = 1883;
	private String inputTopic = "devices/avm/msg";
	
	private int msgMinLength = 76;
	

	// private constructor restricted to this class itself
	private MyConfig()
	{
		readConfig();
	}

	// static method to create instance of Singleton class
	public static MyConfig getInstance()
	{
		if (instance == null)
			instance = new MyConfig();
		
		return instance;
	}
	
	/*
	 * 
	 * try to read config file... otherwise use default
	 * 
	 */
	public void readConfig()
	{
		// try to read properties file
		InputStream input = null;

		try
		{
			// subdirectory of the current workdir
			input = new FileInputStream("./config/config.properties");

			// load the properties file
			prop.load(input);

			if ((get("traccar.enabled") != null) && (get("traccar.enabled").equals("true")))
				traccarEnabled = true;
			
			if (get("traccar.url") != null)
				traccarUrl = get("traccar.url");
			
			if ((get("mqtt.broker.enabled") != null) && (get("mqtt.broker.enabled").equals("true")))
			{
				mqttBrokerEnabled = true;
			}
				
			if (get("base.uri") != null)
				baseUri = get("base.uri");
			if (get("mqtt.broker.host") != null)
				mqttBrokerHost = get("mqtt.broker.host");
			if (get("mqtt.broker.port") != null)
				mqttBrokerPort = Integer.parseInt(get("mqtt.broker.port"));
			if (get("mqtt.in.topic") != null)
				inputTopic = get("mqtt.in.topic");
			if (get("message.minlength") != null)
				msgMinLength = Integer.parseInt(get("message.minlength"));
		} catch (Exception ex)
		{
			// ex.printStackTrace();
			System.out.println("Error reading config.properties");
			System.out.println("using default values...");
		} finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public String getBaseUri()
	{
		return baseUri;
	}

	public boolean isTraccarEnabled()
	{
		return traccarEnabled;
	}
	
	public String getTraccarUrl()
	{
		return traccarUrl;
	}
	
	public String getMqttBrokerHost()
	{
		return mqttBrokerHost;
	}

	public int getMqttBrokerPort()
	{
		return mqttBrokerPort;
	}
	
	public String getInputTopic()
	{
		return inputTopic;
	}
	
	public int getMsgMinLength()
	{
		return msgMinLength;
	}
	
	public boolean isMqttBrokerEnabled()
	{
		return mqttBrokerEnabled;
	}
	
	public void printConfig()
	{
		System.out.println("Configuration:");
		System.out.println("traccar.enabled = " + this.isTraccarEnabled());
		System.out.println("traccar.url = " + this.getTraccarUrl());
		
		System.out.println("base.uri = " + this.getBaseUri());
		
		System.out.println("mqtt.broker.enabled = " + this.isMqttBrokerEnabled());
		System.out.println("mqtt.broker.host = " + this.getMqttBrokerHost());
		System.out.println("mqtt.broker.port = " + this.getMqttBrokerPort());
		System.out.println("mqtt.in.topic = " + this.getInputTopic());
		
		System.out.println("message.minlength = " + this.getMsgMinLength());
		System.out.println("...");
		System.out.println("...");
	}
	
	private String get(String name)
	{
		// remove leading and trailing spaces
		return prop.getProperty(name).trim();
	}
}
