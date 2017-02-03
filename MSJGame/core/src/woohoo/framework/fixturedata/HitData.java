package woohoo.framework.fixturedata;

import woohoo.gameobjects.components.MapObjectComponent.Direction;
import woohoo.gameobjects.components.WeaponComponent;

public class HitData extends SensorData
{
    private WeaponComponent weapon;
    
    public HitData(WeaponComponent w)
    {
        super(w);
        
        weapon = (WeaponComponent)w;
    }
	
	public Direction getDirection()
	{
		return weapon.getDirection();
	}
    
    public float getDamage()
    {
        return weapon.getWeaponDamage();
    }
	
	public float getKnockback()
	{
		return weapon.getWeaponKnockback();
	}
}
