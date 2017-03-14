package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

public class HealthBarComponent extends TextureMapObject implements Component 
{
    private float max;
    private float health;
    private TextureRegion green;
    private TextureRegion red;
    private boolean invulnerable;
    
    public HealthBarComponent(int maxHealth)
    {
        health = max = maxHealth;
        
        // Maybe use texturemapobject's setTexture to set border
    }
    
    public HealthBarComponent initializeHealthBar(TextureAtlas healthBar)
    {
        green = new TextureRegion(healthBar.findRegion("green"));
        red = new TextureRegion(healthBar.findRegion("red"));
		return this;
    }
	
    public void update(float delta, Vector2 newPosition)
    {   
        setX(newPosition.x);
        setY(newPosition.y);
    }
	
	public void changeMax(float maxHealth)
	{
        health = max = maxHealth;
	}
    
    public void setInvulnerable(boolean invulnerable)
    {
        this.invulnerable = invulnerable;
    }
    
    public void damage(float damage)
    {
        if (invulnerable) return;
        
        health = Math.max(0, health - damage);
    }
    
    public void draw(Batch batch)
    {
        if (invulnerable) return;
        
        batch.draw(red, getX(), getY(), 1, 0.125f);
        batch.draw(green, getX(), getY(), health / max, 0.125f);
    }
}
