package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import woohoo.framework.DialogueManager;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameworld.AnimationSystem;
import woohoo.gameworld.GameEngine;
import woohoo.gameworld.Mappers;

public class NPCTalkCommand implements InputCommand
{
	private DialogueManager manager;
	private GameEngine engine;
	
	public NPCTalkCommand(DialogueManager dm, GameEngine ge)
	{
		manager = dm;
		engine = ge;
	}
	
	@Override
	public void execute(Entity player)
	{
		for (Entity npc : engine.getEntitiesFor(Family.all(DialogueComponent.class).get()))
		{
			if (engine.getSystem(AnimationSystem.class).isFacing(player, npc))
			{
				manager.startDialogue(Mappers.dialogues.get(npc));
			}
		}
	}
}
