package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import woohoo.framework.DialogueManager;
import woohoo.gameworld.components.DialogueComponent;
import woohoo.gameworld.components.PositionComponent;
import woohoo.gameworld.GameEngine;
import woohoo.gameworld.RenderSystem;

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
			PositionComponent playerPos = player.getComponent(PositionComponent.class);
			PositionComponent npcPos = npc.getComponent(PositionComponent.class);
			
			if (engine.getSystem(RenderSystem.class).isFacing(player, npc) && playerPos.position.dst(npcPos.position) < 1.5f)
			{
				manager.startDialogue(npc);
			}
		}
	}
}