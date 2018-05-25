package oracle.avmcg;

import oracle.iot.client.device.GatewayDevice;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Hashtable;
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


	// the hashtable containing the list of devices already registered
	Hashtable<String, VirtualDevice> hDevices = new Hashtable<String, VirtualDevice>();

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
	public void send(ParserDati msg)
	{
		String deviceId = null;
		VirtualDevice virtualDevice = null;
		
		printData(msg);
		
		// Lazy Registration of Device
		if (hDevices.get(msg.getidChiamante()) == null)
		{
			// NOT FOUND in Hastable, register it

			// add any vendor-specific meta-data to the metaData Map
			Map<String, String> metaData = initMetadataMap(msg.getidChiamante());

			try
			{
				// getIdChiamante() as DEVICE_ACTIVATION_ID
				deviceId = gw.registerDevice(msg.getidChiamante(), metaData, OBD2_URN_MSG);
				
				System.out.println("OK after device registration: " + deviceId);

				virtualDevice = gw.createVirtualDevice(deviceId, deviceModel);
				
				// save in Hashtable
				hDevices.put(msg.getidChiamante(), virtualDevice);
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
			// data set to zero are not available
			//
			
			// now virtualDevice should be in hashtable
			virtualDevice = hDevices.get(msg.getidChiamante());
			
			virtualDevice.update().set("ora_latitude", msg.getLatitudine()).set("ora_longitude", msg.getLongitudine())
					.set("ora_altitude", 0).set("ora_obd2_vehicle_speed", msg.getVelocita())
					.set("ora_obd2_engine_rpm", 0).set("ora_obd2_engine_coolant_temperature", 50)
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

	private void printData(ParserDati pdd)
	{
		System.out.println("PROGR: " + pdd.getSequenzaInformazione());
		System.out.println("Id: " + pdd.getidChiamante());
		System.out.println("Lat: " + pdd.getLatitudine());
		System.out.println("Lon: " + pdd.getLongitudine());
		System.out.println("Speed: " + pdd.getVelocita());
		System.out.println("Km: " + pdd.getChilometraggio());
	}
}
