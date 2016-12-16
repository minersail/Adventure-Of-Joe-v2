package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader
{
    private static Map<String, Texture> textures = new HashMap<>();

    public static Sound dead, flap, coin;

    public static BitmapFont font, shadow;

    private static Preferences prefs;

    public static void load()
    {
        loadTexture("Error", "images/error.png");
        loadTexture("Tileset", "images/tileset.png");
        loadTexture("Joe", "images/joe2.png");
		loadTexture("JoeFace", "images/joeface.png");

        dead = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.wav"));
        flap = Gdx.audio.newSound(Gdx.files.internal("sounds/flap.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));

        font = new BitmapFont(Gdx.files.internal("fonts/text.fnt"));
        font.getData().setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("fonts/shadow.fnt"));
        shadow.getData().setScale(.25f, -.25f);

        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("ZombieBird");

        if (!prefs.contains("highScore"))
        {
            prefs.putInteger("highScore", 0);
        }
    }

    private static void loadTexture(String ID, String filename)
    {
        Texture texture = new Texture(Gdx.files.internal(filename));
        textures.put(ID, texture);
    }

    public static Texture get(String ID)
    {
        if (textures.containsKey(ID))
        {
            return textures.get(ID);
        }
        System.out.println("TEXTURE DOES NOT EXIST");
        return textures.get("Error");
    }

    public static void dispose()
    {
        // We must dispose of the texture when we are finished.
        for (Texture texture : textures.values())
        {
            texture.dispose();
        }

        // Dispose sounds
        dead.dispose();
        flap.dispose();
        coin.dispose();

        font.dispose();
        shadow.dispose();
    }
}
