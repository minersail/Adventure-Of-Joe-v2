package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class AnimMapObjectComponent extends TextureMapObject implements Component
{
    public enum AnimationState
    {
        Walking("walk"),
		Idle("idle"), 
		Fighting("fight"), 
		Death("death");
		
		private String text;
		
		AnimationState(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static AnimationState fromString(String str) 
		{
			for (AnimationState b : AnimationState.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No AnimationState with text " + str + " found.");
		}
    }
	
	public Direction direction = Direction.Down;
	
    public AnimationState animState;	
	public Map<String, Animation<TextureRegion>> animation;	
	public float animationTime = 0;
	
	public Vector2 size;
	
	public AnimMapObjectComponent(TextureAtlas atlas)
    {
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
		animationTime = 0;
	}
	
	public void update(float delta, Vector2 newPosition)
	{
		animationTime += delta;
		
		setX(newPosition.x);
		setY(newPosition.y);
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
	
	public void addTime(float time)
	{
		animationTime += time;
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
