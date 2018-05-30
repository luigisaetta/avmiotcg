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
	private static final int SLEEP_TIME = 60000;
	// Base URI the Grizzly HTTP server will listen on
	// changed to 0.0.0.0 in order to make it OK for Docker
	public static final String BASE_URI = "http://0.0.0.0:8080/avm/";
	private static final String broker = "tcp://mqtt-broker:1883";
	
	private static AVMMQTTSubscriber sub = null;

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startHTTPServer()
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
		final HttpServer server = startHTTPServer();

		System.out.println(String.format(
				"AVM IoT Gateway started with WADL available at " + "%sapplication.wadl\nHit CTRL-C to stop it...",
				BASE_URI));

		sub = startMQTTSuscriber();
		
		System.out.println("AVM MQTT Gateway started, connected with: " + broker);
		
		// loop to keep the server running...
		while(true)
		{
			try
			{
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				
				server.shutdown();
			}
		}
	}
}
