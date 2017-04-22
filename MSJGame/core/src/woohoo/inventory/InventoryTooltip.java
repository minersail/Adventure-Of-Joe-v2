package woohoo.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

public class InventoryTooltip extends Window
{
	public InventoryTooltip(Skin skin)
	{
		super("", skin);
		super.setSize(300, 200);
		super.setMovable(false); // Will automatically move with item

		super.getTitleLabel().setFontScale(0.40f);

		Label description = super.add("", "text", "special").getActor(); // Adds blank label using font "text" and color "special" specified in the uiskin.json
		description.setFontScale(0.4f);
		description.setWrap(true);
		description.setAlignment(Align.left);
	}

	public void setTitle(String message)
	{
		getTitleLabel().setText(message);
	}

	public void setDescription(String message)
	{
		((Label)super.getCells().get(0).getActor()).setText(message);
		((Label)super.getCells().get(0).getActor()).setAlignment(Align.left);
	}
}
