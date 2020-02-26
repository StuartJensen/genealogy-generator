package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Column;
import home.genealogy.schema.all.Row;

import java.util.List;

public class RowHelper
{
	public static int getColumnCount(Row row)
	{
		if (null != row)
		{
			List<Column> lColumns = row.getColumn();
			if (null != lColumns)
			{
				return lColumns.size();
			}
		}
		return 0;
	}
	
    public static Column getColumn(Row row, int iNth)
    {
		if (null != row)
		{
			List<Column> lColumns = row.getColumn();
			if (null != lColumns)
			{			
				return lColumns.get(iNth);
			}
		}
		return null;
    }

}
