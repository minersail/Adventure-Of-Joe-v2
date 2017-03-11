// DEPRECATED

//package woohoo.gameobjects;
//
//import com.badlogic.ashley.core.Entity;
//import woohoo.framework.events.EventListeners;
//
//public abstract class BaseEntity extends Entity
//{
//	protected EventListeners<BaseEntity> listeners = new EventListeners<>();
//	protected float elapsedTime;
//	protected String name = "";
//	
//	public void update(float delta)
//	{
//		elapsedTime += delta;
//		
//		listeners.notifyAll(this);
//	}
//	
//	public EventListeners getListeners()
//	{
//		return listeners;
//	}
//	
//	public void setName(String n)
//	{
//		name = n;
//	}
//	
//	public String getName()
//	{
//		return name;
//	}
//}
