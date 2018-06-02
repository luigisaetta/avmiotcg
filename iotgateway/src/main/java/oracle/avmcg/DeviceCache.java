package oracle.avmcg;

import java.util.Hashtable;
import oracle.iot.client.device.VirtualDevice;

/*
 * 
 * This class wraps an Hashtable that contains
 * virtuaDevices handles
 * 
 * to enable future better implementation
 * for example as a distributed cache (Coherence, Hazelcast)
 * 
 */
public class DeviceCache
{
	// the hashtable containing the list of devices already registered
	private Hashtable<String, VirtualDevice> hDevices = new Hashtable<String, VirtualDevice>();
		
	public DeviceCache()
	{
		
	}
	
	public synchronized void put(String key, VirtualDevice vDev)
	{
		hDevices.put(key, vDev);
	}
	
	public synchronized VirtualDevice get(String key)
	{
		return hDevices.get(key);
	}
}
