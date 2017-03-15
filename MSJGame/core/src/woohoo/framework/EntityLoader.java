package woohoo.framework;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.security.InvalidParameterException;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.*;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameworld.Mappers;
import woohoo.screens.PlayingScreen;

public class EntityLoader
{
	PlayingScreen screen;
	
	public EntityLoader(PlayingScreen scr)
	{
		screen = scr;
		
		screen.getEngine().addEntityListener(Family.one(HitboxComponent.class, LOSComponent.class, WeaponComponent.class).get(), new EntityListener()
		{
			@Override
			public void entityAdded(Entity entity)
			{
				if (Mappers.hitboxes.has(entity))
				{
					Mappers.hitboxes.get(entity).mass.setUserData(new ContactData(Mappers.hitboxes.get(entity).hitboxType, entity));
				}
				
				if (Mappers.sightLines.has(entity))
				{
					Mappers.sightLines.get(entity).mass.setUserData(new ContactData(ContactType.SightLine, entity));
				}
				
				if (Mappers.weapons.has(entity))
				{
					Mappers.weapons.get(entity).mass.setUserData(new ContactData(ContactType.Weapon, entity));
				}
			}

			@Override
			public void entityRemoved(Entity entity)
			{
				if (Mappers.hitboxes.has(entity))
				{
					screen.getWorld().destroyBody(Mappers.hitboxes.get(entity).mass);
				}
				
				if (Mappers.sightLines.has(entity))
				{
					screen.getWorld().destroyBody(Mappers.sightLines.get(entity).mass);
				}
				
				if (Mappers.weapons.has(entity))
				{
					screen.getWorld().destroyBody(Mappers.weapons.get(entity).mass);
				}
			}
		});	
	}	
	
	public void loadPlayer()
	{
		Entity player = new Entity();
		AnimMapObjectComponent mapObject = new AnimMapObjectComponent(screen.getAssets().get("images/entities/youngjoe.pack", TextureAtlas.class));
		PositionComponent position = new PositionComponent(1, 5);
		IDComponent id = new IDComponent("player");
		InventoryComponent inventory = new InventoryComponent();
		EventListenerComponent eventListener = new EventListenerComponent();
		HitboxComponent hitbox = new HitboxComponent(screen.getWorld(), true, ContactType.Player);
		InputComponent input = new InputComponent();
		MovementComponent movement = new MovementComponent(2);
		PlayerComponent playerComp = new PlayerComponent();

		screen.getInventoryManager().fillInventory(inventory);
		hitbox.mass.setUserData(new ContactData(ContactType.Player, player));
		
		player.add(mapObject);
		player.add(position);
		player.add(id);
		player.add(inventory);
		player.add(eventListener);
		player.add(hitbox);
		player.add(input);
		player.add(movement);
		player.add(playerComp);
		
		screen.getEngine().addEntity(player);
	}
	
	public void loadEntities(int area)
	{
		FileHandle handle = Gdx.files.local("data/entities.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element entities = root.getChild(area);         
        
        for (Element e : entities.getChildrenByName("entity"))
        {
			if (!e.getBoolean("enabled")) continue;
			
			Entity entity = new Entity();
			
			for (int i = 0; i < e.getChildCount(); i++)
			{
				loadComponent(entity, e.getChild(i));
			}
			
			screen.getEngine().addEntity(entity);
		}	
	}
	
	private void loadComponent(Entity entity, Element component)
	{
		Component base;
		
		switch (component.getName())
		{
			case "ai":
				if (component.get("state").equals("stay"))
					base = new AIComponent("stay");
				else if (component.get("state").equals("random"))
					base = new AIComponent("random");
				else if (component.get("state").equals("follow"))
					base = new AIComponent(Mappers.positions.get(screen.getEngine().getEntity(component.get("target"))));
				else if (component.get("state").equals("moveto"))
					base = new AIComponent(new Vector2(component.getFloat("targetX"), component.getFloat("targetY")));
				else
					base = new AIComponent();
				break;
			case "anim":
				base = new AnimMapObjectComponent(screen.getAssets().get(component.get("atlas"), TextureAtlas.class));
				break;
			case "contact":
				base = new ContactComponent();
				break;
			case "dialogue":
				base = new DialogueComponent(component.getInt("id"), false);
				break;
			case "eventlistener":
				base = new EventListenerComponent();
				break;
			case "healthbar":
				base = new HealthBarComponent(screen.getAssets().get("healthbar.pack", TextureAtlas.class));
				break;
			case "health":
				base = new HealthComponent(component.getInt("max"));
				break;
			case "hitbox":
				base = new HitboxComponent(screen.getWorld(), component.getBoolean("collides"), ContactType.fromString(component.get("type")));
				break;
			case "id":
				base = new IDComponent(component.get("name"));
				break;
			case "input":
				base = new InputComponent();
				break;
			case "inventory":
				base = new InventoryComponent();
				break;
			case "itemdata":
				base = new ItemDataComponent(component.getChildByName("metadata").getAttributes());
				break;
			case "lineofsight":
				base = new LOSComponent(screen.getWorld());
				break;
			case "mapobject":
				base = new MapObjectComponent(new TextureRegion(screen.getAssets().get(component.get("texture"), Texture.class)));
				break;
			case "movement":
				base = new MovementComponent(component.getFloat("speed"));
				break;
			case "opacity":
				base = new OpacityComponent();
				break;
			case "player":
				base = new PlayerComponent();
				break;
			case "position":
				base = new PositionComponent(component.getFloat("x"), component.getFloat("y"));
				break;
			case "weapon":
				base = new WeaponComponent(screen.getWorld());
				break;
			default:
				throw new InvalidParameterException("No component with name " + component.getName() + " found.");
		}
		
		entity.add(base);
	}
}