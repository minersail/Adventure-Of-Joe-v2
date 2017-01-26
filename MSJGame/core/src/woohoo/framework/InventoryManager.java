package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import woohoo.gameobjects.Item;
import woohoo.gameobjects.Character;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.screens.PlayingScreen;

/*
Note: the UI uses the bottom-left coordinate system
*/
public class InventoryManager
{
    public final int INVENTORY_WIDTH = 8;
    public final int INVENTORY_HEIGHT = 8;
    public final int ITEMX = 72;
    public final int ITEMY = 72;
    
    private TextureRegion slotBackground;
    private PlayingScreen screen;
	private Table table;

    public InventoryManager(PlayingScreen scr, Texture frame, Skin skin) 
    {
        screen = scr;
        slotBackground = new TextureRegion(frame);
        table = new Table();
                
        TextButton closeButton = new TextButton("x", skin);
        table.add(closeButton).prefSize(ITEMX, ITEMY);   
        table.row();
        
        closeButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    closeInventory();
                }
            }        
        );
    }

    public void showInventory() 
    {
        screen.getUI().addActor(table);
        screen.getUI().getActors().add(table);
        screen.setState(PlayingScreen.GameState.Inventory);
    }
	
	public void closeInventory()
	{
		screen.getUI().getActors().removeValue(table, false);
        screen.setState(PlayingScreen.GameState.Playing);
	}
    
    public void loadInventory(Character character)
    {
        InventoryComponent inventory = character.getComponent(InventoryComponent.class);
        
        FileHandle handle = Gdx.files.internal("data/inventory.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());    
        
        for (XmlReader.Element e : root.getChildrenByName("item"))
        {
            inventory.addItem(new Item(screen.getIDManager().getItem(e.getInt("id")).getItem(), screen.getWorld()));
        }
        
        DragAndDrop dnd = new DragAndDrop();
        dnd.setDragActorPosition(30, -30);

        for (int i = 0; i < INVENTORY_WIDTH; i++) 
        {
            for (int j = 0; j < INVENTORY_HEIGHT; j++)
            {
                InventorySlot slot;
                int index = i * INVENTORY_HEIGHT + j;

                if (index < inventory.getItems().size()) 
                {
                    slot = new InventorySlot(slotBackground, inventory.getItems().get(index).getComponent(MapObjectComponent.class).getTextureRegion());
                } 
                else 
                {
                    slot = new InventorySlot(slotBackground, slotBackground);
                }
                table.add(slot).prefSize(ITEMX, ITEMY);

                dnd.addSource(new Source(slot) 
                {
                    @Override
                    public Payload dragStart(InputEvent event, float x, float y, int pointer)
                    {
                        InventorySlot slot = (InventorySlot)getActor();
                        slot.setDragged(true);

                        Payload payload = new Payload();
                        payload.setDragActor(slot.getItem());

                        return payload;
                    }

                    @Override
                    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) 
                    {
                        ((InventorySlot) getActor()).setDragged(false);
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
                        InventorySlot sourceSlot = (InventorySlot)source.getActor();
                        InventorySlot targetSlot = (InventorySlot)getActor();

                        Image sourceItem = sourceSlot.getItem();
                        Image targetItem = targetSlot.getItem();

                        sourceSlot.setItem(targetItem);
                        targetSlot.setItem(sourceItem);

                        sourceSlot.setDragged(false);
                    }
                });
            }
            table.row();
        }

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.center);
    }
    
    public class InventorySlot extends Image
    {
        private Image item;
        private boolean dragged;
        
        public InventorySlot(TextureRegion background, TextureRegion itemSprite)
        {
            super(background);
            item = new Image(itemSprite);
            item.setSize(64, 64);
        }
        
        public Image getItem()
        {
            return item;
        }
        
        public void setItem(Image image)
        {
            item = image;
        }
        
        public void setDragged(boolean drag)
        {
            dragged = drag;
        }
        
        @Override
        public void draw(Batch batch, float parentAlpha)
        {
            super.draw(batch, parentAlpha);            
            item.draw(batch, parentAlpha);
            
            if (!dragged)
            {            
                item.setPosition(getX() + 4, getY() + 4);   
            }
        }
    }
}
