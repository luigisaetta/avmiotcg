package oracle.avmcg;

import java.util.StringTokenizer;

public class ParsingDati
{

	String idChiamante, lat, lng, data, ora, tipoMsg, prot, vett, dataGps, oraGps, dir, vel, km, seqInfo;

	public void parse(String val)
	{
		StringTokenizer stk = new StringTokenizer(val, "^");
		idChiamante = stk.nextToken();
		val = stk.nextToken();
		// check with F.M.: I receive ' ' and not '+' Don't know why
		stk = new StringTokenizer(val, "+");
		val = stk.nextToken();
		data = val.substring(0, 6);
		ora = val.substring(6, 12);
		tipoMsg = val.substring(12, 13);
		prot = val.substring(13, 14);
		vett = val.substring(14, 15);
		dataGps = val.substring(15, 21);
		oraGps = val.substring(21, 27);
		lat = stk.nextToken();
		val = stk.nextToken();
		lng = val.substring(0, 10);
		dir = val.substring(10, 13);
		vel = val.substring(13, 16);
		km = val.substring(16, 22);
		seqInfo = val.substring(22, 26);
	}// end parse

	public String getidChiamante()
	{
		return idChiamante;
	}

	public Double getLatitudine()
	{
		return Double.parseDouble(lat);
	}

	public Double getLongitudine()
	{
		return Double.parseDouble(lng);
	}

	public String getDataInvioChiamata()
	{
		return data;
	}

	public String getOraInvioChiamata()
	{
		return ora;
	}

	public String getTipoMessaggio()
	{
		return tipoMsg;
	}

	public String getProtocollo()
	{
		return prot;
	}

	public String getVettore()
	{
		return vett;
	}

	public String getDataUltimaPosGps()
	{
		return dataGps;
	}

	public String getOraUltimaPosGps()
	{
		return oraGps;
	}

	public String getDirezione()
	{
		return dir;
	}

	public int getVelocita()
	{
		return Integer.parseInt(vel);
	}

	public long getChilometraggio()
	{
		return Long.parseLong(km);
	}

	public String getSequenzaInformazione()
	{
		return seqInfo;
	}

	public static void main(String args[])
	{
		ParsingDati pd = new ParsingDati();
		pd.parse("RM_001_AB^051102121212THG051102120000+123.456789+012.3456780129991234560001~@");

		System.out.println(pd.getidChiamante());
		System.out.println(pd.getLatitudine());
		System.out.println(pd.getLongitudine());
		System.out.println(pd.getDataInvioChiamata());
		System.out.println(pd.getOraInvioChiamata());
		System.out.println(pd.getTipoMessaggio());
		System.out.println(pd.getProtocollo());
		System.out.println(pd.getVettore());
		System.out.println(pd.getDataUltimaPosGps());
		System.out.println(pd.getOraUltimaPosGps());
		System.out.println(pd.getDirezione());
		System.out.println(pd.getVelocita());
		System.out.println(pd.getChilometraggio());
		System.out.println(pd.getSequenzaInformazione());
	}// end main

}// end claas
