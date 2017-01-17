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
		Up, Down, Left, Right
	}
	
	private Direction direction = Direction.Down;
	
	private Map<String, Animation<TextureRegion>> animation;
	private boolean usesAnimation;
	private boolean isIdle;
	
	private float runTime = 0;
	
	private Vector2 size;
	
	public MapObjectComponent(TextureRegion spr)
    {
		usesAnimation = false;
		spr.flip(false, true);
		
		super.setTextureRegion(spr);
		
		size = new Vector2(1, 1);
	}
	
	public MapObjectComponent(TextureAtlas atlas)
    {
		usesAnimation = true;
		animation = new HashMap<>();
		
		addAnimation("Left", new Animation<TextureRegion>(0.333f, atlas.findRegions("left"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Right", new Animation<TextureRegion>(0.333f, atlas.findRegions("right"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Up", new Animation<TextureRegion>(0.333f, atlas.findRegions("up"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Down", new Animation<TextureRegion>(0.333f, atlas.findRegions("down"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Left_Idle", new Animation<TextureRegion>(0.333f, atlas.findRegions("left_idle"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Right_Idle", new Animation<TextureRegion>(0.333f, atlas.findRegions("right_idle"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Up_Idle", new Animation<TextureRegion>(0.333f, atlas.findRegions("up_idle"), Animation.PlayMode.LOOP_PINGPONG));
		addAnimation("Down_Idle", new Animation<TextureRegion>(0.333f, atlas.findRegions("down_idle"), Animation.PlayMode.LOOP_PINGPONG));
		
		size = new Vector2(1, 1);
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
	
	public void setSize(float x, float y)
	{
		size = new Vector2(x, y);
	}
	
	public Vector2 getSize()
	{
		return size;
	}
	
	public void setIdle(boolean idle)
	{
		isIdle = idle;
	}
	
	public boolean isIdle()
	{
		return isIdle;
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
