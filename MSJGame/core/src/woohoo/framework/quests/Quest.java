package woohoo.framework.quests;

import com.badlogic.gdx.graphics.Texture;
import woohoo.gameobjects.QuestIndicator;

public class Quest
{
	public enum QuestState
	{
		Unknown, Discovered, Current, Completed
	}
	
	protected QuestState questState = QuestState.Unknown;
	protected QuestIndicator indicator;
	protected int id;
	protected String description;
	
	public Quest(int ID, String desc, Texture indicatorTexture)
	{
		id = ID;
		description = desc;
		indicator = new QuestIndicator(indicatorTexture);
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
	
	public QuestIndicator getIndicator()
	{
		return indicator;
	}
}