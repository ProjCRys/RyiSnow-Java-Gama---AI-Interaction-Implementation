import poe
import sys

pbCookies = sys.argv[1]
message = sys.argv[2]

client = poe.Client(pbCookies)
response = ''
while response != "Understood.":
    for chunk in client.send_message("capybara", message):
        response += chunk["text_new"]
        if (response == "Understood."):
            break;

    # Print the response
    print(response, end="", flush=True)
    if (response == "Understood."):
        break;
    else:
        # Purge just the last 10 messages
        client.purge_conversation("capybara", count=10)

        # Purge the entire conversation
        client.purge_conversation("capybara")
