package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public interface PositionComponent extends Component
{
	public Vector2 getPosition();
}
