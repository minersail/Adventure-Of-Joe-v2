package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.DialogueComponent;

public class NPC extends Character
{    
    private DialogueComponent dialogue;
    
    public NPC(Texture texture, World world)
    {
		super(new TextureRegion(texture), world);
		
        dialogue = new DialogueComponent(0);
        super.add(dialogue);
		
		collision.setPosition(8, 8);
	}    
	
	@Override
	public void update(float delta)
	{
		collision.update(delta);
		mapObject.update(delta, new Vector2(collision.getPosition().x - 0.5f, collision.getPosition().y - 0.5f));
	}
}
