package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MapObjectComponent;

public class RenderSystem extends IteratingSystem
{
	private GameRenderer renderer;
	
	public RenderSystem(TiledMap map, float scale)
	{
		super(Family.all(MapObjectComponent.class, HitboxComponent.class).get());
		
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
		HitboxComponent hitbox = Mappers.hitboxes.get(entity);

		mapObject.setX(hitbox.getPosition().x);
		mapObject.setY(hitbox.getPosition().y);
	}
}
