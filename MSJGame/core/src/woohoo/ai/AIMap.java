package woohoo.ai;

import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.MapObjectComponent;

public class AIMap implements IndexedGraph
{
	private Array<Node> nodes;
	private int width;
	private int height;
	
	public AIMap(Map map, World world, CollisionComponent component)
	{
		nodes = new Array<>();
		
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("Base");
		width = layer.getWidth();
		height = layer.getHeight();
		
		// Nodes are every 1x1 unit, except walls
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				if (!layer.getCell(i, j).getTile().getProperties().get("isWall", Boolean.class))
				{
					nodes.add(new Node(i, j));
				}				
				
				world.QueryAABB(new EntityQuery(nodes, component.getMass(), i, j), i + 1, j + 1, i, j);
			}
		}
	}
	
	@Override
	public Array getConnections(Object fN)
	{
		Array<DefaultConnection<Node>> connections = new Array<>();
		Node fromNode = (Node)fN;
		
		if (contains(fromNode.getNeighbor(MapObjectComponent.Direction.Up)))
			connections.add(new DefaultConnection<>(fromNode, fromNode.getNeighbor(MapObjectComponent.Direction.Up)));
		if (contains(fromNode.getNeighbor(MapObjectComponent.Direction.Down)))
			connections.add(new DefaultConnection<>(fromNode, fromNode.getNeighbor(MapObjectComponent.Direction.Down)));
		if (contains(fromNode.getNeighbor(MapObjectComponent.Direction.Left)))
			connections.add(new DefaultConnection<>(fromNode, fromNode.getNeighbor(MapObjectComponent.Direction.Left)));
		if (contains(fromNode.getNeighbor(MapObjectComponent.Direction.Right)))
			connections.add(new DefaultConnection<>(fromNode, fromNode.getNeighbor(MapObjectComponent.Direction.Right)));
		
		return connections;
	}

	@Override
	public int getIndex(Object node)
	{
		return nodes.indexOf((Node)node, false);
	}
	
	public Node get(int index)
	{
		return nodes.get(index);
	}

	@Override
	public int getNodeCount()
	{
		return nodes.size;
	}
	
	public boolean contains(Node node)
	{
		return nodes.contains(node, false);
	}
	
	/**
	 * Class created to check if any fixtures overlap a tile
	 */
	public class EntityQuery implements QueryCallback
	{
		Array<Node> nodeList;
		Body exclude;
		int x;
		int y;
		
		public EntityQuery(Array<Node> nodes, Body body, int X, int Y)
		{
			nodeList = nodes;
			exclude = body;
			x = X;
			y = Y;
		}
		
		@Override
		public boolean reportFixture(Fixture fixture)
		{			
			for (Fixture fix : exclude.getFixtureList())
			{
				if (fixture.equals(fix))
					return true; // Don't include fixtures on the body performing the pathfinding
			}
			
			// Finds a fixture overlapping the tile, and that fixture has collsion activated
			if (!fixture.isSensor())
			{
				// Remove this as a possible node for the pathfinding graph
				nodeList.removeValue(new Node(x, y), false);
				return false; // Only need to check for one
			}
			
			return true; // Keep searching
		}
	}
}
