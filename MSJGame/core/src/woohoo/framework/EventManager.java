package woohoo.framework;

import com.badlogic.gdx.math.Vector2;
import woohoo.framework.events.EventListener;
import woohoo.framework.events.MoveEvent;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen;

public class EventManager
{
	PlayingScreen screen;
	DialogueComponent globalDialogue; // Contains all the dialogue for event-driven dialogue
	
	public EventManager(PlayingScreen scr)
	{
		screen = scr;
		globalDialogue = new DialogueComponent(0);
	}
	
	public void createEvents()
	{
		screen.getEngine().getPlayer().addListener(new EventListener(new MoveEvent(new Vector2(8.5f, 8.5f), 1, 0, this) 
		{
			@Override
			public void activate()
			{
				screen.getDialogueManager().startDialogue(globalDialogue);
			}		
		}));
	}
	
	public int getCurrentGameArea()
	{
		return screen.currentArea;
	}
}
