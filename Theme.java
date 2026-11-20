import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/** Colours, fonts and small drawing helpers shared by all GUI classes. */
public final class Theme
{
    private Theme() { }

    // Dark "midnight plum" palette with rose and gold accents
    public static final Color BG_TOP = new Color(0x120C1F);
    public static final Color BG_BOTTOM = new Color(0x2A1D45);
    public static final Color PANEL = new Color(0x241A38);
    public static final Color PANEL_LIGHT = new Color(0x33254F);
    public static final Color PANEL_BORDER = new Color(0x4A3870);
    public static final Color GOLD = new Color(0xF2B84B);
    public static final Color GOLD_DARK = new Color(0xD99A2B);
    public static final Color ROSE = new Color(0xFF8FB4);
    public static final Color ROSE_DARK = new Color(0xFF7FAE);
    public static final Color ROSE_GOLD = new Color(0xE0AA96);
    public static final Color TEAL = new Color(0x5FE0D0);
    public static final Color MINT = new Color(0x6FD9BE);
    public static final Color RED = new Color(0xFF7A8D);
    public static final Color GREEN = new Color(0x7BE0A8);
    public static final Color PURPLE = new Color(0xB79BFF);
    public static final Color LAVENDER = new Color(0xA68BF0);
    public static final Color TEXT = new Color(0xF3E9FA);
    public static final Color MUTED = new Color(0xA394BD);

    private static final String UI_FAMILY;
    private static final String STORY_FAMILY;

    static {
        HashSet<String> families = new HashSet<String>(Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        UI_FAMILY = families.contains("Nunito") ? "Nunito"
                  : families.contains("Quicksand") ? "Quicksand"
                  : families.contains("Poppins") ? "Poppins"
                  : families.contains("Segoe UI") ? "Segoe UI"
                  : families.contains("Helvetica Neue") ? "Helvetica Neue" : Font.SANS_SERIF;
        STORY_FAMILY = families.contains("Georgia") ? "Georgia"
                     : families.contains("Palatino") ? "Palatino" : Font.SERIF;
    }

    public static Font ui(int style, int size) { return new Font(UI_FAMILY, style, size); }
    public static Font story(int style, int size) { return new Font(STORY_FAMILY, style, size); }

    public static void quality(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static Color alpha(Color c, int a)
    {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
    }

    public static Color blend(Color a, Color b, float t)
    {
        t = Math.max(0f, Math.min(1f, t));
        return new Color((int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                         (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                         (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t),
                         (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t));
    }

    /** Draw a small padlock centred at (cx, cy). */
    public static void drawLock(Graphics2D g, int cx, int cy, Color c)
    {
        Stroke old = g.getStroke();
        g.setColor(c);
        g.setStroke(new BasicStroke(1.8f));
        g.drawArc(cx - 4, cy - 9, 8, 10, 0, 180);
        g.setStroke(old);
        g.fillRoundRect(cx - 6, cy - 3, 12, 9, 3, 3);
    }

    /** Draw a filled heart centred at (cx, cy); size is the full width. */
    public static void drawHeart(Graphics2D g, double cx, double cy, double size, Color c)
    {
        double r = size / 4;
        java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
        p.moveTo(cx, cy + size * 0.38);
        p.curveTo(cx - size * 0.75, cy - size * 0.05, cx - size * 0.35, cy - size * 0.55, cx, cy - size * 0.15);
        p.curveTo(cx + size * 0.35, cy - size * 0.55, cx + size * 0.75, cy - size * 0.05, cx, cy + size * 0.38);
        p.closePath();
        g.setColor(c);
        g.fill(p);
    }

    /** Draw a four-point sparkle centred at (cx, cy) with the given radius. */
    public static void drawSparkle(Graphics2D g, double cx, double cy, double r, Color c)
    {
        java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
        double k = r * 0.22;
        p.moveTo(cx, cy - r);
        p.quadTo(cx + k, cy - k, cx + r, cy);
        p.quadTo(cx + k, cy + k, cx, cy + r);
        p.quadTo(cx - k, cy + k, cx - r, cy);
        p.quadTo(cx - k, cy - k, cx, cy - r);
        p.closePath();
        g.setColor(c);
        g.fill(p);
    }

    /** Draw a five-point star centred at (cx, cy) with the given outer radius. */
    public static void drawStar(Graphics2D g, double cx, double cy, double r, Color c)
    {
        java.awt.geom.Path2D.Double p = new java.awt.geom.Path2D.Double();
        for(int i = 0; i < 10; i++) {
            double a = -Math.PI / 2 + i * Math.PI / 5;
            double rr = (i % 2 == 0) ? r : r * 0.45;
            double x = cx + Math.cos(a) * rr, y = cy + Math.sin(a) * rr;
            if(i == 0) { p.moveTo(x, y); } else { p.lineTo(x, y); }
        }
        p.closePath();
        g.setColor(c);
        g.fill(p);
    }

    /** Give a scroll pane the slim, transparent look of this theme. */
    public static void styleScroll(JScrollPane sp)
    {
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar bar = sp.getVerticalScrollBar();
        bar.setUI(new SlimScrollBarUI());
        bar.setPreferredSize(new Dimension(9, 0));
        bar.setOpaque(false);
        bar.setUnitIncrement(16);
    }

    private static class SlimScrollBarUI extends BasicScrollBarUI
    {
        protected JButton createDecreaseButton(int orientation) { return zero(); }
        protected JButton createIncreaseButton(int orientation) { return zero(); }

        private JButton zero()
        {
            JButton b = new JButton();
            Dimension d = new Dimension(0, 0);
            b.setPreferredSize(d); b.setMinimumSize(d); b.setMaximumSize(d);
            return b;
        }

        protected void paintTrack(Graphics g, JComponent c, Rectangle r) { }

        protected void paintThumb(Graphics g, JComponent c, Rectangle r)
        {
            if(r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            quality(g2);
            g2.setColor(alpha(MUTED, 120));
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 6, 6);
            g2.dispose();
        }
    }
}
