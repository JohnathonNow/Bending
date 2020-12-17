Bending
=======
![Build Status](https://github.com/JohnathonNow/Bending/workflows/build/badge.svg)

About the Project
-----------------

This is a 2D sidescrolling multiplayer shooter, in which players use magical
abilities to defeat their opponents and manipulate the world. The game features
destructible terrain, some basic sand simulation, a fair variety of spells,
and "working" online multiplayer<sup  id="a1">[1](#fn1)</sup>.

There should be no expectation that the code here is "good". It was almost entirely
written by a highschooler who was undeterred by two consecutive last-place finishes in
[Java4k](https://en.wikipedia.org/wiki/Java_4K_Game_Programming_Contest).
As such, most of the code is laid out very poorly, completely ignores all best practices, and is absolutely riddled with linter warnings. That said, the game compiles
fine, and it even runs, too. In fact, it's been tested to run on both Linux and Windows!<sup  id="a2">[2](#fn2)</sup>

Building + Running
------------------

As of 2020 the game can be built using maven and Java 8+. Simply clone this repo, cd into its root,
and run `mvn package`. This will create a jar file under the `target` directory, which can be run through
`java -jar ./target/bending.jar`

If you don't want to build it yourself, you can simply [download the latest release](https://github.com/JohnathonNow/Bending/releases/latest/download/bending.jar)
from this page and run that jar file.

Playing
-------

When you launch the game, you will be presented with a login screen. You can log in
with whatever username and password you want, the login server hasn't been used in half
a decade. 

From there, you can choose a costume for your player in the Gear menu, and you can
choose your spells in the Loadouts menu.

You can host the server yourself by clicking the Host button. It will
ask you for server details - your answers here don't matter anymore. The game will be hosted
on port 25565. You can join a server, including your own, by clicking the Join button. It will
prompt you for a server address - if you are hosting it yourself, you can leave this blank.
Otherwise enter the address of the server you wish to play on. I will for as long as reasonable have a server running
on [johnwesthoff.com](johnwesthoff.com) for the latest release of the game.

Controls
--------

| Action         | Control           |
| -------------- | ----------------- |
| Walk           | A and D           |
| Jump           | W                 |
| Dig            | S                 |
| Cast Spell 1   | Left Click        |
| Cast Spell 2   | Right Click       |
| Cast Spell 3   | Middle Click      |
| Choose Spell   | 1-5 + Cast Spell  |
| Chat           | Enter             |
| Move Camera    | Q and E           |
| Move Camera    | CTRL + move mouse |
| Reset Camera   | Z                 |

<a name="fn1"><sup>1</sup></a>Where "working" means "almost certainly has terrible bugs, but probably works well enough."[↩](#a1)  
<a name="fn2"><sup>2</sup></a>OK, well, on Linux it creates a folder named `null` wherever you run it, because back when I worked hard on this project I just wanted the game to work on my raspberry pi.[↩](#a2)
