package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.events.AIEvent;
import woohoo.framework.events.CutsceneEvent;
import woohoo.framework.events.CutsceneTrigger;
import woohoo.framework.events.Event;
import woohoo.framework.events.EventListener;
import woohoo.framework.events.EventTrigger;
import woohoo.framework.events.MoveTrigger;
import woohoo.framework.events.QuestEvent;
import woohoo.gameobjects.Character;
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
			Array<Element> eventEls = eventListener.getChildrenByName("event");
			
			// Create event trigger
			switch (triggerEl.get("type").toLowerCase())
			{
				case "move":
					trigger = new MoveTrigger(new Vector2(triggerEl.getFloat("locX"), triggerEl.getFloat("locY")), triggerEl.getFloat("dist"));
					break;
				case "cutscene":
					trigger = new CutsceneTrigger();
					break;
				default:
					trigger = null;
					break;
			}
			
			EventListener EL = new EventListener(trigger);
			
			// Add events to the event trigger
			for (Element eventEl : eventEls)
			{
				switch (eventEl.get("type").toLowerCase())
				{
					case "cutscene":
						event = new CutsceneEvent(screen.getCutsceneManager(), eventEl.getInt("id"));
						break;
					case "ai":
						event = new AIEvent((Character)screen.getEngine().getEntity(eventEl.get("entity")), eventEl.get("mode"), 
											eventEl.get("targetChar", null) == null ? null : (Character)screen.getEngine().getEntity(eventEl.get("targetChar")),
											eventEl.getInt("targetX", 0), eventEl.getInt("targetY", 0));
						break;
					case "quest":
						event = new QuestEvent(eventEl.getInt("id"), eventEl.get("action"), screen.getQuestManager());
						break;
					default:
						event = null;
						break;
				}
				
				EL.addEvent(event);
			}
			
			
			
			if (eventListener.get("owner").equals("entity"))
			{
				screen.getEngine().getEntity(eventListener.get("entity")).getListeners().addListener(EL);
			}
			else if (eventListener.get("owner").equals("system"))
			{
				switch (eventListener.get("system"))
				{
					case "cutscene":
						screen.getCutsceneManager().getListeners().addListener(EL);
						break;
				}					
			}
		}
	}
}
