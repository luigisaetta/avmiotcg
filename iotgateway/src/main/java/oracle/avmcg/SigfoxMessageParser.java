package oracle.avmcg;

import java.util.StringTokenizer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SigfoxMessageParser implements MessageParser
{
	public TempHumMessage process(String mess)
	{
		TempHumMessage th = new TempHumMessage();
		
		JsonParser parser = new JsonParser();
		
		JsonElement jsonTree = parser.parse(mess);

		if (jsonTree.isJsonObject())
		{
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			
			String device = jsonObject.get("device").getAsString();
			String data = jsonObject.get("data").getAsString();
			
			StringTokenizer stk = new StringTokenizer(hexToAscii(data), ",");
			
			th.setDeviceId(device);
			th.setTemp(stk.nextToken());
			th.setHum(stk.nextToken());
			th.setLum(stk.nextToken());
			
			printData(th);
		}
		return th;
	}
	
	/*
	 * the String sent from Sigfox is HEX, must be converted to ascii
	 * 
	 */
	private static String hexToAscii(String hexStr) 
	{
	    StringBuilder output = new StringBuilder("");
	     
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}
	
	private void printData(TempHumMessage th)
	{
		System.out.println("device : " + th.getDeviceId());
		System.out.println("Temp: " + th.getTemp());
		System.out.println("Hum: " + th.getHum());
		System.out.println("Lum.: " + th.getLum());
	}
}
