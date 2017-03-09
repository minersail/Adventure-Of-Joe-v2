package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.AIComponent.AIMode;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.LOSComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MapObjectComponent.AnimationState;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen.WBodyType;

/*
Not to be confused with CharacterData/CharacterManager

This is a physical/visual representation base class for 
entities that can move and collide.
*/
public class Character extends BaseEntity
{
    protected MapObjectComponent mapObject;
	protected MovementComponent collision;
	protected InventoryComponent inventory;
    protected HealthBarComponent healthBar;
	protected AIComponent brain;
	protected LOSComponent lineOfSight;
	
	protected float speed = 2;
    protected boolean dead;
	    
	public Character(TextureAtlas atlas, WBodyType type)
	{		
		this(type);
		mapObject = new MapObjectComponent(atlas);
		super.add(mapObject);
	}
	
	public Character(TextureRegion region, WBodyType type)
	{
		this(type);
		mapObject = new MapObjectComponent(region);
		super.add(mapObject);
	}
	
	private Character(WBodyType type)
	{
		collision = new MovementComponent(type);
		inventory = new InventoryComponent();
        healthBar = new HealthBarComponent(10);
		brain = new AIComponent();
		lineOfSight = new LOSComponent(type);
		
        super.add(collision);
		super.add(inventory);
        super.add(healthBar);
		super.add(brain);
		super.add(lineOfSight);
	}
	
	@Override
	public void update(float delta)
	{		        
		super.update(delta);
		
		collision.update(delta);
		mapObject.update(delta, collision.getPosition());
		inventory.update(delta);
        healthBar.update(delta, collision.getPosition());
		brain.update(delta, collision.getPosition());
		lineOfSight.update(delta);
		lineOfSight.setPosition(collision.getPosition().x, collision.getPosition().y);
		lineOfSight.rotate(mapObject.getDirection());
		
		collision.setImmovable(brain.getAIMode() == AIMode.Stay);
		
		if (brain.getAIMode() != AIMode.Input)
		{
			stop();
			move(brain.calculateDirection(collision.getPosition()));
		}				
		
		if (collision.isStopped() && mapObject.getAnimationState() == AnimationState.Walking)
        {
            mapObject.setAnimationState(AnimationState.Idle);
        }
		
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
	}
    
    public Vector2 getPosition()
    {
        return new Vector2(mapObject.getX(), mapObject.getY());
    }
	
    public void setPosition(float x, float y)
    {
        collision.setPosition(x, y);
    }
	
	public Direction getDirection()
	{
		return mapObject.getDirection();
	}
    
    public void setDirection(Direction direction)
    {
        mapObject.setDirection(direction);
    }
	
	public Vector2 getCenter()
	{
		return new Vector2(mapObject.getX() + mapObject.getSize().x / 2, mapObject.getY() + mapObject.getSize().y / 2);
	}
	
	public void setSize(float x, float y)
	{
		mapObject.setSize(x, y);
	}
	
	public Vector2 getSize()
	{
		return mapObject.getSize();
	}
	
	public void setSpeed(float newSpeed)
	{
		speed = newSpeed;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void move(Direction dir)
	{
		if (dir == null) return;
		
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
		
		mapObject.setAnimationState(AnimationState.Walking);
	}
	
	public void stop()
	{
		collision.setVelocity(0, 0);
	}
	
	public float distanceTo(Character other)
	{
		float dX = getCenter().x - other.getCenter().x;
		float dY = getCenter().y - other.getCenter().y;
		
		return (float)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}
	
	public float distanceTo(Vector2 position)
	{
		float dX = getCenter().x - position.x;
		float dY = getCenter().y - position.y;
		
		return (float)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}
	
	public boolean isFacing(Character other)
	{
		switch (getDirection())
		{
			case Up:
				if (getCenter().x > other.getPosition().x &&					 // Center must be within left and right bounds of other
					getCenter().x < other.getPosition().x + other.getSize().x && // (Basically a check to see if *this* is below or above other)
					getCenter().y > other.getCenter().y)						 // Center must be below other's center
				{
					return true;
				}
				break;
			case Down:
				if (getCenter().x > other.getPosition().x &&					 // Center must be within left and right bounds of other
					getCenter().x < other.getPosition().x + other.getSize().x && // (Basically a check to see if *this* is below or above other)
					getCenter().y < other.getCenter().y)						 // Center must be above other's center
				{
					return true;
				}				
				break;
			case Left:
				if (getCenter().y > other.getPosition().y &&					 // Center must be within top and bottom bounds of other
					getCenter().y < other.getPosition().y + other.getSize().y && // (Basically a check to see if *this* is to left or right of other)
					getCenter().x > other.getCenter().x)						 // Center must be left of other's center
				{
					return true;
				}				
				break;
			case Right:
				if (getCenter().y > other.getPosition().y &&					 // Center must be within top and bottom bounds of other
					getCenter().y < other.getPosition().y + other.getSize().y && // (Basically a check to see if *this* is to left or right of other)
					getCenter().x < other.getCenter().x)						 // Center must be right of other's center
				{
					return true;
				}						
				break;
		}
		return false;
	}
	
	public void changeMaxHealth(float newMax)
	{
		healthBar.changeMax(newMax);
	}
    
    public void applyDamage(int damage)
    {
        healthBar.damage(damage);
    }
	
	public void setAIMode(AIMode mode)
	{
		brain.setAIMode(mode);
	}
	
	public void setAIMode(String mode)
	{
		switch(mode)
		{
			case "stay":
				brain.setAIMode(AIMode.Stay);
				break;
			case "follow":
				brain.setAIMode(AIMode.Follow);
				break;
			case "random":
				brain.setAIMode(AIMode.Random);
				break;
			case "moveto":
				brain.setAIMode(AIMode.MoveTo);
				break;
			case "sentry":
				brain.setAIMode(AIMode.Sentry);
				break;
		}
	}
	
	public void setMoveTimestep(float time)
	{
		brain.setTimeStep(time);
	}
    
    public void die()
    {
        mapObject.setAnimationState(AnimationState.Death);
        dead = true;
    }
	
	/**
	 * Sets a position for the character to start moving towards
	 * @param position target location
	 */
	public void setTarget(Vector2 position)
	{
		brain.setTargetPosition(collision.getPosition(), position);
		brain.setAIMode(AIMode.MoveTo);
	}
	
	/**
	 * Sets a character for the character to start following
	 * @param character target character
	 */
	public void setTarget(Character character)
	{
		brain.setTargetCharacter(collision.getPosition(), character);
		brain.setAIMode(AIMode.Follow);
	}
}
