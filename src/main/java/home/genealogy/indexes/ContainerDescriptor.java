package home.genealogy.indexes;

public class ContainerDescriptor
{
	private long m_lContainerId;
    private long m_lSubContainerId;
    
    public ContainerDescriptor(long lContainerId, long lSubContainerId)
    {
    	m_lContainerId = lContainerId;
        m_lSubContainerId = lSubContainerId;
    }
    
    public long getContainerId()
    {
    	return m_lContainerId;
    }
    
    public long getSubContainerId()
    {
    	return m_lSubContainerId;
    }
}
