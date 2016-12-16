package woohoo.gameobjects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import woohoo.framework.AssetLoader;
import woohoo.framework.InputHandler;

public class Player extends TextureMapObject
{	
    private Animation rightAnim;
    private Animation leftAnim;
    private Animation upAnim;
    private Animation downAnim;
    private int moveTimer = 0;
    private int currentDir = Keys.LEFT;
    
    private final int FRAMES_PER_ANIM = 5;
    
    public Player(TextureRegion spr)
    {
		setTextureRegion(spr);
        {
            int x = 0;
            int w = 56;
            int h = 32;//AssetLoader.get("Joe").getHeight() / 4;

            TextureRegion t1 = new TextureRegion(AssetLoader.get("Joe"), x, 0, w, h);
            TextureRegion t2 = new TextureRegion(AssetLoader.get("Joe"), x, h, w, h);
            TextureRegion t3 = new TextureRegion(AssetLoader.get("Joe"), x, h * 2, w, h);
            TextureRegion t4 = new TextureRegion(AssetLoader.get("Joe"), x, h * 3, w, h);
            t1.flip(false, true);
            t2.flip(false, true);
            t3.flip(false, true);
            t4.flip(false, true);
            rightAnim = new Animation(1.0f, t1, t2, t3, t4);
            rightAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }
        {
            int x = 56;
            int w = 56;
            int h = 32;//AssetLoader.get("Joe").getHeight() / 4;

            TextureRegion t1 = new TextureRegion(AssetLoader.get("Joe"), x, 0, w, h);
            TextureRegion t2 = new TextureRegion(AssetLoader.get("Joe"), x, h, w, h);
            TextureRegion t3 = new TextureRegion(AssetLoader.get("Joe"), x, h * 2, w, h);
            TextureRegion t4 = new TextureRegion(AssetLoader.get("Joe"), x, h * 3, w, h);
            t1.flip(false, true);
            t2.flip(false, true);
            t3.flip(false, true);
            t4.flip(false, true);
            leftAnim = new Animation(1.0f, t1, t2, t3, t4);
            leftAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }
        {
            int xOff = 112;
            int y = 0;
            int w = 32;
            int h = 56;//AssetLoader.get("Joe").getHeight() / 4;

            TextureRegion t1 = new TextureRegion(AssetLoader.get("Joe"), xOff, y, w, h);
            TextureRegion t2 = new TextureRegion(AssetLoader.get("Joe"), xOff + w, y, w, h);
            TextureRegion t3 = new TextureRegion(AssetLoader.get("Joe"), xOff + w * 2, y, w, h);
            TextureRegion t4 = new TextureRegion(AssetLoader.get("Joe"), xOff + w * 3, y, w, h);
            t1.flip(false, true);
            t2.flip(false, true);
            t3.flip(false, true);
            t4.flip(false, true);
            downAnim = new Animation(1.0f, t1, t2, t3, t4);
            downAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }
        {
            int xOff = 112;
            int y = 72;
            int w = 32;
            int h = 56;//AssetLoader.get("Joe").getHeight() / 4;

            TextureRegion t1 = new TextureRegion(AssetLoader.get("Joe"), xOff, y, w, h);
            TextureRegion t2 = new TextureRegion(AssetLoader.get("Joe"), xOff + w, y, w, h);
            TextureRegion t3 = new TextureRegion(AssetLoader.get("Joe"), xOff + w * 2, y, w, h);
            TextureRegion t4 = new TextureRegion(AssetLoader.get("Joe"), xOff + w * 3, y, w, h);
            t1.flip(false, true);
            t2.flip(false, true);
            t3.flip(false, true);
            t4.flip(false, true);
            upAnim = new Animation(1.0f, t1, t2, t3, t4);
            upAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        }
    }
    
    public Vector2 getPosition()
    {
        return new Vector2(getX(), getY());
    }
    
    public int getDirection()
    {
        return currentDir;
    }
    
    /*
    Used for drawing
    */
    public Vector2 getAnimationScale(int dir)
    {
        if (dir == Keys.RIGHT || dir == Keys.LEFT)
            return new Vector2(56.0f / 32.0f, 1);
        else if (dir == Keys.UP || dir == Keys.DOWN)
            return new Vector2(1, 56.0f / 32.0f);   
        else
            return new Vector2(1, 1);
    }
    
    /*
    Used for drawing
    */
    public Vector2 getAnimationOffset(int dir)
    {
        switch (dir)
        {
            case Keys.RIGHT:
                return new Vector2(0.0f, 0.0f);
            case Keys.LEFT:
                return new Vector2(-48.0f, 0.0f);
            case Keys.UP:
                return new Vector2(0.0f, -48.0f);
            case Keys.DOWN:
                return new Vector2(0.0f, 0.0f);
            default:
                return new Vector2(0.0f, 0.0f);
        }
    }
    
    public Animation getAnimation(int dir)
    {
        switch (dir)
        {
            case Keys.RIGHT:
                return rightAnim;
            case Keys.LEFT:
                return leftAnim;
            case Keys.UP:
                return upAnim;
            case Keys.DOWN:
                return downAnim;
            default:
                return null;
        }
    }
    
    public float getTimer()
    {
        return moveTimer / FRAMES_PER_ANIM;
    }
    
    public void setPosition(int coordX, int coordY)
    {
        setX(coordX);
        setY(coordY);
    }
    
    public void update(float delta)
    {        
		System.out.println(getX());
        if (moveTimer > 0)
        {
            moveTimer++;
        }
        else if (InputHandler.isKeyPressed(Keys.LEFT))// && position.x != 0)
        {
            currentDir = Keys.LEFT;
            moveTimer++;
        }
        else if (InputHandler.isKeyPressed(Keys.RIGHT))// && position.x != mapWidth - 1)
        {
            currentDir = Keys.RIGHT;     
            moveTimer++;       
        }
        else if (InputHandler.isKeyPressed(Keys.UP))// && position.y != 0)
        {
            currentDir = Keys.UP;
            moveTimer++;
        }
        else if (InputHandler.isKeyPressed(Keys.DOWN))// && position.y != mapHeight - 1)
        {
            currentDir = Keys.DOWN;
            moveTimer++;
        }
        else
        {
            currentDir = Keys.LEFT;
        }
        
        if (moveTimer >= FRAMES_PER_ANIM * 4)
        {
            movePlayer(currentDir);
            moveTimer = 0;
        }
    }
    
    public void movePlayer(int i)
    { 
        switch (i)
        {
            case Keys.UP:
                setY(getY() - 1);
                break;
            case Keys.RIGHT:
                setX(getX() + 1);
                break;
            case Keys.DOWN:
                setY(getY() + 1);
                break;
            case Keys.LEFT:
                setX(getX() - 1);
                break;
            default:
                break;
        }
    }
    
    public void checkMove()
    {
        
    }
}
