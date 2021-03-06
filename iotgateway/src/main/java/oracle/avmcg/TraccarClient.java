package oracle.avmcg;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

public class TraccarClient
{
	/*
	 * 
	 * Using Apache HTTP client and Google JSON
	 * 
	 */
	private static MyConfig config = MyConfig.getInstance();
	
	public static void sendToVServer(OBD2Message msg)
	{
		String tracUrl = config.getTraccarUrl();
		
		// make a POST request using TRACCAR owntracks protocol
		
		// build JSON request object
		// this is an example of the payload
		// {"lon":2.29513,"lat":48.85833,"vel": 61,"_type":"location","tst":1497476456,
		// "tid":"JJ"}
		Gson gson = new Gson();

		long tst = System.currentTimeMillis() / 1000;

		TraccarMessage trcMess = new TraccarMessage(msg.getLng(), msg.getLat(), msg.getVel(), tst, 
				msg.getDeviceId(), "R09", "location");

		try
		{
			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpPost post = new HttpPost(tracUrl);
			StringEntity postingString = new StringEntity(gson.toJson(trcMess));

			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			
			// POST to Traccar
			httpClient.execute(post);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
