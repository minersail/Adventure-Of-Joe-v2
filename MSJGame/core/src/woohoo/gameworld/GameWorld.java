package woohoo.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import woohoo.framework.DialogueManager;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.NPC;
import woohoo.screens.PlayingScreen;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameWorld extends Engine
{
    public enum GameState
    {
        Playing, Dialogue
    }
    
	private PlayingScreen screen;
    private DialogueManager dialogueManager;
    private GameState state;
    
    public float runtime;
	
	public GameWorld(PlayingScreen scr, World physics)
	{
		screen = scr;
        dialogueManager = new DialogueManager(new TextureRegion(screen.getAsset("images/dialoguebox.png", Texture.class)));
	}
    
	@Override
    public void update(float delta)
    {
        if (state == GameState.Playing)
        {
            runtime += delta;

            for (Entity entity : getEntities())
            {
                ((BaseEntity)entity).update(delta);
            }
        }
        else if (state == GameState.Dialogue)
        {
            dialogueManager.runDialogue();
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
    
    public void setState(GameState s)
    {
        state = s;
        
        if (s == GameState.Dialogue)
        {
            TextureMapObject box = new TextureMapObject();
            box.setTextureRegion(dialogueManager.getBox());
            screen.addObject(box, 3);
        }
    }
    
    public GameState getState()
    {
        return state;
    }
    
    public DialogueManager getManager()
    {
        return dialogueManager;
    }
}
