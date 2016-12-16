package woohoo.framework;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer
{
	public CustomOrthogonalTiledMapRenderer(TiledMap map, float scale)
	{
		super(map, scale);
	}
	
	@Override
	public void renderObject(MapObject object)
	{
		if (object instanceof TextureMapObject)
		{
			TextureMapObject obj = (TextureMapObject)object;
			batch.draw(obj.getTextureRegion(), obj.getX(), obj.getY(), 8, 8);
		}
	}
}
