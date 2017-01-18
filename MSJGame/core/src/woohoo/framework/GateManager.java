package woohoo.framework;

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
import woohoo.screens.PlayingScreen;
import woohoo.screens.PlayingScreen.WBodyType;

/*
Creates the sensors that allow the player to move between game areas
*/
public class GateManager 
{
    public GateManager(final PlayingScreen scr, World world)
    {
        world.setContactListener(
            new ContactListener() 
            {
                @Override
                public void beginContact(Contact contact) {
                    Fixture fA = contact.getFixtureA();
                    Fixture fB = contact.getFixtureB();
                    
                    if ((fA.getBody().getUserData() == WBodyType.Gate && fB.getBody().getUserData() == WBodyType.Player))
                    {
                        scr.switchScreens((int)fA.getUserData());
                    }
                    else if (fB.getBody().getUserData() == WBodyType.Gate && fA.getBody().getUserData() == WBodyType.Player)
                    {
                        scr.switchScreens((int)fB.getUserData());
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
    
    public void createGate(World world, Vector2 position, int targetArea)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);

        Body body = world.createBody(bodyDef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(targetArea);
        body.setUserData(WBodyType.Gate);        
    }
}
