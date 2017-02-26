package woohoo.framework.events;

import woohoo.framework.QuestManager;
import woohoo.framework.quests.Quest.QuestState;

public class QuestEvent implements Event
{
	int questID;
	QuestState state;
	QuestManager manager;
	
	public final String START = "start";
	public final String END = "end";
	public final String DISCOVER = "discover";
	
	public QuestEvent(int id, String st, QuestManager qm)
	{
		questID = id;
		manager = qm;
		
		switch(st)
		{
			case START:
				state = QuestState.Current;			
				break;
			case END:
				state = QuestState.Completed;
				break;
			case DISCOVER:
				state = QuestState.Incomplete;
				break;
		}
	}
	
	@Override
	public void activate()
	{
		manager.getQuest(questID).setState(state);
	}
}
