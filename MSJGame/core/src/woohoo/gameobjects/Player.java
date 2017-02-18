package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.AIComponent.AIMode;
import woohoo.gameobjects.components.CollisionComponent.Movement;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Player extends Character
{		  	
    private WeaponComponent weapon;
	    
    public Player(TextureAtlas atlas)
    {
		super(atlas, WBodyType.Player);
		
		brain.setAIMode(AIMode.Input);
	}
	
    public Player(Texture region)
    {
		super(new TextureRegion(region), WBodyType.Player);
		
		brain.setAIMode(AIMode.Input);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		mapObject.setIdle(collision.isStopped());
				
		if (weapon != null)
		{
			weapon.update(delta);
			weapon.setAngle(mapObject.getDirection());    
			mapObject.setFighting(weapon.isActive());
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
	
	public void unequip()
	{
		weapon.removeMass();
		weapon = null;
	}
	
	public void attack()
	{
		if (weapon != null)
			weapon.swing();	
	}
	
	public void setMovement(Movement movement)
	{
		collision.setMovement(movement);
	}
	
	public Movement getMovement()
	{
		return collision.getMovement();
	}
}
