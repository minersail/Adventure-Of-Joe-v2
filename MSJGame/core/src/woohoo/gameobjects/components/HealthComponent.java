package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component
{
	public float maxHealth;
	public float currentHealth;
	public boolean invulnerable;
	public boolean dead;
	
	private float incomingDamage; // Running total of all damage entity has taken this game tick
	
	public HealthComponent()
	{
		this(10);
	}	
	
	public HealthComponent(float max)
	{
		currentHealth = maxHealth = max;
		invulnerable = false;
	}	
    
    public void damage(float damage)
    {
        if (!invulnerable)
			incomingDamage += damage;
    }
	
	public float getIncomingDamage()
	{
		return incomingDamage;
	}
	
	public void resetDamage()
	{
		incomingDamage = 0;
	}
}
