package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.framework.fixturedata.HitData;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.SensorComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Enemy extends Character
{    
	SensorComponent hitBox;
	AIComponent brain;
	
    public Enemy(Texture texture)
    {
		super(new TextureRegion(texture), WBodyType.Enemy, new Vector2(6, 3));
		
		hitBox = new SensorComponent(WBodyType.Enemy);
		brain = new AIComponent();
		
		hitBox.setContactData(new SensorContact(hitBox, WBodyType.Weapon));
		
		super.add(hitBox);
		super.add(brain);
		
		speed = 1f;
	}    
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		stop();
		move(brain.calculateDirection(collision.getPosition()));
		
		hitBox.setPosition(collision.getPosition().x, collision.getPosition().y);
		
		if (hitBox.hasContact())
		{
			switch(((HitData)hitBox.getContact()).getDirection())
			{
				case Up:
					collision.applyImpulse(0, -500);
					break;
				case Down:
					collision.applyImpulse(0, 500);
					break;
				case Left:
					collision.applyImpulse(-500, 0);
					break;
				case Right:
					collision.applyImpulse(500, 0);
					break;
			}
            healthBar.damage(1);
		}
	}
}
