package woohoo.framework;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameobjects.components.ItemDataComponent.ItemType;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.gameworld.Mappers;
import woohoo.gameworld.RenderSystem;
import woohoo.gameworld.WeaponSystem;
import woohoo.screens.PlayingScreen;

/**
 * Note: the UI uses the bottom-left coordinate system
 * @author jordan
 */
public class InventoryManager
{
    public final int INVENTORY_WIDTH = 5;
    public final int INVENTORY_HEIGHT = 8;
    public final int ITEMX = 64;
    public final int ITEMY = 64;
	public final int ITEMBORDERX = 8;
	public final int ITEMBORDERY = 8;
	
	public final int PADDING_LEFT = 100;
	public final int PADDING_RIGHT = 100;
	public final int INVENTORY_BOTTOM = (Gdx.graphics.getHeight() - ((INVENTORY_HEIGHT + 1) * ITEMY)) / 2;
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
	private Table table2;
	private TextButton closeButton;
	private InventorySlot weaponSlot;

	/**
	 * Initializes the UI with blank items, to be later filled with fillInventory()
	 * @param scr Reference to the game screen
	 * @param atlas Atlas containing backgrounds and borders for the inventory
	 * @param skin Really only necessary to get the font. (Can change later)
	 */
    public InventoryManager(PlayingScreen scr, TextureAtlas atlas, Skin skin) 
    {
        screen = scr;
        table = new Table();
        table2 = new Table();
		
        slotBackground = atlas.findRegion("itemframe");
		blankItem = atlas.findRegion("blank");
                
        closeButton = new TextButton("x", skin);
        weaponSlot = new InventorySlot(slotBackground, blankItem);
		weaponSlot.setWeaponSlot(true);
        table.add(closeButton).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);   
        table.add(weaponSlot).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);
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
        dnd.addTarget(new InventoryTarget(weaponSlot) // Weapon slot has special function when dropped to
        {
            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) 
            {
				InventorySlot sourceSlot = (InventorySlot)source.getActor();
				ItemDataComponent itemData = Mappers.items.get(sourceSlot.getItem());
				
                if (itemData.type == ItemType.Weapon)
                {
					WeaponComponent weapon = new WeaponComponent(screen.getWorld());
					weapon.damage = Float.parseFloat((String)itemData.metaData.get("damage", "0.25f"));
					weapon.knockback = Float.parseFloat((String)itemData.metaData.get("knockback", "1"));
					
					weapon.mass.setUserData(new ContactData(ContactType.Weapon, screen.getEngine().getPlayer()));
					screen.getEngine().getPlayer().add(weapon);
					screen.getEngine().getSystem(WeaponSystem.class).equip(screen.getEngine().getPlayer(), weapon);
					
                    super.drop(source, payload, x, y, pointer);
                }
                else
                {
					sourceSlot.setDragged(false);
					weaponSlot.setColor(Color.WHITE);
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
                table.add(slot).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);

                dnd.addSource(new InventorySource(slot));
                dnd.addTarget(new InventoryTarget(slot));
				
                InventorySlot slot2 = new InventorySlot(slotBackground, blankItem);
                table2.add(slot2).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);

                dnd.addSource(new InventorySource(slot2));
                dnd.addTarget(new InventoryTarget(slot2));
            }
            table.row();
			table2.row();
        }

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.left);
		table.padLeft(PADDING_LEFT);
		
		table2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table2.align(Align.right);
		table2.padRight(PADDING_RIGHT);
    }

    public void showInventory() 
    {
        screen.getUI().addActor(table);
        screen.getUI().addActor(table2);
        screen.setState(PlayingScreen.GameState.Inventory);
    }
	
	public void closeInventory()
	{
		table.remove();
		table2.remove();
        screen.setState(PlayingScreen.GameState.Playing);
	}
    
	/**
	 * Function to load InventoryComponents from XML
	 * @param inventory InventoryComponent to load
	 */
    public void loadInventory(InventoryComponent inventory)
    {        
        FileHandle handle = Gdx.files.local("data/inventory.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());    
        
        for (XmlReader.Element e : root.getChildrenByName("item"))
        {	
            Entity item = new Entity();
			ItemDataComponent itemData = new ItemDataComponent(e.getChildByName("metadata").getAttributes());
			item.add(itemData);			
            inventory.addItem(item);
        }
	}
	
	/**
	 * Function to fill the inventory UI with a character's inventory
	 * @param inventory Character to fill inventory UI with
	 */
	public void fillInventory(InventoryComponent inventory)
	{
		currentInventory = inventory;
		
		// Starts at 2 since first two items are "X" button and item slot
		for (int i = 2; i < table.getCells().size; i++)
		{		
			if (i >= currentInventory.getItems().size() + 2) break;
			
			Entity item = currentInventory.getItems().get(i - 2); // i - 2 because the inventory starts at 0
			
			// Use the id from the itemdatacomponent to retrive a texture from the id manager
			TextureRegion region = new TextureRegion(screen.getIDManager().getItem(Integer.parseInt((String)Mappers.items.get(item).metaData.get("id"))).getItemTexture());
			Image image = new Image(region);
			image.setSize(ITEMX, ITEMY);
			
			((InventorySlot)table.getCells().get(i).getActor()).setImage(image).setItem(item).setCount(1);
		}
	}
	
	/**
	 * Function to add a single item into both the inventory of the character passed in and the UI
	 * If the item only needs to be added to the character, instead use only InventoryComponent.addItem()
	 * 
	 * Can only be called after fillInventory() has been called at least once
	 * @param inventory InventoryComponent item will be added to
	 * @param item The item to be added
	 */
	public void addItem(InventoryComponent inventory, Entity item)
	{
		currentInventory = inventory;
		currentInventory.addItem(item);
		
		// Starts at 2 since first two items are "X" button and item slot
		for (int i = 2; i < table.getCells().size; i++)
		{		
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			
			if (slot.getCount() == 0)
			{
				Mappers.mapObjects.get(item).getTextureRegion().flip(false, true); // Because the world is in y-down and the UI is in y-up
				Image image = new Image(Mappers.mapObjects.get(item).getTextureRegion());
				slot.setImage(image).setItem(item).setCount(1);
				
				// Remove unnecessary components while item is transferred to inventory
				screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap().getLayers().get("Items").getObjects().remove(Mappers.mapObjects.get(item));
				item.remove(MapObjectComponent.class);
				screen.getWorld().destroyBody(Mappers.hitboxes.get(item).mass);
				item.remove(HitboxComponent.class);
				item.remove(PositionComponent.class);
				return;
			}
		}
		
		// If the code reaches here the inventory is full
	}
	
	/**
	 * Removes an item from both the UI and the current character's inventory
	 * (as set by either addItem() or fillInventory())
	 * and drops it into the game world
	 * @param item item to be dropped
	 */
	public void dropItem(Entity item)
	{				
		// Starts at 1 since first item is "X" button
		for (int i = 1; i < table.getCells().size; i++)
		{
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			
			if (drop(item, slot)) return;
		}
		
		// Check both tables
		for (int i = 0; i < table2.getCells().size; i++)
		{
			InventorySlot slot = (InventorySlot)table2.getCells().get(i).getActor();
			
			if (drop(item, slot)) return;
		}
	}
	
	/**
	 * Utility function, saves me from typing the exact same thing in both item for loops
	 * @param item item from dropItem()
	 * @param slot slot from dropItem()
	 * @return true if the item was successfully dropped
	 */
	private boolean drop(Entity item, InventorySlot slot)
	{		
		if (slot.getItem() != null && slot.getItem().equals(item))
		{			
			// Create empty slot
			slot.setItem(null).setImage(new Image(blankItem)).setCount(0);

			currentInventory.removeItem(item);

			// Create position/mapObject components to go along with the item
			PositionComponent position = new PositionComponent(Mappers.positions.get(screen.getEngine().getPlayer()).position.cpy());
			MapObjectComponent mapObject = new MapObjectComponent(screen.getIDManager().getItem(Integer.parseInt((String)Mappers.items.get(item).metaData.get("id"))).getItemTexture());

			HitboxComponent hitbox = new HitboxComponent(screen.getWorld(), false, ContactType.Item);
			hitbox.mass.setTransform(position.position.cpy().add(0.5f, 0.5f), 0);
			item.add(hitbox);
			item.add(position);
			item.add(mapObject);

			screen.getEngine().addEntity(item);
			return true;
		}
		
		// Item was not in slot
		return false;
	}
	
	/**
	 * Stores the image data for a single slot in the inventory UI
	 * 
	 * Setters return this item for method chaining
	 */
    public class InventorySlot extends Image
    {
		private Entity item; // Item entity, starts as null
        private Image itemImage; // Scene2D actor used for payload
        private boolean dragged;
		private int count;
		
		private boolean isWeaponSlot;
        
        public InventorySlot(TextureRegion background, TextureRegion itemSprite)
        {
            super(background);
            itemImage = new Image(itemSprite);
            itemImage.setSize(ITEMX, ITEMY);
        }
        
        public Image getImage()
        {
            return itemImage;
        }
		
		public Entity getItem()
		{
			return item;
		}
        
        public InventorySlot setImage(Image image)
        {
            itemImage = image;			
            itemImage.setSize(ITEMX, ITEMY);
			return this;
        }
		
		public InventorySlot setItem(Entity entity)
		{		
			item = entity;	
			return this;
		}
        
        public InventorySlot setDragged(boolean drag)
        {
            dragged = drag;			
			return this;
        }
		
		public InventorySlot setWeaponSlot(boolean weapon)
		{
			isWeaponSlot = true;
			return this;
		}
		
		public InventorySlot setCount(int newCount)
		{
			count = newCount;
			return this;
		}
		
		public boolean isDragged()
		{
			return dragged;
		}
		
		public boolean isWeaponSlot()
		{
			return isWeaponSlot;
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
                itemImage.setPosition(getX() + ITEMBORDERX / 2, getY() + ITEMBORDERY / 2);   
            }
        }
    }
    
    /**
     * Could be an anonymous but looks cleaner in its own class
     */
    private class InventorySource extends Source
    {
        public InventorySource(InventorySlot slot)
        {
            super(slot);
        }

		/*
		Called when a drag is started
		*/
        @Override
        public Payload dragStart(InputEvent event, float x, float y, int pointer)
        {
            InventorySlot slot = (InventorySlot)getActor();
            // Can't drag empty frame
            if (slot.getCount() == 0) return null;

            slot.setDragged(true); // Let the item be freely dragged
			
			if (Mappers.items.get(slot.getItem()).type != ItemType.Weapon)// Grey out boxes this item can't be placed in
			{
				weaponSlot.setColor(Color.DARK_GRAY);
			}

            Payload payload = new Payload();
            payload.setDragActor(slot.getImage());

            return payload;
        }

		/*
		Called when a drag is stopped
		*/
        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) 
        {
            if (target == null) // Payload was not dropped on a target (e.g. outside the inventory)
            {
                // Remove the item from the inventory and add it to the world
                Entity dropped = ((InventorySlot)getActor()).getItem();

				if (((InventorySlot)getActor()).isWeaponSlot())
				{
					screen.getEngine().getSystem(WeaponSystem.class).unequip(screen.getEngine().getPlayer());
				}
				
                dropItem(dropped);
            }

            ((InventorySlot)getActor()).setDragged(false);
			weaponSlot.setColor(Color.WHITE);
        }
    }
    
    private class InventoryTarget extends Target
    {
        public InventoryTarget(InventorySlot slot)
        {
            super(slot);
        }
        
		/*
		Called when a payload is moved over a target
		*/
        @Override
        public boolean drag(Source source, Payload payload, float x, float y, int pointer) 
        {
            return true;
        }

		/*
		Called when a payload moves out of a target's bounds
		*/
        @Override
        public void reset(Source source, Payload payload) {}

        /*
		Called when the payload is dropped on a target		
        */
        @Override
        public void drop(Source source, Payload payload, float x, float y, int pointer) 
        {			
			// Switch the images, items, item counts, etc. of the two inventory slots
            InventorySlot sourceSlot = (InventorySlot)source.getActor();
            InventorySlot targetSlot = (InventorySlot)getActor();
			
			if (sourceSlot.isWeaponSlot()) // If the item was dragged from the weapon slot
			{
				if (targetSlot.getItem() != null)
				{	// Prevent non-weapons from going into weapon slot if weapon switches with them
					sourceSlot.setDragged(false);
					return;
				}
				else
				{
					screen.getEngine().getSystem(WeaponSystem.class).unequip(screen.getEngine().getPlayer());
				}
			}

            Image sourceImage = sourceSlot.getImage();
            Image targetImage = targetSlot.getImage();

            Entity sourceItem = sourceSlot.getItem();
            Entity targetItem = targetSlot.getItem();

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
			
			weaponSlot.setColor(Color.WHITE);
        }
    }
}
