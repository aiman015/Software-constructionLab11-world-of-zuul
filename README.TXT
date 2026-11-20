WORLD OF ZUUL - "Escape the Haunted Castle"   (Lab 11: Designing Classes - II)

CUTE EDITION: the GUI now has a dark midnight-plum + rose-gold look (no italic fonts) and a little animated
heroine. She walks from room to room on the map, hops when she arrives, vanishes in a
puff of sparkles when the beamer or the portal teleports her, and cheers or cries at
the end. A speech bubble in the header lets her comment on what happens. The ghost and
the wizard appear on the map when they are in your room. (New classes: Avatar draws the
characters, AvatarView shows the hero in the header and on the start/end cards.)

HOW TO RUN THE GUI (recommended): Compile, then right-click class GameGUI >
void main(String[] args).
TEXT VERSION: right-click class Game > void main(String[] args).
Outside BlueJ:  javac *.java   then   java GameGUI   (or java Game)

GOAL: find the key, unlock the garden door (south of the great hall) and
escape within 60 commands.

COMMANDS: go <dir>, look, back, items, take <item>, drop <item>, eat <item>,
unlock <dir>, talk <person>, give <item>, charge, fire, help, quit.
Any unknown command, missing/extra word, or impossible action prints an error.

WALKTHROUGH: go east, take beamer, go west, charge, go north, go down,
take key, fire, unlock south, go south.

Written answers to every exercise of Chapter 6: see ANSWERS.md
