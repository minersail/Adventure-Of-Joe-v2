package woohoo.gameworld.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class PlayerComponent implements Component
{
	public Array<Entity> touchedItems;
	public int money;
	public float armor;
    
    public PlayerComponent()
    {
        touchedItems = new Array<>();
		money = 0;
		armor = 0;
    }
}
