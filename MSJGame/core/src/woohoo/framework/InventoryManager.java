package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Align;

/*
Note: the UI uses the bottom-left coordinate system
*/
public class InventoryManager
{
    public final int INVENTORY_WIDTH = 8;
    public final int INVENTORY_HEIGHT = 8;
    public final int ITEMX = 64;
    public final int ITEMY = 64;
    
    Stage ui;
	Table background;
    Table items;

    public InventoryManager(Stage s, Texture image, Texture frame) 
    {
        ui = s;
        background = new Table();
		items = new Table();
        DragAndDrop dnd = new DragAndDrop();
		dnd.setDragActorPosition(30, -30);
        
        for (int i = 0; i < INVENTORY_WIDTH; i++)
        {
            for (int j = 0; j < INVENTORY_HEIGHT; j++)
            {
                final Image item = new Image(new TextureRegion(image));
				final Image slot = new Image(new TextureRegion(frame));
                background.add(slot).prefSize(ITEMX, ITEMY);
                items.add(item).prefSize(ITEMX, ITEMY);
				
				item.setUserObject(i * INVENTORY_HEIGHT + j); // Store the index of the slot or item in its user object
				slot.setUserObject(i * INVENTORY_HEIGHT + j);
                
                dnd.addSource(new Source(item)
                {
                    @Override
                    public Payload dragStart(InputEvent event, float x, float y, int pointer) 
                    {
                        Payload payload = new Payload();
                        payload.setDragActor(item);

                        return payload;
                    }
                });
                
                dnd.addTarget(new Target(slot) 
                {
                    @Override
                    public boolean drag(Source source, Payload payload, float x, float y, int pointer) 
                    {
                        return true;
                    }

                    @Override
                    public void reset(Source source, Payload payload) {}

                    @Override
                    public void drop(Source source, Payload payload, float x, float y, int pointer) 
                    {
						int draggedIndex = (int)payload.getDragActor().getUserObject();
						
						items.addActorAt(draggedIndex, payload.getDragActor());
                        items.swapActor(payload.getDragActor(), getActor());
						
						payload.getDragActor().setPosition(getActor().getX(), getActor().getY());
						getActor().setPosition(background.getCells().get(draggedIndex).getActorX(), background.getCells().get(draggedIndex).getActorY());
                    }
                });
            }
            background.row();
			items.row();
        }
        
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.align(Align.center);
        items.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        items.align(Align.center);
    }

    public void showScreen() 
    {
        ui.addActor(background);
		ui.addActor(items);
    }
}
