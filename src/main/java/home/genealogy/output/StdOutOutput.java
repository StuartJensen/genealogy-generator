package home.genealogy.output;

import java.io.IOException;

public class StdOutOutput implements IOutputStream
{
	public void initialize()
		throws IOException
	{
		// Nothing to do
	}
		
	public void output(String str)
	{
		System.out.print(str);
	}
		
	public void deinitialize()
		throws IOException
	{
		// Nothing to do
	}
}
