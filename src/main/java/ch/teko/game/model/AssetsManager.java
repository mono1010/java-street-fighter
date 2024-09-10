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


/**
 * The {@code AssetsManager} class handles the loading and management of game assets.
 * It loads sprite images and metadata for various asset states (e.g., running, jumping, etc.).
 * The assets are loaded from disk based on their metadata and image files and stored for future use.
 */
public class AssetsManager {

    // Logger for logging information about asset loading
    private Logger log = LogManager.getLogger(Main.class);

    // Array to store assets based on their state
    Asset assets[] = new Asset[Asset.State.values().length];

    /**
     * Constructs an {@code AssetsManager} object and loads all assets from the provided directory.
     *
     * @param assetsDir the directory containing the asset files and metadata.
     */
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

    /**
     * Retrieves the metadata for a specific asset based on its state.
     *
     * @param assetsMetadatas the list of asset metadata loaded from the CSV file.
     * @param asset the state of the asset for which metadata is requested.
     * @return the {@code AssetsMetadata} object for the specified asset.
     * @throws Error if the metadata for the requested asset is not found.
     */
    public AssetsMetadata getAssetMetadata(List<AssetsMetadata> assetsMetadatas, Asset.State asset) {
        String name = assetFiles[asset.ordinal()];
        for (AssetsMetadata metadata : assetsMetadatas) {
            if (metadata.name.equals(name)) {
                return metadata;
            }
        }

        throw new Error("metadata not found");
    }

    // Array containing the file names of assets corresponding to their states
    private static String[] assetFiles = {
            "Run",
            "Idle",
            "Jump",
            "Fall",
            "Attack1",
            "Attack2",
    };

    /**
     * Retrieves the asset for a given state.
     *
     * @param asset the state of the asset to be retrieved.
     * @return the {@code Asset} object corresponding to the requested state.
     */
    public Asset getAsset(Asset.State asset) {
        return this.assets[asset.ordinal()];
    }

    /**
     * Loads the metadata for all assets from a CSV file located at the given path.
     * Each row in the CSV file contains the asset name, sprite columns and sprite rows.
     *
     * @param path the file path to the CSV file containing the asset metadata.
     * @return a list of {@code AssetsMetadata} objects representing the metadata of the assets.
     */
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

    /**
     * Loads an image from the disk at the specified path.
     *
     * @param path the file path of the image to be loaded.
     * @return a {@code BufferedImage} object containing the loaded image, or {@code null} if the image could not be loaded.
     */
    private static BufferedImage loadImageFromDisk(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

