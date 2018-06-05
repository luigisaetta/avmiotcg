package oracle.avmcg;

public class MessageParserFactory
{
	public static MessageParser createProcessor(String msgType)
	{
		MessageParser processor = null;
		
		switch (msgType)
		{
			case "STRING":
				processor = new StringMessageParser();
				break;
			case "AVM":
				processor = new AVMMessageParser();
				break;	
			case "JSON":
				processor = new JSONMessageParser();
				break;
			case "SIGFOX":
				processor = new SigfoxMessageParser();
				break;
		}
		return processor;
	}
}
