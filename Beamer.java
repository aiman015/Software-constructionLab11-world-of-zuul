/**
 * A beamer is an item that can be charged and fired. Charging memorises a
 * room; firing transports the player back to that room.
 */
public class Beamer extends Item
{
    private Room chargedRoom;

    public Beamer()
    {
        super("beamer", "a humming brass device with a big red button", 500, true);
        chargedRoom = null;
    }

    public void charge(Room room) { chargedRoom = room; }
    public boolean isCharged() { return chargedRoom != null; }

    /** Fire the beamer: returns the memorised room and discharges the device. */
    public Room fire()
    {
        Room destination = chargedRoom;
        chargedRoom = null;
        return destination;
    }
}
