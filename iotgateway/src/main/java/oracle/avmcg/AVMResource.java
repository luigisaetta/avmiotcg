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
 * 
 * this class provide methods to handle HTTP requests compliant to Cotral AVM
 * protocol
 */
@Path("r")
public class AVMResource
{
	// le due variabili sono impostate come variabili di ambiente
	// nella shell di lancio
	private static final String SEC_PWD = System.getenv("SEC_PWD");
	private static final String SEC_FILE = System.getenv("SEC_FILE");

	private static IoTGatewayClient gwClient = new IoTGatewayClient(SEC_FILE, SEC_PWD);

	// Access config
	private static MyConfig config = MyConfig.getInstance();

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
		// to solve the problem with + in string
		String sOut = encodeUTF8(s);
		
		System.out.println("..........");
		System.out.println("POST input request s: " + sOut);

		if ((sOut != null) && (isPayloadOK(sOut)))
		{
			ParserDati pdd = new ParserDati();

			// encapsulate data in pdd
			pdd.parseAVM(sOut);

			// send to Oracle IoT CS the msg
			gwClient.send(pdd);

			// send to Visualization Server
			// now Traccar
			if (config.isTraccarEnabled())
				TraccarClient.sendToVServer(pdd);

			return "OK";
		} else
		{
			// malformed input msg
			System.out.println("Malformed request...");
			return "KO";
		}

	}

	//
	// assuming POSITION ONLY AVM msg
	//
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

	private static final int MIN_LENGTH = config.getMsgMinLength();

	private boolean isPayloadOK(String s)
	{
		if (s != null && s.length() >= MIN_LENGTH)
			return true;
		else
			return false;
	}
}
