package woohoo.gameworld.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.loading.EntityMold;

/**
 * Class allowing an entity to spawn projectiles
 * Melee weapons are just long projectiles with a short range
 */
public class WeaponComponent implements Component
{    
	private EntityMold projectile;
	private final int projectileID; // New weapon must be created if weapon functionality changes
	
	public float cooldown;
	public float cooldownTimer;
	
	public boolean projectileSpawned;
		
	public WeaponComponent(int projID, float CD)
	{
		FileHandle handle = Gdx.files.local("data/projectiles.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());
		Element proj = root.getChild(projID);
        
		projectile = new EntityMold(proj);
		projectileID = projID;
		cooldown = CD;
		cooldownTimer = 0;
		projectileSpawned = false;
	}
	
	public EntityMold getProjectile()
	{
		return projectile;
	}
	
	public void spawnProjectile()
	{
		if (cooldownTimer == 0) // Spawn projectile if it weapon is off cooldown
			projectileSpawned = true;
	}
	
	public int getID()
	{
		return projectileID;
	}
}
