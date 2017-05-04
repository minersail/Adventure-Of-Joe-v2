package woohoo.gameobjects.components;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

/**
 * Boilerplate class to ensure that both {@link MapObjectComponent} and {@link AnimMapObjectComponent}
 * have a reference to a size variable
 * @author jordan
 */
public class SizedMapObject extends TextureMapObject
{
	private Vector2 size = new Vector2(1, 1);
	
	public Vector2 getSize()
	{
		return size;
	}
	
	public void setSize(Vector2 newSize)
	{
		size = newSize.cpy();
	}
}
