package home.genealogy.lists;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import home.genealogy.output.IOutputStream;
import home.genealogy.util.StringUtil;

public class ListsValidationEventHandler
	implements ValidationEventHandler
{
	private IOutputStream m_outputStream;
	
	public ListsValidationEventHandler(IOutputStream outputStream)
	{
		m_outputStream = outputStream;
	}
	
	public boolean handleEvent(ValidationEvent event)
	{
		StringBuilder sb = new StringBuilder();
		
		switch(event.getSeverity())
		{
			case ValidationEvent.FATAL_ERROR:
				sb.append("Severity: Fatal Error\n");
				break;
			case ValidationEvent.ERROR:
				sb.append("Severity: Error\n");
				break;
			case ValidationEvent.WARNING:
				sb.append("Severity: Warning\n");
				break;
		}
		if (StringUtil.exists(event.getMessage()))
		{
			sb.append(event.getMessage()).append("\n");
		}
		if (null != event.getLinkedException())
		{
			sb.append("Exception Class: ").append(event.getLinkedException().getClass().getName()).append("\n");
			sb.append("Exception Message: ").append(event.getLinkedException().getMessage()).append("\n");
		}
		ValidationEventLocator locator = event.getLocator();
		if (null != locator)
		{
			sb.append("Line: ").append(locator.getLineNumber()).append("\n");
			sb.append("Column: ").append(locator.getColumnNumber()).append("\n");
			sb.append("Offset: ").append(locator.getOffset()).append("\n");
		}
		m_outputStream.output(sb.toString());
		return false;
	}
}
