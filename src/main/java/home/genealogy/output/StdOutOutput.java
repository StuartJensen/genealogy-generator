package home.genealogy.output;

public class StdOutOutput implements IOutputStream
{
	public void initialize()
		throws Exception
	{
		// Nothing to do
	}
		
	public void output(String str)
	{
		System.out.print(str);
	}
		
	public void deinitialize()
		throws Exception
	{
		// Nothing to do
	}
}
