package home.genealogy.indexes;

import java.util.HashMap;
import java.util.Iterator;

public class TaggedContainerDescriptor
{
	private ContainerDescriptor m_containerDescriptor;
	
	// Key: The tag name
	// Value: The quality of the tag
	private HashMap<String, String> m_hmTags;
    
    public TaggedContainerDescriptor(long lContainerId, long lSubContainerId)
    {
    	m_containerDescriptor = new ContainerDescriptor(lContainerId, lSubContainerId);
    	m_hmTags = new HashMap<String, String>();
    }
    
    public void add(String strTagType, String strQuality)
    {
    	String strExistingQuality = m_hmTags.get(strTagType);
    	if (null == strExistingQuality)
    	{
    		m_hmTags.put(strTagType, strQuality);
    	}
    }
    
    public long getContainerId()
    {
    	return m_containerDescriptor.getContainerId();
    }
    
    public long getSubContainerId()
    {
    	return m_containerDescriptor.getSubContainerId();
    }
    
    public String getQuality(String strTagType)
    {
    	return m_hmTags.get(strTagType);
    }
    
    public String getConcatenatedTagNames()
    {
    	StringBuffer sb = new StringBuffer();
    	Iterator<String> iter = m_hmTags.keySet().iterator();
    	while (iter.hasNext())
    	{
    		String strTagName = iter.next();
    		if (0 != sb.length())
    		{
    			sb.append(", ");
    		}
    		sb.append(strTagName);
    	}
    	return sb.toString();
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer(256);
    	sb.append("C:").append(getContainerId()).append(":SC:").append(getSubContainerId());
    	Iterator<String> iter = m_hmTags.keySet().iterator();
    	while (iter.hasNext())
    	{
    		sb.append("\n");
    		String strTag = iter.next();
    		String strQuality = m_hmTags.get(strTag);
    		sb.append("  ").append(strTag).append(" - ").append(strQuality);
    	}
    	return sb.toString();
    }
}
