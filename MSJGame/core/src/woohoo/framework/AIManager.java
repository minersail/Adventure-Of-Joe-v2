package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.gameobjects.BaseEntity;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.Enemy;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.screens.PlayingScreen;

public class AIManager
{
	PlayingScreen screen;
	
	public AIManager(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public void initializePathfinding(int area)
	{
		FileHandle handle = Gdx.files.local("data/pathfinding.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());    
		Element data = root.getChild(area);
		
		ArrayList<Vector2> extraNodes = new ArrayList<>();
		int topRow = data.getChildByName("settings").getInt("topRow", 0);
		int botRow = data.getChildByName("settings").getInt("botRow", 0);
		int leftCol = data.getChildByName("settings").getInt("leftCol", 0);
		int rightCol = data.getChildByName("settings").getInt("rightCol", 0);
        
        for (Element nodeData : data.getChildrenByName("node"))
		{
			extraNodes.add(new Vector2(nodeData.getInt("x"), nodeData.getInt("y")));
		}
		
		ArrayList<BaseEntity> entities = screen.getEngine().getDuplicateList();
		
		for (BaseEntity entity : entities)
		{
			if (entity instanceof Character)
			{ // exclude is list of box2d bodies that would otherwise interfere with the pathfinding grid
				ArrayList<CollisionComponent> exclude = new ArrayList<>();
				exclude.add(entity.getComponent(CollisionComponent.class));
				
				if (entity instanceof Enemy)
				{
					entity.getComponent(AIComponent.class).setTargetCharacter(entity.getComponent(CollisionComponent.class).getPosition(), 
																			  screen.getEngine().getPlayer());
					exclude.add(screen.getEngine().getPlayer().getComponent(CollisionComponent.class));
				}
				
				entity.getComponent(AIComponent.class).initializePathfinding(screen.getRenderer().getMap(), screen.getWorld(), exclude, extraNodes,
																			 topRow, botRow, leftCol, rightCol);
			}
		}
	}
}
