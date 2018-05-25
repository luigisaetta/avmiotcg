package oracle.avmcg;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "r" path)
 */
@Path("r")
public class MyResource
{
	// le due variabili conviene impostarle come variabili di ambiente
	// nella shell di lancio
	private static final String SEC_PWD = System.getenv("SEC_PWD");
	private static final String SEC_FILE = System.getenv("SEC_FILE");

	//
	// assuming POSITION ONLY AVM msg
	//
	private static final int MIN_LENGTH = 76;

	private static IoTGatewayClient gwClient = new IoTGatewayClient(SEC_FILE, SEC_PWD);

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt()
	{
		return "OK";
	}

	/**
	 * Method handling HTTP POST requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String doPost(@QueryParam("s") String s)
	{
		String s2 = null;
		try
		{
			// to solve the problem with + in string
			s2 = new String(s.getBytes("UTF-8"));
			
			System.out.println("..........");
			System.out.println("POST input request s: " + s2);

			if (s2 != null && s2.length() >= MIN_LENGTH)
			{
				ParserDati pdd = new ParserDati();

				// encapsulate data in pdd
				pdd.parseAVM(s2);

				// send to Oracle IoT CS the msg
				gwClient.send(pdd);

				return "OK";
			} else
			{
				// malformed input msg
				System.out.println("Malformed request...");
				return "KO";
			}
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			
			return "KO";
		}
	}
}
