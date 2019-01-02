package court;

import enums.Player;

import java.awt.*;


public class Cell {

    private Point position;
    private Player owner;

    public Cell(Point position) {
        this.position = position;
        this.owner = Player.DEFAULT;
    }

    public Cell(int x, int y) {
        this(new Point(x, y));
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Point getPosition() {
        return position;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Cell copy() {
        Cell result = new Cell((int) position.getX(), (int) position.getY());
        result.setOwner(owner);
        return result;
    }
}
