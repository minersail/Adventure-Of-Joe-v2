// DEPRECATED


//package woohoo.framework;
//
//import com.badlogic.gdx.physics.box2d.Contact;
//import com.badlogic.gdx.physics.box2d.ContactImpulse;
//import com.badlogic.gdx.physics.box2d.ContactListener;
//import com.badlogic.gdx.physics.box2d.Manifold;
//import com.badlogic.gdx.physics.box2d.World;
//import java.util.ArrayList;
//import woohoo.gameobjects.components.HitboxComponent;
//import woohoo.gameobjects.components.HitboxComponent.ContactType;
//
//public class ContactManager
//{
//	private ArrayList<ContactCheck> checks;
//	
//	public ContactManager()
//	{
//		checks = new ArrayList<>();
//	}
//	
//	public void addCommand(ContactCheck command, World world)
//	{
//		checks.add(command);
//		updateContactListener(world);
//	}
//	
//	private void updateContactListener(World world)
//	{
//		world.setContactListener(new ContactListener() 
//		{
//			@Override
//			public void beginContact(Contact contact)
//			{
//				for (ContactCheck command : checks)
//				{
//					command.detectStart(contact);
//				}
//			}
//
//			@Override
//			public void endContact(Contact contact)
//			{
//				for (ContactCheck command : commands)
//				{
//					command.detectEnd(contact);
//				}			
//			}
//
//			@Override
//			public void preSolve(Contact contact, Manifold oldManifold) {}
//
//			@Override
//			public void postSolve(Contact contact, ContactImpulse impulse) {}
//		});
//	}
//	
//	/**
//	 * Since only one ContactListener can be active at the same time, all the
//	 * code for contacts has to be placed in the same ContactListener
//	 *
//	 * This class will check for collisions, then flag the collisions in
//	 * ContactData
//	 *
//	 * @author jordan
//	 */
//	public class ContactCheck {
//
//		public ContactType type1;
//		public ContactType type2;
//
//		public ContactCheck(ContactType t1, ContactType t2) {
//			type1 = t1;
//			type2 = t2;
//		}
//
//		public boolean detectStart(Contact contact) 
//		{
//			HitboxComponent.ContactData data1 = (HitboxComponent.ContactData) contact.getFixtureA().getBody().getUserData();
//			HitboxComponent.ContactData data2 = (HitboxComponent.ContactData) contact.getFixtureB().getBody().getUserData();
//
//			return (data1.type == type1 && data2.type == type2 || data1.type == type2 && data2.type == type1);
//		}
//
//		public boolean detectEnd(Contact contact) 
//		{
//			HitboxComponent.ContactData data1 = (HitboxComponent.ContactData) contact.getFixtureA().getBody().getUserData();
//			HitboxComponent.ContactData data2 = (HitboxComponent.ContactData) contact.getFixtureB().getBody().getUserData();
//
//			return (data1.type == type1 && data2.type == type2 || data1.type == type2 && data2.type == type1);
//		}
//	}
//}
