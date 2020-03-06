package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.schema.all.ParentRelationshipType;
import home.genealogy.schema.all.Parents;
import home.genealogy.util.StringUtil;

public class ParentsHelper
{
	public static Parents getPreferredParents(List<Parents> lParents)
	{
		if ((null != lParents) && (!lParents.isEmpty()))
		{
			for (Parents candidate : lParents)
			{	// Look at all instances to see if there is
				// an explicitly marked preferred parents.
				Boolean bPreferred = candidate.isPreferred();
				if ((null != bPreferred) &&
					(bPreferred.booleanValue()))
				{	// Return explicitly preferred instance
					return candidate;
				}
			}
			// Zero instances were marked as preferred, that
			// means all are preferred, so return first (and
			// hopefully the only)
			return lParents.get(0);
		}
		return null;
	}
	
	public static Parents getBloodParents(List<Parents> lParents)
	{
		if ((null != lParents) && (!lParents.isEmpty()))
		{
			Parents parent = getParents(lParents,
										ParentRelationshipType.MOTHER_BLOOD,
										ParentRelationshipType.FATHER_BLOOD);
			if (null != parent)
			{	// Explicitly designated blood parents found
				return parent;
			}
			// Zero instances were explicitly marked as blood.
			if (1 == lParents.size())
			{	// There is only one instance...
				if (lParents.get(0).getParentRelationship().isEmpty())
				{	// ...AND that was an instance that had zero
					// relationship designations. So default is blood.
					return lParents.get(0);
				}
			}
			// All instances have a "non-blood" explicit
			// designation, so no blood parents specified.
		}
		return null;
	}

	public static Parents getParents(List<Parents> lParents,
									ParentRelationshipType relationshipMother,
									ParentRelationshipType relationshipFather)
	{
		if ((null != lParents) && (!lParents.isEmpty()))
		{
			for (Parents candidate : lParents)
			{	// Look at all instances to see if there is
				// an explicitly marked preferred parents.
				boolean bMotherOk = true;
				boolean bFatherOk = true;
				List<ParentRelationshipType> lRelationships = candidate.getParentRelationship();
				if (null != relationshipMother)
				{
					if (!lRelationships.contains(relationshipMother))
					{
						bMotherOk = false;
					}
				}
				if (null != relationshipFather)
				{
					if (!lRelationships.contains(relationshipFather))
					{
						bFatherOk = false;
					}
				}
				if (bMotherOk && bFatherOk)
				{
					return candidate;
				}
			}
		}
		return null;
	}

	public static Parents getPreferredBloodThenAnyParents(List<Parents> lParents)
	{
		Parents parents = getPreferredParents(lParents);
		if (null == parents)
		{
			parents = getBloodParents(lParents);
			if (null == parents)
			{
				parents = getParents(lParents,
									(ParentRelationshipType)null,
									(ParentRelationshipType)null);
			}
		}
		return parents;
	}
	
	public static boolean isFatherBlood(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.FATHER_BLOOD);
	}
	
	public static boolean isMotherBlood(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.MOTHER_BLOOD);
	}
	
	public static boolean isFatherDeFacto(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.FATHER_DE_FACTO);
	}
	
	public static boolean isMotherDeFacto(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.MOTHER_DE_FACTO);
	}
	
	public static boolean isFatherAdopted(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.FATHER_ADOPTED);
	}
	
	public static boolean isMotherAdopted(Parents parents)
	{
		return isRelationship(parents, ParentRelationshipType.MOTHER_ADOPTED);
	}
	
	public static boolean isRelationship(Parents parents, ParentRelationshipType relationship)
	{
		if (null != parents)
		{
			List<ParentRelationshipType> lRelationships = parents.getParentRelationship();
			if (!lRelationships.isEmpty())
			{
				return lRelationships.contains(relationship);
			}
		}
		// There is no explicit designation, so if the queried
		// relationship is the default (blood) then return true,
		// else false.
		return ((relationship == ParentRelationshipType.FATHER_BLOOD) ||
				(relationship == ParentRelationshipType.MOTHER_BLOOD));
	}
	
	public static String getConcatenatedRelationshipNames(Parents parents)
	{
		StringBuilder sb = new StringBuilder();
		List<ParentRelationshipType> lRelationships = parents.getParentRelationship();
		for (ParentRelationshipType relationship : lRelationships)
		{
			StringUtil.commaTerminateExisting(sb);
			sb.append(relationship.value());
		}
		return sb.toString();
	}
}
