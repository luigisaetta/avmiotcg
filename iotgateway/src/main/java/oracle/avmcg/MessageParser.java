package oracle.avmcg;

public interface MessageParser
{
	public OBD2Message process(String mess);
}
