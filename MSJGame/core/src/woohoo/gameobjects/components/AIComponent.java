package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.ai.AIHeuristic;
import woohoo.ai.AIMap;
import woohoo.ai.Node;
import woohoo.ai.aipatterns.*;
import woohoo.gameobjects.components.MovementComponent.Direction;

public class AIComponent implements Component
{
	private AIMap nodes;
	private AIHeuristic heuristic;
	private IndexedAStarPathFinder pathFinder;
	private DefaultGraphPath path;
	
	public Direction currentDirection;
    public boolean lockDirection;
	
	// I don't think the timestep stuff is currently being used
	public float timer;    // Internal timer to keep track of time
	public float timeStep; // How often the AI should switch directions
	
	public final float DEFAULT_TIMESTEP = 0.5f;
	
	private AIPattern pattern;
	
	public AIComponent()
	{		
		currentDirection = Direction.None;
		timeStep = DEFAULT_TIMESTEP;
		heuristic = new AIHeuristic();
		pattern = null;
	}
	
	public AIComponent(String str)
	{
		this();
		if (str.equals("wander"))
		{
			pattern = new WanderPattern();
		}
		else if (str.equals("stay"))
		{
			pattern = new StayPattern();
		}
	}
	
	public AIComponent(String pushed, Vector2 target)
	{
		this();
		pattern = new PushPattern(pushed, target);
	}
	
	public AIComponent(Vector2 target)
	{
		this();
		pattern = new MovePattern(target);
	}
	
	public AIComponent(String str, String target)
	{
		this();		
		if (str.equals("attackchase"))
		{
			pattern = new AttackChasePattern(target);
		}
		else if (str.equals("chase"))
		{
			pattern = new ChasePattern(target);			
		}
	}
	
	public AIComponent(Array<Vector2> patrol)
	{
		this();
		pattern = new ChaseSentryPattern(patrol);
	}
	
	/*
	Generates pathfinding grid
	*/
	public void initializePathfinding(Map map, World world, Element data)
	{
		int topRow = 0, botRow = 0, leftCol = 0, rightCol = 0;		
		ArrayList<Vector2> extraNodes = new ArrayList<>();
		
		if (data.getChildByName("settings") != null)
		{
			topRow = data.getChildByName("settings").getInt("topRow", 0);
			botRow = data.getChildByName("settings").getInt("botRow", 0);
			leftCol = data.getChildByName("settings").getInt("leftCol", 0);
			rightCol = data.getChildByName("settings").getInt("rightCol", 0);
		}
        
        for (Element nodeData : data.getChildrenByName("node"))
		{
			extraNodes.add(new Vector2(nodeData.getInt("x"), nodeData.getInt("y")));
		}
		
		nodes = new AIMap(map, world, extraNodes, topRow, botRow, leftCol, rightCol);
		pathFinder = new IndexedAStarPathFinder(nodes);
		path = new DefaultGraphPath();
	}
	
	/*
	Runs the pathfinding algorithm and stores the resulting path in path
	*/
	private void calculatePath(Vector2 current, Vector2 target)
	{
		Node nodeStart = nodes.get(Math.round(current.x), Math.round(current.y));
		Node nodeEnd = nodes.get(Math.round(target.x), Math.round(target.y));
		
		if (nodeStart == null)
			nodeStart = nodes.getFirst();
		if (nodeEnd == null)
			nodeEnd = nodes.getLast();
		
		path.clear();
		pathFinder.searchNodePath(nodeStart, nodeEnd, heuristic, path);
	}
	
	public Direction getDirectionFromPath(Vector2 current, Vector2 target)
	{		
		calculatePath(current, target);
		
        if (path.nodes.size <= 1) return currentDirection;
        		
		float dX = current.x - ((Node)path.nodes.get(1)).x; // path.nodes.get(0) is current position, path.nodes.get(1) is next
		float dY = current.y - ((Node)path.nodes.get(1)).y;
		
		if (Math.abs(dX) > Math.abs(dY))
		{
			currentDirection = dX > 0 ? Direction.Left : Direction.Right;
		}
		else
		{
			currentDirection = dY > 0 ? Direction.Up : Direction.Down;
		}
		
        return currentDirection;
	}
	
	public void resetTimeStep()
	{
		timeStep = DEFAULT_TIMESTEP;
	}
	
	public AIMap getAIMap()
	{
		return nodes;
	}
	
	public AIPattern getPattern()
	{
		return pattern;
	}
	
	public void setPattern(AIPattern newPattern)
	{
		pattern = newPattern;
	}
}
