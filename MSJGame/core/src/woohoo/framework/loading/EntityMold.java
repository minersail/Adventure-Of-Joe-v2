package woohoo.framework.loading;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Class that describes the XML necessary to load an entity
 * @author jordan
 */
public class EntityMold
{
	private Array<Element> loadData;
	private String name;
	private boolean enabled;
	
	public EntityMold(Element entityData)
	{
		name = entityData.get("name");
		enabled = entityData.getBoolean("enabled");
		loadData = new Array<>();
		
		for (int i = 0; i < entityData.getChildCount(); i++)
		{  
			loadData.add(entityData.getChild(i));
		}
	}
	
	public Array<Element> getData()
	{
		return loadData;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}
}
