package woohoo.gameworld.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapObjectComponent extends SizedMapObject implements Component
{		
	public MapObjectComponent(TextureRegion spr)
    {
		spr.flip(false, true);
		
		super.setTextureRegion(spr);
	}
}
