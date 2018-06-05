package oracle.avmcg;

import java.util.StringTokenizer;

public class AVMMessageParser implements MessageParser
{

	@Override
	public OBD2Message process(String val)
	{
		// CODE provided by F. Marini (2018)
		OBD2Message outMess = new OBD2Message();
		
		StringTokenizer stk = new StringTokenizer(val, "^");
		outMess.setDeviceId(stk.nextToken());
		val = stk.nextToken();
		
		stk = new StringTokenizer(val, "+");
		val = stk.nextToken();
		
		outMess.setData(val.substring(0, 6));
		outMess.setOra(val.substring(6, 12));
		outMess.setTipoMsg(val.substring(12, 13));
		outMess.setProt(val.substring(13, 14));
		outMess.setVett(val.substring(14, 15));
		outMess.setDataGps(val.substring(15, 21));
		outMess.setOraGps(val.substring(21, 27));
		outMess.setLat(stk.nextToken());
		val = stk.nextToken();
		outMess.setLng(val.substring(0, 10));
		outMess.setDir(val.substring(10, 13));
		outMess.setVel(val.substring(13, 16));
		outMess.setKm(val.substring(16, 22));
		outMess.setSeqInfo(val.substring(22, 26));
		
		return outMess;
	}
}
