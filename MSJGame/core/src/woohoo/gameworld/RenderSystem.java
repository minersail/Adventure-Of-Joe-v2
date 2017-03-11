package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;

public class RenderSystem extends IteratingSystem
{
	private GameRenderer renderer;
	
	public RenderSystem(TiledMap map, float scale)
	{
		super(Family.all(PositionComponent.class).one(MapObjectComponent.class, AnimMapObjectComponent.class).get());
		
		renderer = new GameRenderer(map, scale);
		
		for (Entity entity : super.getEngine().getEntities())
		{
			if (Mappers.mapObjects.has(entity))
			{
				renderer.getMap().getLayers().get("Objects").getObjects().add(Mappers.mapObjects.get(entity));
			}
			else if (Mappers.animMapObjects.has(entity))
			{
				renderer.getMap().getLayers().get("Objects").getObjects().add(Mappers.animMapObjects.get(entity));
			}
		}
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		renderer.render();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent pos = Mappers.positions.get(entity);
		
		if (Mappers.mapObjects.has(entity))
		{
			MapObjectComponent mapObject = Mappers.mapObjects.get(entity);
			
			mapObject.setX(pos.position.x);
			mapObject.setY(pos.position.y);
		}
		else if (Mappers.animMapObjects.has(entity))
		{
			AnimMapObjectComponent animMapObject = Mappers.animMapObjects.get(entity);
			
			animMapObject.setX(pos.position.x);
			animMapObject.setY(pos.position.y);
		}
	}
}
