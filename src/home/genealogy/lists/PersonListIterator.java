package home.genealogy.lists;

import home.genealogy.schema.all.Person;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PersonListIterator implements Iterator<Person>
{
	private Person[] m_arPersonList;
	private int m_iCursor;
	
	PersonListIterator(Person[] arPersonList)
	{
		m_arPersonList = arPersonList;
		m_iCursor = 0;
		advance();
	}
	
	public boolean hasNext()
	{
		if (m_iCursor < m_arPersonList.length)
		{	// Cursor still within range of list. By virtue of how the next()
			// function is implemented, this means there is a next element.
			return true;
		}
		return false;
	}
	
	public Person next()
	{
		if (m_iCursor >= m_arPersonList.length)
		{	// Cursor out of range of list
			throw new NoSuchElementException();
		}
		Person person = m_arPersonList[m_iCursor];
		// Find the next valid (non-null) entry and set the cursor to point there
		m_iCursor++;
		advance();
		return person;
	}
	
	public void remove()
	{
		throw new UnsupportedOperationException("Functionality not supported!");
	}
	
	private void advance()
	{
		while (m_iCursor < m_arPersonList.length)
		{
			if (null != m_arPersonList[m_iCursor])
			{	// The cursor now points at the next entry
				break;
			}
			m_iCursor++;
		}
	}
}
