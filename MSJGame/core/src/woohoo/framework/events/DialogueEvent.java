package woohoo.framework.events;

import woohoo.framework.DialogueManager;
import woohoo.gameobjects.components.DialogueComponent;

public class DialogueEvent implements Event
{
	private DialogueManager manager;
	private DialogueComponent dialogue;
	
	public DialogueEvent(DialogueManager dm, DialogueComponent dia)
	{
		manager = dm;
		dialogue = dia;
	}
	
	@Override
	public void activate()
	{
		manager.startDialogue(dialogue);
	}
}
