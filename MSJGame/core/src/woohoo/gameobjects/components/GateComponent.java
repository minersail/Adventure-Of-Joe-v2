package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.contactcommands.ContactData;

public class GateComponent implements Component
{
    private Vector2 playerOffset;
	
	public Vector2 position;
	public Vector2 size;
	public Vector2 playerPos; // Where the player will exit the gate
	public int destArea;
	public boolean triggered;

    public GateComponent(World world, Element gate)
    {        
		size = new Vector2(gate.getFloat("sizeX"), gate.getFloat("sizeY"));
        position = new Vector2(gate.getFloat("destX") + 0.5f, gate.getFloat("destY") + playerOffset.y + 0.5f);
		destArea = gate.getInt("destArea");
		
        // setPlayerOffset() must be called before playerPos can be used
        playerOffset = null;
		playerPos = null;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		// Make it so the XML refers to the top-left coordinate as opposed to center for box2D bodies
		bodyDef.position.set(gate.getFloat("locX") + gate.getFloat("sizeX") / 2, gate.getFloat("locY") + gate.getFloat("sizeY") / 2);

		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(gate.getFloat("sizeX") / 2 - 0.02f, gate.getFloat("sizeY") / 2 - 0.02f); // subtract 0.02f so slightly smaller than tile

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;

		body.createFixture(fixtureDef);
		body.setUserData(new ContactData());
    }

    public void setPlayerOffset(Vector2 offset)
    {
        // Took me forever to figure this out
        // Reposition the character relative to the gate's exit location based on where the player entered the gate's entrance
        playerOffset = new Vector2(Math.min(Math.max(0, offset.x), size.x - 1), Math.min(Math.max(0, offset.y), size.y - 1));
		playerPos = position.cpy().add(playerOffset);
    }
}
