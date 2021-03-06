package game;

import file.FileLoader;
import file.WorldLoader;
import gui.*;
import gui.window.Window;
import item.AttackItem;
import item.BFG9001;
import item.DefenseItem;
import item.Inventory;
import npc.player.Player;
import npc.SomeBoss;
import npc.player.PlayerStats;
import room.Room;
import tiles.NPCSpawner;
import tiles.SaveTile;
import tiles.Tile;
import world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;



public class Game extends JFrame{
    public static World world;
    public static GamePanel gamePanel;
    public static Game game;
    public static GUI gui;
    public ArrayList<GUI> guis = new ArrayList<>();
    public ArrayList<Window> windows = new ArrayList<Window>();
    public static boolean gameRunning = true;
    public Thread gameThread;
    public Thread consoleThread;
    public static int GAME_WIDTH = 640;
    public static int GAME_HEIGHT = 480;

    public static int tilesetSize = 48;
    public static ArrayList<BufferedImage> sprites = new ArrayList<>();



    public static void main(String[] args) throws Exception {
        gamePanel = new GamePanel();
        new Game().init();
    }

    public static String name = "script";






    public void init() {
        System.setProperty("sun.java2d.opengl", "true");
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/DTM-Mono.otf")));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initializeSprites();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeGUIs();
        setGUI(new GUIMainMenu());
        game = this;
        gamePanel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));


        this.setTitle("Undertale Clone #" + generateRandomNumber());
        this.getContentPane().add(gamePanel);
        this.pack();

        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocus();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameThread = new Thread("Game Loop Thread"){
            @Override
            public void run() {
                gamePanel.gameLoop();
            }
        };
        gameThread.start();
        consoleThread = new Thread("Console Loop Thread"){
            @Override
            public void run() {
                gamePanel.consoleLoop();
            }
        };
        consoleThread.start();

    }

    private int generateRandomNumber(){
        Random r = new Random();
        String stringNumber = "";
        for(int i = 0; i < 6; i++){
            stringNumber += Integer.toString(r.nextInt(9));
        }
        return Integer.parseInt(stringNumber);
    }

    private void initializeSprites() throws IOException{
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/fight0.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/fight1.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/act0.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/act1.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/item0.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/item1.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/spare0.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/spare1.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/undertalebackground.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/alphys0.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/alphys1.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/alphys2.png"))));
        sprites.add(Game.toBufferedImage(ImageIO.read(this.getClass().getResource("/heart.png"))));
    }



    private void initializeGUIs() {
        this.addGUI(new GUIMainMenu());
        this.addGUI(new GUILevelEditor());
        this.addGUI(new GUIPauseMenu());
        this.addGUI(new GUIBattle());
    }

    public static int parseIntNoException(String s){
        try{
            return Integer.parseInt(s);
        }catch (Exception e){
            return 0;
        }
    }



    @Deprecated
    public void setGUI(GUI gui){

        if(this.gui != null) {
            this.getContentPane().removeMouseListener(this.gui);
            this.getContentPane().removeMouseMotionListener(this.gui);
            this.removeKeyListener(this.gui);
            this.gui.onDestroy();
        }
        this.gui = null;
        this.gui = gui;
        this.gui.onStart();
        this.getContentPane().addMouseListener(this.gui);
        this.getContentPane().addMouseMotionListener(this.gui);
        this.addKeyListener(this.gui);
    }

    private void addGUI(GUI gui) {
        guis.add(gui);

    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void start(boolean isEditing, String room){
        try {
            WorldLoader.loadWorld(isEditing);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileLoader.loadFile();
        } catch (IOException e) {

            Game.world.inventory = new Inventory();
            Game.world.playerStats = new PlayerStats(1, 1, 0, null, null);


        }



        if(isEditing){
            world.onPostStart(room);
        } else {
            world.onPostStart();
        }
        world.onPostGUIStart();

    }

    private void initializeRooms() {
        world.addRoom(new Room(1, "ROOM1.map"));

    }

    public void initializeTiles() {
        world.addTile(new Tile(0, "nothing", "/purple.png", "000000", false, false));
        world.addTile(new Tile(1, "darktile", "/darktile.png", "000000", true, true));
        world.addTile(new SaveTile(2, "saveTile", "/tilesetreal.png", "000000", true, true));

        world.addTile(new NPCSpawner(3, "npcSpawner", "/tilesetreal.png", "000000", true, true));

    }

    public void initializeNPCs() {
        world.addNPC(new Player(1, "Player", "/reallyfrisktile.png", 0, 0, 19, 29, 40, 59, true));
        world.addNPC(new SomeBoss(2, "Enemy", "/reallyfrisktile.png", 0, 0, 19, 29, 40, 59, true));
    }

    public void initializeItEMs() {
        world.addItem(new AttackItem(1, "Stick", 0));
        world.addItem(new DefenseItem(2, "Bandage", 0));
        world.addItem(new BFG9001(3, "BFG9001"));
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }


        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);


        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();


        return bimage;
    }
}
