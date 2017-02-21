package woohoo.gameworld;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.MapObjectComponent;

/*
Anything that is drawn will be managed by this class
(Currently only the TileMap)
*/
public class GameRenderer extends OrthogonalTiledMapRenderer
{    
	private boolean skipNextFrame;
	
    public GameRenderer(TiledMap map, float scale)
	{
		super(map, scale);
	}
	
	@Override
	public void renderObject(MapObject object)
	{
		if (object instanceof MapObjectComponent)
		{
			MapObjectComponent obj = (MapObjectComponent)object;
			if (obj.isAnimated())
			{
				String animString;
                
                switch(obj.getAnimationState())
                {
                    case Idle:
                        animString = obj.getDirection().toString() + "_Idle";
                        break;
                    case Fighting:
                        animString = obj.getDirection().toString() + "_Fight";
                        break;
                    case Death:
                        animString = "Death";
                        break;
                    case Walking:
                    default:
                        animString = obj.getDirection().toString();
                        break;
                }
				
				batch.draw(obj.getAnimation(animString).getKeyFrame(obj.getTime()),
						   obj.getX(), obj.getY(), obj.getSize().x, obj.getSize().y);
			}
			else
			{			
				batch.draw(obj.getTextureRegion(), obj.getX(), obj.getY(), obj.getSize().x, obj.getSize().y);
			}
		}
        else if (object instanceof HealthBarComponent)
        {
            HealthBarComponent healthBar = (HealthBarComponent)object;
            
            healthBar.draw(batch);
        }
	}
	
	@Override
	public void render()
	{
		if (!skipNextFrame)
			super.render();
		else
			skipNextFrame = false;
	}
	
	// Creates a one frame buffer between screens
	public void skipFrame()
	{
		skipNextFrame = true;
	}
}
