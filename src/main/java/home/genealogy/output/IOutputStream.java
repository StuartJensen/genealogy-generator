package home.genealogy.output;

import java.io.IOException;

public interface IOutputStream
{
	public void initialize()
		throws IOException;
	
	public void output(String str);
	
	public void deinitialize()
		throws IOException;
}
