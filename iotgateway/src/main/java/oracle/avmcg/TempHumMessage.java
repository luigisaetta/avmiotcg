package oracle.avmcg;

public class TempHumMessage extends Message
{
	private String temp;
	private String hum;
	private String lum;
	
	public double getTemp()
	{
		return (Math.round(Double.parseDouble(temp)*100.0)/100.0);
	}
	public void setTemp(String temp)
	{
		this.temp = temp;
	}
	public float getHum()
	{
		return Float.parseFloat(hum);
	}
	public void setHum(String hum)
	{
		this.hum = hum;
	}
	public float getLum()
	{
		return Float.parseFloat(lum);
	}
	public void setLum(String lum)
	{
		this.lum = lum;
	}
}
