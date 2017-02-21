package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import woohoo.ai.AIHeuristic;
import woohoo.ai.AIMap;
import woohoo.ai.Node;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class AIComponent implements Component
{
	public enum AIMode
	{
		Follow, Stay, MoveTo, Input
	}
	
	private AIMap nodes;
	private AIHeuristic heuristic;
	private IndexedAStarPathFinder pathFinder;
	private DefaultGraphPath path;
	
	// Reference to player, change later to reference of viable targets to follow
	private Character targetChar;
	private Vector2 targetPos;
	
	private Direction nextDirection;
    private boolean lockDirection;
	private float timer;
	private float timeStep; // How often the AI should switch directions
	
	private final float DEFAULT_TIMESTEP = 0.5f;
	
	private AIMode mode;
	
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
	
	public void initializePathfinding(Map map, World world, ArrayList<CollisionComponent> exclude, ArrayList<Vector2> extraNodes, int topRow, int botRow, int leftCol, int rightCol)
	{
		nodes = new AIMap(map, world, exclude, extraNodes, topRow, botRow, leftCol, rightCol);
		pathFinder = new IndexedAStarPathFinder(nodes);
		path = new DefaultGraphPath();
	}
	
	public void calculatePath(Vector2 current, Vector2 target)
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
	
	private Direction getDirection(Vector2 current, Vector2 target)
	{
		// If timer is not up yet, return previous direction
        if (lockDirection) return nextDirection;
		
		calculatePath(current, target);
		
        if (path.nodes.size <= 1) return nextDirection;
        		
		float dX = current.x - ((Node)path.nodes.get(1)).x; // path.nodes.get(0) is current position, path.nodes.get(1) is next
		float dY = current.y - ((Node)path.nodes.get(1)).y;
		
		if (Math.abs(dX) > Math.abs(dY))
		{
			nextDirection = dX > 0 ? Direction.Left : Direction.Right;
		}
		else
		{
			nextDirection = dY > 0 ? Direction.Up : Direction.Down;
		}
		
		lockDirection = true;
        return nextDirection;
	}
	
	public Direction calculateDirection(Vector2 current)
	{
		switch(mode)
		{
			case Follow:
				return getDirection(current, targetChar.getPosition());
			case MoveTo:								
				return getDirection(current, targetPos);
			case Stay:
				return null;
			default:
				return null;			
		}
	}
	
	public AIMode getAIMode()
	{
		return mode;
	}
	
	public void setAIMode(AIMode aimode)
	{
		mode = aimode;
	}
	
	public void setTargetPosition(Vector2 current, Vector2 target)
	{
		targetPos = target;
	}
	
	public void setTargetCharacter(Vector2 current, Character character)
	{
		targetChar = character;
	}
	
	public void setTimeStep(float newStep)
	{
		timeStep = newStep;
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
