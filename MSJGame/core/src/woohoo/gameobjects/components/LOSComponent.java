package woohoo.gameobjects.components;

import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
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
	}
    
    @Override
    public Shape getShape()
    {
        // WRITE THIS
        return null;
    }
}
