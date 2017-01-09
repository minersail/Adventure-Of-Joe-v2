package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class Player extends BaseEntity
{	
	private MapObjectComponent mapObject;
	private CollisionComponent collision;
	    
    public Player(TextureAtlas atlas, int sizeX, int sizeY, World world)
    {
		mapObject = new MapObjectComponent(atlas, sizeX, sizeY);
		collision = new CollisionComponent(this, world);
		
		collision.setPosition(new Vector2(5, 5));
		
		super.add(mapObject);
	}
	
	@Override
	public void update(float delta)
	{
		collision.update(delta);
		mapObject.update(delta, new Vector2(collision.getPosition().x - 0.5f, collision.getPosition().y - 0.5f));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(mapObject.getX(), mapObject.getY());
	}
	
	public void move(Direction dir, boolean changeDir)
	{
		int speed = 1000;
		switch (dir)
		{
			case Up:
				collision.addForce(0, -speed);
				break;
			case Down:
				collision.addForce(0, speed);
				break;
			case Left:
				collision.addForce(-speed, 0);
				break;
			case Right:
				collision.addForce(speed, 0);
				break;
		}
		
		if (changeDir)
			mapObject.setDirection(dir);
	}
	
	public void stop()
	{
		//collision.setVelocity(0, 0);
	}
}
