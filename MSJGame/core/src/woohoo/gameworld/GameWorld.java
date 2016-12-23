package woohoo.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.BaseEntity;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameWorld extends Engine
{
	private Screen screen;
    public float runtime;
	
	public GameWorld(Screen scr, World physics)
	{
		screen = scr;
	}
    
	@Override
    public void update(float delta)
    {
        runtime += delta;
		
		for (Entity entity : getEntities())
		{
			((BaseEntity)entity).update(delta);
		}
    }
}
