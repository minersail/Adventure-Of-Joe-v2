package woohoo.gameworld;

import woohoo.gameworld.components.InputComponent;
import woohoo.gameworld.components.AnimMapObjectComponent;
import woohoo.gameworld.components.InventoryComponent;
import woohoo.gameworld.components.OpacityComponent;
import woohoo.gameworld.components.DialogueComponent;
import woohoo.gameworld.components.HealthBarComponent;
import woohoo.gameworld.components.IDComponent;
import woohoo.gameworld.components.SpawnComponent;
import woohoo.gameworld.components.HealthComponent;
import woohoo.gameworld.components.HitboxComponent;
import woohoo.gameworld.components.EventListenerComponent;
import woohoo.gameworld.components.MapObjectComponent;
import woohoo.gameworld.components.LOSComponent;
import woohoo.gameworld.components.MovementComponent;
import woohoo.gameworld.components.GateComponent;
import woohoo.gameworld.components.ContactComponent;
import woohoo.gameworld.components.AIComponent;
import woohoo.gameworld.components.WeaponComponent;
import woohoo.gameworld.components.PlayerComponent;
import woohoo.gameworld.components.ChaseComponent;
import woohoo.gameworld.components.PositionComponent;
import woohoo.gameworld.components.ProjectileComponent;
import woohoo.gameworld.components.ItemDataComponent;
import com.badlogic.ashley.core.ComponentMapper;

public class Mappers 
{
	public static ComponentMapper<EventListenerComponent> eventListeners = ComponentMapper.getFor(EventListenerComponent.class);
	public static ComponentMapper<AnimMapObjectComponent> animMapObjects = ComponentMapper.getFor(AnimMapObjectComponent.class);
	public static ComponentMapper<ProjectileComponent> projectiles = ComponentMapper.getFor(ProjectileComponent.class);
	public static ComponentMapper<InventoryComponent> inventories = ComponentMapper.getFor(InventoryComponent.class);
	public static ComponentMapper<MapObjectComponent> mapObjects = ComponentMapper.getFor(MapObjectComponent.class);
	public static ComponentMapper<HealthBarComponent> healthBars = ComponentMapper.getFor(HealthBarComponent.class);
	public static ComponentMapper<PositionComponent> positions = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<MovementComponent> movements = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<DialogueComponent> dialogues = ComponentMapper.getFor(DialogueComponent.class);
	public static ComponentMapper<OpacityComponent> opacities = ComponentMapper.getFor(OpacityComponent.class);
	public static ComponentMapper<ContactComponent> contacts = ComponentMapper.getFor(ContactComponent.class);
	public static ComponentMapper<ItemDataComponent> items = ComponentMapper.getFor(ItemDataComponent.class);
	public static ComponentMapper<HitboxComponent> hitboxes = ComponentMapper.getFor(HitboxComponent.class);
	public static ComponentMapper<WeaponComponent> weapons = ComponentMapper.getFor(WeaponComponent.class);
	public static ComponentMapper<PlayerComponent> players = ComponentMapper.getFor(PlayerComponent.class);
	public static ComponentMapper<HealthComponent> lives = ComponentMapper.getFor(HealthComponent.class);
	public static ComponentMapper<ChaseComponent> chasers = ComponentMapper.getFor(ChaseComponent.class);
	public static ComponentMapper<LOSComponent> sightLines = ComponentMapper.getFor(LOSComponent.class);
	public static ComponentMapper<SpawnComponent> spawns = ComponentMapper.getFor(SpawnComponent.class);
	public static ComponentMapper<InputComponent> inputs = ComponentMapper.getFor(InputComponent.class);
	public static ComponentMapper<GateComponent> gates = ComponentMapper.getFor(GateComponent.class);
	public static ComponentMapper<IDComponent> ids = ComponentMapper.getFor(IDComponent.class);
	public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
}
