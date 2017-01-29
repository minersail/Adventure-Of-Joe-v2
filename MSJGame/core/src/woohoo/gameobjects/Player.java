package woohoo.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen.WBodyType;

public class Player extends Character
{		    
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
	}
	
	public void move(Direction dir)
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
	}
	
	public void stop()
	{
		collision.setForce(0, 0);
	}
    
    public void setPosition(float x, float y)
    {
        collision.setPosition(x, y);
    }
}
