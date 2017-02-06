package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.events.DialogueEvent;
import woohoo.framework.events.Event;
import woohoo.framework.events.EventListener;
import woohoo.framework.events.EventTrigger;
import woohoo.framework.events.MoveTrigger;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen;

public class EventManager
{
	PlayingScreen screen;
	
	public EventManager(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public void createEvents(int area)
	{
		FileHandle handle = Gdx.files.internal("data/events.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element eventListeners = root.getChild(area);  
        
        for (Element eventListener : eventListeners.getChildrenByName("eventlistener"))
        {		
			EventTrigger trigger;
			Event event;
			
			Element triggerEl = eventListener.getChildByName("trigger");
			Element eventEl = eventListener.getChildByName("event");
			
			if (triggerEl.get("type").equals("move"))
			{
				trigger = new MoveTrigger(new Vector2(triggerEl.getFloat("locX"), triggerEl.getFloat("locY")), triggerEl.getFloat("dist"));
			}
			else
			{
				trigger = null;
			}
			
			if (eventEl.get("type").equals("dialogue"))
			{
				event = new DialogueEvent(screen.getDialogueManager(), new DialogueComponent(eventEl.getInt("id"), true));
			}
			else
			{
				event = null;
			}
			
			screen.getEngine().getPlayer().addListener(new EventListener(trigger, event));
		}
	}
	
	public int getCurrentGameArea()
	{
		return screen.currentArea;
	}
}
