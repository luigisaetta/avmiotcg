package oracle.avmcg;

/*
 * 
 * POJO class for msg to Traccar
 * 
 */
public class TracMessage
{
	private double lon;
	private double lat;
	private int vel;
	private long tst;
	private String _type;
	private String tid;
	
	public TracMessage(double lon, double lat, int vel, long tst, String tid, String type)
	{
		this.lon = lon;
		this.lat = lat;
		this.vel = vel;
		this.tst = tst;
		this.tid = tid;
		this._type = type;
	}
	
	public double getLon()
	{
		return lon;
	}
	public void setLon(double lon)
	{
		this.lon = lon;
	}
	public double getLat()
	{
		return lat;
	}
	public void setLat(double lat)
	{
		this.lat = lat;
	}
	public int getVel()
	{
		return vel;
	}
	public void setVel(int vel)
	{
		this.vel = vel;
	}
	public long getTst()
	{
		return tst;
	}
	public void setTst(long tst)
	{
		this.tst = tst;
	}
	public String get_type()
	{
		return _type;
	}
	public void set_type(String _type)
	{
		this._type = _type;
	}
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
}
