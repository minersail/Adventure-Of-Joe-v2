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
                createCharacter(manager, name); // Regex to remove .png and file path
            
            if (name.startsWith("images/items/"))            
                createItem(manager, name);
        }
		
		// For some reason they start backwards
		Collections.sort(characters);
		Collections.sort(items);		
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
    
	private void createCharacter(AssetManager manager, String filename)
	{
		CharacterData data = new CharacterData(manager.get(filename, Texture.class), filename);
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
    
	private void createItem(AssetManager manager, String filename)
	{
		ItemData data = new ItemData(manager.get(filename, Texture.class), filename);
		items.add(data);
	}
    
    public class CharacterData implements Comparable<CharacterData>
    {
        private TextureRegion face;
        private String name;
		private int ID;

        public CharacterData(Texture texture, String str)
        {
            face = new TextureRegion(texture);
            name = str.replaceAll(".+_(.+)\\..*", "$1"); // Remove file path and extension
			ID = Integer.parseInt(str.replaceAll("\\D+","")); // Get only numbers in string
        }

        public TextureRegion getFace()
        {
            return face;
        }

        public String getName() 
        {
            return name;
        }
		
		public int getID()
		{
			return ID;
		}

		@Override
		public int compareTo(CharacterData o)
		{
			return getID() - o.getID();
		}
    }
    
    public class ItemData implements Comparable<ItemData>
    {
        private TextureRegion item;
        private String name;
		private int ID;

        public ItemData(Texture texture, String str)
        {
            item = new TextureRegion(texture);
            name = str.replaceAll(".+_(.+)\\..*", "$1"); // Remove file path and extension
			ID = Integer.parseInt(str.replaceAll("\\D+","")); // Get only numbers in string
        }

        public TextureRegion getItemTexture()
        {
            return item;
        }

        public String getName() 
        {
            return name;
        }
		
		public int getID()
		{
			return ID;
		}

		@Override
		public int compareTo(ItemData o)
		{
			return getID() - o.getID();
		}
    }
}
