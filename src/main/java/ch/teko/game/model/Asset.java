package ch.teko.game.model;

import java.awt.image.BufferedImage;

class Asset {

    public enum State {
        RUN,
        IDLE,
        JUMP,
        FALL,
    }

    public AssetsMetadata metadata;
    public BufferedImage image;

    public Asset(AssetsMetadata metadata, BufferedImage image) {
        this.metadata = metadata;
        this.image = image;
    }
}

class AssetsMetadata {
    public String name;
    public int column;
    public int row;

    public AssetsMetadata(String name, int column, int row) {
        this.name = name;
        this.column = column;
        this.row = row;
    }
}