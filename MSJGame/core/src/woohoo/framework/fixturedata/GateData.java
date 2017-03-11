//package woohoo.framework.fixturedata;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.XmlReader;
//
//public class GateData extends FixtureData
//{
//    private final XmlReader.Element gate;
//    private Vector2 playerOffset;
//
//    public GateData(XmlReader.Element g)
//    {        
//        gate = g;
//        playerOffset = new Vector2(0, 0);
//    }
//
//    public void setPlayerOffset(float x, float y)
//    {
//        // Took me forever to figure this out
//        // Reposition the character relative to the gate's exit location based on where the player entered the gate's entrance
//        playerOffset = new Vector2(Math.min(Math.max(0, x), gateSize().x - 1), Math.min(Math.max(0, y), gateSize().y - 1));
//    }
//
//    public Vector2 gatePos()
//    {
//        // Return center of top-left block
//        return new Vector2(gate.getFloat("locX") + 0.5f, gate.getFloat("locY") + 0.5f);
//    }
//
//    public Vector2 gateSize()
//    {
//        return new Vector2(gate.getFloat("sizeX"), gate.getFloat("sizeY"));
//    }
//
//    public Vector2 playerPos()
//    {
//        return new Vector2(gate.getFloat("destX") + playerOffset.x, gate.getFloat("destY") + playerOffset.y);
//    }
//
//    public int destArea()
//    {
//        return gate.getInt("destArea");
//    }
//}