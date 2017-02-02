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
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.screens.PlayingScreen;

/*
Note: the UI uses the bottom-left coordinate system
*/
public class InventoryManager
{
    public final int INVENTORY_WIDTH = 5;
    public final int INVENTORY_HEIGHT = 8;
    public final int ITEMX = 72;
    public final int ITEMY = 72;
	
	public final int PADDING_LEFT = 100;
	// I have no idea why the -6 needs to be there but it does
	public final int INVENTORY_BOTTOM = -6 + (Gdx.graphics.getHeight() - ((INVENTORY_HEIGHT + 1) * ITEMY)) / 2;
	public final int INVENTORY_TOP = INVENTORY_BOTTOM + (INVENTORY_HEIGHT * ITEMY);
	public final int INVENTORY_LEFT = PADDING_LEFT;
	public final int INVENTORY_RIGHT = INVENTORY_LEFT + (INVENTORY_WIDTH * ITEMX);
	
	public final int DRAG_OFFSET_X = 32;
	public final int DRAG_OFFSET_Y = -32;
	
	private final TextureRegion slotBackground;
	private final TextureRegion blankItem;
    
	private InventoryComponent currentInventory;
    private PlayingScreen screen;
	private Table table;

	/*
	Initializes the UI with blank items, to be later filled with fillInventory()
	*/
    public InventoryManager(PlayingScreen scr, Texture frame, Texture blank, Skin skin) 
    {
        screen = scr;
        table = new Table();
		
        slotBackground = new TextureRegion(frame);
		blankItem = new TextureRegion(blank);
                
        TextButton closeButton = new TextButton("x", skin);
        table.debugTable();
        InventorySlot weaponSlot = new InventorySlot(slotBackground, blankItem);
        table.add(closeButton).prefSize(ITEMX, ITEMY);   
        table.add(weaponSlot).prefSize(ITEMX, ITEMY);
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
        
        DragAndDrop dnd = new DragAndDrop();
        
        dnd.addSource(new InventorySource(weaponSlot));
        dnd.addTarget(new InventoryTarget(weaponSlot)
        {
            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) 
            {
                if (((InventorySlot)source.getActor()).getItem().isWeapon())
                {
                    super.drop(source, payload, x, y, pointer);
                    screen.getEngine().getPlayer().equip(((InventorySlot)source.getActor()).getItem());
					screen.getContactManager().addCommand(screen.getEngine().getPlayer().getComponent(WeaponComponent.class).getContactData(), screen.getWorld());
                }
                else
                {
                    // figure out why it's being dropped
                    // look into invalid target maybe
                }
            }
        });
        
        dnd.setDragActorPosition(DRAG_OFFSET_X, DRAG_OFFSET_Y);
		dnd.setDragTime(0);

        for (int i = 0; i < INVENTORY_HEIGHT; i++) 
        {
            for (int j = 0; j < INVENTORY_WIDTH; j++)
            {
                InventorySlot slot = new InventorySlot(slotBackground, blankItem);
                table.add(slot).prefSize(ITEMX, ITEMY);

                dnd.addSource(new InventorySource(slot));
                dnd.addTarget(new InventoryTarget(slot));
            }
            table.row();
        }

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.left);
		table.padLeft(PADDING_LEFT);
    }

    public void showInventory() 
    {
        screen.getUI().addActor(table);
        screen.setState(PlayingScreen.GameState.Inventory);
    }
	
	public void closeInventory()
	{
		table.remove();
        screen.setState(PlayingScreen.GameState.Playing);
	}
    
	/*
	Function to load InventoryComponents from XML
	*/
    public void loadInventory(Character character)
    {
        InventoryComponent inventory = character.getComponent(InventoryComponent.class);
        
        FileHandle handle = Gdx.files.internal("data/inventory.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());    
        
        for (XmlReader.Element e : root.getChildrenByName("item"))
        {	
            Item item;
            if (e.getChildByName("metadata") == null)
            {   // Absoutely disgusting method chain
                item = new Item(screen.getIDManager().getItem(e.getInt("id")).getItem());
            }
            else
            {   
                item = new Item(screen.getIDManager().getItem(e.getInt("id")).getItem(), e.getChildByName("metadata"));
            } 
            inventory.addItem(item);
        }
	}
	
	/*
	Function to fill the inventory UI with a character's inventory
	*/
	public void fillInventory(Character character)
	{
		currentInventory = character.getComponent(InventoryComponent.class);
		
		// Starts at 2 since first two items are "X" button and item slot
		for (int i = 2; i < table.getCells().size; i++)
		{		
			if (i >= currentInventory.getItems().size() + 2) break;
			
			Item item = currentInventory.getItems().get(i - 2); // i - 2 because the inventory starts at 0
			item.flipImage(); // When MapObjectComponent is initialized textures are flipped by default
			TextureRegion region = new TextureRegion(item.getComponent(MapObjectComponent.class).getTextureRegion());
			Image image = new Image(region);
			image.setSize(ITEMX - 8, ITEMY - 8);
			
			((InventorySlot)table.getCells().get(i).getActor()).setImage(image).setItem(item).setCount(1);
		}
	}
	
	/*
	Function to add a single item into both the inventory of the character passed in and the UI
	If the item only needs to be added to the character, instead use only InventoryComponent.addItem()
	
	Can only be called after fillInventory() has been called at least once
	*/
	public void addItem(Character character, Item item)
	{
		currentInventory = character.getComponent(InventoryComponent.class);
		currentInventory.addItem(item);
		
		// Starts at 2 since first two items are "X" button and item slot
		for (int i = 2; i < table.getCells().size; i++)
		{		
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			
			if (slot.getCount() == 0)
			{
				item.flipImage(); // UI is y-up while game world is y-down
				Image image = new Image(item.getComponent(MapObjectComponent.class).getTextureRegion());
				slot.setImage(image).setItem(item).setCount(1);
				return;
			}
		}
		
		// If the code reaches here the inventory is full
	}
	
	/*
	Removes an item from both the UI and the current character's inventory
	(as set by either addItem() or fillInventory())
	and drops it into the game world
	*/
	public void dropItem(Item item)
	{		
		// Starts at 2 since first two items are "X" button and item slot
		for (int i = 2; i < table.getCells().size; i++)
		{
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			
			if (slot.getItem() != null && slot.getItem().equals(item))
			{
				slot.setItem(null).setImage(new Image(blankItem)).setCount(0);

				currentInventory.removeItem(item);
				screen.addEntity(item);

				item.setPosition(screen.getEngine().getPlayer().getPosition().x, screen.getEngine().getPlayer().getPosition().y);
				item.update(0);
				item.flipImage(); // Because the world is in y-down and the UI is in y-up
				return;
			}
		}
	}
	
	/*
	Stores the image data for a single slot in the inventory UI
	
	Setters return this item for method chaining
	*/
    public class InventorySlot extends Image
    {
		private Item item; // Entity, starts as null
        private Image itemImage; // Scene2D actor used for payload
        private boolean dragged;
		private int count;
        
        public InventorySlot(TextureRegion background, TextureRegion itemSprite)
        {
            super(background);
            itemImage = new Image(itemSprite);
            itemImage.setSize(64, 64);
        }
        
        public Image getImage()
        {
            return itemImage;
        }
		
		public Item getItem()
		{
			return item;
		}
        
        public InventorySlot setImage(Image image)
        {
            itemImage = image;			
            itemImage.setSize(64, 64);
			return this;
        }
		
		public InventorySlot setItem(Item it)
		{
			item = it;
			return this;
		}
        
        public InventorySlot setDragged(boolean drag)
        {
            dragged = drag;			
			return this;
        }
		
		public InventorySlot setCount(int newCount)
		{
			count = newCount;
			return this;
		}
		
		public int getCount()
		{
			return count;
		}
        
        @Override
        public void draw(Batch batch, float parentAlpha)
        {
            super.draw(batch, parentAlpha);            
            itemImage.draw(batch, parentAlpha);
            
            if (!dragged)
            {            
                itemImage.setPosition(getX() + 4, getY() + 4);   
            }
        }
    }
    
    /*
    Could be a lambda but looks cleaner in its own class
    */
    private class InventorySource extends Source
    {
        public InventorySource(InventorySlot slot)
        {
            super(slot);
        }

        @Override
        public Payload dragStart(InputEvent event, float x, float y, int pointer)
        {
            InventorySlot slot = (InventorySlot)getActor();
            // Can't drag empty frame
            if (slot.getCount() == 0) return null;

            slot.setDragged(true);

            Payload payload = new Payload();
            payload.setDragActor(slot.getImage());

            return payload;
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) 
        {
            float X = payload.getDragActor().getX() + DRAG_OFFSET_X;
            float Y = payload.getDragActor().getY() - DRAG_OFFSET_Y; // I have no idea why this formula works but it does

            /*
            Right now assumes that the drop occurs out of player inventory, change later
            */
            if (X < INVENTORY_LEFT || X > INVENTORY_RIGHT ||
                Y < INVENTORY_BOTTOM || Y > INVENTORY_TOP)
            {
                // Remove the item from the inventory and add it to the world
                Item dropped = ((InventorySlot)getActor()).getItem();

                dropItem(dropped);
            }

            ((InventorySlot)getActor()).setDragged(false);						
        }
    }
    
    private class InventoryTarget extends Target
    {
        public InventoryTarget(InventorySlot slot)
        {
            super(slot);
        }
        
        @Override
        public boolean drag(Source source, Payload payload, float x, float y, int pointer) 
        {
            return true;
        }

        @Override
        public void reset(Source source, Payload payload) {}

        /*
		Switch the images, items, item counts, etc. of the two inventory slots
        */
        @Override
        public void drop(Source source, Payload payload, float x, float y, int pointer) 
        {
            InventorySlot sourceSlot = (InventorySlot)source.getActor();
            InventorySlot targetSlot = (InventorySlot)getActor();

            Image sourceImage = sourceSlot.getImage();
            Image targetImage = targetSlot.getImage();

            Item sourceItem = sourceSlot.getItem();
            Item targetItem = targetSlot.getItem();

            // If the image was swapped with an empty slot
            if (targetSlot.getCount() == 0) 
            {
                sourceSlot.setCount(0);
            }

            if (targetItem != null) 
            {
                sourceSlot.setItem(targetItem);
            } 
            else
            {
                sourceSlot.setItem(null);
            }

            // Switch items
            sourceSlot.setImage(targetImage).setDragged(false);
            targetSlot.setImage(sourceImage).setItem(sourceItem).setCount(1);
        }
    }
}
