package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class NPC extends Character
{    
    private DialogueComponent dialogue;
    
    public NPC(Texture texture, World world)
    {
		super(new TextureRegion(texture), world, WBodyType.NPC);
		
        dialogue = new DialogueComponent(0);
        super.add(dialogue);
		
		collision.setPosition(5, 5);
	}    
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
}
