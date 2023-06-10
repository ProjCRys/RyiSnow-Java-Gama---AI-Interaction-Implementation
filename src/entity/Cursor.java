package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import main.GamePanel;
import main.MouseHandler;

public class Cursor extends Entity {

    MouseHandler mouseH;

    public int screenX;
    public int screenY;
    int rgb = 255;
    int cursorCounter = 0;
    int opacity = 150;

    public Cursor(GamePanel gp, MouseHandler mouseH) {
        super(gp);
        this.mouseH = mouseH;

        name = "cursor";
        speed = 3;
        solidArea = new Rectangle(8, 0, 16, 16);
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize) + gp.tileSize;
    }

    public void update() {
        if (gp.mouseH.mousePoint != null) {
            worldX = gp.player.worldX - screenX + gp.mouseH.mousePoint.x - 8;
            worldY = gp.player.worldY - screenY + gp.mouseH.mousePoint.y - 8;
        }
        collisionOn = false;
        collisionCheck();
    }

    public void collisionCheck() {
        int objIndex = gp.cChecker.checkObject(this, true);
        interactObject(objIndex);
        gp.eHandler.checkEvent();
        gp.mouseH.mouseClicked = false;
    }

    public void interactObject(int i) {
        if (i != 999) {
            if (gp.obj[gp.currentMap][i].objectType == obtainable) {
                if (gp.mouseH.mouseClicked == true) {
                    gp.obj[gp.currentMap][i] = null;
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new BasicStroke(1));
        if (gp.mouseH.mousePoint != null) {
            cursorCounter++;
            //cursor disappear if not moved
            if (cursorCounter > 150) {
                if (opacity > 0) {
                    opacity--;
                    rgb--;
                }
                g2.setColor(new Color(rgb, rgb, rgb, opacity));
            }
            if (gp.mouseH.mouseMoved == true || gp.mouseH.mousePressed == true) {
                cursorCounter = 0;
                rgb = 255;
                opacity = 150;
                gp.mouseH.mouseMoved = false;
                gp.mouseH.mousePressed = false;
            }
            g2.fillOval(gp.mouseH.mousePoint.x, gp.mouseH.mousePoint.y-8, solidArea.width, solidArea.height);
        }
    }
}
