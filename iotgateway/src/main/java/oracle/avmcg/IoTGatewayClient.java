package oracle.avmcg;

import oracle.iot.client.device.GatewayDevice;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.VirtualDevice;

public class IoTGatewayClient
{
	private static final String MANUFACTURER = "Saetta Corporation";
	private static final String DEVICE_MODEL_NUMBER = "MN101";
	private static final String OBD2_URN_MSG = "urn:com:oracle:iot:device:obd2";

	private GatewayDevice gw = null;
	private DeviceModel deviceModel = null;


	// the cache containing the list of devices already registered
	DeviceCache hDevices = new DeviceCache();

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

			System.out.println("OK after Gateway activation ...");

			deviceModel = gw.getDeviceModel(OBD2_URN_MSG);

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
	public void send(OBD2Message msg)
	{
		String deviceId = null;
		VirtualDevice virtualDevice = null;
		
		// added check to avoid NPE
		if (msg == null)
			return;
		
		printData(msg);
		
		// Lazy Registration of Device
		if (hDevices.get(msg.getIdChiamante()) == null)
		{
			// NOT FOUND in Hastable, register it

			// add any vendor-specific meta-data to the metaData Map
			Map<String, String> metaData = initMetadataMap(msg.getIdChiamante());

			try
			{
				// getIdChiamante() as DEVICE_ACTIVATION_ID
				deviceId = gw.registerDevice(msg.getIdChiamante(), metaData, OBD2_URN_MSG);
				
				System.out.println("OK after device registration: " + deviceId);

				virtualDevice = gw.createVirtualDevice(deviceId, deviceModel);
				
				// save in Hashtable
				hDevices.put(msg.getIdChiamante(), virtualDevice);
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (GeneralSecurityException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			//
			// data set to zero are not available now
			//
			
			// now virtualDevice should be in hashtable
			virtualDevice = hDevices.get(msg.getIdChiamante());
			
			virtualDevice.update().set("ora_latitude", msg.getLat()).set("ora_longitude", msg.getLng())
					.set("ora_altitude", 0)
					.set("ora_obd2_vehicle_speed", msg.getVel())
					.set("ora_obd2_engine_rpm", msg.getRpm())
					.set("ora_obd2_engine_coolant_temperature", msg.getCoolantTemp())
					.set("ora_obd2_mass_air_flow", msg.getMaf())
					.set("ora_obd2_runtime_since_engine_start", msg.getRuntime())
					.set("ora_obd2_number_of_dtcs", 0).finish();
			
			System.out.println("Msg sent to Iot...");
		} catch (Exception e)
		{
			// continue
			e.printStackTrace();
		}
	}

	/**
	 * init metadata Map
	 */
	private Map<String, String> initMetadataMap(String deviceActivationId)
	{
		// create meta-data with the indirectly-connected device's
		// manufacturer, model, and serial number
		Map<String, String> metaData = new HashMap<String, String>();
		
		metaData.put(oracle.iot.client.device.GatewayDevice.MANUFACTURER, MANUFACTURER);
		metaData.put(oracle.iot.client.device.GatewayDevice.MODEL_NUMBER, DEVICE_MODEL_NUMBER);
		metaData.put(oracle.iot.client.device.GatewayDevice.SERIAL_NUMBER, deviceActivationId);
		
		return metaData;
	}

	private void printData(OBD2Message msg)
	{
		System.out.println("PROGR: " + msg.getSeqInfo());
		System.out.println("Id: " + msg.getIdChiamante());
		System.out.println("Lat: " + msg.getLat());
		System.out.println("Lon: " + msg.getLng());
		System.out.println("Speed: " + msg.getVel());
		System.out.println("Km: " + msg.getKm());
	}
}
