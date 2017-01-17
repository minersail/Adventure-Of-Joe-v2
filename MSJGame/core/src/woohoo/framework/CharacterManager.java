package woohoo.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;

/*
Manages Character Data, not Characters.
*/
public class CharacterManager
{
	public ArrayList<CharacterData> characters;
	
	public CharacterManager(AssetManager manager)
	{
		characters = new ArrayList<>();
		
		createCharacter(manager, "oldman.png", "Old Man");
		createCharacter(manager, "ginger.png", "John");
	}
	
	public CharacterData getCharacter(int ID)
	{
		if (ID >= characters.size())
			return null;
		
		return characters.get(ID);
	}
	
	// Accepts filename path relative to the images/faces folder
	private void createCharacter(AssetManager manager, String filename, String name)
	{
		CharacterData data = new CharacterData(manager.get("images/faces/" + filename, Texture.class), name);
		characters.add(data);
	}
}
