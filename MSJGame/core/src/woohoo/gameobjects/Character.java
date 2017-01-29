package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.MapObjectComponent;
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
	protected CollisionComponent collision;
	protected InventoryComponent inventory;
	    
	public Character(TextureAtlas atlas, WBodyType type, Vector2 position)
	{		
		mapObject = new MapObjectComponent(atlas);
		collision = new CollisionComponent(type);
		inventory = new InventoryComponent();
		
		collision.setStartPosition(position.x, position.y);
		
		super.add(mapObject);
        super.add(collision);
		super.add(inventory);
	}
	
	public Character(TextureRegion region, WBodyType type, Vector2 position)
	{		
		mapObject = new MapObjectComponent(region);
		collision = new CollisionComponent(type);
		inventory = new InventoryComponent();
		
		collision.setStartPosition(position.x, position.y);
		
		super.add(mapObject);
        super.add(collision);
		super.add(inventory);
	}
	
	@Override
	public void update(float delta)
	{		
		collision.update(delta);
		mapObject.update(delta, collision.getPosition());
		inventory.update(delta);
	}
    
    public Vector2 getPosition()
    {
        return new Vector2(mapObject.getX(), mapObject.getY());
    }
	
	public Direction getDirection()
	{
		return mapObject.getDirection();
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
	
	public float distanceTo(Character other)
	{
		float dX = getCenter().x - other.getCenter().x;
		float dY = getCenter().y - other.getCenter().y;
		
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
	
	
}
