package ch.teko.game.model;

/**
 * Represents metadata for an asset.
 */
public class AssetsMetadata {

    /**
     * The name of the asset.
     */
    public String name;

    /**
     * The number of columns in the sprite sheet.
     */
    public int column;

    /**
     * The number of rows in the sprite sheet
     */
    public int row;

    /**
     * Constructs an AssetsMetadata object.
     *
     * @param name   The name of the asset.
     * @param column The number of columns in the sprite sheet.
     * @param row    The number of rows in the sprite sheet
     */
    public AssetsMetadata(String name, int column, int row) {
        this.name = name;
        this.column = column;
        this.row = row;
    }
}
