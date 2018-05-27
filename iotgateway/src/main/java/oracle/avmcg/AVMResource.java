package oracle.avmcg;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Root resource (exposed at "r" path)
 * 
 * this class provide methods to handle HTTP requests compliant to Cotral AVM
 * protocol
 */
@Path("r")
public class AVMResource
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

			if (isPayloadOK(s2))
			{
				ParserDati pdd = new ParserDati();

				// encapsulate data in pdd
				pdd.parseAVM(s2);

				// send to Oracle IoT CS the msg
				gwClient.send(pdd);

				// send to Visualization Server
				// now Traccar
				sendToVServer(pdd);

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
	
	private boolean isPayloadOK(String s)
	{
		if (s != null && s.length() >= MIN_LENGTH)
			return true;
		else
			return false;
	}
	
	/*
	 * 
	 * Using Apache HTTP client and Google JSON
	 * 
	 */
	private void sendToVServer(ParserDati msg)
	{
		// make a POST request using TRACCAR owntracks protocol
        String tracUrl = "http://localhost:5144";
        
		// build JSON request object
		// this is an example of the payload
		// {"lon":2.29513,"lat":48.85833,"vel": 61,"_type":"location","tst":1497476456,
		// "tid":"JJ"}
		Gson gson = new Gson();

		long tst = System.currentTimeMillis()/1000;

		TracMessage trcMess = new TracMessage(msg.getLongitudine(), msg.getLatitudine(), msg.getVelocita(), tst, "R9",
				"location");

		try
		{
			HttpClient httpClient = HttpClientBuilder.create().build();
			
			HttpPost     post          = new HttpPost(tracUrl);
			StringEntity postingString = new StringEntity(gson.toJson(trcMess));
			
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			
			HttpResponse  response = httpClient.execute(post);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
