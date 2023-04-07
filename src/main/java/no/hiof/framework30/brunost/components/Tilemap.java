package no.hiof.framework30.brunost.components;

/**
 * Represents the entire network of tiles.
 * Can be seen as a network of grids (Like a chessboard), with each Tile having their own texture.
 * Can be used to facilitate easier placement within a Scene and consistent sizing.
 *
 * @see Tileset
 * @see Tile
 */
public class Tilemap {
    private Tile[][] tiles;
    private int size;

    public Tilemap(int height, int width){
        this.tiles = new Tile[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                tiles[i][j] = new Tile();
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
