package woohoo.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.io.IOException;
import woohoo.gameobjects.components.CollisionComponent;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameworld.GameWorld;

public class NPC extends BaseEntity
{    
	private MapObjectComponent mapObject;
	private CollisionComponent collision;
    private DialogueComponent dialogue;
    
    public NPC(Texture texture, int sizeX, int sizeY, World world, GameWorld engine)
    {
		mapObject = new MapObjectComponent(new TextureRegion(texture), sizeX, sizeY);
		collision = new CollisionComponent(world);
        dialogue = new DialogueComponent(0);
		
		collision.setPosition(new Vector2(8, 8));
		
		super.add(mapObject);
        super.add(collision);
        super.add(dialogue);
	}    
	
	@Override
	public void update(float delta)
	{
		collision.update(delta);
		mapObject.update(delta, new Vector2(collision.getPosition().x - 0.5f, collision.getPosition().y - 0.5f));
	}
    
    public Vector2 getPosition()
    {
        return new Vector2(mapObject.getX(), mapObject.getY());
    }
}
