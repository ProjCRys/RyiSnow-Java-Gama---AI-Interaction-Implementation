package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
    GamePanel gp;
    public int worldX, worldY;
    public String direction = "down";
    public boolean onPath = false, idle = false;
    public String entityType = "";
    
    public Rectangle solidArea = new Rectangle(0, 0, 64, 64);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision;
    public boolean collisionOn = false;
    String[] dialogues = new String [20];
    
    public BufferedImage up1, up2, up3, up4,
            down1, down2, down3, down4,
            left1, left2, left3, left4,
            right1, right2, right3, right4;
    public BufferedImage runUp1, runUp2, runUp3, runUp4, runUp5, runUp6,
            runDown1, runDown2, runDown3, runDown4, runDown5, runDown6,
            runLeft1, runLeft2, runLeft3, runLeft4, runLeft5, runLeft6,
            runRight1, runRight2, runRight3, runRight4, runRight5, runRight6;
    public BufferedImage idleUp1, idleUp2, idleUp3, idleUp4, idleUp5, idleUp6,
            idleDown1, idleDown2, idleDown3, idleDown4, idleDown5, idleDown6,
            idleLeft1, idleLeft2, idleLeft3, idleLeft4, idleLeft5, idleLeft6,
            idleRight1, idleRight2, idleRight3, idleRight4, idleRight5, idleRight6;
    public BufferedImage sourceImg, image, image2, image3, image4, image5, image6, image7, image8;
    
    //Counter
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int deviationError = 0;
    public int rebootCounter = 0;
    
    //Attributes
    public String name, prompt, personality, oocPrompt, scenario, itemDesc = "";
    public int addSpeed, speed, maxLife, life, maxSanity, sanity, maxStamina, stamina;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public boolean stackable = false;
    public int amount = 1;
    public int maxInventorySize = 0;
    
    public int objectType = 0;
    public final int nonObtainable = 0;
    public final int obtainable = 1;
    public final int obstacle = 2;
    
    
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public int imageAdjustLayer; // number of tiles on y axis adjusted 
    
    
    
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    public void setAction(){}
    public void speak(){
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        
        switch(gp.player.direction){
            case "up":
                direction = "down";
                spriteNum = 1;
                break;
            case "down":
                direction = "up";
                spriteNum = 1;
                break;
            case "left":
                direction = "right";
                spriteNum = 1;
                break;
            case "right":
                direction = "left";
                spriteNum = 1;
                break;
        }
    }
    public void interact(){}
    public void checkCollision(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
    }
    public void update(){
        setAction();
        checkCollision();
        if (collisionOn == false) {
            deviationError = 0;
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
        else if (collisionOn == true) {
            gp.ui.glitchFx = deviationError > 20;
            switch (direction) {
                case "up":worldY += speed; deviationError++; collisionOn = false; direction = "down"; break;
                case "down":worldY -= speed; deviationError++; collisionOn = false; direction = "up"; break;
                case "left":worldX += speed; deviationError++; collisionOn = false; direction = "right"; break;
                case "right":worldX -= speed; deviationError++; collisionOn = false; direction = "left"; break;
            }
        }
        if (deviationError >= 40) {
            gp.player.rebootCounter++;
            gp.currentMap = 0;
            gp.tileM.loadMap("/res/maps/data/Masako Bedroom.txt", gp.currentMap);
            gp.player.setDefaultValues();
            gp.aSetter.setObject();
            gp.aSetter.setNPC();
        }

        //Animation
        spriteCounter++;
        idle = speed == 0;
        if (spriteCounter > 10 && idle == false) {
            switch (spriteNum) {
                case 1:spriteNum = 2;break;
                case 2:spriteNum = 3;break;
                case 3:spriteNum = 4;break;
                default: spriteNum = 1;break;
            }
            spriteCounter = 0;
        }
        if (spriteCounter > 10 && idle == true) {
            switch (spriteNum) {
                case 1: spriteNum = 2; break;
                case 2: spriteNum = 3; break;
                case 3: spriteNum = 4; break;
                case 4: spriteNum = 5; break;
                case 5: spriteNum = 6; break;
                default: spriteNum = 1; break;
            }
            spriteCounter = 0;
        }
        if (deviationError > 0) {
            
            if (collisionOn == true && deviationError >= 20) {
                rebootCounter++;
                gp.currentMap = 0;
                gp.tileM.loadMap("/res/maps/data/Masako Bedroom.txt", gp.currentMap);
                gp.player.setDefaultValues();
                gp.aSetter.setObject();
                gp.aSetter.setNPC();
            }
        }
        
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
            
        if (worldX + gp.tileSize*5 > gp.player.worldX - gp.player.screenX && 
                worldX - gp.tileSize*5 < gp.player.worldX + gp.player.screenX && 
                worldY + gp.tileSize*5 > gp.player.worldY - gp.player.screenY && 
                worldY - gp.tileSize*5 < gp.player.worldY + gp.player.screenY) {
            //Fix sprite Size rendering
            screenY -= gp.tileSize * imageAdjustLayer;
            switch (direction) {
           case "up":
               //Walking
                if (idle == false) {
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
                    if (spriteNum == 3) {image = up3;}
                    if (spriteNum >= 4) {image = up4;}
                 }
                //Idle
                if (idle == true) {
                    if (spriteNum == 1) {image = idleUp1;}
                    if (spriteNum == 2) {image = idleUp2;}
                    if (spriteNum == 3) {image = idleUp3;}
                    if (spriteNum == 4) {image = idleUp4;}
                    if (spriteNum == 5) {image = idleUp5;}
                    if (spriteNum == 6) {image = idleUp6;}
                }
                break;
            case "down":
                //Walking
                if (idle == false) {
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                    if (spriteNum == 3) {image = down3;}
                    if (spriteNum >= 4) {image = down4;}
                }
                //Idle
                if (idle == true) {
                    if (spriteNum == 1) {image = idleDown1;}
                    if (spriteNum == 2) {image = idleDown2;}
                    if (spriteNum == 3) {image = idleDown3;}
                    if (spriteNum == 4) {image = idleDown4;}
                    if (spriteNum == 5) {image = idleDown5;}
                    if (spriteNum == 6) {image = idleDown6;}
                }
                break;
            case "left":
                //Walking
                if (idle == false) {
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                    if (spriteNum == 3) {image = left3;}
                    if (spriteNum >= 4) {image = left4;}
                }
                //Idle
                if (idle == true) {
                    if (spriteNum == 1) {image = idleLeft1;}
                    if (spriteNum == 2) {image = idleLeft2;}
                    if (spriteNum == 3) {image = idleLeft3;}
                    if (spriteNum == 4) {image = idleLeft4;}
                    if (spriteNum == 5) {image = idleLeft5;}
                    if (spriteNum == 6) {image = idleLeft6;}
                }
                break;
            case "right":
                //Walking
                if (idle == false) {
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                    if (spriteNum == 3) {image = right3;}
                    if (spriteNum >= 4) {image = right4;}
                }
                //Idle
                if (idle == true) {
                    if (spriteNum == 1) {image = idleRight1;}
                    if (spriteNum == 2) {image = idleRight2;}
                    if (spriteNum == 3) {image = idleRight3;}
                    if (spriteNum == 4) {image = idleRight4;}
                    if (spriteNum == 5) {image = idleRight5;}
                    if (spriteNum == 6) {image = idleRight6;}
                }
                break;
            }
            //Shadow
            if (entityType.equals("moving")) {
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillOval(screenX + solidArea.x, screenY + solidArea.y + gp.tileSize * imageAdjustLayer, solidArea.width, solidArea.height);
            }
            
            //Image
            g2.drawImage(image, screenX, screenY, null);
            
            //Draw Clock
            if (name.contains("Clock") ) {
                Date date = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                String time = timeFormat.format(date);
                
                Font serif = new Font("Serif", Font.PLAIN, 10);
                g2.setColor(new Color(255, 56,56));
                g2.setFont(serif);
                
                stringImage(g2, time, screenX + solidArea.x, screenY + solidArea.y + gp.tileSize * imageAdjustLayer, serif, Font.PLAIN, new Color(255, 56,56), 8F);
            }
            
            if (gp.keyH.showDebugText == true && gp.keyH.showDebugTile == true) {
                g2.setColor(Color.black);
                if (collisionOn == true) {
                    g2.setColor(Color.red);
                }
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y + gp.tileSize * imageAdjustLayer, solidArea.width, solidArea.height);
            }
        }
    }
    public BufferedImage setup(String imagePath, String action, String frame, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + "/" + action + "/" + frame + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void searchPath(int goalCol, int goalRow){
        int startCol = (worldX + solidArea.x)/gp.tileSize;
        int startRow = (worldY + solidArea.y)/gp.tileSize;
        gp.pFinder.setNode(startCol, startRow, goalCol, goalRow);
        if (gp.pFinder.search() == true) {
            //Next worldX & worldY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
            
            //Entity's solidArea  position
            int entityLeftX = worldX + solidArea.x;
            int entityRightX = worldX + solidArea.x + solidArea.width;
            int entityTopY = worldY + solidArea.y;
            int entityBottomY = worldY + solidArea.y + solidArea.height;
            
            if (entityLeftX > nextX && entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {
                direction = "left";
            }
            else if (entityLeftX < nextX && entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {
                direction = "right";
                checkCollision();
            }
            else if (entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {
                //left or right
                if (entityTopY > nextY) {
                    direction = "up";
                }
                if (entityTopY < nextY) {
                    direction = "down";
                }
            }
            
            
            else if (entityTopY > nextY && entityLeftX > nextX){
                //up or left
                direction = "up";
                checkCollision();
                if (collisionOn == true) {
                    direction = "left";
                }
            }
            else if (entityTopY > nextY && entityLeftX < nextX){
                //up or right
                direction = "up";
                checkCollision();
                if (collisionOn == true) {
                    direction = "right";
                }
            }
            else if (entityTopY < nextY && entityLeftX > nextX){
                //down or left
                direction = "down";
                checkCollision();
                if (collisionOn == true) {
                    direction = "left";
                }
            }
            else if (entityTopY < nextY && entityLeftX < nextX){
                //down or right
                direction = "down";
                checkCollision();
                if (collisionOn == true) {
                    direction = "right";
                }
            }
            //If reaches the goal, stop search
//            int nextCol = gp.pFinder.pathList.get(0).col;
//            int nextRow = gp.pFinder.pathList.get(0).row;
//            if (nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
//            }
        }
    }
    public void stringImage(Graphics2D g2, String text, int x, int y, Font font, int fontStyle, Color color, float size) {
        // Create a new BufferedImage with the desired width and height
        BufferedImage titleImage = new BufferedImage(gp.getWidth(), gp.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Get a Graphics2D object to draw onto the image
        Graphics2D g2d = titleImage.createGraphics();

        // Enable anti-aliasing for text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(font.deriveFont(fontStyle, size * gp.getPreferredSize().height / 480f)); // Use the fontStyle parameter to set the font style
        g2d.setColor(color);

        // Split the text by newline character (\n) and draw each line separately
        String[] lines = text.split("\n");
        int lineHeight = g2d.getFontMetrics().getHeight();
        for (int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], x, y + i * lineHeight);
        }

        g2.drawImage(titleImage, 0, 0, null);
        g2d.dispose();
    }
}
