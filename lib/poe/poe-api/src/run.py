import poe
import sys

pbCookies = sys.argv[1]
message = sys.argv[2]

client = poe.Client(pbCookies)
for chunk in client.send_message("capybara", message):
    print(chunk["text_new"], end="", flush=True)

# Purge just the last 10 messages
client.purge_conversation("capybara", count=10)

# Purge the entire conversation
client.purge_conversation("capybara")
