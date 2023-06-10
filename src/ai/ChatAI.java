package ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import main.GamePanel;

public final class ChatAI {

    public String pbCookies = "";
    public String prevResponse = "", response = "";
    public String depthScannedConvo = "", fullConvo = "",
            username = "User", botName = "Bot",
            botPersonality = "", characterNote = "";
    public int messageScan = 10;
    public List<String> dialogueList = new ArrayList<>();
    GamePanel gp;

    public ChatAI(GamePanel gp) {
        this.gp = gp;
        this.pbCookies = "";
        String fileName = "Put_poe_pbCookies_value_here.txt";

        try {
            File file = new File(fileName);

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                this.pbCookies = reader.readLine();
                reader.close();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testCode() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Input: ");
            String userInput = in.nextLine();
            if (userInput.equalsIgnoreCase("exit")) {
                break;
            } else {
                sendMessage(userInput);
            }
        }
        printDepthScannedDialogues(messageScan);
        printFullDialogue();
    }

    public void sendMessage(String userInput) {
        try {
            // Specify the Python interpreter
            String pythonInterpreter = "python";

            // Specify the path to the Python script
            String pythonScript = "lib/poe/poe-api/src/run.py";

            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreter, pythonScript, pbCookies, userInput);

            // Start the process and capture its output
            Process process = processBuilder.start();
            long startTime = System.currentTimeMillis(); // Track start time

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the output and display the result
            System.out.print("Response: ");
            StringBuilder responseBuilder = new StringBuilder(); // Create a StringBuilder to store the response
            char[] buffer = new char[4096];
            int bytesRead, charaCount = 0;
            String incompleteWord = ""; // Variable to store incomplete word from previous iteration
            while ((bytesRead = reader.read(buffer)) != -1) {
                if (bytesRead > 0) {
                    if (gp.keyH.text.equals(gp.keyH.savedUserText) && gp.keyH.chatEnterNext < 1) {
                        gp.keyH.text = "";
                        gp.keyH.chatEnterNext++;
                    }
                    String response = new String(buffer, 0, bytesRead);
                    responseBuilder.append(response); // Append each line to the responseBuilder

                    prevResponse += response;

                    // Split the response into words, considering incomplete words from previous iteration
                    String[] words = (incompleteWord + response).split("(?<=\\s)|(?=\\s)");

                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];
                        boolean isLastWord = (i == words.length - 1);

                        // Check if adding the current word exceeds the character limit
                        if (charaCount + word.length() > 90) {
                            gp.keyH.text += "\n"; // Create a new line
                            charaCount = 0; // Reset the character count
                            System.out.println("");
                        }

                        if (isLastWord && !word.isEmpty()) {
                            // Store incomplete word for next iteration
                            incompleteWord = word;
                        } else {
                            gp.keyH.text += word;
                            System.out.print(word);
                            charaCount += word.length();
                        }
                    }
                }
            }

            // Handle the remaining incomplete word
            if (!incompleteWord.isEmpty()) {
                gp.keyH.text += incompleteWord;
                System.out.print(incompleteWord);
            }

            // Error handling
            int exitCode = process.waitFor();
            if (exitCode >= 1) {
                String response = "Connection Error...\n";
                responseBuilder.append(response);
                System.out.print(response);
            }

            // Update the response and text variables
            String response = responseBuilder.toString();
            prevResponse = response;
            // Wait for the process to finish
            long endTime = System.currentTimeMillis(); // Track end time
            double responseTime = (endTime - startTime) / 1000.0; // Convert to seconds
            System.out.printf("\nResponse Time: %.4f seconds%n", responseTime);
            System.out.println("Response Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void jailbreak(String prompt) {
        try {
            // Specify the Python interpreter
            String pythonInterpreter = "python";

            // Specify the path to the Python script
            String pythonScript = "lib/poe/poe-api/src/run_jailbreak.py";

            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreter, pythonScript, pbCookies, prompt);

            // Start the process and capture its output
            Process process = processBuilder.start();
            long startTime = System.currentTimeMillis(); // Track start time

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the output and display the result
            System.out.print("Response: ");
            StringBuilder responseBuilder = new StringBuilder(); // Create a StringBuilder to store the response
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                if (bytesRead > 0) {
                    String response = new String(buffer, 0, bytesRead);
                    System.out.print(response);
                    responseBuilder.append(response); // Append each line to the responseBuilder
                }
            }

            // Error handling
            int exitCode = process.waitFor();
            if (exitCode >= 1) {
                String response = "Connection Error...\n";
                responseBuilder.append(response);
                System.out.print(response);
            }

            // Update the response and text variables
            String response = responseBuilder.toString();
            prevResponse = response;

            // Wait for the process to finish
            long endTime = System.currentTimeMillis(); // Track end time
            double responseTime = (endTime - startTime) / 1000.0; // Convert to seconds
            System.out.printf("\nResponse Time: %.4f seconds%n", responseTime);
            System.out.println("Response Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            // Specify the Python interpreter
            String pythonInterpreter = "python";

            // Specify the path to the Python script
            String pythonScript = "lib/poe/poe-api/src/clearMessages.py";

            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreter, pythonScript, pbCookies);

            // Start the process and capture its output
            Process process = processBuilder.start();
            long startTime = System.currentTimeMillis(); // Track start time

            // Error
            int exitCode = process.waitFor();
            if (exitCode == 1) {
                response = "Connection Error...\n";
                prevResponse = response;
                gp.keyH.text = response;
                System.out.print(response);
            }

            // Wait for the process to finish
            long endTime = System.currentTimeMillis(); // Track end time
            double responseTime = (endTime - startTime) / 1000.0; // Convert to seconds
            System.out.printf("\nResponse Time: %.4f seconds%n", responseTime);
            System.out.println("Response Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printDepthScannedDialogues(int messageScan) {
        int startIndex = Math.max(dialogueList.size() - messageScan * 2, 0);
        System.out.println("- - - - - - Last " + messageScan + " Dialogues - - - - - -");
        for (int i = startIndex; i < dialogueList.size(); i += 2) {
            String user = dialogueList.get(i);
            String bot = dialogueList.get(i + 1);
            depthScannedConvo += username + ": " + user + "\n" + botName + ": " + bot + "\n";
        }
        System.out.println(depthScannedConvo);
    }

    public String depthScannedDialogues(int depthScanned) {
        int startIndex = Math.max(dialogueList.size() - depthScanned * 2, 0);
        for (int i = startIndex; i < dialogueList.size(); i += 2) {
            String user = dialogueList.get(i);
            String bot = dialogueList.get(i + 1);
            depthScannedConvo += username + ": " + user + "\n" + botName + ": " + bot + "\n";
        }
        return depthScannedConvo;
    }

    public void printFullDialogue() {
        System.out.println("- - - - - - Full Dialogue - - - - - -");
        for (int i = 0; i < dialogueList.size(); i += 2) {
            String user = dialogueList.get(i);
            String bot = dialogueList.get(i + 1);
            fullConvo += username + ": " + user + "\n" + botName + ": " + bot + "\n";
        }
        System.out.println(fullConvo);
    }
}
