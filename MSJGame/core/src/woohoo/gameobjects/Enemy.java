package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Enemy extends Character
{    
	SensorComponent hitBox;
	
    public Enemy(Texture texture)
    {
		super(new TextureRegion(texture), WBodyType.Enemy, new Vector2(6, 3));
		
		hitBox = new SensorComponent(WBodyType.Enemy);
		hitBox.setContactData(new SensorContact(hitBox, WBodyType.Weapon));
		
		super.add(hitBox);
	}    
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		hitBox.setPosition(collision.getPosition().x, collision.getPosition().y);
		
		if (hitBox.hasContact())
		{
			collision.addImpulse(500, 500);
		}
	}
}
