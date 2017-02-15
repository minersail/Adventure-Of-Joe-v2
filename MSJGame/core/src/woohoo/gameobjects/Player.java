package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import woohoo.framework.contactcommands.SensorContact;
import woohoo.gameobjects.components.AIComponent.AIMode;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.screens.PlayingScreen.WBodyType;

public class Player extends Character
{		    
	public enum Movement
	{
		None, Horizontal, Vertical
	}
	
	private Movement movement;
	private Vector2 suppression = new Vector2(0, 0);
    private WeaponComponent weapon;
	    
    public Player(TextureAtlas atlas)
    {
		super(atlas, WBodyType.Player);
		
		brain.setAIMode(AIMode.Input);
		movement = Movement.None;
	}
	
    public Player(Texture region)
    {
		super(new TextureRegion(region), WBodyType.Player);
		
		brain.setAIMode(AIMode.Input);
		movement = Movement.None;
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
	
	public void setMovement(Movement move)
	{
		if (movement == Movement.Horizontal && move == Movement.Vertical)
			collision.addVelocity(0, suppression.y);
		if (movement == Movement.Vertical && move == Movement.Horizontal)
			collision.addVelocity(suppression.x, 0);
		
		movement = move;
	}
	
	public Movement getMovement()
	{
		return movement;
	}
	
	@Override
	public void move(Direction dir)
	{
		super.move(dir);
		
		switch(movement)
		{
			case Vertical:
				suppression.x = collision.getVelocity().x;
				collision.setVelocity(0, collision.getVelocity().y);
				break;
			case Horizontal:
				suppression.y = collision.getVelocity().y;
				collision.setVelocity(collision.getVelocity().x, 0);
				break;
			case None:
				break;
		}
	}
}
