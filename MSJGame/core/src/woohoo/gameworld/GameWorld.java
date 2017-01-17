package woohoo.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.NPC;
import woohoo.gameobjects.Player;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameWorld extends Engine
{
	private PlayingScreen screen;
    
    public float runtime;
	
	public GameWorld(PlayingScreen scr, World physics)
	{
		screen = scr;
	}
    
	@Override
    public void update(float delta)
    {
		runtime += delta;

		for (Entity entity : getEntities())
		{
			((BaseEntity)entity).update(delta);
		}
    }
    
    public ArrayList<NPC> getNPCs()
    {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        for (Entity entity : getEntities())
        {
            if (entity instanceof NPC)
                npcs.add((NPC)entity);
        }
        return npcs;
    }
	
	/*
	Check to see if the player is facing an NPC
	*/
	public boolean checkDialogue(Player player)
	{
		for (NPC npc : getNPCs())
        {
            if (player.isFacing(npc))
            {                
                screen.getDialogueManager().startDialogue(npc.getComponent(DialogueComponent.class));
				return true;
            }
        }
		
		return false;
	}
}
