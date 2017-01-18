package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
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
        
        scr.getWorld().setContactListener(
            new ContactListener() 
            {
                @Override
                public void beginContact(Contact contact) {
                    Fixture fA = contact.getFixtureA();
                    Fixture fB = contact.getFixtureB();
                    
                    if ((fA.getBody().getUserData() == WBodyType.Gate && fB.getBody().getUserData() == WBodyType.Player))
                    {
                        nextGate = (GateData)fA.getUserData();
                        switchArea = true;
                    }
                    else if (fB.getBody().getUserData() == WBodyType.Gate && fA.getBody().getUserData() == WBodyType.Player)
                    {
                        nextGate = (GateData)fB.getUserData();
                        switchArea = true;
                    }
                }

                @Override
                public void endContact(Contact contact){}

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {}

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {}
            }
        );
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
            bodyDef.position.set(gate.getInt("locX") + 0.5f, gate.getInt("locY") + 0.5f);

            Body body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.48f, 0.48f); // Slightly less than 0.5f

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
		
		TiledMap map = new TiledMap();
		
		MapLayers layers = screen.getMapLoader().load("maps/" + nextGate.destArea() + ".txt", (Texture)screen.getAssets().get("images/tileset.png"), 
                                                      (Texture)screen.getAssets().get("images/tileset2.png"), screen.getWorld());			
		MapLayer objects = screen.getRenderer().getMap().getLayers().get("Objects");
		
        map.getLayers().add(layers.get("Base"));
		map.getLayers().add(objects);
        map.getLayers().add(layers.get("Decorations"));		
		screen.getRenderer().setMap(map);
        
        screen.getEngine().getPlayer().setPosition((int)nextGate.playerPos().x, (int)nextGate.playerPos().y);
        createGates(screen.getWorld(), nextGate.destArea());
        
        switchArea = false;
	}
    
    public class GateData
    {
        private final XmlReader.Element gate;
        
        public GateData(XmlReader.Element g)
        {
            gate = g;
        }
        
        public Vector2 gatePos()
        {
            return new Vector2(gate.getInt("locX"), gate.getInt("locy"));
        }
        
        public Vector2 playerPos()
        {
            return new Vector2(gate.getInt("destX"), gate.getInt("destY"));
        }
        
        public int destArea()
        {
            return gate.getInt("destArea");
        }
    }
}
