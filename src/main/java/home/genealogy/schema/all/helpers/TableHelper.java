package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import home.genealogy.schema.all.Body;
import home.genealogy.schema.all.Column;
import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Row;
import home.genealogy.schema.all.Table;
import home.genealogy.schema.all.Tag;

public class TableHelper
{
/*	
	public static int getRowCount(Table table)
	{
		if (null != table)
		{
			String strRows = table.getRows();
			if (null != strRows)
			{
				try
				{
					return Integer.parseInt(strRows);
				}
				catch (NumberFormatException nfe)
				{
					// Return zero rows
				}
			}
		}
		return 0;
	}
	
	public static int getColumnCount(Table table)
	{
		if (null != table)
		{
			String strColumns = table.getCols();
			if (null != strColumns)
			{
				try
				{
					return Integer.parseInt(strColumns);
				}
				catch (NumberFormatException nfe)
				{
					// Return zero columns
				}
			}
		}
		return 0;
	}
*/	
	public static int getRowCount(Table table)
	{
		if (null != table)
		{
			List<Row> lRows = table.getRow();
			if (null != lRows)
			{
				return lRows.size();
			}
		}
		return 0;
	}
	
    public static Row getRow(Table table, int iNth)
    {
		if (null != table)
		{
			List<Row> lRows = table.getRow();
			if (null != lRows)
			{			
				return lRows.get(iNth);
			}
		}
		return null;
    }
    
    public static boolean isBorderSingleLine(Table table)
    {
		if (null != table)
		{
			String strBorder = table.getBorder();
			if (null != strBorder)
			{
				if (strBorder.equals("SingleLine"))
				{
					return true;
				}
			}
		}
		return false;
    }
    
	public static List<Tag> getTagList(Table table, List<Tag> lTags)
	{
		if (null == lTags)
		{
			lTags = new ArrayList<Tag>();
		}
		if (null != table)
		{
			int iRowCount = getRowCount(table);
			for (int i=0; i<iRowCount; i++)
			{
				Row row = getRow(table, i);
				int iColumnCount = RowHelper.getColumnCount(row);
				for (int c=0; c<iColumnCount; c++)
				{
					Column column = RowHelper.getColumn(row, c);
					int iParagraphCount = ColumnHelper.getParagraphCount(column);
					for (int p=0; p<iParagraphCount; p++)
					{
						Paragraph paragraph = ColumnHelper.getParagraph(column, p);
						ParagraphHelper.getTagList(paragraph, lTags);
					}
				}
			}
		}
		return lTags;
	}
}
