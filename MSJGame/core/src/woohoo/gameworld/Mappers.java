package woohoo.gameworld;

import com.badlogic.ashley.core.ComponentMapper;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.EventListenerComponent;
import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.IDComponent;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;

public class Mappers 
{
	public static ComponentMapper<EventListenerComponent> eventListeners = ComponentMapper.getFor(EventListenerComponent.class);
	public static ComponentMapper<AnimMapObjectComponent> animMapObjects = ComponentMapper.getFor(AnimMapObjectComponent.class);
	public static ComponentMapper<MapObjectComponent> mapObjects = ComponentMapper.getFor(MapObjectComponent.class);
	public static ComponentMapper<PositionComponent> positions = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<MovementComponent> movements = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<ItemDataComponent> items = ComponentMapper.getFor(ItemDataComponent.class);
	public static ComponentMapper<HitboxComponent> hitboxes = ComponentMapper.getFor(HitboxComponent.class);
	public static ComponentMapper<GateComponent> gates = ComponentMapper.getFor(GateComponent.class);
	public static ComponentMapper<IDComponent> ids = ComponentMapper.getFor(IDComponent.class);
	public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
}
