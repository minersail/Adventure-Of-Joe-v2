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
		mapObject.setX(collision.getPosition().x - 0.5f);
		mapObject.setY(collision.getPosition().y - 0.5f);
		
		mapObject.update(delta);
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(mapObject.getX(), mapObject.getY());
	}
	
	public void move(Direction dir, boolean changeDir)
	{
		switch (dir)
		{
			case Up:
				collision.addVelocity(0, -1);
				break;
			case Down:
				collision.addVelocity(0, 1);
				break;
			case Left:
				collision.addVelocity(-1, 0);
				break;
			case Right:
				collision.addVelocity(1, 0);
				break;
		}
		
		if (changeDir)
			mapObject.setDirection(dir);
	}
	
	public void stop()
	{
		collision.setVelocity(0, 0);
	}
}
