package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.MovementComponent.Movement;

public class RenderSystem extends EntitySystem
{
	private GameRenderer renderer;
	
	public RenderSystem(TiledMap map, float scale)
	{
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
		for (Entity entity : super.getEngine().getEntities())
		{
			if (Mappers.mapObjects.has(entity))
			{
				MapObjectComponent mapObject = Mappers.mapObjects.get(entity);
				MovementComponent movement = Mappers.movements.get(entity);
				
				Mappers.mapObjects.get(entity).setX(Mappers.movements.get(entity).getPosition().x);
				Mappers.mapObjects.get(entity).setY(Mappers.movements.get(entity).getPosition().y);
				
				if (movement.isStopped() && mapObject.getAnimationState() == MapObjectComponent.AnimationState.Walking)
				{
					mapObject.setAnimationState(MapObjectComponent.AnimationState.Idle);
				}

				if (!movement.isStopped()) 
				{
					if (movement.getMovement() == Movement.Horizontal)
					{
						if (movement.getVelocity().x > 0) {
							mapObject.setDirection(MapObjectComponent.Direction.Right);
						} else {
							mapObject.setDirection(MapObjectComponent.Direction.Left);
						}
					}
					else if (movement.getMovement() == Movement.Vertical)
					{
						if (movement.getVelocity().y > 0) 
						{
							mapObject.setDirection(MapObjectComponent.Direction.Down);
						} else {
							mapObject.setDirection(MapObjectComponent.Direction.Up);
						}
					}
				}
			}
		}
		renderer.render();
	}
}
