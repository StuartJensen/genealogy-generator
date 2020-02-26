package home.genealogy.output;

public interface IOutputStream
{
	public void initialize()
		throws Exception;
	
	public void output(String str);
	
	public void deinitialize()
		throws Exception;
}
