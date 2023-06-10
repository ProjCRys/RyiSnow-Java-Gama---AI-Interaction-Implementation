package main;

import ai.ChatAI;
import ai.PathFinder;
import entity.Cursor;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import main.tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    //Screen Settings
    public final int orgTileSize = 32;
    public final int scale = 2;
    
    //Aspect Ratio
    public int scaleRatio = 65;//65
    public int maxScreenCol = 16;//16
    public int maxScreenRow = 9;//9
    public int screenWidth = maxScreenCol*scaleRatio,screenHeight = maxScreenRow*scaleRatio;
    public int tileSize = screenWidth/(maxScreenCol*(int)1.5);
    
    //World Settings
    /*Make sure maxWorldCol and maxWorldRow is equal to map size 
    and if greater than map size it will only draw col0*/
    public final int maxWorldCol = 200;
    public final int maxWorldRow = 200;
    
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    public final int maxMaps = 100;
    public int currentMap = 0;
    
    //Full Screen
    public boolean fulleScreenOn = false;
    public int alterScreenWidth = screenWidth;
    public int alterScreenHeight = screenHeight;
    BufferedImage alteredScreen, zoomedScreen;
    Graphics2D g2;
    public boolean errorCursor = false;
    
    //Zoom
    public double zoomX = 0;
    public int zoomY = 0;
    public boolean zoomedIn = false;
    
    //FPS
    public int FPS = 60;
    public int currentFPS = 0;
    
    //Game System
    Sound music = new Sound();
    Sound sfx = new Sound();
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public EventHandler eHandler = new EventHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Config config = new Config(this);
    public UI ui = new UI(this);
    public PathFinder pFinder = new PathFinder(this);
    public ChatAI chat = new ChatAI(this);
    SystemRequirements sys = new SystemRequirements();
    
    //Entities
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);;
    public Cursor cursor = new Cursor(this, mouseH);
    public Entity npc[][] = new Entity[maxMaps][100];
    public Entity obj[][] = new Entity[maxMaps][50];
    ArrayList<Entity> entityList = new ArrayList<>();
    Thread gameThread;
    
    //Game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int charaStatusState = 4;
    public final int optionState = 5;
    public final int transitionState = 6;
    public final int chatState = 7;
    
    
    public GamePanel(){
        if (fulleScreenOn == false) {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        }
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.addMouseWheelListener(mouseH);
        this.setFocusable(true);
        chat.clear(); // Delete leftover convo
        
        //Hide Mouse Cursor
        try {
            BufferedImage cursorImg = ImageIO.read(getClass().getResourceAsStream("/res/ui/glitchCursor.png"));
            java.awt.Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg.getSubimage(0, 0, 325, 325), new Point(0, 0), "blank cursor");
            this.setCursor(blankCursor);
        } catch (Exception e) {
            errorCursor = true;
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            java.awt.Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
            this.setCursor(blankCursor);
        }
    }
    public void setupGame() {
        if (fulleScreenOn == true) {
            //Get Local Screen device
            setFullScreen();
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            player = new Player(this, keyH);
            alteredScreen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        else{
            alteredScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        }
        
        g2 = (Graphics2D)alteredScreen.getGraphics();
        
        aSetter.setObject();
        aSetter.setNPC();
        gameState = titleState;
        playMusic(1);
    }
    public void zoom() {
        if (fulleScreenOn != true) {
            
        }
        if (gameState == playState) {
            if (/*mouseH.mouseWheelRotation < 0*/ keyH.hidePressed == true && zoomY < 40) {//zoom in // scroll up //-1
                zoomX+=16;
                zoomY+=9;
            }
            if (/*mouseH.mouseWheelRotation > 0*/ keyH.hidePressed == false && zoomY > 0) {//zoom out // scroll down //1
                zoomX-=16;
                zoomY-=9;
            }
        }
        else{
            zoomX = 0;
            zoomY = 0;
        }
        zoomedScreen = alteredScreen.getSubimage((int) zoomX*4, zoomY*4, screenWidth - (int) (zoomX * 8), screenHeight - (zoomY * 8));
//        System.out.println(zoomedScreen.getWidth()+" "+zoomedScreen.getHeight());
    }
    public void setFullScreen(){
        //Get Local Screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(main.window);
        
        //Get Full Screen width and height
        screenWidth = main.window.getWidth();
        screenHeight = main.window.getHeight();
        tileSize = screenWidth/(maxScreenCol*(int)1.5);
//        System.out.println(screenWidth+" "+screenHeight);
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            
            if(delta >= 1){
                //Update
                update();
                //Draw
                drawToAlteredScreen(); //Draw game into buffered image
                drawToScreen(); //Draw buffered image to screen
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                if (keyH.showDebugText == true) {
                    currentFPS = drawCount;
                }
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update(){
        cursor.update();
        if (gameState == playState || gameState == chatState) {
            //Player
            player.update();
            //NPC
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
        }
        if (gameState == pauseState) {}
    }
    public void drawToAlteredScreen(){
        //Debug
        long drawStart = 0;
        if (keyH.showDebugText == true) {
            drawStart = System.nanoTime();
        }
        //TitleScreen
        if (gameState == titleState) {
            ui.draw(g2);
        }
        //Others
        else{
            //Tiles
            tileM.draw(g2);
            //Interactive Tiles Note: add to entity list if tile ratio > 1:1
            
            //Add Entities to list
            entityList.add(player);
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            //Sort all Entities
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });
            //Draw Entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            //Reset List
            entityList.clear();
            
            //UI
            ui.draw(g2);
        }
        
        //Cursor
        if (errorCursor == true) {
            cursor.draw(g2);
        }
        
        //Debug
        if (keyH.showDebugText == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setFont(new Font("Serif", Font.PLAIN, 20));
            g2.setColor(new Color(255, 255, 0, 200));
            int x = 10;
            int y = tileSize *5 - 32;
            int lineHeight = 20;
            
            // Player Coords
            g2.drawString("X Coordinates: " + player.worldX, x, y); y += lineHeight;
            g2.drawString("Y Coordinates: " + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
            g2.drawString("Stamina: " + player.stamina, x, y); y += lineHeight;
            y += lineHeight;
            
            // Cursor Coords
            g2.drawString("Cursor X Coordinates: " + cursor.worldX, x, y); y += lineHeight;
            g2.drawString("Cursor Y Coordinates: " + cursor.worldY, x, y); y += lineHeight;
            g2.drawString("Cursor Col: " + (cursor.worldX + cursor.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Cursor Row: " + (cursor.worldY + cursor.solidArea.y)/tileSize, x, y); y += lineHeight;
            y += lineHeight;
            //Draw Time (the lower the better)
            g2.drawString("Draw time: " + passed, x, y);y += lineHeight;
            g2.drawString("FPS: " + currentFPS, x, y); y += lineHeight;
            g2.drawString("Deviation Error: " + player.deviationError, x, y); y += lineHeight;
            g2.drawString("Loops: " + player.rebootCounter, x, y);
        }
    }
    public void drawToScreen(){
        Graphics g = getGraphics();
        zoom();
        g.drawImage(zoomedScreen, 0, 0, screenWidth, screenHeight, null);
        g.dispose();
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSFX(int i){
        sfx.setFile(i);
        sfx.play();
    }
}
