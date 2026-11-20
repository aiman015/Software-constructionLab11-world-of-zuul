# 🏰 WORLD OF ZUUL – Escape the Haunted Castle
### Lab 11: Designing Classes – II

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

## 📚 Chapter 6 of Book(Objects First with Java): – Written Exercises

Written answers to every exercise of **Chapter 6** are available in:

`ANSWERS.md`

---

### 🛠️ Technologies Used
- Java
- Java Swing (GUI)
- Object-Oriented Programming (OOP)
- BlueJ IDE

### 🎀 Project
**WORLD OF ZUUL – Escape the Haunted Castle**

*Explore the castle, collect the key, unlock the garden, and escape before time runs out!*
