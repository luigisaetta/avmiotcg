package oracle.avmcg;

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
	//
	// assuming POSITION ONLY msg
	//
	private static final int MIN_LENGTH = 76;
	
	IoTGatewayClient gClient = new IoTGatewayClient();
	
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
		return "Got it!";
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
    	System.out.println("POST input request s: " + s);
    	
    	if (s != null && s.length() >= MIN_LENGTH)
    	{    		
    		ParsingDati pdd = new ParsingDati();
    		
    		pdd.parse(s);
    		
    		// for debugging purposes
    		printData(pdd);
    		
    		// send to IoT
    		gClient.send();
    		
    		return "OK";
    	}
    	else
    	{
    		// malformed input msg
    		System.out.println("Malformed request...");
    		return "KO";
    	}
    	
    }

	private void printData(ParsingDati pdd)
	{
		System.out.println("Id: " + pdd.getidChiamante());
		System.out.println("Lat: " + pdd.getLatitudine());
		System.out.println("Lon: " + pdd.getLongitudine());
		System.out.println("Speed: " + pdd.getVelocita());
		System.out.println("Km: " + pdd.getChilometraggio());
	}
}
