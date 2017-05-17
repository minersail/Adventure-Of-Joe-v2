package woohoo.framework.loading;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.gameworld.components.ContactComponent.ContactType;
import woohoo.gameworld.components.ContactComponent.Faction;

public class HitboxMold 
{
	private boolean enableCollision; // Whether this hitbox will have collision
	private boolean startsStatic; // Whether or not this hitbox will start immovable (with no AI this setting is permanent)
	private ContactType type; // ContactType for this hitbox (extracted later automatically to add ContactData to this hitbox's mass)
	private Faction faction; // Faction for this hitbox (extracted later automatically to add ContactData to this hitbox's mass)
	private Shape shape; //Shape of the hitbox
	
	public HitboxMold(Element component)
	{
		enableCollision = component.getBoolean("collides", true);
		startsStatic = component.getBoolean("static", false);
		type = ContactType.fromString(component.get("type"));
		faction = Faction.fromString(component.get("faction", "neutral"));
		shape = makeShape(component.get("shape", "circle"));
	}
	
	public HitboxMold(boolean collision, boolean isStatic, ContactType contType, Faction fac, String shapeStr)
	{
		enableCollision = collision;
		startsStatic = isStatic;
		type = contType;
		faction = fac;
		shape = makeShape(shapeStr);
	}
	
	public boolean getColliding()
	{
		return enableCollision;
	}
	
	public boolean getStatic()
	{
		return startsStatic;
	}
	
	public ContactType getType()
	{
		return type;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	private Shape makeShape(String str)
	{
		switch(str)
		{			
			case "melee":
			{
				PolygonShape s = new PolygonShape(); // arrowhead shape
				float x = 0.2f; // width of the arrowhead
				float y = x / (float)Math.sqrt(2);
				Vector2[] vertices =
				{
					new Vector2(0, 0), new Vector2(0.5f, -0.5f), new Vector2(0.5f - y, -0.5f - y),
					new Vector2(- 2 * y, 0), new Vector2(0.5f - y, 0.5f + y), new Vector2(0.5f, 0.5f)
				};
				s.set(vertices);
				return s;
			}
            case "arrow":
			{
				PolygonShape s = new PolygonShape(); // arrowhead shape
				float x = 0.1f; // width of the arrowhead
				float y = x / 2;
				Vector2[] vertices =
				{
					new Vector2(0.5f - y, 0 + y), new Vector2(0.5f, 0), new Vector2(0.5f - y, 0 - y),
                    new Vector2(-(0.5f - y), -(0 + y)), new Vector2(-0.5f, 0), new Vector2(-(0.5f - y), -(0 - y))
				};
				s.set(vertices);
				return s;
			}
			case "square":
			{				
				PolygonShape s = new PolygonShape();
				s.setAsBox(0.49f, 0.49f);
				return s;
			}
			case "smallcircle":
			{
				CircleShape s = new CircleShape();
				s.setRadius(0.25f);
				return s;
			}
			case "circle":
			default:
			{
				CircleShape s = new CircleShape();
				s.setRadius(0.49f);
				return s;
			}
		}
	}
}
