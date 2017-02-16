package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class NPC extends Character
{    
    private DialogueComponent dialogue;
    
    public NPC(Texture texture, int ID)
    {
		super(new TextureRegion(texture), WBodyType.NPC);
		
        dialogue = new DialogueComponent(ID, false);
        super.add(dialogue);
        
        healthBar.setInvulnerable(true);
	}  
	
	public NPC(TextureAtlas atlas, int ID)
    {
		super(atlas, WBodyType.NPC);
		
        dialogue = new DialogueComponent(ID, false);
        super.add(dialogue);
        
        healthBar.setInvulnerable(true);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
}
