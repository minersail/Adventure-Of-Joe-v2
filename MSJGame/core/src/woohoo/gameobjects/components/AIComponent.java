package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;
import woohoo.ai.AIHeuristic;
import woohoo.ai.AIMap;
import woohoo.ai.Node;
import woohoo.ai.aistates.AIState;
import woohoo.gameobjects.components.MovementComponent.Direction;

public class AIComponent implements Component
{
	public enum AIMode
	{
		Follow("follow"), 
		Stay("stay"), 
		MoveTo("moveto"), 
		Input("input"), 
		Random("random"), 
		Sentry("sentry");
		
		private String text;
		
		AIMode(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static AIMode fromString(String str) 
		{
			for (AIMode b : AIMode.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No AIMode with text " + str + " found.");
		}
	}
	
	private AIMap nodes;
	private AIHeuristic heuristic;
	private IndexedAStarPathFinder pathFinder;
	private DefaultGraphPath path;
	
	// Reference to player, change later to reference of viable targets to follow
	private Character targetChar;
	private Vector2 targetPos;
	
	public Direction currentDirection;
    public boolean lockDirection;
	public float timer;    // Internal timer to keep track of time
	public float timeStep; // How often the AI should switch directions
	
	public final float DEFAULT_TIMESTEP = 0.5f;
	
	public AIMode mode;
	public AIState state;
	
	public AIComponent()
	{		
		mode = AIMode.Stay;
		timeStep = DEFAULT_TIMESTEP;
		heuristic = new AIHeuristic();
	}
	
	public void update(float delta, Vector2 currentPos)
	{
		timer += delta;
		
		if (timer > timeStep)
		{
			timer = 0;
			lockDirection = false;
		}
		
		// If moving towards a certain spot
		if (mode == AIMode.MoveTo)
		{
			float dX = currentPos.x - targetPos.x;
			float dY = currentPos.y - targetPos.y;

			if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) < 0.5f)
				mode = AIMode.Stay;
		}
	}
	
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
	
	public Direction getDirection(Vector2 current, Vector2 target)
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
}
