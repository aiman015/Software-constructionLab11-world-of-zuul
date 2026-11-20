aiman015
Software-constructionLab11-world-of-zuul
Repository navigation
Code
Issues
Pull requests
Agents
Actions
Projects
Wiki
Security and quality
Insights
Settings
aiman015
Software-constructionLab11-world-of-zuul
Public
Go to file
t
T
aiman015
aiman015
Update README.TXT
e031029
 · 
2 minutes ago
Name		
screenshots
first commit
6 minutes ago
.gitignore
first commit
6 minutes ago
ANSWERS.md
first commit
6 minutes ago
Avatar.java
first commit
6 minutes ago
AvatarView.java
first commit
6 minutes ago
BarMeter.java
first commit
6 minutes ago
Beamer.java
first commit
6 minutes ago
Command.java
first commit
6 minutes ago
CommandWord.java
first commit
6 minutes ago
CommandWords.java
first commit
6 minutes ago
ConsoleOutput.java
first commit
6 minutes ago
FancyButton.java
first commit
6 minutes ago
Game.java
first commit
6 minutes ago
GameCharacter.java
first commit
6 minutes ago
GameGUI.java
first commit
6 minutes ago
GameOutput.java
first commit
6 minutes ago
InstanceCounter.java
first commit
6 minutes ago
Item.java
first commit
6 minutes ago
MapPanel.java
first commit
6 minutes ago
MessageType.java
first commit
6 minutes ago
Overlay.java
first commit
6 minutes ago
Parser.java
first commit
6 minutes ago
Player.java
first commit
6 minutes ago
Position.java
first commit
6 minutes ago
README.TXT
Update README.TXT
2 minutes ago
Room.java
first commit
6 minutes ago
RoundedPanel.java
first commit
6 minutes ago
StaticDemo.java
first commit
6 minutes ago
Theme.java
first commit
6 minutes ago
TimingTest.java
first commit
6 minutes ago
package.bluej
first commit
6 minutes ago
Repository files navigation
README
# 🏰 WORLD OF ZUUL – Escape the Haunted Castle
### Lab 11

## ✨ Cute Edition

The GUI features a beautiful **dark midnight-plum and rose-gold theme**, with no italic fonts, and a little animated heroine.

- 🚶 **Walking Animation:** The heroine walks from room to room on the map.
- 🦘 **Arrival Animation:** She hops when she reaches a new room.
- ✨ **Teleportation Effect:** She disappears in a puff of sparkles when the beamer or portal teleports her.
- 🎉 **Ending Animations:** She cheers when she wins and cries when she loses.
- 💬 **Speech Bubble:** A speech bubble in the header allows her to comment on game events.
- 👻 **Character Display:** The ghost and wizard appear on the map when they are in the player's current room.

### New Classes
- `Avatar` – Draws the game characters.
- `AvatarView` – Displays the heroine in the header and on the start/end screens.

---

## 🚀 How to Run the Game

### Option 1: Run GUI Version (Recommended)
1. Open the project in BlueJ.
2. Compile all Java classes.
3. Right-click the `GameGUI` class.
4. Select `void main(String[] args)`.

### Option 2: Run Text Version
1. Open the project in BlueJ.
2. Right-click the `Game` class.
3. Select `void main(String[] args)`.

### Option 3: Run Outside BlueJ

Open the terminal in the project directory and execute:

```bash
javac *.java
java GameGUI
```

To run the text version:

```bash
java Game
```

---

## 🎯 Game Objective

Find the key, unlock the garden door located south of the Great Hall, and escape the haunted castle within **60 commands**.

## 🎮 Available Commands

| Command | Description |
|---|---|
| `go <dir>` | Move in a specified direction |
| `look` | View the current room |
| `back` | Return to the previous room |
| `items` | Check your inventory |
| `take <item>` | Pick up an item |
| `drop <item>` | Drop an item |
| `eat <item>` | Eat an item |
| `unlock <dir>` | Unlock a door |
| `talk <person>` | Talk to a character |
| `give <item>` | Give an item |
| `charge` | Charge the beamer |
| `fire` | Activate the beamer |
| `help` | Display available commands |
| `quit` | Exit the game |

**Input Validation:** Any unknown command, missing or extra word, or impossible action displays an appropriate error message.

---

## 🗺️ Game Walkthrough

Follow these commands to complete the game:

```text
go east
take beamer
go west
charge
go north
go down
take key
fire
unlock south
go south
```

---

## 📚 Chapter 6 of Book(Objects First with Java):– Written Exercises

Written answers to every exercise of **Chapter 6** are available in:

`ANSWERS.md`

---

### 🛠️ Technologies Used
- Java
- Java Swing (GUI)
- Object-Oriented Programming (OOP)
- BlueJ IDE


*Explore the castle, collect the key, unlock the garden, and escape before time runs out!*
About

A dark-themed Java Swing version of the classic World of Zuul text adventure, with a GUI, map panel, avatar, items, and a win screen. Built with BlueJ.

Resources
Readme
Activity
Stars
0 stars
Watchers
0 watching
Forks
0 forks
Releases
No releases published
Create a new release
Packages
No packages published
Publish your first package
Contributors
1
 (1)
@aiman015
aiman015Aiman Farooq
Languages
Java
100%
