package woohoo.framework.fixturedata;

import woohoo.gameobjects.components.SensorComponent;

public class SensorData extends FixtureData
{    
    private SensorComponent sensor;
    
    public SensorData(SensorComponent s) 
    {        
        sensor = s;
    }   
    
    public boolean isActive()
    {
        return sensor.isActive();
    }
    
    public SensorComponent getComponent()
    {
        return sensor;
    }
}
