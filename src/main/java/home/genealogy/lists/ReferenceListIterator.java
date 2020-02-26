package home.genealogy.lists;

import home.genealogy.schema.all.Reference;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ReferenceListIterator implements Iterator<Reference>
{
	private Reference[] m_arReferenceList;
	private int m_iCursor;
	
	ReferenceListIterator(Reference[] arReferenceList)
	{
		m_iCursor = 0;
		m_arReferenceList = arReferenceList;
		advance();
	}
	
	public boolean hasNext()
	{
		if (m_iCursor < m_arReferenceList.length)
		{	// Cursor still within range of list. By virtue of how the next()
			// function is implemented, this means there is a next element.
			return true;
		}
		return false;
	}
	
	public Reference next()
	{
		if (m_iCursor >= m_arReferenceList.length)
		{	// Cursor out of range of list
			throw new NoSuchElementException();
		}
		Reference reference = m_arReferenceList[m_iCursor];
		// Find the next valid (non-null) entry and set the cursor to point there
		m_iCursor++;
		advance();
		return reference;
	}
	
	public void remove()
	{
		throw new UnsupportedOperationException("Functionality not supported!");
	}
	
	private void advance()
	{
		while (m_iCursor < m_arReferenceList.length)
		{
			if (null != m_arReferenceList[m_iCursor])
			{	// The cursor now points at the next entry
				break;
			}
			m_iCursor++;
		}
	}
}
