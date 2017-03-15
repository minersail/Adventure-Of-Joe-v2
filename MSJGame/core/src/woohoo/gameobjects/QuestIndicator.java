// DEPRECATED

//package woohoo.gameobjects;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import woohoo.gameobjects.components.MapObjectComponent;
//
//public class QuestIndicator extends BaseEntity
//{
//	private MapObjectComponent mapObject;
//	
//	public QuestIndicator(Texture texture)
//	{
//		mapObject = new MapObjectComponent(new TextureRegion(texture));
//		
//		super.add(mapObject);
//	}
//	
//	@Override
//	public void update(float delta)
//	{
//		super.update(delta);
//		
//		// Formula for opacity, such that y = (sin(2x) + 1) / 2
//		float opacity = ((float)Math.sin(3 * elapsedTime) + 1) / 2;
//		
//		mapObject.setColor(mapObject.getColor().set(1, 1, 1, opacity));
//	}
//	
//	public void setPosition(float x, float y)
//	{
//		mapObject.setX(x);
//		mapObject.setY(y);
//	}
//}