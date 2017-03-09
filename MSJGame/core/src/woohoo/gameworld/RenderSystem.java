package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Movement;

public class RenderSystem extends IteratingSystem
{
	private GameRenderer renderer;
	
	public RenderSystem(TiledMap map, float scale)
	{
		super(Family.all(MapObjectComponent.class).get());
		
		renderer = new GameRenderer(map, scale);
		
		for (Entity entity : super.getEngine().getEntities())
		{
			if (Mappers.mapObjects.has(entity))
			{
				renderer.getMap().getLayers().get("Objects").getObjects().add(Mappers.mapObjects.get(entity));
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
		MapObjectComponent mapObject = Mappers.mapObjects.get(entity);
		MovementComponent movement = Mappers.movements.get(entity);

		Mappers.mapObjects.get(entity).setX(Mappers.positions.get(entity).getPosition().x);
		Mappers.mapObjects.get(entity).setY(Mappers.positions.get(entity).getPosition().y);
	}
}
