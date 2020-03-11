package home.genealogy.lists.place;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import home.genealogy.output.IOutputStream;
import home.genealogy.util.StringUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PlaceActionScanner
{
	private IOutputStream m_outputStream;
	private List<PlaceAction> m_actions;
	
	public PlaceActionScanner(File fActionFile, IOutputStream outputStream)
		throws IOException
	{
		if (!fActionFile.exists())
		{
			throw new FileNotFoundException("The Place Action File: " + fActionFile.getAbsolutePath() + " does not exist!");
		}
		BufferedReader br = null;
		try
		{
			m_outputStream = outputStream;
			m_actions = new ArrayList<PlaceAction>();
			br = new BufferedReader(new FileReader(fActionFile));
			
			String strLine = br.readLine();
			while (null != strLine)
			{
				if (StringUtil.exists(strLine))
				{
					StringTokenizer st = new StringTokenizer(strLine, ",", false);
					String strAction = st.nextToken();
					if (strAction.equals(PlaceDeleteAction.ACTION))
					{
						if (st.hasMoreTokens())
						{
							m_actions.add(new PlaceDeleteAction(st.nextToken()));
						}
					}
					else if (strAction.equals(PlaceReplaceAction.ACTION))
					{
						if (st.hasMoreTokens())
						{
							String strToBeReplaced = st.nextToken();
							if (st.hasMoreTokens())
							{
								PlaceReplaceAction action = new PlaceReplaceAction(strToBeReplaced, st.nextToken());
								while (st.hasMoreTokens())
								{
									String strType = st.nextToken();
									if ("locale".equalsIgnoreCase(strType))
									{
										if (st.hasMoreTokens())
										{
											action.setLocale(st.nextToken());
										}
									}
									else if ("street".equalsIgnoreCase(strType))
									{
										if (st.hasMoreTokens())
										{
											action.setStreet(st.nextToken());
										}
									}
									else if ("spot".equalsIgnoreCase(strType))
									{
										if (st.hasMoreTokens())
										{
											action.setSpot(st.nextToken());
										}
									}
									else
									{
										outputStream.output("WARNING: Unrecognized token in Place Action Scanner: Replace: " + strType + ": in " + strLine);
									}
								}
								m_actions.add(action);
							}
						}
					}
					else
					{
						m_outputStream.output("WARNING: Ignoring Place Action: " + strLine);
					}
				}
				strLine = br.readLine();
			}
		}
		finally
		{
			if (null != br)
			{
				try{br.close();}catch(IOException ioe) {}
			}
		}
	}
	
	public Iterator<PlaceAction> getActions()
	{
		if (null != m_actions)
		{
			return m_actions.iterator();
		}
		return Collections.emptyIterator();
	}
	
	public boolean placeListModified()
	{
		Iterator<PlaceAction> iter = getActions();
		while (iter.hasNext())
		{
			PlaceAction pa = iter.next();
			if (pa.placeListModified())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean personListModified()
	{
		Iterator<PlaceAction> iter = getActions();
		while (iter.hasNext())
		{
			PlaceAction pa = iter.next();
			if (pa.personListModified())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean marriageListModified()
	{
		Iterator<PlaceAction> iter = getActions();
		while (iter.hasNext())
		{
			PlaceAction pa = iter.next();
			if (pa.marriageListModified())
			{
				return true;
			}
		}
		return false;
	}

	public boolean referenceListModified()
	{
		Iterator<PlaceAction> iter = getActions();
		while (iter.hasNext())
		{
			PlaceAction pa = iter.next();
			if (pa.referenceListModified())
			{
				return true;
			}
		}
		return false;
	}

	public boolean photoListModified()
	{
		Iterator<PlaceAction> iter = getActions();
		while (iter.hasNext())
		{
			PlaceAction pa = iter.next();
			if (pa.photoListModified())
			{
				return true;
			}
		}
		return false;
	}
}
