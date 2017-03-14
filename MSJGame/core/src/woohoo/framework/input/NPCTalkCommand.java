package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.DialogueManager;
import woohoo.gameworld.Mappers;

public class NPCTalkCommand implements InputCommand
{
	private DialogueManager manager;
	
	@Override
	public void execute(Entity entity)
	{
		manager.startDialogue(Mappers.dialogues.get(entity));
	}
}
