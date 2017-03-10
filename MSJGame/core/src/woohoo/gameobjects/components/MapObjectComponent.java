package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

public class MapObjectComponent extends TextureMapObject implements Component
{	
	public enum Direction
	{
		Up("up"), 
		Down("down"),
		Left("left"),
		Right("right");		
		
		private String text;
		
		Direction(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static Direction fromString(String str) 
		{
			for (Direction b : Direction.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No Direction with text " + str + " found.");
		}
	}
	
	public Direction direction = Direction.Down;	
	public Vector2 size;
	
	public MapObjectComponent(TextureRegion spr)
    {
		spr.flip(false, true);
		
		super.setTextureRegion(spr);
		
		size = new Vector2(1, 1);
	}
	
	public void update(float delta, Vector2 newPosition)
	{		
		setX(newPosition.x);
		setY(newPosition.y);
	}
	
	public void setSize(float x, float y)
	{
		size = new Vector2(x, y);
	}
	
	public Vector2 getSize()
	{
		return size;
	}
}
