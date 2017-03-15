package woohoo.framework.quests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;

public class Quest
{
	public enum QuestState
	{
		Unknown, Discovered, Current, Completed
	}
	
	protected QuestState questState = QuestState.Unknown;
	protected Entity indicator;
	protected int id;
	protected String description;
	
	public Quest(int ID, String desc, Texture indicatorTexture)
	{
		id = ID;
		description = desc;
		
		indicator = new Entity();
		MapObjectComponent mapObject = new MapObjectComponent(new TextureRegion(indicatorTexture));
		PositionComponent position = new PositionComponent();
		indicator.add(mapObject);
		indicator.add(position);
	}
			
	public void setState(QuestState state)
	{
		questState = state;
	}
	
	public QuestState getState()
	{
		return questState;
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Entity getIndicator()
	{
		return indicator;
	}
}