package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import main.GamePanel;

public class NPC extends Entity{
    
    public NPC(GamePanel gp){
        super(gp);
        
        //Info
        name = "NPC name";
        direction = "down";
        imageAdjustLayer = 1;
        
        //Hit Box
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize/7;
        solidArea.y = gp.tileSize/2 + gp.tileSize/7;
        solidArea.width = gp.tileSize - (solidArea.x * 2);
        solidArea.height = gp.tileSize/2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        personality = "set personality here";
        }
        entityType = "moving";
        getImage();
        setDialogue();
    }
    public void getImage(){
        //Walking Sprites
        up1 = setup("/res/npc/" + name, "walkUp", "0001", gp.tileSize, gp.tileSize*2);
        up2 = setup("/res/npc/" + name, "walkUp", "0002", gp.tileSize, gp.tileSize*2);
        up3 = setup("/res/npc/" + name, "walkUp", "0003", gp.tileSize, gp.tileSize*2);
        up4 = setup("/res/npc/" + name, "walkUp", "0004", gp.tileSize, gp.tileSize*2);

        down1 = setup("/res/npc/" + name, "walkDown", "0001", gp.tileSize, gp.tileSize*2);
        down2 = setup("/res/npc/" + name, "walkDown", "0002", gp.tileSize, gp.tileSize*2);
        down3 = setup("/res/npc/" + name, "walkDown", "0003", gp.tileSize, gp.tileSize*2);
        down4 = setup("/res/npc/" + name, "walkDown", "0004", gp.tileSize, gp.tileSize*2);

        left1 = setup("/res/npc/" + name, "walkLeft", "0001", gp.tileSize, gp.tileSize*2);
        left2 = setup("/res/npc/" + name, "walkLeft", "0002", gp.tileSize, gp.tileSize*2);
        left3 = setup("/res/npc/" + name, "walkLeft", "0003", gp.tileSize, gp.tileSize*2);
        left4 = setup("/res/npc/" + name, "walkLeft", "0004", gp.tileSize, gp.tileSize*2);

        right1 = setup("/res/npc/" + name, "walkRight", "0001", gp.tileSize, gp.tileSize*2);
        right2 = setup("/res/npc/" + name, "walkRight", "0002", gp.tileSize, gp.tileSize*2);
        right3 = setup("/res/npc/" + name, "walkRight", "0003", gp.tileSize, gp.tileSize*2);
        right4 = setup("/res/npc/" + name, "walkRight", "0004", gp.tileSize, gp.tileSize*2);
    }
    public void update(){//Follow player when near // add this for aggro monsters later
        super.update();
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY - gp.tileSize);
        int tileDistance = (xDistance + yDistance)/gp.tileSize;
        
        if (onPath == false && tileDistance < 5) {
            onPath = true;
        }
        else {
            onPath = false;
        }
    }
    public void setDialogue(){
        //Fixed Dialogue
        dialogues[0] = "✨❕";
        dialogues[1] = "🌼~";
        dialogues[2] = "♡~";
        dialogues[3] = "🎶~";
        dialogues[4] = "—💦";
        dialogues[5] = "☁...";
        dialogues[6] = "🔥🔥🔥";
        dialogues[7] = "...❔";
        dialogues[8] = "💢❕";
        dialogues[9] = "...";
        dialogues[10] = "❕❕❕";
    }
    public void setAction(){
        if (onPath == true) {
            //Follow PLayer
            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
//            if (gp.mouseH.mousePoint != null && gp.mouseH.mousePressed == true) {
//                int goalCol = (gp.cursor.worldX) / gp.tileSize;
//                int goalRow = (gp.cursor.worldY) / gp.tileSize;
//                searchPath(goalCol, goalRow);
//                gp.mouseH.mousePressed = false;
//            }
            searchPath(goalCol, goalRow);
        } 
        else {
            actionLockCounter++;
            if (actionLockCounter >= 30) {
                Random r = new Random();
                int i = r.nextInt(100) + 1;
                int i2 = r.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                    if (collisionOn == true) {
                        actionLockCounter = 0;
                        direction = "left";
                    }
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                    if (collisionOn == true) {
                        actionLockCounter = 0;
                        direction = "right";
                    }
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                    if (collisionOn == true) {
                        actionLockCounter = 0;
                        direction = "down";
                    }
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                    if (collisionOn == true) {
                        actionLockCounter = 0;
                        direction = "up";
                    }
                }
                if (i2 <= 20) {
                    speed = 0;
                    spriteCounter = 0;
                    actionLockCounter = 0;
                }
                if (i2 > 20) {
                    speed = gp.tileSize*4/64;
                    spriteCounter = 0;
                    actionLockCounter = -20;
                }

                int alcTime = r.nextInt(20);
                actionLockCounter = alcTime;
            }
        }
    }
    public void speak(){
        super.speak();
        onPath = true;//After talking npc will follow
    }
}
