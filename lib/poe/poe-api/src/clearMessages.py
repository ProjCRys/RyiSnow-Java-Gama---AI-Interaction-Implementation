import poe
import sys

pbCookies = sys.argv[1]
client = poe.Client(pbCookies)
# Purge just the last 10 messages
client.purge_conversation("capybara", count=10)

# Purge the entire conversation
client.purge_conversation("capybara")
