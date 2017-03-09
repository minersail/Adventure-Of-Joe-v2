package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public interface MovementComponent extends Component
{
	
	public boolean isStopped();
	public Vector2 getVelocity();
	public Movement getMovement();
	public void addVelocity(float x, float y);
	public void setVelocity(float x, float y);
	public void setMovement(Movement move);
}
