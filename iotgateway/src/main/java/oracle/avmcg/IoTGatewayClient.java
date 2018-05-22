package oracle.avmcg;

import oracle.iot.client.device.GatewayDevice;

import java.util.HashMap;
import java.util.Map;
import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.VirtualDevice;

public class IoTGatewayClient
{
	private static final String MANUFACTURER = "Saetta Corporation";

	private static final String DEVICE_MODEL_NUMBER = "MN101";

	private static final String DEVICE_SERIAL = "RM_001_AB";

	private static final String DEVICE_ACTIVATION_ID = "RM_001_AB";

	private static final String OBD2_URN_MSG = "urn:com:oracle:iot:device:obd2";

	private GatewayDevice gw = null;
	private DeviceModel deviceModel = null;
	private String deviceId = null;
	VirtualDevice virtualDevice = null;
	
	Map<String, String> metaData = new HashMap<String, String>();
	
	public IoTGatewayClient(String configFilePath, String pwd)
	{
		System.out.println("IoTCLient params: " + configFilePath);

		try
		{
			gw = new GatewayDevice(configFilePath, pwd);

			if (!gw.isActivated())
			{
				gw.activate();
			}

			System.out.println("OK after activation Gateway...");

			String urnToUse = OBD2_URN_MSG;

			deviceModel = gw.getDeviceModel(urnToUse);

			// add any vendor-specific meta-data to the metaData Map
			initMetadataMap();

			// register it
			deviceId = gw.registerDevice(DEVICE_ACTIVATION_ID, metaData, urnToUse);

			System.out.println("OK after registration device: " + deviceId);

			virtualDevice = gw.createVirtualDevice(deviceId, deviceModel);

		} catch (Exception e)
		{
			e.printStackTrace();

			// abend
			System.exit(-1);
		}
	}

	/**
	 * 
	 * send the message to IoT using Oracle SDK
	 */
	public void send(ParsingDati msg)
	{
		System.out.println("Msg sent to Iot...");

		printData(msg);
		
		try
		{			
			virtualDevice.update().set("ora_latitude", msg.getLatitudine()).set("ora_longitude", msg.getLongitudine())
			.set("ora_altitude", 0)
			.set("ora_obd2_vehicle_speed", msg.getVelocita()).set("ora_obd2_engine_rpm", 0)
			.set("ora_obd2_engine_coolant_temperature", 50)
			.set("ora_obd2_number_of_dtcs", 0)
			.finish();
			
			System.out.println("Message sent to IoT...");
		} catch (Exception e)
		{
			// continue
			e.printStackTrace();
		}
	}

	/**
	 * init metadata Map
	 */
	public void initMetadataMap()
	{
		// create meta-data with the indirectly-connected device's
		// manufacturer, model, and serial number

		metaData.put(oracle.iot.client.device.GatewayDevice.MANUFACTURER, MANUFACTURER);
		metaData.put(oracle.iot.client.device.GatewayDevice.MODEL_NUMBER, DEVICE_MODEL_NUMBER);
		metaData.put(oracle.iot.client.device.GatewayDevice.SERIAL_NUMBER, DEVICE_SERIAL);
	}
	
	private void printData(ParsingDati pdd)
	{
		System.out.println("PROGR: " + pdd.getSequenzaInformazione());
		System.out.println("Id: " + pdd.getidChiamante());
		System.out.println("Lat: " + pdd.getLatitudine());
		System.out.println("Lon: " + pdd.getLongitudine());
		System.out.println("Speed: " + pdd.getVelocita());
		System.out.println("Km: " + pdd.getChilometraggio());
	}
}
