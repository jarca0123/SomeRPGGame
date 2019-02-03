package tiles;

import game.Game;
import geometry.GameRect;
import npc.NPC;
import npc.dialogue.Dialogue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;


public class Tile implements Cloneable, Serializable{
    public int id;
    public String name;
    public String data;
    public  BufferedImage image;
    public String pixelCode;
    public int x;
    public int y;
    public GameRect bounds;
    public Properties properties;
    public boolean solid;
    public BufferedImage[][] subImages;
    public int subImageX;
    public int subImageY;
    public int subImageRows;
    public int subImageCols;
    public boolean useTileset;
    public int roomX;
    public int roomY;
    public Dialogue npcDialogue;

    public Tile(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset){
        this.id = id;
        this.name = name;
        this.data = "";
        try {
            this.image = ImageIO.read(getClass().getResource(imageSrc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pixelCode = pixelCode;
        this.solid = solid;
        this.properties = new Properties();
        this.useTileset = useTileset;
        if(image != null && useTileset)
        splitTileset();
    }

    public Tile(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset, int x, int y){
        this.id = id;
        this.name = name;
        this.data = "";
        try {
            this.image = ImageIO.read(new File(imageSrc));
            BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = tempImage.createGraphics();
            g.drawImage(tempImage, 0, 0, null);
            g.dispose();
            this.image = tempImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pixelCode = pixelCode;
        this.solid = solid;
        this.x = x;
        this.y = y;
        this.properties = new Properties();
        this.useTileset = useTileset;
        if(image != null && useTileset)
        splitTileset();
    }

    public void readData(){
        try {
            properties.load(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e){

        }
        if(useTileset){
            String subImage = properties.getProperty("subImage");
            if(subImage != null) {
                String[] subImageCoords = subImage.split(";");
                image = subImages[Integer.parseInt(subImageCoords[0])][Integer.parseInt(subImageCoords[1])];
                subImageX = Integer.parseInt(subImageCoords[0]);
                subImageY = Integer.parseInt(subImageCoords[1]);
            }
        }
    }

    public void update(){
        this.bounds = new GameRect(x, y, Game.tilesetSize, Game.tilesetSize);
    }

    public void writeData(String key, String value) throws IOException {
        StringWriter sw = new StringWriter();
        properties.setProperty(key, value);
        properties.store(sw, "");
        data = sw.toString().replace("#" + new Date().toString(), "").replaceFirst("#", "").replaceFirst("\r\n\r\n", "");
    }

    public void splitTileset() {


        subImageRows = image.getWidth() / Game.tilesetSize;
        subImageCols = image.getHeight() / Game.tilesetSize;
        subImages = new BufferedImage[subImageRows][subImageCols];
        int count = 0;

        for (int x = 0; x < subImageRows; x++) {
            for (int y = 0; y < subImageCols; y++) {

                subImages[y][x] = image.getSubimage(Game.tilesetSize * x, Game.tilesetSize * y, Game.tilesetSize, Game.tilesetSize);
            }
        }

    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }

    public void onEntrance(NPC npc){
    }

    public void onEnter(){

    }

    public void onInteraction() {
    }


}
