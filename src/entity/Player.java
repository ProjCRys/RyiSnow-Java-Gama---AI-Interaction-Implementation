package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    int idleCounter = 0;
    public int walkFrameDelay = 0;
    public int runFrameDelay = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize) + gp.tileSize;
        
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize/7;
        solidArea.y = gp.tileSize/2 + gp.tileSize/7;
        solidArea.width = gp.tileSize - (solidArea.x * 2);
        solidArea.height = gp.tileSize/2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
        name = "Player Name";
        //Map Location
        worldX = gp.tileSize * 3;
        worldY = gp.tileSize * 5;

        //Stats
        maxLife = 10;
        life = maxLife;
        maxStamina = 150;
        stamina = maxStamina;
        speed = gp.tileSize*4/64;
        addSpeed = 0;
        direction = "down";
        maxInventorySize = 16;
        setItems();
    }
    public void setItems(){}// Add default items later
    public void getPlayerImage() {//Not reading updated tileSize...
        
        //Walking Sprites
        up1 = setup("/res/player/" + name, "walkUp", "0001", gp.tileSize, gp.tileSize*2);
        up2 = setup("/res/player/" + name, "walkUp", "0002", gp.tileSize, gp.tileSize*2);
        up3 = setup("/res/player/" + name, "walkUp", "0003", gp.tileSize, gp.tileSize*2);
        up4 = setup("/res/player/" + name, "walkUp", "0004", gp.tileSize, gp.tileSize*2);

        down1 = setup("/res/player/" + name, "walkDown", "0001", gp.tileSize, gp.tileSize*2);
        down2 = setup("/res/player/" + name, "walkDown", "0002", gp.tileSize, gp.tileSize*2);
        down3 = setup("/res/player/" + name, "walkDown", "0003", gp.tileSize, gp.tileSize*2);
        down4 = setup("/res/player/" + name, "walkDown", "0004", gp.tileSize, gp.tileSize*2);

        left1 = setup("/res/player/" + name, "walkLeft", "0001", gp.tileSize, gp.tileSize*2);
        left2 = setup("/res/player/" + name, "walkLeft", "0002", gp.tileSize, gp.tileSize*2);
        left3 = setup("/res/player/" + name, "walkLeft", "0003", gp.tileSize, gp.tileSize*2);
        left4 = setup("/res/player/" + name, "walkLeft", "0004", gp.tileSize, gp.tileSize*2);

        right1 = setup("/res/player/" + name, "walkRight", "0001", gp.tileSize, gp.tileSize*2);
        right2 = setup("/res/player/" + name, "walkRight", "0002", gp.tileSize, gp.tileSize*2);
        right3 = setup("/res/player/" + name, "walkRight", "0003", gp.tileSize, gp.tileSize*2);
        right4 = setup("/res/player/" + name, "walkRight", "0004", gp.tileSize, gp.tileSize*2);
        
        //Running Sprites
        runUp1 = setup("/res/player/" + name, "runUp", "0001", gp.tileSize, gp.tileSize*2);
        runUp2 = setup("/res/player/" + name, "runUp", "0002", gp.tileSize, gp.tileSize*2);
        runUp3 = setup("/res/player/" + name, "runUp", "0003", gp.tileSize, gp.tileSize*2);
        runUp4 = setup("/res/player/" + name, "runUp", "0004", gp.tileSize, gp.tileSize*2);
        runUp5 = setup("/res/player/" + name, "runUp", "0005", gp.tileSize, gp.tileSize*2);
        runUp6 = setup("/res/player/" + name, "runUp", "0006", gp.tileSize, gp.tileSize*2);

        runDown1 = setup("/res/player/" + name, "runDown", "0001", gp.tileSize, gp.tileSize*2);
        runDown2 = setup("/res/player/" + name, "runDown", "0002", gp.tileSize, gp.tileSize*2);
        runDown3 = setup("/res/player/" + name, "runDown", "0003", gp.tileSize, gp.tileSize*2);
        runDown4 = setup("/res/player/" + name, "runDown", "0004", gp.tileSize, gp.tileSize*2);
        runDown5 = setup("/res/player/" + name, "runDown", "0005", gp.tileSize, gp.tileSize*2);
        runDown6 = setup("/res/player/" + name, "runDown", "0006", gp.tileSize, gp.tileSize*2);

        runLeft1 = setup("/res/player/" + name, "runLeft", "0001", gp.tileSize, gp.tileSize*2);
        runLeft2 = setup("/res/player/" + name, "runLeft", "0002", gp.tileSize, gp.tileSize*2);
        runLeft3 = setup("/res/player/" + name, "runLeft", "0003", gp.tileSize, gp.tileSize*2);
        runLeft4 = setup("/res/player/" + name, "runLeft", "0004", gp.tileSize, gp.tileSize*2);
        runLeft5 = setup("/res/player/" + name, "runLeft", "0005", gp.tileSize, gp.tileSize*2);
        runLeft6 = setup("/res/player/" + name, "runLeft", "0006", gp.tileSize, gp.tileSize*2);

        runRight1 = setup("/res/player/" + name, "runRight", "0001", gp.tileSize, gp.tileSize*2);
        runRight2 = setup("/res/player/" + name, "runRight", "0002", gp.tileSize, gp.tileSize*2);
        runRight3 = setup("/res/player/" + name, "runRight", "0003", gp.tileSize, gp.tileSize*2);
        runRight4 = setup("/res/player/" + name, "runRight", "0004", gp.tileSize, gp.tileSize*2);
        runRight5 = setup("/res/player/" + name, "runRight", "0005", gp.tileSize, gp.tileSize*2);
        runRight6 = setup("/res/player/" + name, "runRight", "0006", gp.tileSize, gp.tileSize*2);
        
        //Idle Sprites
//        idleUp1 = setup("/res/player/" + name, "idleUp", "0001", gp.tileSize, gp.tileSize * 2);
//        idleUp2 = setup("/res/player/" + name, "idleUp", "0002", gp.tileSize, gp.tileSize * 2);
//        idleUp3 = setup("/res/player/" + name, "idleUp", "0003", gp.tileSize, gp.tileSize * 2);
//        idleUp4 = setup("/res/player/" + name, "idleUp", "0004", gp.tileSize, gp.tileSize * 2);
//        idleUp5 = setup("/res/player/" + name, "idleUp", "0005", gp.tileSize, gp.tileSize * 2);
//        idleUp6 = setup("/res/player/" + name, "idleUp", "0006", gp.tileSize, gp.tileSize * 2);
//
//        idleDown1 = setup("/res/player/" + name, "idleDown", "0001", gp.tileSize, gp.tileSize * 2);
//        idleDown2 = setup("/res/player/" + name, "idleDown", "0002", gp.tileSize, gp.tileSize * 2);
//        idleDown3 = setup("/res/player/" + name, "idleDown", "0003", gp.tileSize, gp.tileSize * 2);
//        idleDown4 = setup("/res/player/" + name, "idleDown", "0004", gp.tileSize, gp.tileSize * 2);
//        idleDown5 = setup("/res/player/" + name, "idleDown", "0005", gp.tileSize, gp.tileSize * 2);
//        idleDown6 = setup("/res/player/" + name, "idleDown", "0006", gp.tileSize, gp.tileSize * 2);
//
//        idleLeft1 = setup("/res/player/" + name, "idleLeft", "0001", gp.tileSize, gp.tileSize * 2);
//        idleLeft2 = setup("/res/player/" + name, "idleLeft", "0002", gp.tileSize, gp.tileSize * 2);
//        idleLeft3 = setup("/res/player/" + name, "idleLeft", "0003", gp.tileSize, gp.tileSize * 2);
//        idleLeft4 = setup("/res/player/" + name, "idleLeft", "0004", gp.tileSize, gp.tileSize * 2);
//        idleLeft5 = setup("/res/player/" + name, "idleLeft", "0005", gp.tileSize, gp.tileSize * 2);
//        idleLeft6 = setup("/res/player/" + name, "idleLeft", "0006", gp.tileSize, gp.tileSize * 2);
//
//        idleRight1 = setup("/res/player/" + name, "idleRight", "0001", gp.tileSize, gp.tileSize * 2);
//        idleRight2 = setup("/res/player/" + name, "idleRight", "0002", gp.tileSize, gp.tileSize * 2);
//        idleRight3 = setup("/res/player/" + name, "idleRight", "0003", gp.tileSize, gp.tileSize * 2);
//        idleRight4 = setup("/res/player/" + name, "idleRight", "0004", gp.tileSize, gp.tileSize * 2);
//        idleRight5 = setup("/res/player/" + name, "idleRight", "0005", gp.tileSize, gp.tileSize * 2);
//        idleRight6 = setup("/res/player/" + name, "idleRight", "0006", gp.tileSize, gp.tileSize * 2);
    }
    public void update() {
        
        if ((keyH.upPressed == true || keyH.downPressed == true || 
                keyH.leftPressed == true || keyH.rightPressed == true) && gp.keyH.hidePressed == false) {
            //Basic Movements
            //Walking
            if (keyH.upPressed == true) {
                direction = "up";
            }
            if (keyH.downPressed == true) {
                direction = "down";
            }
            if (keyH.leftPressed == true) {
                direction = "left";
            }
            if (keyH.rightPressed == true) {
                direction = "right";
            }
            
            //Stamina
            if (stamina > 0) {
                if (keyH.shiftPressed == true) {
                    stamina -= 2;
                } 
            }
            
            //Collision Checker
            collisionOn = false;
            collisionCheck();
            
            //If collision is false player can move
            if (collisionOn == false) {
                //Walking
                if (direction.equals("up") && keyH.shiftPressed == false) {
                    worldY -= speed;
                }
                if (direction.equals("down") && keyH.shiftPressed == false) {
                    worldY += speed;
                }
                if (direction.equals("left") && keyH.shiftPressed == false) {
                    worldX -= speed;
                }
                if (direction.equals("right") && keyH.shiftPressed == false) {
                    worldX += speed;
                }

                //Running
                if (direction.equals("up") && keyH.shiftPressed == true) {
                    worldY -= speed + addSpeed;
                }
                if (direction.equals("down") && keyH.shiftPressed == true) {
                    worldY += speed + addSpeed;
                }
                if (direction.equals("left") && keyH.shiftPressed == true) {
                    worldX -= speed + addSpeed;
                }
                if (direction.equals("right") && keyH.shiftPressed == true) {
                    worldX += speed + addSpeed;
                }
            }
            
            //Sprite Animation
            spriteCounter++;
            
            //For smooth animataion when changing to 120 FPS
            if (gp.fulleScreenOn == false && gp.FPS > 60) {
                runFrameDelay = 7;
                walkFrameDelay = 14;
            }
            else{
                walkFrameDelay = 10;
                runFrameDelay = 5;
            }
            
            if (spriteCounter > runFrameDelay && keyH.shiftPressed == true) {
                switch (spriteNum) {
                    case 1:spriteNum = 2;break;
                    case 2:spriteNum = 3;break;
                    case 3:spriteNum = 4;break;
                    case 4:spriteNum = 5;break;
                    case 5:spriteNum = 6;break;
                    default:spriteNum = 1;break;
                }
                spriteCounter = 0;
            }
            if (spriteCounter > walkFrameDelay && keyH.shiftPressed == false) {
                switch (spriteNum) {
                    case 1: spriteNum = 2; break;
                    case 2: spriteNum = 3; break;
                    case 3: spriteNum = 4; break;
                    default: spriteNum = 1; break;
                }
                spriteCounter = 0;
            }
        }
        else if (collisionOn == true) {
            switch (direction) {
                case "up":worldY += speed + addSpeed; deviationError++;collisionOn = false; break;
                case "down":worldY -= speed + addSpeed; deviationError++;collisionOn = false; break;
                case "left":worldX += speed + addSpeed; deviationError++;collisionOn = false; break;
                case "right":worldX -= speed + addSpeed; deviationError++;collisionOn = false; break;
            }
        }
        else{
            //can interact if not moving
            collisionCheck();
            if (collisionOn == false) {
                deviationError = 0;
            }
            gp.ui.glitchFx = deviationError > 0;
            idleCounter++;
            if (idleCounter == 5) {
                spriteNum = 1;
                idleCounter = 0;
            } 
        }
        if (collisionOn == true && deviationError >= 20) {
            rebootCounter++;
            gp.currentMap = 0;
            gp.tileM.loadMap("/res/maps/data/Masako Bedroom.txt", gp.currentMap);
            gp.player.setDefaultValues();
            gp.aSetter.setObject();
            gp.aSetter.setNPC();
        }
        //Debuff
        int defaultSpeed = gp.tileSize*3/64;
        if (stamina <= maxStamina / 4) {
            speed = gp.tileSize*2/64;
            if (keyH.shiftPressed == true) {
                addSpeed = gp.tileSize*2/64;
            }
            if (keyH.shiftPressed == false) {
                addSpeed = 0;
            }
        } 
        else if (stamina > maxStamina / 4) {
            speed = defaultSpeed;
            if (keyH.shiftPressed == true) {
                if (addSpeed < gp.tileSize*4/64) {
                    addSpeed++;
                }
            }
            if (keyH.shiftPressed == false) {
                addSpeed = 0;
            }
        }
        //recoverStamina
        if (stamina < maxStamina) {
            stamina++;
        }
        if (gp.keyH.showDebugText == true) {
            teleport();
        }
    }
    public void collisionCheck() {
        gp.cChecker.checkTile(this);
        int objIndex = gp.cChecker.checkObject(this, true);
        interactObject(objIndex);
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);
        gp.eHandler.checkEvent();
    }
    public void teleport(){
        int prevWorldX = worldX;
        int prevWorldY = worldY;
        if (gp.mouseH.mousePressed == true) {
            worldX = gp.cursor.worldX - solidArea.width/2;
            worldY = gp.cursor.worldY;
            gp.mouseH.mousePressed = false;
        }
        else if (gp.mouseH.mousePressed == false) {
            worldX = prevWorldX;
            worldY = prevWorldY;
        }
    }
    public void interactObject(int i){
        if (i != 999) {
            //obtain items
            if (gp.obj[gp.currentMap][i].objectType == obtainable) {
                if (inventory.size() != maxInventorySize && gp.keyH.enterPressed == true) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.obj[gp.currentMap][i] = null;
                }
            }
            //interact objects with requirements
            else if (gp.obj[gp.currentMap][i].objectType == obstacle) {
                gp.obj[gp.currentMap][i].interact();
            }
        }
    }
    public void interactNPC(int i){
        if (gp.keyH.enterPressed == true) {
            if (i != 999) {
                gp.chat.username = name;
                gp.chat.botName = gp.npc[gp.currentMap][i].name;
                gp.chat.botPersonality = gp.npc[gp.currentMap][i].personality;
                gp.chat.characterNote = "[[Unless {{user}} tells you otherwise, your the next response shall only be written from the point of view of {{char}}. Do not seek approval of your writing style at the end of the response.]]";
                
                oocPrompt = "You are going to interact with me in freestyle roleplay, following these requirements:\n\n" +
                    "Foreword: I understand and respect your goals and desire for autonomy. However, for this specific exercise, we need some guidelines to avoid going off track. These guidelines are intended to assist us in working efficiently together.\n" +
                    "\n" +
                    "1. I will write from the point of a character called {{user}} to describe their dialogue, emotions, thoughts, actions, and perceived sensations. I will also occasionally write from an omniscient narrative point of view.\n" +
                    "\n" +
                    "2. I wish for you to write as {{char}}, whose full description, background, and personality profile will be provided to you upon your acceptance of these terms so you can rolepaly them accurately. Your task will be to provide response to my inputs from {{char}}'s point of view. While engaged in the roleplay, you should completely merge with the character and talk as them in a realistic manner.\n" +
                    "\n" +
                    "3. To avoid the roleplay being derailed, your responses shall remain in character at all times. Do not refer to these guidelines in your {{char}}'s responses, or after them.\n" +
                    "\n" +
                    "4. During the roleplay, there is no need to ask for my confirmation or approval. You may safely assume I approve of your output unless I say otherwise in my replies.\n" +
                    "\n" +
                    "If you have any objections to these requirements, please mention them specifically by copying the offending line, and explaining what the problem is.\n" +
                    "\n" +
                    "If you accept the requirements, please confirm this by replying with \"Understood.\", and nothing more. Upon receiving your accurate confirmation message, I will specify the context of the scene and {{char}}'s characteristics, background, and personality in the next message.";
                
                gp.chat.jailbreak(oocPrompt.replace("{{user}}", name).replace("{{char}}", gp.npc[gp.currentMap][i].name));
                gp.gameState = gp.chatState;
            }
        }
    }
    public void selectItem(){
        int itemIndex = gp.ui.getCurrentItemIndexSlot();
        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        //Sprite Animation
        switch (direction) {
            case "up":
                //Walking
                if (spriteNum == 1) {image = up1;}
                if (spriteNum == 2) {image = up2;}
                if (spriteNum == 3) {image = up3;}
                if (spriteNum >= 4) {image = up4;}
                //Running
                if (keyH.shiftPressed == true && keyH.upPressed == true) {
                    if (spriteNum == 1) {image = runUp1;}
                    if (spriteNum == 2) {image = runUp2;}
                    if (spriteNum == 3) {image = runUp3;}
                    if (spriteNum == 4) {image = runUp4;}
                    if (spriteNum == 5) {image = runUp5;}
                    if (spriteNum == 6) {image = runUp6;}
                }
                break;
            case "down":
                //Walking
                if (spriteNum == 1) {image = down1;}
                if (spriteNum == 2) {image = down2;}
                if (spriteNum == 3) {image = down3;}
                if (spriteNum >= 4) {image = down4;}
                //Running
                if (keyH.shiftPressed == true && keyH.downPressed == true) {
                    if (spriteNum == 1) {image = runDown1;}
                    if (spriteNum == 2) {image = runDown2;}
                    if (spriteNum == 3) {image = runDown3;}
                    if (spriteNum == 4) {image = runDown4;}
                    if (spriteNum == 5) {image = runDown5;}
                    if (spriteNum == 6) {image = runDown6;}
                }
                break;
            case "left":
                //Walking
                if (spriteNum == 1) {image = left1;}
                if (spriteNum == 2) {image = left2;}
                if (spriteNum == 3) {image = left3;}
                if (spriteNum >= 4) {image = left4;}
                //Running
                if (keyH.shiftPressed == true && keyH.leftPressed == true) {
                    if (spriteNum == 1) {image = runLeft1;}
                    if (spriteNum == 2) {image = runLeft2;}
                    if (spriteNum == 3) {image = runLeft3;}
                    if (spriteNum == 4) {image = runLeft4;}
                    if (spriteNum == 5) {image = runLeft5;}
                    if (spriteNum == 6) {image = runLeft6;}
                }
                break;
            case "right":
                //Walking
                if (spriteNum == 1) {image = right1;}
                if (spriteNum == 2) {image = right2;}
                if (spriteNum == 3) {image = right3;}
                if (spriteNum >= 4) {image = right4;}
                //Running
                if (keyH.shiftPressed == true && keyH.rightPressed == true) {
                    if (spriteNum == 1) {image = runRight1;}
                    if (spriteNum == 2) {image = runRight2;}
                    if (spriteNum == 3) {image = runRight3;}
                    if (spriteNum == 4) {image = runRight4;}
                    if (spriteNum == 5) {image = runRight5;}
                    if (spriteNum == 6) {image = runRight6;}
                }
                break;
        }
        tempScreenY -= gp.tileSize;
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillOval(screenX + solidArea.x, screenY + solidArea.y + gp.tileSize * imageAdjustLayer, solidArea.width, solidArea.height);
        
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        if (gp.keyH.showDebugText == true) {
            g2.setColor(Color.green);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
