package woohoo.gameworld;

import com.badlogic.ashley.core.ComponentMapper;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;

public class Mappers 
{
	public static ComponentMapper<MapObjectComponent> mapObjects = ComponentMapper.getFor(MapObjectComponent.class);
	public static ComponentMapper<MovementComponent> movements = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<PositionComponent> positions = ComponentMapper.getFor(PositionComponent.class);
}
