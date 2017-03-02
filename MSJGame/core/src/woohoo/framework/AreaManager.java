package woohoo.framework;

import com.badlogic.gdx.utils.Array;
import woohoo.screens.PlayingScreen;

public class AreaManager
{
	private PlayingScreen screen;
	private Array<Area> areas;
	
	private final int NUM_AREAS = 4;
	
	public AreaManager(PlayingScreen scr)
	{
		screen = scr;
		areas = new Array<>();
		
		for (int i = 0; i < NUM_AREAS; i++)
		{
			areas.add(new Area(i, 0));
		}
	}
	
	public int getAreaState(int areaID)
	{
		for (Area area : areas)
		{
			if (area.getID() == areaID) return area.getState();
		}
		
		return -1;
	}
	
	public void setAreaState(int areaID, int state)
	{
		for (Area area : areas)
		{
			if (area.getID() == areaID) area.setState(state);
		}
	}
	
	public class Area
	{
		private int id;
		private int state;
		
		public Area(int ID, int startState)
		{
			id = ID;
			state = startState;
		}
		
		public int getID()
		{
			return id;
		}
		
		public int getState()
		{
			return state;
		}
		
		public void setState(int s)
		{
			state = s;
		}
	}
}
