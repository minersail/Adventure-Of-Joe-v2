package woohoo.framework.fixturedata;

import woohoo.gameobjects.components.HitboxComponent;

public class HitboxData extends FixtureData
{    
    private HitboxComponent hitbox;
    
    public HitboxData(HitboxComponent hb) 
    {        
        hitbox = hb;
    }   
    
    public boolean isActive()
    {
        return hitbox.isActive();
    }
    
    public HitboxComponent getComponent()
    {
        return hitbox;
    }
}
