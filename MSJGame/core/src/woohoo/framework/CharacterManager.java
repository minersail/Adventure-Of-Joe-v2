package woohoo.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;

/*
Class managing data for a character

Does NOT represent the in-game physics, position, etc.

Think of it like an imdb style list of all characters
 */
public class CharacterManager
{
	public ArrayList<CharacterData> characters;
	
	public CharacterManager(AssetManager manager)
	{
		characters = new ArrayList<>();
		
        for (String name : manager.getAssetNames())
        {
            if (name.startsWith("images/faces/"))            
                createCharacter(manager, name, name.replace("images/faces/", ""));
        }
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
		CharacterData data = new CharacterData(manager.get(filename, Texture.class), name);
		characters.add(data);
	}
    
    public class CharacterData {

        private TextureRegion face;
        private String name;

        public CharacterData(Texture t, String s) {
            face = new TextureRegion(t);
            name = s;
        }

        public TextureRegion getFace() {
            return face;
        }

        public String getName() {
            return name;
        }
    }
}
