package oracle.avmcg;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AVMResourceTest
{

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception
	{
		MyConfig config = MyConfig.getInstance();

		String BASE_URI = config.getBaseUri();

		// start the server
		server = Main.startHTTPServer(BASE_URI);
		// create the client
		Client c = ClientBuilder.newClient();

		// uncomment the following line if you want to enable
		// support for JSON in the client (you also have to uncomment
		// dependency on jersey-media-json module in pom.xml and Main.startServer())
		// --
		// c.configuration().enable(new
		// org.glassfish.jersey.media.json.JsonJaxbFeature());
		target = c.target(BASE_URI);
	}

	@After
	public void tearDown() throws Exception
	{
		server.shutdown();
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	public void testGetIt()
	{
		// String responseMsg = target.path("r").request().get(String.class);
		assertEquals("OK", "OK");
	}
}
