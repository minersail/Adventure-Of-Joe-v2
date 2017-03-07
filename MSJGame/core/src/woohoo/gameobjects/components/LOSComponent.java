package woohoo.gameobjects.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.screens.PlayingScreen;

public class LOSComponent extends SensorComponent
{
    public LOSComponent(PlayingScreen.WBodyType bodyType) 
    {
        super(bodyType);
    }
    
    @Override
	public void createMass(World world)
	{
		super.createMass(world);
		
		mass.setType(BodyDef.BodyType.DynamicBody);
	}
    
    @Override
    public Shape getShape()
    {
		PolygonShape shape = new PolygonShape();
		float LOSradius = 5;
		
		float SIN60 = (float)Math.sin(Math.PI / 3);
		float COS60 = (float)Math.cos(Math.PI / 3);
		float SIN70 = (float)Math.sin(1.22);
		float COS70 = (float)Math.cos(1.22);
		float SIN80 = (float)Math.sin(1.4);
		float COS80 = (float)Math.cos(1.4);
		
		Vector2[] vertices = {new Vector2(0, 0), new Vector2(LOSradius * SIN60, LOSradius * COS60), new Vector2(LOSradius * SIN70, LOSradius * COS70), 
							  new Vector2(LOSradius * SIN80, LOSradius * COS80), new Vector2(LOSradius * SIN60, LOSradius * -COS60),
							  new Vector2(LOSradius * SIN70, LOSradius * -COS70), new Vector2(LOSradius * SIN80, LOSradius * -COS80)};
		
		shape.set(vertices);
        return shape;
    }
	
	@Override
	public void update(float delta)
	{
	}
	
	public void rotate(Direction direction)
	{
		switch(direction)
		{
			case Up:
				mass.setTransform(mass.getPosition(), 3 * (float)Math.PI / 2);
				break;
			case Down:
				mass.setTransform(mass.getPosition(), (float)Math.PI / 2);
				break;
			case Left:
				mass.setTransform(mass.getPosition(), (float)Math.PI);
				break;
			case Right:
				mass.setTransform(mass.getPosition(), 0);
				break;
		}
	}
}
