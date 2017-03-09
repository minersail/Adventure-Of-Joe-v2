package woohoo.gameworld;

import com.badlogic.ashley.core.ComponentMapper;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MapObjectComponent;

public class Mappers 
{
	public static ComponentMapper<MapObjectComponent> mapObjects = ComponentMapper.getFor(MapObjectComponent.class);
	public static ComponentMapper<MovementComponent> movements = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<HitboxComponent> hitboxes = ComponentMapper.getFor(HitboxComponent.class);
}
