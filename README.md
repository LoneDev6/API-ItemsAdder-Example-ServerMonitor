# IAServerMonitor
Example usage of ItemsAdder API to create custom HUDs in Spigot 

I know the code is not perfect but I just wanted to show a basic API usage, you're free to copy it, edit it and include in your plugin.

# Important:
You must copy and paste `data` folder content into your `plugins\ItemsAdder\` folder, then use `/iazip` to generate the resourcepack

# Example
![](https://i.imgur.com/yxXluUY.png)

# Commands
- /monitor cpu
- /monitor ram

[API docs](https://itemsadder.devs.beer/developers/java-api)

[Plugin page](https://www.spigotmc.org/resources/%E2%9C%85must-have%E2%9C%85-itemsadder%E2%9C%A8-custom-items-huds-guis-textures-3dmodels-emojis-blocks-wings-hats.73355/)

[Addons website](https://addons.devs.beer/itemsadder)


## Known issues

The CPU bar seems not working on Java 16 and/or in some servers, I can't do anything about this. Check the method `PlayersManager#getProcessCpuLoad()`
