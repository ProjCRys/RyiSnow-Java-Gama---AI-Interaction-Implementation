# RyiSnow-Java-Gama---AI-Interaction-Implementation
Some classes on how to implement the AI interaction using poe.com python wrapper

I only included the classes and folders related for the implementation though there were some features in my game mixed in but you are free to use them which are:
- Auto-adjusting title screen image resolution when full screen along with the texts in the UI
- Screen glitch effect, zoom in, and greyscale effect
- Implementation of Cursor

Features of AI implementation to Java Game
- Auto updates the Poe wrapper to reduce the amount of updating the reposutory when the library is outdated and unusable.
- Auto installs all requirements to run the Poe wrapper to the latest version (tls_client, websocket, websocket-client)
- Can send messages to NPC and respond quickly (Can respond in 1-2 seconds if internet is good.)
- Use of poe.com pb-cookies like SillyTavern or Tavern AI using poe.com
- Auto deletes conversation from poe.com to secure privacy
- Uses 'Sage' or 'capybara' though you can use and change the models down below:
  "capybara": "Sage",
  "a2": "Claude-instant",
  "nutria": "Dragonfly",
  "a2_100k": "Claude-instant-100k",
  "beaver": "GPT-4",
  "chinchilla": "ChatGPT",
  "a2_2": "Claude+"
  
Notes:

Recommended Models for Roleplays: 
- Sage
- ChatGPT
- Claude-instant

Limited Use: 
- Claude+ for 3 time use

For GPT-4 and Claude-instant-100k IDK why they aren't available anymore.
