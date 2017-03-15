package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import woohoo.framework.events.AIFollowEvent;
import woohoo.framework.events.AIMoveToEvent;
import woohoo.framework.events.AttributeXMLEvent;
import woohoo.framework.events.CutsceneEvent;
import woohoo.framework.events.CutsceneTrigger;
import woohoo.framework.events.Event;
import woohoo.framework.events.EventListener;
import woohoo.framework.events.EventTrigger;
import woohoo.framework.events.MoveTrigger;
import woohoo.framework.events.QuestEvent;
import woohoo.gameobjects.components.EventListenerComponent;
import woohoo.screens.PlayingScreen;

public class EventSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public EventSystem(PlayingScreen scr)
	{
		super(Family.all(EventListenerComponent.class).get());
		
		screen = scr;
	}
	
	public void initialize(int area)
	{
		FileHandle handle = Gdx.files.local("data/events.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());       
        XmlReader.Element eventListeners = root.getChild(area);        
        
        for (XmlReader.Element eventListener : eventListeners.getChildrenByName("eventlistener"))
        {		
			if (eventListener.get("state").equals("disabled")) continue;
			
			EventTrigger trigger;
			Event event;
			
			XmlReader.Element triggerEl = eventListener.getChildByName("trigger");
			Array<XmlReader.Element> eventEls = eventListener.getChildrenByName("event");
			
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
			
			EventListener EL = new EventListener(eventListener.get("state"), eventListener.getInt("id"), area, trigger);
			
			// Create events to be activated by the event trigger
			for (final XmlReader.Element eventEl : eventEls)
			{				
				switch (eventEl.get("type").toLowerCase())
				{
					case "cutscene":
						event = new CutsceneEvent(screen.getCutsceneManager(), eventEl.getInt("id"));
						break;
					case "aifollow":
						event = new AIFollowEvent(Mappers.ai.get(screen.getEngine().getEntity(eventEl.get("entity"))), 
												  Mappers.positions.get(screen.getEngine().getEntity(eventEl.get("targetChar"))));
						break;
					case "aimoveto":
						event = new AIMoveToEvent(Mappers.ai.get(screen.getEngine().getEntity(eventEl.get("entity"))), 
												  eventEl.getFloat("targetX"), eventEl.getFloat("targetY"));
						break;
					case "quest":
						event = new QuestEvent(eventEl.getInt("id"), eventEl.get("action"), screen.getQuestManager());
						break;
					case "editxml":
						event = new AttributeXMLEvent(eventEl.get("filename"), eventEl.get("attribute"), eventEl.get("value"), eventEl.getInt("area"), 
													  eventEl.get("elementname"), eventEl.get("selectorname"), eventEl.get("selectorvalue"));
						break;
					default:
						event = null;
						break;
				}
				
				EL.addEvent(event);
			}		
			
			// Combine the event trigger and events and allocate them to the correct place
			if (eventListener.get("owner").equals("entity"))
			{
				Mappers.eventListeners.get(screen.getEngine().getEntity(eventListener.get("entity"))).listeners.addListener(EL);
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

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		Mappers.eventListeners.get(entity).listeners.notifyAll(entity);
	}
}
