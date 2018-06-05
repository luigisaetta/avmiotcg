package oracle.avmcg;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONMessageParser implements MessageParser
{

	@Override
	public OBD2Message process(String mess)
	{
		OBD2Message outMess = new OBD2Message();
		
		// tranform String to JSON object
		JsonParser parser = new JsonParser();
		JsonElement jsonTree = parser.parse(mess);
		
		if(jsonTree.isJsonObject()) 
		{
		    JsonObject jsonObject = jsonTree.getAsJsonObject();
		    
		    String carId = jsonObject.get("CARID").getAsString();
		    String lat = jsonObject.get("LAT").getAsString();
		    String lon = jsonObject.get("LON").getAsString();
		    String speed = jsonObject.get("SPEED").getAsString();
		    String km = jsonObject.get("DISTANCE").getAsString();
		    String rpm = jsonObject.get("RPM").getAsString();
		    String coolantTemp = jsonObject.get("COOLANT_TEMP").getAsString();
		    String maf = jsonObject.get("MAF").getAsString();
		    String runtime = jsonObject.get("RUN_TIME").getAsString();
		    
		    System.out.println("Id: " + carId);
		    System.out.println("Lat: " + lat);
		    System.out.println("Lon: " + lon);
		    System.out.println("RPM: " + rpm);
		    
		    outMess.setDeviceId(carId);
		    outMess.setLat(lat);
		    outMess.setLng(lon);
		    outMess.setVel(speed);
		    outMess.setKm(km);
		    outMess.setSeqInfo("1");
		    outMess.setRpm(rpm);
		    outMess.setCoolantTemp(coolantTemp);
		    outMess.setMaf(maf);
		    outMess.setRuntime(runtime);
		}
		else
		{
			System.out.println("Error in parsing: Not a JSON tree !!!");
		}
		
		return outMess;
	}

}
