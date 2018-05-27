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
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://localhost:8080/avm/";

	private static MyMQTTSubscriber sub = null;

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
	public static MyMQTTSubscriber startMQTTSuscriber()
	{
		MyMQTTSubscriber theSub = new MyMQTTSubscriber();
		
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
				"AVM IoT Gateway started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));

		sub = startMQTTSuscriber();
		
		System.out.println("AVM MQTT Gateway started...");
		
		System.in.read();

		server.shutdown();
	}
}
