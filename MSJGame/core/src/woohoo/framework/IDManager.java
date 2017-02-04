package woohoo.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Collections;

/*
Class managing data for a character

Does NOT represent the in-game physics, position, etc.

Think of it like an imdb style list of all characters
 */
public class IDManager
{
	private ArrayList<CharacterData> characters;
	private ArrayList<ItemData> items;
	
	public IDManager(AssetManager manager)
	{
		characters = new ArrayList<>();
		items = new ArrayList<>();
		
        for (String name : manager.getAssetNames())
        {
            if (name.startsWith("images/faces/"))            
                createCharacter(manager, name, name.replaceAll(".+_(.+)\\..*", "$1")); // Regex to remove .png and file path
            
            if (name.startsWith("images/items/"))            
                createItem(manager, name, name.replaceAll(".+_(.+)\\..*", "$1"));
        }
		
		// For some reason they start backwards
		Collections.reverse(characters);
		Collections.reverse(items);		
	}
	
	public CharacterData getCharacter(int ID)
	{
		if (ID >= characters.size())
			return null;
		
		return characters.get(ID);
	}
	
	public CharacterData getCharacter(String name)
	{
		for (CharacterData data : characters)
        {
            if (data.getName().equals(name))
            {
                return data;
            }
        }
        return null;
	}
    
	private void createCharacter(AssetManager manager, String filename, String name)
	{
		CharacterData data = new CharacterData(manager.get(filename, Texture.class), name);
		characters.add(data);
	}
    
    public ItemData getItem(int ID)
	{
		if (ID >= items.size())
			return null;
		
		return items.get(ID);
	}
	
	public ItemData getItem(String name)
	{
		for (ItemData data : items)
        {
            if (data.getName().equals(name))
            {
                return data;
            }
        }
        return null;
	}
    
	private void createItem(AssetManager manager, String filename, String name)
	{
		ItemData data = new ItemData(manager.get(filename, Texture.class), name);
		items.add(data);
	}
    
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
    
    public class ItemData 
    {
        private TextureRegion item;
        private String name;

        public ItemData(Texture t, String s)
        {
            item = new TextureRegion(t);
            name = s;
        }

        public TextureRegion getItemTexture()
        {
            return item;
        }

        public String getName() 
        {
            return name;
        }
    }
}
