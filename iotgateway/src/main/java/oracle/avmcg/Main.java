package oracle.avmcg;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;


/**
 * Main class.
 *
 */
public class Main
{
	// Singleton to access configuration
	// try to read config from config.properties
	private static MyConfig config = MyConfig.getInstance();
	
	private static final int SLEEP_TIME = 60000;
	
	private static final String broker = "tcp://" + config.getMqttBrokerHost() + ":" + config.getMqttBrokerPort();

	private static AVMMQTTSubscriber sub = null;

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startHTTPServer(String BASE_URI)
	{
		// create a resource config that scans for JAX-RS resources and providers
		// in oracle.avmcg package
		final ResourceConfig rc = new ResourceConfig().packages("oracle.avmcg");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/*
	 * 
	 * extension added to support incoming MQTT messages
	 * 
	 */
	public static AVMMQTTSubscriber startMQTTSuscriber()
	{
		AVMMQTTSubscriber theSub = new AVMMQTTSubscriber();

		return theSub;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		printHeader();
		config.printConfig();
		
		final HttpServer server = startHTTPServer(config.getBaseUri());

		System.out.println(String.format(
				"AVM IoT Gateway started with WADL available at " + "%sapplication.wadl\nHit CTRL-C to stop it...",
				config.getBaseUri()));

		if (config.isMqttBrokerEnabled())
		{
			sub = startMQTTSuscriber();
			System.out.println("AVM MQTT Gateway started, connected with: " + broker);
		}
			
		// loop and stay up
		loop();
		
		server.shutdown();
	}
	
	private static void printHeader()
	{
		System.out.println("***");
		System.out.println("***");
		System.out.println("*** IoTGateway...");
		System.out.println("***");
	}
	
	private static void loop()
	{
		// loop to keep the server running...
		while (true)
		{
			try
			{
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
