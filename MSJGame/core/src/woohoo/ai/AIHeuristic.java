package woohoo.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class AIHeuristic implements Heuristic
{
	@Override
	public float estimate(Object node, Object endNode)
	{
		Node start = (Node)node;
		Node end = (Node)endNode;
		
		// Manhattan distance
		return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
	}
}
