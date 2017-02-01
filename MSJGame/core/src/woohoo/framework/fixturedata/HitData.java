package woohoo.framework.fixturedata;

import woohoo.gameobjects.components.WeaponComponent;

public class HitData extends SensorData
{
    WeaponComponent weapon;
    
    public HitData(WeaponComponent w)
    {
        super(w);
        
        weapon = (WeaponComponent)w;
    }
}
