package home.genealogy.lists;

import home.genealogy.schema.all.Marriage;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MarriageListIterator implements Iterator<Marriage>
{
	private Marriage[] m_arMarriageList;
	private int m_iCursor;
	
	MarriageListIterator(Marriage[] arMarriageList)
	{
		m_iCursor = 0;
		m_arMarriageList = arMarriageList;
		advance();
	}
	
	public boolean hasNext()
	{
		if (m_iCursor < m_arMarriageList.length)
		{	// Cursor still within range of list. By virtue of how the next()
			// function is implemented, this means there is a next element.
			return true;
		}
		return false;
	}
	
	public Marriage next()
	{
		if (m_iCursor >= m_arMarriageList.length)
		{	// Cursor out of range of list
			throw new NoSuchElementException();
		}
		Marriage marriage = m_arMarriageList[m_iCursor];
		// Find the next valid (non-null) entry and set the cursor to point there
		m_iCursor++;
		advance();
		return marriage;
	}
	
	public void remove()
	{
		throw new UnsupportedOperationException("Functionality not supported!");
	}
	
	private void advance()
	{
		while (m_iCursor < m_arMarriageList.length)
		{
			if (null != m_arMarriageList[m_iCursor])
			{	// The cursor now points at the next entry
				break;
			}
			m_iCursor++;
		}
	}
}
