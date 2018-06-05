package oracle.avmcg;

public class OBD2Message extends Message
{
	String lat, lng, data, ora, tipoMsg, prot, vett, dataGps, oraGps, dir, vel, km, seqInfo;
	
	String rpm, coolantTemp, maf, runtime;
	
	public Double getLat()
	{
		return Double.parseDouble(lat);
	}

	public void setLat(String lat)
	{
		this.lat = lat;
	}

	public Double getLng()
	{
		return Double.parseDouble(lng);
	}

	public void setLng(String lng)
	{
		this.lng = lng;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getOra()
	{
		return ora;
	}

	public void setOra(String ora)
	{
		this.ora = ora;
	}

	public String getTipoMsg()
	{
		return tipoMsg;
	}

	public void setTipoMsg(String tipoMsg)
	{
		this.tipoMsg = tipoMsg;
	}

	public String getProt()
	{
		return prot;
	}

	public void setProt(String prot)
	{
		this.prot = prot;
	}

	public String getVett()
	{
		return vett;
	}

	public void setVett(String vett)
	{
		this.vett = vett;
	}

	public String getDataGps()
	{
		return dataGps;
	}

	public void setDataGps(String dataGps)
	{
		this.dataGps = dataGps;
	}

	public String getOraGps()
	{
		return oraGps;
	}

	public void setOraGps(String oraGps)
	{
		this.oraGps = oraGps;
	}

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
	}

	public int getVel()
	{
		return Integer.parseInt(vel);
	}

	public void setVel(String vel)
	{
		this.vel = vel;
	}

	public long getKm()
	{
		return Long.parseLong(km);
	}

	public void setKm(String km)
	{
		this.km = km;
	}

	public String getSeqInfo()
	{
		return seqInfo;
	}

	public void setSeqInfo(String seqInfo)
	{
		this.seqInfo = seqInfo;
	}
	
	public float getRpm()
	{
		if (rpm == null)
			rpm = "0";
		
		return Float.parseFloat(rpm);
	}

	public void setRpm(String rpm)
	{
		this.rpm = rpm;
	}
	
	public int getCoolantTemp()
	{
		if (coolantTemp == null)
			coolantTemp = "0";
		
		return Integer.parseInt(coolantTemp);
	}

	public void setCoolantTemp(String coolantTemp)
	{
		this.coolantTemp = coolantTemp;
	}

	public double getMaf()
	{
		if (maf == null)
			maf = "0";
		
		return (Math.round(Double.parseDouble(maf)*100.0)/100.0);
	}

	public void setMaf(String maf)
	{
		this.maf = maf;
	}

	public int getRuntime()
	{
		if (runtime == null)
			runtime = "0";
		
		return Integer.parseInt(runtime);
	}

	public void setRuntime(String runtime)
	{
		if (runtime == null)
			runtime = "0";
		
		this.runtime = runtime;
	}
	
}
