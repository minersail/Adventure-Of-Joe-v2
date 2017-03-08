package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.NPC;
import woohoo.screens.PlayingScreen;

public class EntityLoader
{
	PlayingScreen screen;
	
	public EntityLoader(PlayingScreen scr)
	{
		screen = scr;
	}	
	
	public void loadEntities(int area)
	{
		FileHandle handle = Gdx.files.local("data/entities.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());       
        XmlReader.Element entities = root.getChild(area);         
        
        for (XmlReader.Element entity : entities.getChildrenByName("entity"))
        {
			if (!entity.getBoolean("enabled")) continue;
			
			String eClass = entity.get("class");
			
			BaseEntity base = null;
			
			switch (eClass)
			{
				case "npc":					
					base = entity.getChildByName("characterdata").getBoolean("animated", false) ? 
						new NPC(screen.getAssets().get("images/entities/" + entity.getChildByName("characterdata").get("texture"), TextureAtlas.class), entity.getChildByName("npcdata").getInt("dialogueid")) :
						new NPC(screen.getAssets().get("images/entities/" + entity.getChildByName("characterdata").get("texture"), Texture.class), entity.getChildByName("npcdata").getInt("dialogueid")); 
					break;
				case "item":
					base = new Item(screen.getIDManager().getItem(entity.getInt("id")).getItemTexture());
					Item item = (Item)base;
					item.setPosition(entity.getFloat("locX"), entity.getFloat("locY"));
					item.setType(entity.get("type"));
					item.flipImage();
					break;
				case "enemy":
					base = new Enemy(screen.getAssets().get("images/entities/" + entity.getChildByName("characterdata").get("texture"), Texture.class));
					Enemy enemy = (Enemy)base;
					enemy.changeMaxHealth(entity.getFloat("health"));
					break;
			}
			if (base == null) return;
			
			screen.addEntity(base);
			base.setName(entity.get("name", ""));
			
			Element chdata = entity.getChildByName("characterdata");
			Element aidata = entity.getChildByName("aidata");
			
			if (chdata != null)
			{
				Character cha = (Character)base;
				cha.setPosition(chdata.getFloat("locX"), chdata.getFloat("locY"));
				cha.setSpeed(chdata.getFloat("speed", cha.getSpeed()));
			}
			if (aidata != null)
			{
				Character cha = (Character)base;
				cha.setAIMode(aidata.get("mode", "stay"));
				cha.setMoveTimestep(aidata.getFloat("timestep", 0.5f));
			}
		}
	}
}
