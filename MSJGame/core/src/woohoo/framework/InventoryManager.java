package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
    Table table;

    public InventoryManager(Stage s, Texture image) 
    {
        ui = s;
        table = new Table();
        DragAndDrop dnd = new DragAndDrop();
        
        for (int i = 0; i < INVENTORY_WIDTH; i++)
        {
            for (int j = 0; j < INVENTORY_HEIGHT; j++)
            {
                final Image item = new Image(new TextureRegion(image));
                table.add(item).prefSize(ITEMX, ITEMY);
                
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
                
                dnd.addTarget(new Target(item) 
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
                        table.swapActor(payload.getDragActor(), getActor());
                    }
                });
            }
            table.row();
        }
        
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.center);
    }

    public void showScreen() 
    {
        ui.addActor(table);
        System.out.println(table.getChildren().size);
    }
}
