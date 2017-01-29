package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class NPC extends Character
{    
    private DialogueComponent dialogue;
    
    public NPC(Texture texture)
    {
		super(new TextureRegion(texture), WBodyType.NPC, new Vector2(5, 5));
		
        dialogue = new DialogueComponent(0);
        super.add(dialogue);
	}    
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
}
