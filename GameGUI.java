import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

/**
 * Graphical user interface for World of Zuul.
 * This class is the "view/controller": it shows what the game reports (through
 * GameOutput) and turns button clicks and typed text into commands. The game
 * logic itself lives in Game, Room, Player, ... and knows nothing about Swing.
 *
 * Start it with:  right-click GameGUI > void main(String[] args)
 */
public class GameGUI extends JFrame implements GameOutput
{
    private Game game;
    private final MapPanel map = new MapPanel();
    private final BarMeter commandsMeter = new BarMeter("COMMANDS LEFT", Theme.MINT);
    private final BarMeter loadMeter = new BarMeter("CARRYING (g)", Theme.LAVENDER);
    private final JLabel locationLabel = new JLabel(" ");
    private final JTextPane story = new JTextPane();
    private final StyledDocument doc = story.getStyledDocument();
    private final JPanel hereList = new JPanel();
    private final JPanel packList = new JPanel();
    private final CommandField input = new CommandField();
    private final HashMap<String, FancyButton> dirButtons = new HashMap<String, FancyButton>();
    private FancyButton unlockBtn, chargeBtn, fireBtn;
    private final ArrayList<String> history = new ArrayList<String>();
    private int historyPos = 0;
    private final Overlay overlay = new Overlay();
    private final AvatarView mascot = new AvatarView(0.95, true);

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new GameGUI().setVisible(true));
    }

    public GameGUI()
    {
        super("Haunted Castle - World of Zuul (cute dark edition)");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        defineStyles();
        buildUI();
        setGlassPane(overlay);
        game = new Game(this);
        map.setGame(game);
        setSize(1220, 800);
        setMinimumSize(new Dimension(1060, 720));
        setLocationRelativeTo(null);
        refresh();
        overlay.setMood(Avatar.Mood.WAVE);
        overlay.present("THE HAUNTED CASTLE", Theme.ROSE_DARK,
            "You wake up in an old, haunted castle.<br>Find the rusty key, unlock the garden door and escape "
            + "before you run out of commands.<br><br><span style='color:#9C8AA8'>Click the buttons or type "
            + "commands such as <b>go east</b>, <b>take lamp</b> or <b>help</b>.</span>",
            "Begin the escape", this::begin, "Exit", this::dispose);
    }

    // ------------------------------------------------------------------ game control

    private void begin()
    {
        overlay.setVisible(false);
        game.start();
        refresh();
        input.requestFocusInWindow();
    }

    private void newGame()
    {
        overlay.setVisible(false);
        story.setText("");
        history.clear();
        historyPos = 0;
        game = new Game(this);
        map.setGame(game);
        game.start();
        refresh();
        input.requestFocusInWindow();
    }

    /** Send a command to the game (typed or from a button). */
    private void send(String text)
    {
        text = text.trim();
        if(text.isEmpty() || game.isFinished() || overlay.isVisible()) {
            return;
        }
        history.add(text);
        historyPos = history.size();
        show(MessageType.ECHO, text);
        game.processLine(text);
        refresh();
        if(game.isFinished()) {
            showEnd();
        }
    }

    private void showEnd()
    {
        switch(game.getOutcome()) {
            case WON:
                overlay.setMood(Avatar.Mood.HAPPY);
                overlay.present("YOU ESCAPED!", Theme.GREEN,
                    "You slipped out into the moonlit garden using only <b>" + game.getCommandsUsed()
                    + "</b> of " + game.getMaxCommands() + " commands.<br>The castle will never hold you again.",
                    "Play again", this::newGame, "Exit", this::dispose);
                break;
            case LOST:
                overlay.setMood(Avatar.Mood.SAD);
                overlay.present("GAME OVER", Theme.RED,
                    "Time ran out... the haunted castle claims another soul.<br>Plan a shorter route and try again.",
                    "Try again", this::newGame, "Exit", this::dispose);
                break;
            default:
                overlay.setMood(Avatar.Mood.WAVE);
                overlay.present("FAREWELL", Theme.PURPLE,
                    "You gave up on the escape.<br>The castle waits patiently for your return.",
                    "Play again", this::newGame, "Exit", this::dispose);
        }
    }

    // ------------------------------------------------------------------ GameOutput

    public void show(MessageType type, String text)
    {
        switch(type) {
            case ROOM:
                String[] lines = text.split("\n");
                append(lines[0], "roomTitle");
                mascot.say(lines[0], Avatar.Mood.NORMAL);
                for(int i = 1; i < lines.length; i++) {
                    append(lines[i], "roomInfo");
                }
                break;
            case STORY:
                append(text, "story");
                mascot.say(text.startsWith("You wake up") ? "Let's escape together!" : text, Avatar.Mood.NORMAL);
                break;
            case INFO:    append(text, "info"); break;
            case ERROR:   append(text, "error"); mascot.say(text, Avatar.Mood.SAD); break;
            case SUCCESS: append(text, "success"); mascot.say(text, Avatar.Mood.HAPPY); break;
            case SPEECH:  append(text, "speech"); mascot.say(text, Avatar.Mood.WAVE); break;
            case ECHO:    append("\u203A " + text, "echo"); break;
            default:      append(text, "system");
        }
    }

    private void append(String text, String styleName)
    {
        try {
            int start = doc.getLength();
            Style style = doc.getStyle(styleName);
            doc.insertString(start, text + "\n", style);
            doc.setParagraphAttributes(start, text.length() + 1, style, false);
            story.setCaretPosition(doc.getLength());
        }
        catch(BadLocationException ex) {
            // cannot happen: we always insert at the end
        }
    }

    private void defineStyles()
    {
        style("story", Theme.story(Font.PLAIN, 16), new Color(0xC9B8E6), 6, 6);
        style("roomTitle", Theme.story(Font.BOLD, 18), Theme.ROSE_DARK, 14, 2);
        style("roomInfo", Theme.ui(Font.PLAIN, 14), Theme.TEAL, 0, 1);
        style("info", Theme.ui(Font.PLAIN, 14), Theme.TEXT, 4, 4);
        style("error", Theme.ui(Font.BOLD, 14), Theme.RED, 4, 4);
        style("success", Theme.ui(Font.BOLD, 14), Theme.GREEN, 4, 4);
        style("speech", Theme.story(Font.PLAIN, 15), Theme.PURPLE, 4, 4);
        style("echo", Theme.ui(Font.BOLD, 14), Theme.PURPLE, 12, 2);
        style("system", Theme.ui(Font.PLAIN, 12), Theme.MUTED, 2, 2);
    }

    private void style(String name, Font f, Color c, int above, int below)
    {
        Style s = doc.addStyle(name, null);
        StyleConstants.setFontFamily(s, f.getFamily());
        StyleConstants.setFontSize(s, f.getSize());
        StyleConstants.setBold(s, f.isBold());
        StyleConstants.setItalic(s, f.isItalic());
        StyleConstants.setForeground(s, c);
        StyleConstants.setSpaceAbove(s, above);
        StyleConstants.setSpaceBelow(s, below);
        StyleConstants.setLineSpacing(s, 0.15f);
    }

    // ------------------------------------------------------------------ refresh the panels

    private void refresh()
    {
        Player p = game.getPlayer();
        Room room = p.getCurrentRoom();
        commandsMeter.setValues(game.getCommandsLeft(), game.getMaxCommands());
        commandsMeter.setWarn(game.getCommandsLeft() <= 10);
        loadMeter.setValues(p.getTotalWeight(), p.getMaxWeight());
        loadMeter.setWarn(p.getTotalWeight() > 0.85 * p.getMaxWeight());
        locationLabel.setText(room.getName());

        for(Map.Entry<String, FancyButton> e : dirButtons.entrySet()) {
            boolean exists = room.getExits().containsKey(e.getKey());
            e.getValue().setEnabled(exists);
            e.getValue().setLocked(exists && room.isLocked(e.getKey()));
        }
        unlockBtn.setEnabled(firstLockedDirection(room) != null && p.hasItem("key"));
        Beamer beamer = p.getBeamer();
        chargeBtn.setEnabled(beamer != null);
        fireBtn.setEnabled(beamer != null && beamer.isCharged());

        updateHere(room);
        updatePack(p, room);
        map.repaint();
    }

    private String firstLockedDirection(Room room)
    {
        for(String direction : room.getExits().keySet()) {
            if(room.isLocked(direction)) {
                return direction;
            }
        }
        return null;
    }

    private void updateHere(Room room)
    {
        hereList.removeAll();
        boolean any = false;
        for(Item item : room.getItems()) {
            any = true;
            FancyButton take = item.canBePickedUp() ? cmdButton("Take", "take " + item.getName()) : null;
            String sub = item.getWeight() + " g  \u00B7  " + (item.canBePickedUp() ? "portable" : "too heavy to lift");
            hereList.add(itemRow(cap(item.getName()), sub, item.getDescription(), take));
        }
        for(GameCharacter c : room.getCharacters()) {
            any = true;
            hereList.add(itemRow(cap(c.getName()), c.isMoving() ? "wanders the castle" : "someone is here",
                                 "Say hello", cmdButton("Talk", "talk " + c.getName())));
        }
        if(!any) {
            hereList.add(emptyNote("Nothing of interest here."));
        }
        hereList.add(Box.createVerticalGlue());
        hereList.revalidate();
        hereList.repaint();
    }

    private void updatePack(Player p, Room room)
    {
        packList.removeAll();
        boolean any = false;
        for(Item item : p.getItems()) {
            any = true;
            ArrayList<FancyButton> buttons = new ArrayList<FancyButton>();
            if(item.isEdible()) {
                buttons.add(cmdButton("Eat", "eat " + item.getName()));
            }
            if(!room.getCharacters().isEmpty()) {
                buttons.add(cmdButton("Give", "give " + item.getName()));
            }
            buttons.add(cmdButton("Drop", "drop " + item.getName()));
            String sub = item.getWeight() + " g";
            if(item instanceof Beamer) {
                sub += "  \u00B7  " + (((Beamer) item).isCharged() ? "charged" : "not charged");
            }
            packList.add(itemRow(cap(item.getName()), sub, item.getDescription(),
                                 buttons.toArray(new FancyButton[0])));
        }
        if(!any) {
            packList.add(emptyNote("Your backpack is empty."));
        }
        packList.add(Box.createVerticalGlue());
        packList.revalidate();
        packList.repaint();
    }

    private String cap(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private FancyButton cmdButton(String label, String command)
    {
        FancyButton b = new FancyButton(label, FancyButton.Kind.SECONDARY).compact();
        b.addActionListener(e -> send(command));
        return b;
    }

    private JLabel emptyNote(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(Theme.ui(Font.PLAIN, 13));
        l.setForeground(Theme.MUTED);
        l.setBorder(new EmptyBorder(6, 2, 6, 2));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel itemRow(String name, String sub, String tip, FancyButton... buttons)
    {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 2, 5, 2));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel n = new JLabel(name);
        n.setFont(Theme.ui(Font.BOLD, 14));
        n.setForeground(Theme.TEXT);
        JLabel s = new JLabel(sub);
        s.setFont(Theme.ui(Font.PLAIN, 11));
        s.setForeground(Theme.MUTED);
        text.add(n);
        text.add(s);
        row.add(text, BorderLayout.CENTER);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        btns.setOpaque(false);
        for(FancyButton b : buttons) {
            if(b != null) {
                btns.add(b);
            }
        }
        row.add(btns, BorderLayout.EAST);
        row.setToolTipText(tip);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        return row;
    }

    // ------------------------------------------------------------------ building the window

    private void buildUI()
    {
        JPanel root = new JPanel(new BorderLayout(0, 14)) {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Theme.BG_TOP, 0, getHeight(), Theme.BG_BOTTOM));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(16, 20, 18, 20));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(16, 0));
        middle.setOpaque(false);
        middle.add(buildMapCard(), BorderLayout.WEST);
        middle.add(buildStoryCard(), BorderLayout.CENTER);
        middle.add(buildSideColumn(), BorderLayout.EAST);
        root.add(middle, BorderLayout.CENTER);

        root.add(buildControls(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader()
    {
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        GradientTitle title = new GradientTitle("Haunted Castle", Theme.ui(Font.BOLD, 30));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("WORLD OF ZUUL  \u00B7  ESCAPE BEFORE TIME RUNS OUT");
        sub.setFont(Theme.ui(Font.BOLD, 11));
        sub.setForeground(Theme.MUTED);
        titles.add(Box.createVerticalGlue());
        titles.add(title);
        titles.add(sub);
        titles.add(Box.createVerticalGlue());
        header.add(titles, BorderLayout.WEST);

        mascot.setPreferredSize(new Dimension(320, 76));
        header.add(mascot, BorderLayout.CENTER);

        JPanel meters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 4));
        meters.setOpaque(false);
        meters.add(commandsMeter);
        meters.add(loadMeter);
        header.add(meters, BorderLayout.EAST);
        return header;
    }

    /** A title painted with a rose-to-lavender gradient. */
    private static class GradientTitle extends JComponent
    {
        private final String text;

        GradientTitle(String text, Font font)
        {
            this.text = text;
            setFont(font);
            setOpaque(false);
        }

        public Dimension getPreferredSize()
        {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(text) + 6, fm.getAscent() + 6);
        }

        public Dimension getMaximumSize()
        {
            return getPreferredSize();
        }

        protected void paintComponent(Graphics g0)
        {
            Graphics2D g = (Graphics2D) g0.create();
            Theme.quality(g);
            g.setFont(getFont());
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(text);
            g.setColor(new Color(150, 90, 170, 40));
            g.drawString(text, 1, fm.getAscent() + 2);
            g.setPaint(new GradientPaint(0, 0, Theme.ROSE_DARK, w, 0, Theme.PURPLE));
            g.drawString(text, 0, fm.getAscent());
            g.dispose();
        }
    }

    private JLabel sectionTitle(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(Theme.ui(Font.BOLD, 11));
        l.setForeground(Theme.MUTED);
        l.setBorder(new EmptyBorder(0, 2, 8, 0));
        return l;
    }

    private RoundedPanel card(int pad)
    {
        RoundedPanel p = new RoundedPanel(new BorderLayout(), Theme.PANEL, 22);
        p.setBorder(new EmptyBorder(pad, pad + 2, pad, pad + 2));
        return p;
    }

    private JPanel buildMapCard()
    {
        RoundedPanel c = card(14);
        c.setPreferredSize(new Dimension(350, 0));
        c.add(sectionTitle("CASTLE MAP"), BorderLayout.NORTH);
        c.add(map, BorderLayout.CENTER);
        return c;
    }

    private JPanel buildStoryCard()
    {
        RoundedPanel c = card(16);
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JLabel caption = new JLabel("YOU ARE IN");
        caption.setFont(Theme.ui(Font.BOLD, 11));
        caption.setForeground(Theme.MUTED);
        locationLabel.setFont(Theme.ui(Font.BOLD, 26));
        locationLabel.setForeground(Theme.ROSE_DARK);
        locationLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        top.add(caption);
        top.add(locationLabel);
        c.add(top, BorderLayout.NORTH);

        story.setEditable(false);
        story.setFocusable(false);
        story.setOpaque(false);
        story.setBackground(new Color(0, 0, 0, 0));
        story.setBorder(new EmptyBorder(2, 2, 2, 10));
        JScrollPane scroll = new JScrollPane(story);
        Theme.styleScroll(scroll);
        c.add(scroll, BorderLayout.CENTER);
        return c;
    }

    private JPanel buildSideColumn()
    {
        JPanel col = new JPanel(new GridLayout(2, 1, 0, 14));
        col.setOpaque(false);
        col.setPreferredSize(new Dimension(330, 0));
        col.add(listCard("IN THIS ROOM", hereList));
        col.add(listCard("BACKPACK", packList));
        return col;
    }

    private JPanel listCard(String title, JPanel list)
    {
        RoundedPanel c = card(14);
        c.add(sectionTitle(title), BorderLayout.NORTH);
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        JScrollPane sp = new JScrollPane(list);
        Theme.styleScroll(sp);
        c.add(sp, BorderLayout.CENTER);
        return c;
    }

    private JPanel buildControls()
    {
        RoundedPanel bar = new RoundedPanel(new BorderLayout(22, 0), Theme.PANEL, 22);
        bar.setBorder(new EmptyBorder(14, 18, 16, 18));

        // compass
        JPanel compass = new JPanel(new GridBagLayout());
        compass.setOpaque(false);
        addDir(compass, "north", "North", 1, 0);
        addDir(compass, "up", "Up", 2, 0);
        addDir(compass, "west", "West", 0, 1);
        addDir(compass, "east", "East", 2, 1);
        addDir(compass, "south", "South", 1, 2);
        addDir(compass, "down", "Down", 2, 2);
        FancyButton look = new FancyButton("Look", FancyButton.Kind.PRIMARY);
        look.setPreferredSize(new Dimension(84, 34));
        look.addActionListener(e -> send("look"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 1; gc.gridy = 1; gc.insets = new Insets(3, 3, 3, 3);
        compass.add(look, gc);
        bar.add(compass, BorderLayout.WEST);

        // actions + input
        JPanel right = new JPanel(new BorderLayout(0, 12));
        right.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(plainAction("Back", "back"));
        actions.add(plainAction("Items", "items"));
        actions.add(plainAction("Help", "help"));
        unlockBtn = new FancyButton("Unlock door", FancyButton.Kind.SECONDARY);
        unlockBtn.addActionListener(e -> {
            String d = firstLockedDirection(game.getPlayer().getCurrentRoom());
            if(d != null) {
                send("unlock " + d);
            }
        });
        actions.add(unlockBtn);
        chargeBtn = plainAction("Charge beamer", "charge");
        fireBtn = plainAction("Fire beamer", "fire");
        actions.add(chargeBtn);
        actions.add(fireBtn);
        FancyButton quit = new FancyButton("Quit", FancyButton.Kind.DANGER);
        quit.addActionListener(e -> send("quit"));
        actions.add(quit);
        right.add(actions, BorderLayout.NORTH);

        JPanel inputRow = new JPanel(new BorderLayout(10, 0));
        inputRow.setOpaque(false);
        inputRow.add(input, BorderLayout.CENTER);
        FancyButton sendBtn = new FancyButton("Send", FancyButton.Kind.PRIMARY);
        sendBtn.setPreferredSize(new Dimension(96, 40));
        inputRow.add(sendBtn, BorderLayout.EAST);
        right.add(inputRow, BorderLayout.SOUTH);
        bar.add(right, BorderLayout.CENTER);

        ActionListener submit = e -> {
            send(input.getText());
            input.setText("");
        };
        input.addActionListener(submit);
        sendBtn.addActionListener(submit);
        input.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_UP && historyPos > 0) {
                    historyPos--;
                    input.setText(history.get(historyPos));
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(historyPos < history.size() - 1) {
                        historyPos++;
                        input.setText(history.get(historyPos));
                    }
                    else {
                        historyPos = history.size();
                        input.setText("");
                    }
                }
            }
        });
        return bar;
    }

    private FancyButton plainAction(String label, String command)
    {
        FancyButton b = new FancyButton(label, FancyButton.Kind.SECONDARY);
        b.addActionListener(e -> send(command));
        return b;
    }

    private void addDir(JPanel compass, String direction, String label, int x, int y)
    {
        FancyButton b = new FancyButton(label, FancyButton.Kind.SECONDARY);
        b.setPreferredSize(new Dimension(84, 34));
        b.addActionListener(e -> send("go " + direction));
        dirButtons.put(direction, b);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x; gc.gridy = y; gc.insets = new Insets(3, 3, 3, 3);
        compass.add(b, gc);
    }

    // ------------------------------------------------------------------ command text field

    private static class CommandField extends JTextField
    {
        CommandField()
        {
            setOpaque(false);
            setForeground(Theme.TEXT);
            setCaretColor(Theme.ROSE_DARK);
            setFont(Theme.ui(Font.PLAIN, 15));
            setBorder(new EmptyBorder(9, 20, 9, 20));
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        protected void paintComponent(Graphics g0)
        {
            Graphics2D g = (Graphics2D) g0.create();
            Theme.quality(g);
            g.setColor(new Color(0x170F28));
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g.setColor(isFocusOwner() ? Theme.ROSE : Theme.PANEL_BORDER);
            g.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1.2f));
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            if(getText().isEmpty()) {
                g.setFont(getFont().deriveFont(Font.PLAIN));
                g.setColor(Theme.MUTED);
                FontMetrics fm = g.getFontMetrics();
                g.drawString("Type a command, e.g. go east  (or use the buttons)", getInsets().left,
                             (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
            g.dispose();
            super.paintComponent(g0);
        }
    }
}
