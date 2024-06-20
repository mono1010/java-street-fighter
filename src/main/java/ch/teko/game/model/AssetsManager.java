package ch.teko.game.model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.Main;
import ch.teko.game.model.*;

public class AssetsManager {

    private Logger log = LogManager.getLogger(Main.class);

    Asset assets[] = new Asset[Asset.State.values().length];

    public AssetsManager(String assetsDir) {
        try {
            List<AssetsMetadata> assetsMetadata = AssetsManager.loadAssetsMetadatas(assetsDir + "sprites.csv");

            for (Asset.State a : Asset.State.values()) {
                String assetPath = assetsDir + assetFiles[a.ordinal()] + ".png";
                log.info("Loading asset {}", assetPath);
                this.assets[a.ordinal()] = new Asset(getAssetMetadata(assetsMetadata, a), loadImageFromDisk(assetPath), a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AssetsMetadata getAssetMetadata(List<AssetsMetadata> assetsMetadatas, Asset.State asset) {
        String name = assetFiles[asset.ordinal()];
        for (AssetsMetadata metadata : assetsMetadatas) {
            if (metadata.name.equals(name)) {
                return metadata;
            }
        }

        throw new Error("metadata not found");
    }

    private static String[] assetFiles = {
            "Run",
            "Idle",
            "Jump",
            "Fall",
            "Attack1",
            "Attack2",
    };

    public Asset getAsset(Asset.State asset) {
        return this.assets[asset.ordinal()];
    }

    private static List<AssetsMetadata> loadAssetsMetadatas(String path) {
        List<AssetsMetadata> assetsMetadataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                assetsMetadataList
                        .add(new AssetsMetadata(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assetsMetadataList;
    }

    private static BufferedImage loadImageFromDisk(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
