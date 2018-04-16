package server;

public class Player implements Moveable {

    private int x,y;

    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String getResourcePath() {
        return "TileSprites/Playertest.png";
    }
}
