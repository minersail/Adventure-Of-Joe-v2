package woohoo.framework;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
Class representing data for a character

Does NOT represent the in-game physics, position, etc.

Think of it like an imdb style list of all characters
*/
public class CharacterData
{
	private TextureRegion face;
	private String name;
	
	public CharacterData(Texture t, String s)
	{
		face = new TextureRegion(t);
		name = s;
	}
	
	public TextureRegion getFace()
	{
		return face;
	}
	
	public String getName()
	{
		return name;
	}
}
