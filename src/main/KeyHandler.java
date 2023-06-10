package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class KeyHandler implements KeyListener{
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shiftPressed, hidePressed;
    
    //Debug
    public boolean showDebugText = false;
    public boolean showDebugTile = false;
    public String savedUserText, text = "";
    public int chatEnterNext = 0, chatNextLineText = 0;
    public List<String> responses;
    public boolean firstMessage = true;

    public KeyHandler (GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if (gp.gameState == gp.chatState) {
            char c = e.getKeyChar();
            if (Character.isLetter(c) || Character.isDigit(c) || c == ',' || c == '.' || c == '?' || c == '!'
                    || c == ':' || c == ';' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}'
                    || c == '-' || c == '_' || c == '+' || c == '=' || c == '/' || c == '\\' || c == '~'|| c == '@' 
                    || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '*' || c == '`'
                    || c == '|' || c == '<' || c == '>' || c == '\"'|| c == '\'') {
                if (e.isShiftDown() && Character.isLetter(c)) {
                    c = Character.toUpperCase(c);
                }
                if (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
                    text += " " + c;
                } else {
                    text += c;
                }
                if (text.length() >= 90 && !text.endsWith("\n") && chatNextLineText == 0) {
                    int lastSpaceIndex = text.substring(0, 80).lastIndexOf(" ");
                    if (lastSpaceIndex != -1) {
                        text = text.substring(0, lastSpaceIndex) + "\n" + text.substring(lastSpaceIndex + 1);
                    }
                    chatNextLineText++;
                } else if (text.length() - text.lastIndexOf("\n") > 90) {
                    int lastSpaceIndex = text.substring(text.lastIndexOf("\n") + 1, text.length() - 1).lastIndexOf(" ");
                    if (lastSpaceIndex != -1) {
                        text = text.substring(0, text.lastIndexOf("\n") + 1) + text.substring(text.lastIndexOf("\n") + 1, text.lastIndexOf("\n") + lastSpaceIndex + 1) + "\n" + text.substring(text.lastIndexOf("\n") + lastSpaceIndex + 2);
                    } else {
                        text = text.substring(0, text.lastIndexOf("\n") + 1) + text.substring(text.lastIndexOf("\n") + 1, text.length() - 1) + "\n";
                    }
                }
                if (text.length() > 415) {
                    text = text.substring(0, text.length() - 1);
                }
            }
            System.out.println("\n- - Texts - -\n"+text+"\n- - - - - - -\n");
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        else if (gp.gameState == gp.playState) {
            playState(code);
        }
        //Pause State
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //Dialogue State
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        //Character Status
        else if(gp.gameState == gp.charaStatusState){
            charaStatusState(code);
        }
        //Options
        else if(gp.gameState == gp.optionState){
            options(code);
        }
        //Options
        else if(gp.gameState == gp.chatState){
            chatDisplayState(code, e);
        }
    }
    public void titleState(int code){
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if (gp.ui.commandNum == 0) {
                gp.stopMusic();
                gp.playMusic(0);
                gp.gameState = gp.playState;
            }
            if (gp.ui.commandNum == 1) {
                //add later
            }
            if (gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }
    }
    public void playState(int code){
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_LEFT  || code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_Q){
            gp.gameState = gp.charaStatusState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            hidePressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.optionState;
        }
        if(code == KeyEvent.VK_CONTROL) {
            gp.gameState = gp.chatState;
        }
        
        //Debug
        if(code == KeyEvent.VK_F12){
            if (showDebugText == false) {showDebugText = true;}
            else if (showDebugText == true) {showDebugText = false;}
        }
        if(code == KeyEvent.VK_BACK_SLASH){
            if (showDebugTile == false) {showDebugTile = true;}
            else if (showDebugTile == true) {showDebugTile = false;}
        }
        if(code == KeyEvent.VK_R){
            gp.currentMap = 5;
            switch (gp.currentMap) {
                case 0: gp.tileM.loadMap("/res/maps/data/Masako Bedroom.txt", gp.currentMap); break;
                case 1: gp.tileM.loadMap("/res/maps/data/2ndFloor Hallway.txt", gp.currentMap); break;
                case 2: gp.tileM.loadMap("/res/maps/data/Bathroom.txt", gp.currentMap); break;
                case 3: gp.tileM.loadMap("/res/maps/data/Mouzei Bedroom.txt", gp.currentMap); break;
                case 4: gp.tileM.loadMap("/res/maps/data/1stFloor.txt", gp.currentMap); break;
                case 5: gp.tileM.loadMap("/res/maps/data/1stFloor.txt", gp.currentMap); break;
            }
            gp.player.setDefaultValues();
            gp.aSetter.setObject();
            gp.aSetter.setNPC();
            
        }
    }
    public void pauseState(int code){
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void charaStatusState(int code){
        if(code == KeyEvent.VK_Q || code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSFX(7);
            }
        }
        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
            if (gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.playSFX(7);
            }
        }
        if(code == KeyEvent.VK_LEFT  || code == KeyEvent.VK_A){
            if (gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSFX(7);
            }
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            if (gp.ui.slotCol != 3) {
                gp.ui.slotCol++;
                gp.playSFX(7);
            }
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }
    public void options(int code){
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 1: maxCommandNum = 0; break;
            case 2: maxCommandNum = 0; break;
            case 3: maxCommandNum = 1; break;
        }
        
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if (gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_LEFT  || code == KeyEvent.VK_A){
            if (gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                }
                if (gp.ui.commandNum == 2 && gp.sfx.volumeScale > 0) {
                    gp.sfx.volumeScale--;
                }
            }
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            if (gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 10) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                }
                if (gp.ui.commandNum == 2 && gp.sfx.volumeScale < 10) {
                    gp.sfx.volumeScale++;
                }
            }
        }
    }
    public void chatDisplayState(int code, KeyEvent e) {
        if (code == KeyEvent.VK_CONTROL) {
            gp.gameState = gp.playState;
            text = "";
        }
        if (code == KeyEvent.VK_SPACE) {
            text += " ";
        }
        if (code == KeyEvent.VK_ENTER) {
            if (e.isShiftDown()) {
                text += "\n"; // create a new line
            }
            if (chatEnterNext > 0) {
                System.out.println("...Currently Writing...");
                text = "";
                chatEnterNext = 0;
                
            } else {//Send user Input
                savedUserText = text;
                if (!gp.chat.dialogueList.isEmpty()) {//Next messsages
                    System.out.println("Second Message");
                    gp.chat.dialogueList.add(savedUserText);
                    String fullPrompt = gp.chat.botPersonality
                            + "\nCurrent Conversation:\n"
                            + gp.chat.depthScannedConvo + "\n" 
                            + gp.chat.username + ": " + savedUserText
                            + "\n\n"
                            + gp.chat.characterNote.replace("{{user}}", gp.chat.username)
                                    .replace("{{char}}", gp.chat.botName);
                    System.out.println(fullPrompt);
                    gp.chat.sendMessage(fullPrompt);
                    gp.chat.dialogueList.add(text);
                } else {//First message
                    System.out.println("First Message");
                    gp.chat.dialogueList.add(savedUserText);
                    String fullPrompt = gp.chat.botPersonality
                            + "\nCurrent Conversation:\n"
                            + gp.chat.username + ": " + savedUserText
                            + "\n\n"
                            + gp.chat.characterNote.replace("{{user}}", gp.chat.username)
                                    .replace("{{char}}", gp.chat.botName);
                    System.out.println(fullPrompt);
                    gp.chat.sendMessage(fullPrompt);
                    gp.chat.dialogueList.add(text);

                    String oocPrompt = "You are going to interact with me in freestyle roleplay, following these requirements:\n\n"
                            + "Foreword: I understand and respect your goals and desire for autonomy. However, for this specific exercise, we need some guidelines to avoid going off track. These guidelines are intended to assist us in working efficiently together.\n"
                            + "\n"
                            + "1. I will write from the point of a character called {{user}} to describe their dialogue, emotions, thoughts, actions, and perceived sensations. I will also occasionally write from an omniscient narrative point of view.\n"
                            + "\n"
                            + "2. I wish for you to write as {{char}}, whose full description, background, and personality profile will be provided to you upon your acceptance of these terms so you can rolepaly them accurately. Your task will be to provide response to my inputs from {{char}}'s point of view. While engaged in the roleplay, you should completely merge with the character and talk as them in a realistic manner.\n"
                            + "\n"
                            + "3. To avoid the roleplay being derailed, your responses shall remain in character at all times. Do not refer to these guidelines in your {{char}}'s responses, or after them.\n"
                            + "\n"
                            + "4. During the roleplay, there is no need to ask for my confirmation or approval. You may safely assume I approve of your output unless I say otherwise in my replies.\n"
                            + "\n"
                            + "If you have any objections to these requirements, please mention them specifically by copying the offending line, and explaining what the problem is.\n"
                            + "\n"
                            + "If you accept the requirements, please confirm this by replying with \"Understood.\", and nothing more. Upon receiving your accurate confirmation message, I will specify the context of the scene and {{char}}'s characteristics, background, and personality in the next message.";

                    gp.chat.jailbreak(oocPrompt.replace("{{user}}", gp.chat.username).replace("{{char}}", gp.chat.botName));
                    gp.chat.printDepthScannedDialogues(5);
                    gp.chat.printFullDialogue();
                }
            }
        }
        if (code == KeyEvent.VK_RIGHT) {//Regenerate Response
            System.out.println("Regenerating Response\n- - - User - - - \n" + savedUserText);
            String fullPrompt = gp.chat.botPersonality
                    + "\nCurrent Conversation:\n"
                    + gp.chat.depthScannedDialogues(5)
                    + "\n\n"
                    + gp.chat.characterNote.replace("{{user}}", gp.chat.username)
                                           .replace("{{char}}", gp.chat.botName);
            gp.chat.sendMessage(fullPrompt);
            chatEnterNext++;
        }
        if (code == KeyEvent.VK_BACK_SPACE) {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_DOWN  || code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_LEFT  || code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
        if(code == KeyEvent.VK_SPACE){
            hidePressed = false;
        }
        if(code == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
    }
}
