import java.awt.*;
import java.awt.geom.*;

/**
 * Draws the little characters of the game (the hero, the ghost and the wizard)
 * with Java2D. Nothing here knows about the game rules: it is pure drawing, so
 * the hero can be shown on the map, in the header and on the start screen.
 */
public final class Avatar
{
    private Avatar() { }

    /** How the hero feels: this changes the face and the pose. */
    public enum Mood { NORMAL, HAPPY, SAD, WAVE }

    private static final Color SKIN = new Color(0xFFE2D2);
    private static final Color SKIN_SHADE = new Color(0xF3C9B8);
    private static final Color HAIR = new Color(0x6A4260);
    private static final Color HAIR_LIGHT = new Color(0x8C5C80);
    private static final Color DRESS_TOP = new Color(0xFF9BBF);
    private static final Color DRESS_BOTTOM = new Color(0xE9628F);
    private static final Color CREAM = new Color(0xFFF7EC);
    private static final Color SHOE = new Color(0xC9738F);
    private static final Color EYE = new Color(0x3B2646);

    /**
     * Draw the hero.
     *
     * @param x      horizontal centre in pixels
     * @param feetY  y coordinate of the ground under the feet
     * @param s      scale (1.0 is about 66 pixels tall)
     * @param walk   walking phase in radians (advance it while moving)
     * @param move   0 = standing still ... 1 = walking at full swing
     * @param facing -1 = looking left, 0 = front, +1 = looking right
     * @param t      time in seconds (idle breathing, blinking, waving)
     * @param mood   facial expression and pose
     * @param extraLift extra upward offset in pixels (e.g. an arrival hop)
     * @param alpha  1 = solid, 0 = invisible
     */
    public static void drawHero(Graphics2D g0, double x, double feetY, double s, double walk, double move,
                                double facing, double t, Mood mood, double extraLift, double alpha)
    {
        if(alpha <= 0.01 || s <= 0.01) {
            return;
        }
        Graphics2D g = (Graphics2D) g0.create();
        Theme.quality(g);
        g.translate(x, feetY);
        g.scale(s, s);
        if(alpha < 1) {
            g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
        }

        double swing = Math.sin(walk) * move;
        double bounce = -Math.abs(Math.sin(walk)) * 3.4 * move;
        double breathe = Math.sin(t * 2.6) * 0.9 * (1 - move);
        double jump = mood == Mood.HAPPY ? -Math.abs(Math.sin(t * 5.5)) * 9 : 0;
        double lift = bounce + breathe + jump - extraLift;

        // soft ground shadow (shrinks while jumping)
        double shrink = Math.max(0.6, 1 + (jump - extraLift) / 40.0);
        g.setColor(new Color(0, 0, 0, 70));
        g.fill(new Ellipse2D.Double(-14 * shrink, -3.5, 28 * shrink, 7));

        g.rotate(facing * 0.05 * move);
        g.translate(0, lift);

        double sway = Math.sin(t * 3.0 + walk * 0.5);
        boolean sad = mood == Mood.SAD;

        // ---- pigtails (behind everything)
        double pig = (move > 0.05 ? Math.sin(walk + 1.2) * 5 * move : sway * 1.6) - facing * 2 * move;
        for(int side = -1; side <= 1; side += 2) {
            Path2D.Double tail = new Path2D.Double();
            double sx = side * 13, sy = -49;
            tail.moveTo(sx, sy);
            tail.curveTo(sx + side * 9 + pig * side * 0.3, sy + 4, sx + side * 11 + pig, sy + 14, sx + side * 7 + pig * 1.3, sy + 23);
            tail.curveTo(sx + side * 2 + pig, sy + 17, sx + side * 1, sy + 8, sx, sy);
            g.setColor(HAIR);
            g.fill(tail);
            g.setColor(HAIR_LIGHT);
            g.fill(new Ellipse2D.Double(sx + side * 6 + pig * 0.8 - 1.5, sy + 8, 3, 7));
        }
        g.setColor(HAIR);
        g.fill(new Ellipse2D.Double(-16.5, -62, 33, 30));              // back hair

        // ---- legs and shoes
        for(int side = -1; side <= 1; side += 2) {
            double step = swing * 5.5 * side;
            double up = Math.max(0, Math.sin(walk + (side > 0 ? 0 : Math.PI))) * 3 * move;
            double fx = side * 4.5 + step * 0.35, fy = -2.5 - up - lift * 0.0;
            g.setStroke(new BasicStroke(4.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(SKIN);
            g.draw(new Line2D.Double(side * 4.5, -17, fx, fy - 2));
            g.setColor(SHOE);
            g.fill(new RoundRectangle2D.Double(fx - 5, fy - 1.5, 10, 6, 5, 5));
            g.setColor(CREAM);
            g.fill(new Ellipse2D.Double(fx - 3.5, fy - 0.5, 4, 2));
        }
        g.setStroke(new BasicStroke(1f));

        // ---- arms (back arm first)
        drawArm(g, -1, swing, t, mood);

        // ---- dress
        Path2D.Double dress = new Path2D.Double();
        dress.moveTo(-8.5, -35);
        dress.quadTo(-9.5, -27, -13.5, -13);
        dress.quadTo(0, -9.5, 13.5, -13);
        dress.quadTo(9.5, -27, 8.5, -35);
        dress.quadTo(0, -37.5, -8.5, -35);
        dress.closePath();
        g.setPaint(new GradientPaint(0, -36, DRESS_TOP, 0, -10, DRESS_BOTTOM));
        g.fill(dress);
        g.setColor(new Color(255, 255, 255, 90));                       // highlight
        g.fill(new Ellipse2D.Double(-6.5, -33, 4, 10));
        g.setColor(new Color(0xFFD27A));                                // gold hem
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D.Double hem = new Path2D.Double();
        hem.moveTo(-13, -13.8);
        hem.quadTo(0, -10.3, 13, -13.8);
        g.draw(hem);
        g.setStroke(new BasicStroke(1f));
        g.setColor(CREAM);                                              // collar
        g.fill(new Ellipse2D.Double(-6, -37.5, 12, 6));
        Theme.drawHeart(g, 0, -24, 7, CREAM);                           // heart on the dress
        g.setColor(new Color(0xFFD27A));                                // belt
        g.fill(new RoundRectangle2D.Double(-9.5, -27.5, 19, 2.6, 2, 2));

        // ---- front arm
        drawArm(g, 1, swing, t, mood);

        // ---- head
        g.setColor(new Color(0, 0, 0, 14));
        g.fill(new Ellipse2D.Double(-6, -35.5, 12, 4));                 // neck shadow
        g.setColor(SKIN);
        g.fill(new Ellipse2D.Double(-15.5, -62, 31, 28));
        g.setColor(SKIN_SHADE);
        g.setStroke(new BasicStroke(0.8f));
        g.draw(new Arc2D.Double(-15.5, -62, 31, 28, 205, 130, Arc2D.OPEN));
        g.setStroke(new BasicStroke(1f));

        // face
        double look = facing * 1.6;
        double blinkCycle = t % 3.6;
        boolean blink = blinkCycle > 3.45;
        double eyeH = blink ? 0.7 : (sad ? 5.2 : 5.8);
        for(int side = -1; side <= 1; side += 2) {
            double ex = side * 6 + look;
            g.setColor(EYE);
            if(mood == Mood.HAPPY && !blink) {                          // happy "^ ^" eyes
                g.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(new Arc2D.Double(ex - 2.8, -49.5, 5.6, 5.6, 20, 140, Arc2D.OPEN));
                g.setStroke(new BasicStroke(1f));
            }
            else {
                g.fill(new Ellipse2D.Double(ex - 2.9, -47 + (sad ? 1 : 0) - eyeH / 2 + 2.9, 5.8, eyeH));
                if(!blink) {
                    g.setColor(Color.WHITE);
                    g.fill(new Ellipse2D.Double(ex - 1.6, -47 + (sad ? 1 : 0) - eyeH / 2 + 3.2, 2.3, 2.3));
                    g.fill(new Ellipse2D.Double(ex + 0.6, -47 + (sad ? 1 : 0) + eyeH / 2 - 1.4, 1.2, 1.2));
                }
            }
            g.setColor(new Color(255, 120, 160, mood == Mood.HAPPY ? 150 : 105));
            g.fill(new Ellipse2D.Double(side * 10.5 + look * 0.6 - 3.8, -44, 7.6, 4.4));
        }
        g.setColor(new Color(0xB5546F));
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double mx = look * 0.6;
        if(mood == Mood.HAPPY) {
            Path2D.Double mouth = new Path2D.Double();
            mouth.moveTo(mx - 3.4, -42.2);
            mouth.quadTo(mx, -36.5, mx + 3.4, -42.2);
            mouth.closePath();
            g.setColor(new Color(0xC2486A));
            g.fill(mouth);
            g.setColor(new Color(0xFF9DB5));
            g.fill(new Ellipse2D.Double(mx - 1.6, -40.2, 3.2, 1.8));
        }
        else if(sad) {
            g.draw(new Arc2D.Double(mx - 2.6, -41.6, 5.2, 4, 20, 140, Arc2D.OPEN));
            g.setColor(new Color(110, 190, 255, 210));                   // a little tear
            double ty = -43 + (t * 12 % 9);
            g.fill(new Ellipse2D.Double(look + 7.2, ty, 2.6, 3.6));
        }
        else {
            g.draw(new Arc2D.Double(mx - 2.8, -43.2, 5.6, 4.2, 200, 140, Arc2D.OPEN));
        }
        g.setStroke(new BasicStroke(1f));

        // ---- fringe, hair cap and bow
        g.setColor(HAIR);
        g.fill(new Arc2D.Double(-17, -63.5, 34, 30, 0, 180, Arc2D.PIE));
        double[][] bangs = { { -11, -52.5, 9 }, { -4.5, -53.5, 10 }, { 3, -53.5, 10 }, { 9.5, -52.5, 9 } };
        for(double[] b : bangs) {
            g.fill(new Ellipse2D.Double(b[0] - b[2] / 2 + look * 0.4, b[1] - 2, b[2], 7.5));
        }
        g.setColor(HAIR_LIGHT);
        g.fill(new Arc2D.Double(-9, -62.5, 14, 6, 20, 120, Arc2D.OPEN));
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(HAIR_LIGHT);
        g.draw(new Arc2D.Double(-12, -63, 20, 10, 40, 100, Arc2D.OPEN));
        g.setStroke(new BasicStroke(1f));

        double bowWiggle = Math.sin(t * 2.2) * 2;                       // bow on the head
        AffineTransform keep = g.getTransform();
        g.translate(9.5, -61.5);
        g.rotate(Math.toRadians(14 + bowWiggle));
        Theme.drawHeart(g, -5.5, 0, 11, Theme.ROSE);
        g.rotate(Math.PI);
        Theme.drawHeart(g, -5.5, 0, 11, Theme.ROSE_DARK);
        g.setTransform(keep);
        g.setColor(new Color(0xFFD27A));
        g.fill(new Ellipse2D.Double(9.5 - 2.2, -61.5 - 2.2, 4.4, 4.4));

        g.dispose();
    }

    private static void drawArm(Graphics2D g, int side, double swing, double t, Mood mood)
    {
        double sx = side * 8.5, sy = -32.5;
        double ang;                                         // 0 = straight down, PI = straight up
        if(mood == Mood.HAPPY) {
            ang = Math.PI - 0.6 + Math.sin(t * 5.5) * 0.15;
        }
        else if(mood == Mood.WAVE && side > 0) {
            ang = Math.PI - 0.75 + Math.sin(t * 9) * 0.35;
        }
        else if(mood == Mood.SAD) {
            ang = 0.12;
        }
        else {
            ang = 0.24 + swing * 0.45 * side + Math.sin(t * 2.6) * 0.03;
        }
        double len = 12.5;
        double ex = sx + side * Math.sin(ang) * len;
        double ey = sy + Math.cos(ang) * len;
        g.setStroke(new BasicStroke(4.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(0xFF8FB4));                            // puffy sleeve
        g.draw(new Line2D.Double(sx, sy, sx + (ex - sx) * 0.35, sy + (ey - sy) * 0.35));
        g.setColor(SKIN);
        g.draw(new Line2D.Double(sx + (ex - sx) * 0.35, sy + (ey - sy) * 0.35, ex, ey));
        g.setStroke(new BasicStroke(1f));
        g.fill(new Ellipse2D.Double(ex - 2.9, ey - 2.9, 5.8, 5.8));
    }

    // ---------------------------------------------------------------- friends

    /** A shy little ghost who floats up and down. */
    public static void drawGhost(Graphics2D g0, double x, double feetY, double s, double t)
    {
        Graphics2D g = (Graphics2D) g0.create();
        Theme.quality(g);
        g.translate(x, feetY);
        g.scale(s, s);
        double fl = Math.sin(t * 2.4) * 3.2;
        g.setColor(new Color(0, 0, 0, 55));
        g.fill(new Ellipse2D.Double(-10 + fl * 0.2, -2.5, 20 - Math.abs(fl), 5));
        g.translate(0, -10 + fl);
        Path2D.Double body = new Path2D.Double();
        body.moveTo(-12, 0);
        body.curveTo(-14, -26, -6, -34, 0, -34);
        body.curveTo(6, -34, 14, -26, 12, 0);
        double w = Math.sin(t * 5) * 1.5;
        body.quadTo(9, 5 + w, 6, 1);
        body.quadTo(3, -3 - w, 0, 1);
        body.quadTo(-3, 5 + w, -6, 1);
        body.quadTo(-9, -3 - w, -12, 0);
        body.closePath();
        g.setPaint(new GradientPaint(0, -34, new Color(0xFFFFFF), 0, 4, new Color(0xE4DAFB)));
        g.fill(body);
        g.setColor(new Color(0xCDBDF5));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(body);
        g.setColor(EYE);
        g.fill(new Ellipse2D.Double(-6.5, -21, 4, 5));
        g.fill(new Ellipse2D.Double(2.5, -21, 4, 5));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(-5.8, -20.6, 1.6, 1.6));
        g.fill(new Ellipse2D.Double(3.2, -20.6, 1.6, 1.6));
        g.setColor(new Color(255, 130, 170, 120));
        g.fill(new Ellipse2D.Double(-10.5, -16.5, 5.5, 3.2));
        g.fill(new Ellipse2D.Double(5, -16.5, 5.5, 3.2));
        g.setColor(new Color(0xB5546F));
        g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Ellipse2D.Double(-1.6, -15, 3.2, 3.6));
        g.dispose();
    }

    /** A kind old wizard in a starry hat. */
    public static void drawWizard(Graphics2D g0, double x, double feetY, double s, double t)
    {
        Graphics2D g = (Graphics2D) g0.create();
        Theme.quality(g);
        g.translate(x, feetY);
        g.scale(s, s);
        g.translate(0, Math.sin(t * 2.0) * 0.9);
        g.setColor(new Color(0, 0, 0, 55));
        g.fill(new Ellipse2D.Double(-11, -3, 22, 6));
        Path2D.Double robe = new Path2D.Double();                       // robe
        robe.moveTo(-6, -24);
        robe.quadTo(-9, -10, -13, 0);
        robe.lineTo(13, 0);
        robe.quadTo(9, -10, 6, -24);
        robe.closePath();
        g.setPaint(new GradientPaint(0, -24, new Color(0xA88BF0), 0, 0, new Color(0x7B5BD0)));
        g.fill(robe);
        g.setColor(new Color(0xFFD27A));
        g.fill(new RoundRectangle2D.Double(-6.5, -14, 13, 2.2, 2, 2));
        g.setColor(SKIN);
        g.fill(new Ellipse2D.Double(-8, -37, 16, 15));                  // face
        g.setColor(Color.WHITE);                                        // beard
        Path2D.Double beard = new Path2D.Double();
        beard.moveTo(-7.5, -28);
        beard.quadTo(0, -9, 7.5, -28);
        beard.quadTo(0, -23, -7.5, -28);
        beard.closePath();
        g.fill(beard);
        g.setColor(EYE);
        g.fill(new Ellipse2D.Double(-4.6, -31, 2.8, 3.2));
        g.fill(new Ellipse2D.Double(1.8, -31, 2.8, 3.2));
        g.setColor(new Color(255, 120, 160, 110));
        g.fill(new Ellipse2D.Double(-8, -27.5, 4, 2.4));
        g.fill(new Ellipse2D.Double(4, -27.5, 4, 2.4));
        Path2D.Double hat = new Path2D.Double();                        // hat
        hat.moveTo(-11, -34);
        hat.quadTo(-2, -40, -3, -58);
        hat.quadTo(5, -48, 11, -34);
        hat.quadTo(0, -31, -11, -34);
        hat.closePath();
        g.setPaint(new GradientPaint(0, -58, new Color(0xB79BF5), 0, -32, new Color(0x7B5BD0)));
        g.fill(hat);
        Theme.drawStar(g, 0.5, -42, 3.2, new Color(0xFFD27A));
        double tw = 0.6 + 0.4 * Math.sin(t * 4);
        Theme.drawSparkle(g, -3, -59, 3.5 * tw, Color.WHITE);
        g.dispose();
    }
}
