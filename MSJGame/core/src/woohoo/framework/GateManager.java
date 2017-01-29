package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import java.util.function.Consumer;
import woohoo.framework.contactcommands.GateContact;
import woohoo.screens.PlayingScreen;
import woohoo.screens.PlayingScreen.WBodyType;

/*
Creates the sensors that allow the player to move between game areas
*/
public class GateManager 
{
    private boolean switchArea;
    private GateData nextGate; // When the area is switched, this represents data for the gate that was triggered
            
    private final PlayingScreen screen;
    
    public GateManager(final PlayingScreen scr)
    {
        screen = scr;
        
        scr.getContactManager().addCommand(new GateContact(this), scr.getWorld());
    }
    
    /*
    Creates all gates for a single area
    */
    public void createGates(World world, int area)
    {        
        FileHandle handle = Gdx.files.internal("data/gates.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());       
        XmlReader.Element gates = root.getChild(area);  
        
        for (XmlReader.Element gate : gates.getChildrenByName("gate"))
        {
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

            body.createFixture(fixtureDef).setUserData(new GateData(gate));
            body.setUserData(WBodyType.Gate);  
        }
    }
    
    /*
    Checks if a game area switch has been triggered, and switches areas if it has.
    */
    public void updateArea()
	{ 
        if (!switchArea) return; // Boolean check instead of instantaneous call is necessary so
                                // that the area switching does not take place during world.step();
                                        
        Array<Body> bodies = new Array<>();
		screen.getWorld().getBodies(bodies);
		
		for (Body body: bodies)
		{
			if (body.getUserData().equals(WBodyType.Wall) || body.getUserData().equals(WBodyType.Gate))
				screen.getWorld().destroyBody(body);
		}
		
		final TiledMap map = screen.getMapLoader().load("maps/" + nextGate.destArea() + ".txt", (Texture)screen.getAssets().get("images/tileset.png"), 
                                                  (Texture)screen.getAssets().get("images/tileset2.png"), screen.getWorld());		
		screen.getRenderer().getMap().getLayers().get("Objects").getObjects().forEach(new Consumer<MapObject>()
		{
			@Override
			public void accept(MapObject obj)
			{
				map.getLayers().get("Objects").getObjects().add(obj);
			}			
		});		
		screen.getRenderer().setMap(map);
        
        screen.getEngine().getPlayer().setPosition(nextGate.playerPos().x, nextGate.playerPos().y);
        createGates(screen.getWorld(), nextGate.destArea());
		
		screen.getRenderer().skipFrame();
        
        switchArea = false;
	}
	
	public void setNextGate(GateData gate)
	{
		nextGate = gate;
	}
	
	public GateData getNextGate()
	{
		return nextGate;
	}
	
	public void triggerAreaSwitch()
	{
		switchArea = true;
	}
    
    public class GateData
    {
        private final XmlReader.Element gate;
		private Vector2 playerOffset;
        
        public GateData(XmlReader.Element g)
        {
            gate = g;
			playerOffset = new Vector2(0, 0);
        }
		
		public void setPlayerOffset(float x, float y)
		{
			// Took me forever to figure this out
			// Reposition the character relative to the gate's exit location based on where the player entered the gate's entrance
			playerOffset = new Vector2(Math.min(Math.max(0, x), gateSize().x - 1), Math.min(Math.max(0, y), gateSize().y - 1));
		}
        
        public Vector2 gatePos()
        {
			// Return center of top-left block
            return new Vector2(gate.getFloat("locX") + 0.5f, gate.getFloat("locY") + 0.5f);
        }
		
		public Vector2 gateSize()
		{
			return new Vector2(gate.getFloat("sizeX"), gate.getFloat("sizeY"));
		}
        
        public Vector2 playerPos()
        {
            return new Vector2(gate.getFloat("destX") + playerOffset.x, gate.getFloat("destY") + playerOffset.y);
        }
        
        public int destArea()
        {
            return gate.getInt("destArea");
        }
    }
}
