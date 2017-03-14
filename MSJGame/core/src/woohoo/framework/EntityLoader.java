package woohoo.framework;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent;
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
        Element root = xml.parse(handle.readString());       
        Element entities = root.getChild(area);         
        
        for (Element e : entities.getChildrenByName("entity"))
        {
			if (!e.getBoolean("enabled")) continue;
			
			Entity entity = new Entity();
			
			for (int i = 0; i < e.getChildCount(); i++)
			{
				Component component = loadComponent(e.getChild(i));
				entity.add(component);
			}
		}
	}
	
	private Component loadComponent(Element component)
	{
		switch (component.getName())
		{
			case "ai":
				AIComponent brain = new AIComponent();
				return brain;
			case "anim":
				AnimMapObjectComponent animObject = new AnimMapObjectComponent(screen.getAssets().get(component.get("atlas"), TextureAtlas.class));
				return animObject;
			case "contact":
				ContactComponent contact = new ContactComponent();
		}
	}
}
