package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
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
	
	/**
	 * Generates cone-shaped line of sight
	 * @param current origin of line of sight
	 * @param dir direction of line of sight
	 * @return list of positions that can be seen from the current position and direction
	 */
	public Array<Vector2> getLineOfSight(Vector2 current, Direction dir)
	{
		Array<Vector2> los = new Array<>();
		float radius = 8;
				
		// Bottom part of the cone
		for (double i = 0; i <= radius * Math.sqrt(3) / 2; i++)
		{
			for (double j = Math.floor(-i / 2); j <= Math.ceil(i / 2); j++) // 1 to 2 ratio gives us the 60 degrees line of vision
			{
				los.add(new Vector2(Math.round(current.x + j), Math.round(current.y + i)));
			}
		}
		
		// Top part of the cone
		for (double i = radius * Math.sqrt(3) / 2; i <= radius; i++)
		{
			for (double j = Math.floor(-(radius - i) * 2); j <= Math.ceil((radius - i) * 2); j++)
			{
				los.add(new Vector2(Math.round(current.x + j), Math.round(current.y + i)));
			}
		}
		
		// Rotate cone based on direction
		for (Vector2 loc : los)
		{
			float offsetX = loc.x - Math.round(current.x);
			float offsetY = loc.y - Math.round(current.y);
			
			switch(dir)
			{
				case Up:
					loc.x = -offsetX + current.x;
					loc.y = -offsetY + current.y;
					break;
				case Down:
					break;
				case Left:
					loc.x = -offsetY + current.x;
					loc.y = -offsetX + current.y;
					break;
				case Right:
					loc.x = offsetY + current.x;
					loc.y = offsetX + current.y;
					break;
			}
			
			loc.x = Math.round(loc.x);
			loc.y = Math.round(loc.y);
		}
		
		Array<Vector2> obstructions = new Array<>();
		// Remove locations obscured by line of sight, based on pathfinding grid
		for (Vector2 loc : los)
		{
			Node node = nodes.get((int)loc.x, (int)loc.y);
			
			if (node != null)
			{
				for (Vector2 obstr : obstructions)
				{
					if (obstr.y == loc.y && ((dir == Direction.Left && obstr.x > loc.x) || (dir == Direction.Right && obstr.x < loc.x)) ||
						obstr.x == loc.x && ((dir == Direction.Up && obstr.y > loc.y) || (dir == Direction.Down && obstr.y < loc.y)))
					{
						los.removeValue(loc, false);
					}
				}
			}
			else if (node == null)
			{
				obstructions.add(loc);
				los.removeValue(loc, false);
			}
		}
		
		return los;
	}
}
