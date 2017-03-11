package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class GateSystem extends IteratingSystem
{
	private GateComponent triggeredGate;
	private boolean switchArea;
	private PlayingScreen screen;
	
	public GateSystem(PlayingScreen scr)
	{
		super(Family.all(GateComponent.class, PositionComponent.class).get());
		scr = screen;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		GateComponent gate = Mappers.gates.get(entity);
		
		if (gate.triggered)
		{
			triggeredGate = gate;
			switchArea = true;
		}
		
        if (switchArea) // Boolean check instead of instantaneous call is necessary so
		{                // that the area switching does not take place during world.step();
			updateArea();
		}                   
	}
	
	/*
    Checks if a game area switch has been triggered, and switches areas if it has.
    */
    public void updateArea()
	{ 
		// Clear all walls and gates
        Array<Body> bodies = new Array<>();
		screen.getWorld().getBodies(bodies);
		
		for (Body body: bodies)
		{
			screen.getWorld().destroyBody(body);
		}
		
		// Remove all entities from game world		
		for (Entity entity : screen.getEngine().getDuplicateList())
		{
			if (entity != screen.getEngine().getPlayer())
			{
				screen.removeEntity(entity);
			}
		}
		
		// Remove event handlers from old screen
		Mappers.eventListeners.get(screen.getEngine().getPlayer()).listeners.clearListeners();
		screen.getCutsceneManager().getListeners().clearListeners();
		
		// Just fancy way to move all objects from old map to new map (Change in future to just player)
		final TiledMap map = screen.getMapLoader().load(triggeredGate.destArea);
		map.getLayers().get("Entities").getObjects().add(screen.getEngine().getPlayer().getComponent(MapObjectComponent.class));
		screen.getRenderer().setMap(map);
        
		// Move the player to the entrance of the new map based on where he exited previous map (Took forever to figure out)
        Mappers.positions.get(screen.getEngine().getPlayer()).position.set(triggeredGate.playerPos);
		
		screen.getEntityLoader().loadEntities(triggeredGate.destArea);
		screen.getEventManager().createEvents(triggeredGate.destArea);
		screen.getAIManager().initializePathfinding(triggeredGate.destArea);
		
		// There's one frame where new map is not quite loaded, so skip the frame. Nobody will even notice
		screen.getRenderer().skipFrame();
		
		switchArea = false;
	}
}
