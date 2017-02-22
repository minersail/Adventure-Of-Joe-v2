package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
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
    
    public enum AnimationState
    {
        Walking, Idle, Fighting, Death
    }
	
	private Direction direction = Direction.Down;
    private AnimationState animState;
	
	private Map<String, Animation<TextureRegion>> animation;
	private boolean usesAnimation;
	
	private float animationTime = 0;
	
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
		
		addAnimation("Left", "left", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Right", "right", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Up", "up", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Down", "down", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Left_Idle", "left_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Right_Idle", "right_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Up_Idle", "up_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Down_Idle", "down_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("Left_Fight", "left_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("Right_Fight", "right_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("Up_Fight", "up_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("Down_Fight", "down_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("Death", "death", atlas, 0.5f, Animation.PlayMode.NORMAL);
		
		size = new Vector2(1, 1);
		animState = AnimationState.Idle;
	}
	
	public MapObjectComponent addTo(MapObjects objects)
	{
		objects.add(this);
		return this;
	}
	
	public MapObjectComponent removeFrom(MapObjects objects)
	{
		objects.remove(this);
		return this;
	}
	
	public void update(float delta, Vector2 newPosition)
	{
		animationTime += delta;
		
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
	
	public void setAnimationState(AnimationState state)
	{
		// Both of these have linear animations
		if (state == AnimationState.Death || state == AnimationState.Fighting)
			animationTime = 0;
			
		animState = state;
	}
	
	public AnimationState getAnimationState()
	{
		return animState;
	}
	
	public float getTime()
	{
		return animationTime;
	}
	
	public void setTime(float time)
	{
		animationTime = time;
	}
	
	public void addTime(float time)
	{
		animationTime += time;
	}
	
	public boolean isAnimated()
	{
		return usesAnimation;
	}
		
	public Animation<TextureRegion> getAnimation(String str)
	{
		return animation.get(str);
	}
	
	private void addAnimation(String name, String region, TextureAtlas atlas, float frameTime, Animation.PlayMode mode)
	{
		if (atlas.findRegions(region) != null)
			animation.put(name, new Animation<TextureRegion>(frameTime, atlas.findRegions(region), mode));
	}
}
