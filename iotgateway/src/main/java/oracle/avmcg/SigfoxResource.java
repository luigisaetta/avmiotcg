package oracle.avmcg;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
 * 
 * The class has been added as a template to support
 * Sigfox integration use-case
 * 
 * This is an example of Sigfox message
 * { device: '1D1B7F',
 * data: '32332e342c33342c323331',
 * rssi: '-139.00',
 * duplicate: 'false' }
 * 
 */
@Path("sigfox")
public class SigfoxResource
{
	private static final String SEC_PWD = System.getenv("SEC_PWD");
	private static final String SEC_FILE = System.getenv("SEC_FILE");

	private static IoTGatewayClient gwClient = new IoTGatewayClient(SEC_FILE, SEC_PWD);
	
	MessageParser processor = MessageParserFactory.createProcessor("SIGFOX");
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt()
	{
		return "OK";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void doPost(String request)
	{
		System.out.println("POST input request s: " + request);
		
		if (isPayloadOK(request))
		{
			TempHumMessage th = (TempHumMessage) processor.process(request);
			
			// send to Oracle IoT CS the msg
			gwClient.send(th);
		}
		return;
	}
	
	private boolean isPayloadOK(String s)
	{
		if (s != null)
			return true;
		else
			return false;
	}
}
