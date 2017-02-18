package woohoo.ai;

import woohoo.gameobjects.components.MapObjectComponent.Direction;

public class Node
{
	public int x;
	public int y;
	
	public Node(int X, int Y)
	{
		x = X;
		y = Y;
	}
	
	public Node(float X, float Y)
	{
		x = Math.round(X);
		y = Math.round(Y);
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Node)) return false;
		
		return ((Node)obj).x == x && ((Node)obj).y == y;
	}
	
	public Node getNeighbor(Direction dir)
	{
		switch (dir)
		{
			case Up:
				return new Node(x, y - 1);
			case Down:
				return new Node(x, y + 1);
			case Left:
				return new Node(x - 1, y);
			case Right:
				return new Node(x + 1, y);
			default:
				return new Node(x, y);
		}
	}

	// This was automatically generated
	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 29 * hash + this.x;
		hash = 29 * hash + this.y;
		return hash;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
