package woohoo.framework.quests;

public abstract class Quest
{
	public enum QuestState
	{
		Undiscovered, Incomplete, Current, Completed
	}
	
	protected QuestState questState = QuestState.Undiscovered;
	protected int id;
	
	/**
	 * Called every tick
	 */
	public abstract void indicate();
	
	/**
	 * Called after a quest is completed
	 */
	public abstract void reward();
	
	public void setState(QuestState state)
	{
		questState = state;
	}
	
	public QuestState getState()
	{
		return questState;
	}
	
	public void setID(int ID)
	{
		id = ID;
	}
	
	public int getID()
	{
		return id;
	}
}