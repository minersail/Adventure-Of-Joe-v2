package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import woohoo.screens.PlayingScreen.WBodyType;

public class Enemy extends Character
{    
    public Enemy(Texture texture)
    {
		super(new TextureRegion(texture), WBodyType.Enemy, new Vector2(6, 3));
		mapObject.getTextureRegion().flip(false, true);
	}    
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
}
