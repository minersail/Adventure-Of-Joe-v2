package woohoo.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.gameobjects.components.MovementComponent;

public class Player extends BaseEntity
{	
	private MapObjectComponent mapObject;
	private MovementComponent movement;
	    
    public Player(TextureAtlas atlas, int sizeX, int sizeY)
    {
		mapObject = new MapObjectComponent(atlas, sizeX, sizeY);
		movement = new MovementComponent(1, 1);
		
		super.add(mapObject);
		super.add(movement);
	}
	
	@Override
	public void update(float delta)
	{
		float posX = mapObject.getX();
		float posY = mapObject.getY();
		Direction dir = Direction.None;
		
		if (Gdx.input.isKeyPressed(Keys.UP))
		{
			mapObject.setY(posY - movement.getSpeed().y * delta);
			dir = Direction.Up;
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
		{
			mapObject.setY(posY + movement.getSpeed().y * delta);
			dir = Direction.Down;
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT))
		{
			mapObject.setX(posX - movement.getSpeed().x * delta);			
			dir = Direction.Left;
		}
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			mapObject.setX(posX + movement.getSpeed().x * delta);			
			dir = Direction.Right;
		}
		
		mapObject.update(delta, (dir == Direction.None ? mapObject.getDirection() : dir));
	}
}
