package ch.teko.game.model;

import java.awt.image.BufferedImage;

class Asset {

    public enum State {
        RUN,
        IDLE,
        JUMP,
        FALL,
        ATTACK1,
        ATTACK2,
    }

    public AssetsMetadata metadata;
    public BufferedImage image;
    public State state;

    public Asset(AssetsMetadata metadata, BufferedImage image, State state) {
        this.metadata = metadata;
        this.image = image;
        this.state = state;
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