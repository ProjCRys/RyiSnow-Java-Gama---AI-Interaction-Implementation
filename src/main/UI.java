package main;

import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import object.UI.UI_HP;
import javax.sound.sampled.*;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    public Label label;

    public Font purisaB, pollockRegular, serif, klein;
    
    BufferedImage fullBaseHP, halfBaseHP, oneHP, twoHP, threeHP, fourHP;
    
    public boolean notifON = false;
    public String currentDialogue = "", response = "";
    
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    
    public int timePassed;
    int subState = 0;
    
    int counter = 0;
    boolean fadeInDone = false;
    
    Rectangle button;
    Rectangle mouse;
    
    BufferedImage titleScreen;
    public boolean glitchFx = false;
    
    public UI(GamePanel gp) {
        this.gp = gp;
        
        try {
            //For Child characters
            InputStream is = getClass().getResourceAsStream("/res/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
            
            //For Cursed text
            is = getClass().getResourceAsStream("/res/font/Pollock5CTT Regular.ttf");
            pollockRegular = Font.createFont(Font.TRUETYPE_FONT, is);
            
            //General use
            is = getClass().getResourceAsStream("/res/font/Klein-Text-Book-trial.ttf");
            klein = Font.createFont(Font.TRUETYPE_FONT, is);
            
            //Emoji and Symbols
            serif = new Font("Serif", Font.PLAIN, 40);
        } 
        catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }
        
        //Create HUD Object
        Entity hp = new UI_HP(gp);
        halfBaseHP = hp.image;
        fullBaseHP = hp.image2;
        oneHP = hp.image3;
        twoHP = hp.image4;
        threeHP = hp.image5;
        fourHP = hp.image6;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(klein);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        
        //Title state
        if (gp.gameState == gp.titleState) {
            drawBGTitleScreen();
            drawTitleScreen();
            drawButtonsTitleScreen();
        }
        //Play state
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        //Pause state
        if (gp.gameState == gp.pauseState || gp.keyH.hidePressed == true) {
            grayScale();
        }
        //Dialogue state
        if (gp.gameState == gp.dialogueState) {
            drawDialogieScreen();
        }
        if (gp.gameState == gp.chatState) {
            drawChatScreen();
        }
        //Chara Status state
        if (gp.gameState == gp.charaStatusState) {
            drawInventory();
        }
        //Option State
        if (gp.gameState == gp.optionState) {
            drawOptionMenu();
        }
        //Transition State
        if (gp.gameState == gp.transitionState) {
            drawTransitionFadeIn();
        }
        if (fadeInDone == true) {
            drawTransitionFadeOut();
        }
        if (glitchFx == true) {
            glitchEffect();
        }
    }
    
    public void drawTitleScreen() {
        //Title
        String text = "ProjCrys";
        int x = gp.tileSize * 10;
        int y = gp.tileSize * 2;
        stringImage(text, x, y, pollockRegular, Font.BOLD, Color.RED, 92F);
        
        //Menu KeyInputs
        text = "New Game";
        x = gp.tileSize * 11;
        y += gp.tileSize * 4;
        stringImage(text, x, y, klein, Font.BOLD, Color.gray, 40F);
        if (commandNum == 0) {
            stringImage(text, x, y, klein, Font.BOLD, Color.white, 40F);
        }

        text = "Load Game";
        x = gp.tileSize * 11;
        y += gp.tileSize;
        stringImage(text, x, y, klein, Font.BOLD, Color.gray, 40F);
        if (commandNum == 1) {
            stringImage(text, x, y, klein, Font.BOLD, Color.white, 40F);
        }
        
        text = "Exit";
        x = gp.tileSize * 11;
        y += gp.tileSize;
        stringImage(text, x, y, klein, Font.BOLD, Color.gray, 40F);
        if (commandNum == 2) {
            stringImage(text, x, y, klein, Font.BOLD, Color.white, 40F);
        }
    }
    public void drawButtonsTitleScreen() {
        //New Game
        button = new Rectangle(gp.tileSize*11, gp.tileSize * 21 / 4, gp.tileSize * 9/2, gp.tileSize);
//        g2.drawRect(button.x, button.y, button.width, button.height);//show selection area
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 0;
                if (gp.mouseH.mousePressed == true) {
                    gp.stopMusic();
                    gp.playMusic(0);
                    gp.gameState = gp.playState;
                }
            }
        }

        //Load Game
        button = new Rectangle(gp.tileSize*11, gp.tileSize * 25 / 4, gp.tileSize * 9/2, gp.tileSize);
//        g2.drawRect(button.x, button.y, button.width, button.height);//show selection area
        if (gp.mouseH.mousePoint != null) {
            if (button.intersects(mouse)) {
                commandNum = 1;
                if (gp.mouseH.mousePressed == true) {
                }
            }
        }

        //Exit
        button = new Rectangle(gp.tileSize*11, gp.tileSize * 29 / 4, gp.tileSize * 9/2, gp.tileSize);
//        g2.drawRect(button.x, button.y, button.width, button.height);//show selection area
        if (gp.mouseH.mousePoint != null) {
            if (button.intersects(mouse)) {
                commandNum = 2;
                if (gp.mouseH.mousePressed == true) {
                    System.exit(0);
                }
            }
        }
    }
    public void drawBGTitleScreen(){
        //Title Screen
        titleScreen = setup("/res/titleScreen/Photo_1Opt", gp.screenWidth, gp.screenHeight);
        int x = 0,y = 0;
        while (x < gp.screenWidth && y < gp.screenHeight) {            
            try {
                g2.drawImage(titleScreen.getSubimage(x, y, gp.tileSize, gp.tileSize), x, y, null);
                x += gp.tileSize;
                if (x >= gp.screenWidth) {
                    x = 0;
                    y += gp.tileSize;
                }
            } catch (Exception e) {
                g2.setColor(Color.black);
                g2.drawRect(x, y, gp.tileSize, gp.tileSize);
            }
        }
    }
    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void drawPlayerLife(){
        //Draw HP
        int x = 5;
        int y = gp.tileSize/4 - 16;
        int i = 0;
        
        if (gp.player.life == 1) {
            g2.drawImage(halfBaseHP, x, y,null);
        }
        if (gp.player.life >= 2) {
            g2.drawImage(fullBaseHP, x, y,null);
        }
        while (i < gp.player.life - 2) {
            g2.drawImage(oneHP, x, y,null);
            i++;
            if (i < gp.player.life - 2) {
                g2.drawImage(twoHP, x, y,null);
            }
            i++;
            if (i < gp.player.life - 2) {
                g2.drawImage(threeHP, x, y,null);
            }
            i++;
            if (i < gp.player.life - 2) {
                g2.drawImage(fourHP, x, y,null);
            }
            i++;
            y -= 20;
        }
    }
    public void grayScale(){
        Graphics2D graphic = gp.alteredScreen.createGraphics();
        for (int i = 0; i < gp.alteredScreen.getHeight(); i++) {
            for (int j = 0; j < gp.alteredScreen.getWidth(); j++) {
                Color c = new Color(gp.alteredScreen.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.100);
                Color newColor = new Color(
                        red + green + blue,
                        red + green + blue,
                        red + green + blue);
                gp.alteredScreen.setRGB(j, i, newColor.getRGB());
            }
        }
    }
    public void glitchEffect() {
        Graphics2D graphic = gp.alteredScreen.createGraphics();
        Random random = new Random();
        int screenWidth = gp.alteredScreen.getWidth();
        int screenHeight = gp.alteredScreen.getHeight();

        for (int i = 0; i < screenHeight; i++) {
            for (int j = 0; j < screenWidth; j++) {
                Color c = new Color(gp.alteredScreen.getRGB(j, i));

                // Apply glitch effect calculation
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                // Generate a random displacement for each pixel
                int displacement = random.nextInt(11) - 5; // Random displacement range: -5 to 5

                // Apply horizontal displacement to the pixel
                int newX = j + displacement;
                if (newX < 0) {
                    newX = screenWidth - 1;
                } else if (newX >= screenWidth) {
                    newX = 0;
                }

                // Set the glitched color on the alteredScreen at the new position
                gp.alteredScreen.setRGB(newX, i, new Color(red, green, blue).getRGB());
            }
        }

        // Randomly modify the RGB values (raise or reduce) for a group of pixels
        int numPixels = screenWidth * screenHeight / 100; // Adjust divisor for desired density

        for (int i = 0; i < numPixels; i++) {
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);

            Color c = new Color(gp.alteredScreen.getRGB(x, y));

            int redDelta = random.nextInt(101) - 50; // Random value between -25 and 25
            int greenDelta = random.nextInt(101) - 50; // Random value between -25 and 25
            int blueDelta = random.nextInt(101) - 50; // Random value between -25 and 25

            int red = Math.max(0, Math.min(255, c.getRed() + redDelta));
            int green = Math.max(0, Math.min(255, c.getGreen() + greenDelta));
            int blue = Math.max(0, Math.min(255, c.getBlue() + blueDelta));

            gp.alteredScreen.setRGB(x, y, new Color(red, green, blue).getRGB());
        }

        // Draw the alteredScreen onto the Graphics2D object
        graphic.drawImage(gp.alteredScreen, 0, 0, null);
    }
    public void drawDialogieScreen(){
        //Window
        int x = gp.tileSize;
        int y = gp.tileSize * 6 + (gp.tileSize/2);
        drawDialogBox();
        
        //Texts
        g2.setFont(serif);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        Color c  = new Color(0, 0, 0, 210); //RGB and opacity(0-255) 0 being transparent and 255 being not.
        g2.setColor(c);
        y += gp.tileSize - 15;
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 37;
        }
        
        y = gp.tileSize * 6 + (gp.tileSize/2);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
        c  = new Color(255, 255, 255, 210); //RGB and opacity(0-255) 0 being transparent and 255 being not.
        g2.setColor(c);
        y += gp.tileSize - 15;
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 37;
        }
    }
    public void drawChatScreen() {
        //Window
        int x = gp.tileSize;
        int y = gp.tileSize * 7;
        drawDialogBox();
        String text = gp.keyH.text;
        stringImage(text, x, y, purisaB, Font.PLAIN,Color.white, 13F);
    }
    public void drawDialogBox(){
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/ui/Dialog box.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(image, 0, 0, gp.tileSize*16, gp.tileSize*9,null);
        stringImage("Enter", gp.tileSize*906/(gp.orgTileSize*gp.scale), gp.tileSize*552/(gp.orgTileSize*gp.scale), serif, Font.PLAIN,Color.white, 10F);
    }
    public void drawInventory() {
        //Frame
        int frameX = gp.screenWidth - gp.tileSize*6;
        int frameY = gp.tileSize/2;
        int frameWidth = gp.tileSize*5;
        int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        //Slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize+8;
        
        //Player Items
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).down1 , slotX, slotY, null);
            slotX += slotSize;
            if (i == 3 || i == 7 || i == 11) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }
        
        //Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;
        
        //Draw Cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 5, 5);
        
        //Item Desc Frame
        int descFrameX = gp.tileSize * 7;
        int descFrameY = frameY + frameHeight + gp.tileSize;
        int descFrameWidth = gp.tileSize * 8;
        int descFrameHeight = gp.tileSize*2;
        g2.setStroke(new BasicStroke(4));
        
        
        //Item Desc Text
        int textX = descFrameX + 20;
        int textY = descFrameY + 40;
        g2.setFont(g2.getFont().deriveFont(20F));
        int itemIndex = getCurrentItemIndexSlot();
        
        if (itemIndex < gp.player.inventory.size()) {
            drawSubWindow(descFrameX, descFrameY, descFrameWidth, descFrameHeight);
            for (String line: gp.player.inventory.get(itemIndex).itemDesc.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 25;
            }
        }
    }
    public void drawOptionMenu(){
        g2.setColor(Color.white);
        g2.setFont(klein.deriveFont(20F));
        
        //Frame
        int frameWidth = gp.tileSize*16;
        int frameHeight = gp.tileSize*7;
        int frameX = 0;
        int frameY = gp.tileSize;
        
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/ui/optionBox.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(image, frameX, frameY, frameWidth, frameHeight, null);
        
        frameX = gp.tileSize * 4;
        frameY = gp.tileSize;
        switch (subState) {
            case 0: option_top(frameX, frameY); option_top_button(); break;
            case 1: option_fullscreenNotification(frameX, frameY); option_fullscreenNotification_and_controls_button(); break;
            case 2: option_controls(frameX, frameY); option_fullscreenNotification_and_controls_button(); break;
            case 3: option_exitNotification(frameX, frameY); option_exitNotification_button(); break;
        }
        gp.keyH.enterPressed = false;
    }
    public void option_top(int frameX, int frameY){
        int textX;
        int textY;
        //Title
        String text = "Options";
        textY = frameY + gp.tileSize*5/4;
        setCenterImage(text, textY, klein, Font.BOLD, Color.white, 20F);
        
        //Fullscreen On/Off
        textX = frameX + gp.tileSize/2;
        textY += gp.tileSize;
        if (commandNum == 0) {
            //replace drawString with drawImage
            stringImage("Fullscreen", textX, textY, klein, Font.BOLD,Color.white, 18F);
            if (gp.keyH.enterPressed == true) {
                if (gp.fulleScreenOn == false) {
                    gp.fulleScreenOn = true;
                }
                else if (gp.fulleScreenOn == true) {
                    gp.fulleScreenOn = false;
                }
                subState = 1;
            }
        }
        else{
            stringImage("Fullscreen", textX, textY, klein, Font.PLAIN,Color.darkGray, 18F);
        }
        
        
        //Music
        textY += gp.tileSize/2;
        if (commandNum == 1) {
            //replace drawString with drawImage
            stringImage("Music", textX, textY, klein, Font.BOLD,Color.white, 18F);
        }
        else{
            stringImage("Music", textX, textY, klein, Font.PLAIN,Color.darkGray, 18F);
        }
        
        //SFX
        textY += gp.tileSize/2;
        if (commandNum == 2) {
            //replace drawString with drawImage
            stringImage("SFX", textX, textY, klein, Font.BOLD,Color.white, 18F);
        }
        else{
            stringImage("SFX", textX, textY, klein, Font.PLAIN,Color.darkGray, 18F);
        }
        
        //Control
        textY += gp.tileSize/2;
        if (commandNum == 3) {
            //replace drawString with drawImage
            stringImage("Controls", textX, textY, klein, Font.BOLD,Color.white, 18F);
            if (gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }
        else{
            stringImage("Controls", textX, textY, klein, Font.PLAIN,Color.darkGray, 18F);
        }
        
        //Exit
        textY += gp.tileSize/2;
        if (commandNum == 4) {
            //replace drawString with drawImage
            stringImage("Exit", textX, textY, klein, Font.BOLD,Color.white, 18F);
            if (gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }
        else{
            stringImage("Exit", textX, textY, klein, Font.PLAIN,Color.darkGray, 18F);
        }
        
        //Back
        text = "Back";
        textX = gp.tileSize*11;
        textY = gp.tileSize*434/(gp.orgTileSize*gp.scale);
        if (commandNum == 5) {
            //replace drawString with drawImage
            stringImage(text, textX, textY, klein, Font.BOLD,Color.white, 14F);
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }
        else {
            stringImage(text, textX, textY, klein, Font.PLAIN, Color.gray, 14F);
        }

        //FullScreen Checkbox
        textX = frameX + gp.tileSize*7;
        textY = frameY + gp.tileSize*2;
        g2.setColor(Color.darkGray);
        if (commandNum == 0) {
            g2.setColor(Color.white);
        }
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(textX - 6, textY, gp.tileSize/4, gp.tileSize/4);
        if (gp.fulleScreenOn == true) {
            g2.fillRect(textX - 6, textY, gp.tileSize/4, gp.tileSize/4);
        }
        
        //Music volume
        textY += gp.tileSize/2;
        g2.setColor(Color.darkGray);
        if (commandNum == 1) {
            g2.setColor(Color.white);
        }
        g2.drawRect(textX - gp.tileSize*4 + (gp.tileSize/4), textY, gp.tileSize*4 - gp.tileSize*6/64, gp.tileSize/4); //width = 256, scale 256/10 or 128/5
        int volumeWidth = (gp.tileSize*4/10) * gp.music.volumeScale - gp.tileSize*6/64;
        g2.fillRect(textX - gp.tileSize*4 + (gp.tileSize/4), textY, volumeWidth, gp.tileSize/4);
        
        //SFX volume
        textY += gp.tileSize/2;
        g2.setColor(Color.darkGray);
        if (commandNum == 2) {
            g2.setColor(Color.white);
        }
        g2.drawRect(textX - gp.tileSize*4 + (gp.tileSize/4), textY, gp.tileSize*4 - gp.tileSize*6/64, gp.tileSize/4); //width = 256, scale 256/10 or 128/5
        volumeWidth = (gp.tileSize*4/10) * gp.sfx.volumeScale - gp.tileSize*6/64;
        g2.fillRect(textX - gp.tileSize*4 + (gp.tileSize/4), textY, volumeWidth, gp.tileSize/4);
        
        gp.config.saveConfig();
    }
    public void option_top_button() {
        //Full Screen gp.tileSize*434/(gp.orgTileSize*gp.scale)
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*184/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 0;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 0) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mousePressed == true) {
                    if (gp.fulleScreenOn == false) {
                        gp.fulleScreenOn = true;
                    } else if (gp.fulleScreenOn == true) {
                        gp.fulleScreenOn = false;
                    }
                    subState = 1;
                }
            }
        }
        //Music
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*216/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 1;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 1) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mouseWheelRotation < 0 && gp.music.volumeScale < 10) {//up volume
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.mouseH.mouseWheelRotation = 0;
                }
                if (gp.mouseH.mouseWheelRotation > 0 && gp.music.volumeScale > 0) {//down volume
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.mouseH.mouseWheelRotation = 0;
                }
            }
        }

        //SFX
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*248/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 2;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 2) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mouseWheelRotation < 0 && gp.sfx.volumeScale < 10) {//up volume
                    gp.sfx.volumeScale++;
                    gp.mouseH.mouseWheelRotation = 0;
                }
                if (gp.mouseH.mouseWheelRotation > 0 && gp.sfx.volumeScale > 0) {//down volume
                    gp.sfx.volumeScale--;
                    gp.mouseH.mouseWheelRotation = 0;
                }
            }
        }

        //Controls
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*280/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 3;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 3) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mousePressed == true) {
                    subState = 2;
                    commandNum = 0;
                }
            }
        }
        
        //Exit
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*312/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 4;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 4) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mousePressed == true) {
                    subState = 3;
                    commandNum = 0;
                }
            }
        }
        
        //Back
        button = new Rectangle(gp.tileSize*690/(gp.orgTileSize*gp.scale), gp.tileSize*408/(gp.orgTileSize*gp.scale), gp.tileSize*72/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
//        g2.drawRect(button.x, button.y, button.width, button.height);//show selection area
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 5;
                if (gp.mouseH.mousePressed == true) {
                    gp.gameState = gp.playState;
                    gp.mouseH.mousePressed = false;
                }
            }
        }
    }
    public void option_fullscreenNotification(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize *7/4;
        
        currentDialogue = "The change will take effect \nafter restarting the game.";
        stringImage(currentDialogue, textX, textY, klein, Font.PLAIN,Color.white, 20F);
        
        //Back
        String text = "Back";
        textX = gp.tileSize*11;
        textY = gp.tileSize*434/(gp.orgTileSize*gp.scale);
        stringImage(text, textX, textY, klein, Font.BOLD,Color.white, 14F);
        if (commandNum == 0) {
            if (gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }
    }
    public void option_fullscreenNotification_and_controls_button(){
        //Back
        button = new Rectangle(gp.tileSize*690/(gp.orgTileSize*gp.scale), gp.tileSize*408/(gp.orgTileSize*gp.scale), gp.tileSize*72/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
//        g2.drawRect(button.x, button.y, button.width, button.height);//show selection area
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 0;
                if (gp.mouseH.mousePressed == true) {
                    subState = 0;
                }
                gp.mouseH.mousePressed = false;
            }
        }
    }
    public void option_controls(int frameX, int frameY){
        int textX;
        int textY;
        
        //Title
        String text = "Controls";
        textY = frameY + gp.tileSize*5/4;
        setCenterImage(text, textY, klein, Font.BOLD,Color.white, 20F);
        
        //Description
        text = "Movements: WASD or Arrow keys\n"
                + "Sprint: Shift\n"
                + "Interact: Enter\n"
                + "Inventory: Q\n"
                + "Hide: Space Bar\n"
                + "Pause: P\n"
                + "Options: Escape key";
        textX = frameX + gp.tileSize/2;
        textY += gp.tileSize;
        stringImage(text, textX, textY, klein, Font.PLAIN,Color.white, 18F);
        
        text = "Back";
        textX = gp.tileSize*11;
        textY = gp.tileSize*434/(gp.orgTileSize*gp.scale);
        stringImage(text, textX, textY, klein, Font.BOLD,Color.white, 14F);
        if (commandNum == 0) {
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 3;
            }
        }
    }  
    public void option_exitNotification(int frameX, int frameY){
        int textX;
        int textY;
        
        //Title
        String text = "Return to Title Screen?";
        textY = frameY + gp.tileSize*5/4;
        setCenterImage(text, textY, klein, Font.BOLD, Color.white, 20F);
        
        textX = frameX + gp.tileSize/2;
        textY += gp.tileSize*3/2;
        
        //Yes
        if (commandNum == 0) {
            //replace drawString with drawImage
            stringImage("Save and Quit", textX, textY, klein, Font.BOLD, Color.white, 18F);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                gp.music.stop();
                gp.playMusic(1);
                gp.gameState = gp.titleState;
            }
        }
        else{
            stringImage("Save and Quit", textX, textY, klein, Font.PLAIN, Color.darkGray, 18F);
        }
        
        //No
        textY += gp.tileSize/2;
        if (commandNum == 1) {
            //replace drawString with drawImage
            stringImage("Back", textX, textY, klein, Font.BOLD, Color.white, 18F);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 4;
            }
        }
        else{
            stringImage("Back", textX, textY, klein, Font.PLAIN, Color.darkGray, 18F);
        }
        
    }
    public void option_exitNotification_button(){
        //Save and Quit
        button = new Rectangle( gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*216/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            mouse = new Rectangle(gp.mouseH.mousePoint.x - 8, gp.mouseH.mousePoint.y - 8, 16, 16);
            if (button.intersects(mouse)) {
                commandNum = 0;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 0) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mousePressed == true) {
                    subState = 0;
                    gp.music.stop();
                    gp.gameState = gp.titleState;
                }
            }
        }
        
        //Back
        button = new Rectangle(gp.tileSize*274/(gp.orgTileSize*gp.scale), gp.tileSize*248/(gp.orgTileSize*gp.scale), gp.tileSize*488/(gp.orgTileSize*gp.scale), gp.tileSize / 2);
        if (gp.mouseH.mousePoint != null) {
            if (button.intersects(mouse)) {
                commandNum = 1;
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                if (commandNum != 1) {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect(button.x, button.y, button.width, button.height); //show selection area
                }
                if (gp.mouseH.mousePressed == true) {
                    subState = 0;
                    commandNum = 4;
                }
            }
        }
    }
    public void drawTransitionFadeIn(){
        counter+=5;
        g2.setColor(new Color(0, 0, 0, counter));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        if (counter >= 50) {
            fadeInDone = true;
            counter = 255;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.eHandler.tempCol;
            gp.player.worldY = gp.eHandler.tempRow;
            gp.eHandler.prevEventX = gp.player.worldX;
            gp.eHandler.prevEventY = gp.player.worldY;
        }
    }
    public void drawTransitionFadeOut() {
        if (counter > 0) {
            counter-=5;
        }
        g2.setColor(new Color(0, 0, 0, counter));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        if (counter == 0) {
            fadeInDone = false;
            counter = 0;
        }
    }
    public int getCurrentItemIndexSlot(){
        int itemIndex = slotCol + (slotRow*4); 
        return itemIndex;
    }
    public void drawSubWindow(int x, int y, int width, int height){
        Color c  = new Color(0, 0, 0, 200); //RGB and opacity(0-255) 0 being transparent and 255 being not.
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 20, 20);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    public int allignToRight(String text, int tailX){//Maybe I could use this for later
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
    public void stringImage(String text, int x, int y, Font font, int fontStyle, Color color, float size) {
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
    public void setCenterImage(String text, int y, Font font, int fontStyle, Color color, float size) {
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

        // Calculate the x-coordinate to center the text horizontally
        int x = (gp.getWidth() - g2d.getFontMetrics().stringWidth(text)) / 2;

        for (int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], x, y + i * lineHeight);
        }

        g2.drawImage(titleImage, 0, 0, null);
        g2d.dispose();
    }
}
