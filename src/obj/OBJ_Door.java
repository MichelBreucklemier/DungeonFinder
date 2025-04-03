package obj;

import Utils.UtilityTool;
import entity.Entity;
import tile.MapTile;
import tile.Tile;
import tile.TileManager;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Door extends Obj implements Interactables {
    Tile doorFrame;
    public boolean unlocked = false;
    public OBJ_Door() {
        isVisible = true;
        this.type = "door";
        ANIMATION_SPEED = 5;

        PICK_UP = true;
    }
    public OBJ_Door(int x, int y, int type, int id, MapTile frame){
        this();
        doorFrame=frame;
        doorFrame.collider = true;
        row = y;
        col = x;
        this.id = id;


        setPos(x,y);

        try {
            setImage( new BufferedImage[]{switch(type){
                case 1 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 0, 33)[0];
                case 2 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 1, 33)[0];
                case 3 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 1, 34)[0];
                case 4 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 12, 33)[0];
                case 5 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 4, 37)[0];
                case 6 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 5, 37)[0];
                case 7 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 5, 38)[0];
                case 8 -> UtilityTool.cutImagePiece(UtilityTool.loadImage("tiles\\tiles.png"), 1, 16, TileManager.TILE_SIZE / 16, 6, 38)[0];

                default -> null;
            }});



        } catch (IOException e) {
            e.printStackTrace();
        }
        setCollider(new Rectangle(posX-20,posY-20,collider.width+40,collider.height+40));
        rescale();

    }
    private int openTime = 1000;
    private int timeOpen = 0;
    @Override
    public void update(){

        if(unlocked&&!isVisible) {
            doorFrame.collider = false;
            timeOpen++;
            if (timeOpen > openTime) {
                timeOpen = 0;
                isVisible = true;
            }
        }
    }

    @Override
    public boolean colliding(Entity e) {
        return isVisible && e.getCollider().intersects(collider);
    }

    @Override
    public void interact() {

    }

    @Override
    public Obj pickup() {
        return null;
    }

    @Override
    public Obj onCollide() {
        isVisible = false;
        doorFrame.collider = false;
        unlocked = true;
        return null;
    }
}
