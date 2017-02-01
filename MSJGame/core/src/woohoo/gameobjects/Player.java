package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Player extends Character
{		    
    private WeaponComponent weapon;
    
    public Player(TextureAtlas atlas)
    {
		super(atlas, WBodyType.Player, new Vector2(2, 1));
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		mapObject.setIdle(collision.isStopped());
		
		if (!collision.isStopped())
		{
			if (Math.abs(collision.getVelocity().x) > Math.abs(collision.getVelocity().y))
			{
				if (collision.getVelocity().x > 0)
					mapObject.setDirection(Direction.Right);
				else
					mapObject.setDirection(Direction.Left);
			}
			else
			{
				if (collision.getVelocity().y > 0)
					mapObject.setDirection(Direction.Down);
				else
					mapObject.setDirection(Direction.Up);
			}
		}
		
		if (weapon != null)
		{
			weapon.update(delta);
			weapon.setAngle(mapObject.getDirection());       
		}
	}
	
	public void move(Direction dir)
	{
		int speed = 2;
		switch (dir)
		{
			case Up:
				collision.addVelocity(0, -speed);
				break;
			case Down:
				collision.addVelocity(0, speed);
				break;
			case Left:
				collision.addVelocity(-speed, 0);
				break;
			case Right:
				collision.addVelocity(speed, 0);
				break;
		}
	}
    
    public void equip(Item item)
    {
        weapon = new WeaponComponent(item);
		weapon.createMass(collision.getMass().getWorld());
		weapon.setContactData(new SensorContact(weapon, WBodyType.Enemy));
        super.add(weapon);
        
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = collision.getMass();
        jointDef.bodyB = weapon.getMass();
        
        collision.getMass().getWorld().createJoint(jointDef);
			
        weapon.setAngle(mapObject.getDirection());
    }
	
	public void attack()
	{
		if (weapon != null)
			weapon.swing();	
	}
	
	public void stop()
	{
		collision.setVelocity(0, 0);
	}
    
    public void setPosition(float x, float y)
    {
        collision.setPosition(x, y);
    }
}
