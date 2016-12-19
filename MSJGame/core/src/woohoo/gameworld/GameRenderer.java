package woohoo.gameworld;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import woohoo.gameobjects.components.MapObjectComponent;

/*
Anything that is drawn will be managed by this class
(Currently only the TileMap)
*/
public class GameRenderer extends OrthogonalTiledMapRenderer
{    
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
				batch.draw(obj.getAnimation(obj.getDirection().toString()).getKeyFrame(obj.getTime()),
						   obj.getX(), obj.getY(), obj.getScaleX(), obj.getScaleY());				
			}
			else
			{			
				batch.draw(obj.getTextureRegion(), obj.getX(), obj.getY(), obj.getScaleX(), obj.getScaleY());
			}
		}
	}
}
