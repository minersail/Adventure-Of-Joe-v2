package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;

public class MapObjectComponent extends TextureMapObject implements Component
{	
	public enum Direction
	{
		None, Up, Down, Left, Right
	}
	
	private Map<String, Animation<TextureRegion>> animation;
	
	private Direction direction = Direction.Up;
	private float runTime = 0;
	private boolean usesAnimation;
	
	public MapObjectComponent(TextureRegion spr, int sizeX, int sizeY)
    {
		usesAnimation = false;
		spr.flip(false, true);
		
		super.setTextureRegion(spr);
		
		super.setScaleX(sizeX);
		super.setScaleY(sizeY);
	}
	
	public MapObjectComponent(TextureAtlas atlas, int sizeX, int sizeY)
    {
		usesAnimation = true;
		animation = new HashMap<>();
		
		addAnimation("Left", new Animation<TextureRegion>(0.333f, atlas.findRegions("left"), Animation.PlayMode.LOOP));
		addAnimation("Right", new Animation<TextureRegion>(0.333f, atlas.findRegions("right"), Animation.PlayMode.LOOP));
		addAnimation("Up", new Animation<TextureRegion>(0.333f, atlas.findRegions("up"), Animation.PlayMode.LOOP));
		addAnimation("Down", new Animation<TextureRegion>(0.333f, atlas.findRegions("down"), Animation.PlayMode.LOOP));
		
		super.setScaleX(sizeX);
		super.setScaleY(sizeY);
	}
	
	public void update(float delta, Vector2 newPosition)
	{
		runTime += delta;
		
		setX(newPosition.x);
		setY(newPosition.y);
	}
	
	public void setDirection(Direction dir)
	{
		direction = dir;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public float getTime()
	{
		return runTime;
	}
	
	public boolean isAnimated()
	{
		return usesAnimation;
	}
		
	public Animation<TextureRegion> getAnimation(String str)
	{
		return animation.get(str);
	}
	
	public void addAnimation(String str, Animation<TextureRegion> anim)
	{
		animation.put(str, anim);
	}
}
