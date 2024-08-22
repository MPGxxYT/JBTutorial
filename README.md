
# JBTutorial
This plugin is used to create and manage custom tutorials for new players to use.

## Dependencies
This plugin has no real dependencies. Only "soft" dependencies. Add them for more features, but are not required.

- [Skript](https://github.com/SkriptLang/Skript)
- [ShopGUIPlus](https://www.spigotmc.org/resources/shopgui-1-8-1-21.6515/)


## Commands
- `/tutorial` Brings up the tutorial menu.
- `/tutorial cancel` Will cancel the current tutorial or plan.
- `/tutorial skip` Will cancel the current tutorial or skip a tutorial if in the plan.
- `/tutorial reload` Reloads all the books and configs. [Does not reload player data.] ~ **Perm: jbtutorial.admin**
## Types


<details> 

<summary>StartTypes</summary>

These are ran when a step starts.

All command types allow the %player% placeholder. ex. `/tp %player% 0 0 0`

- **COMMAND** - Runs the given command as console.
- **FIRST_COMMAND** - Runs the given command as console the first time the player does the tutorial.
- **PLAYER_COMMAND** - Runs the given command as the player.
- **PLAYER_FIRST_COMMAND** - Runs the given command as the player the first time the player does the tutorial.
- **GIVE** - Will give the player the an item. `data: <amount> of <item>`
- **FIRST_GIVE** - Will give the player the an item the player the first time the player does the tutorial. `data: <amount> of <item>`

Examples:
```yml
start:
    1:
        type: COMMAND
        data: /tp %player% 0 120 0
    2:
        type: FIRST_COMMAND
        data: /effect give %player% minecraft:speed infinite 255
    3:
        type: PLAYER_COMMAND
        data: /spawn
    4:
        type: FIRST_GIVE
        data: 15 of DIAMOND_BLOCK
```

</details> 

<details> 

<summary>ActionTypes</summary>

These are actions a player must do to complete a step.

- **COMMAND** - Must run a command.
    - `data: /home` *Must run /home.*
- **MINE** - Must break a block.
    - `data: 8 of COBBLESTONE` *Must break 8 of cobblestone.*
    - `data: 8` *Must break 8 of any block.*
- **INVENTORY_CLICK** - Must click an item in their inventory.
    - `data: IRON_DOOR` *Must click an iron door.*
    - `data: GRASS_BLOCK named "&7Spawn"` *Must click a grass block named "&7Spawn".*
- **SELL** - [ShopGUIPlus] Must sell an amount of items.
    - `data: 15 of STONE` *Must sell 15 cobblestone to a shop.*
    - `data: 30` *Must sell 30 of any block.*
    - `data: any` *Must sell anything.*
- **CALL** - [Skript] Awaits a call from a Skript Effect.

  ~ Skript Syntax: `call tutorial %strings% on %player%`
    - `data: custom call` *waits for the call "custom call"*
    - in skript: `call tutorial "custom call" on event-player`

Examples:
```yml
actions:
    1:
        type: COMMAND
        data: /warp pvp
    2:
        type: MINE
        data: 20 of MOSSY_COBBLESTONE
    3:
        type: INVENTORY_CLICK
        data: DIAMOND_SWORD named "&c&lPVP Mode"
    4:
        type: SELL
        data: 1 of PLAYER_HEAD
    5:
        type: CALL
        data: is in safezone
```

</details> 

## YML Formatting

<details> 

<summary>Book Formatting 1</summary>

Your books should have a specific format. Let me show you.

They must have:
```yaml
id: myLife
title: My Life
description: A book about my life.
crucial: false
```
- **id:** should always match the file name. (without the .yml)
- **title & description:** can be whatever you want it to be.
- **crucial:** determines if the book should be put in the "crucial" row of the gui.

Now all books **MUST** have "steps:" to even work. And inside these steps you can have a bunch of different designs. They will ALWAYS have a number first. This is the order they would go.

Here's the default:
```yaml
id: myLife
title: My Life
description: A book about my life.
crucial: false
steps:
    1:
        text: Goto my home.
        actions:
            1:
                type: COMMAND
                data: /warp home
            2:
                type: COMMAND
                data: /home
```

- **text:** is the instruction the player will see.
- **actions:** the different actions they can do to complete the step.


</details> 

<details> 

<summary>Book Formatting 2</summary>

Now there are more features that steps can have.

Let me show you:
```yaml
id: myLife
title: My Life
description: A book about my life.
crucial: false
steps:
    1:
        text: Goto my home.
        actions:
            1:
                type: COMMAND
                data: /warp home
            2:
                type: COMMAND
                data: /home
    2:
        text: Return back to spawn.
        start:
            1:
                type: FIRST_COMMAND
                data: /eeco give %player% 2000
            2:
                type: PLAYER_COMMAND
                data: /effect give %player% speed
        actions:
            1:
                type: COMMAND
                data: /spawn
```
- **start:** is a list of actions that will run when the step starts. Some are specific to happening only once for that player, like `FIRST_COMMAND`.

You may have noticed the formatting for multiple steps, it's pretty simple.

Here's another feature that could be pretty useful:
```yaml
id: myLife
title: My Life
description: A book about my life.
crucial: false
steps:
    1:
        text: Goto my home.
        actions:
            1:
                type: COMMAND
                data: /warp home
            2:
                type: COMMAND
                data: /home
    2:
        text: This house was my home... until an anvil crashed right through it!
        info: true
        delay: 4
    3:
        text: Return back to spawn.
        start:
            1:
                type: FIRST_COMMAND
                data: /eeco give %player% 2000
            2:
                type: PLAYER_COMMAND
                data: /effect give %player% speed
        actions:
            1:
                type: COMMAND
                data: /spawn
```
- **info:** a different kind of step indicating that it is purely just for informing the player.
- **delay:** how long the info will be displayed in **seconds**. [Optional. Default is 3 seconds]

If it is not an info step, it must have at least `actions:` in it.

And that is, so far, how to make a book.yml

</details> 
