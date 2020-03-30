package ui.terrains;

import ui.renderEngine.Loader;
import ui.textures.TerrainTexture;
import ui.textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterTerrain {

    private final TerrainTexture blendMap;
    private final TerrainTexturePack pack;

    private List<Terrain> terrains;
    private HashMap<String, Terrain> gridNumToTerrain;
    private HeightsGenerator heightsGenerator;

    private float minX, minY;
    private float maxX, maxY;

    public MasterTerrain(float minX, float minY, float maxX, float maxY, HeightsGenerator heightsGenerator, Loader loader) {
        this.terrains = new ArrayList<>();
        this.gridNumToTerrain = new HashMap<>();
        this.heightsGenerator = heightsGenerator;
        this.blendMap = new TerrainTexture(loader.loadTexture("game/terrain/blendMap"));
        this.pack = createTexturePack(loader);
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        int minGridX = (int) Math.floor(minX / Terrain.getSIZE());
        int minGridY = (int) Math.floor(minY / Terrain.getSIZE());
        int maxGridX = (int) Math.floor(maxX / Terrain.getSIZE());
        int maxGridY = (int) Math.floor(maxY / Terrain.getSIZE());

        for (int i = minGridX; i <= maxGridX; i++) {
            for (int j = minGridY; j <= maxGridY; j++) {
                Terrain terrain = new Terrain(i, j, loader, pack, blendMap, heightsGenerator);
                this.terrains.add(terrain);
                this.gridNumToTerrain.put("["+i+","+j+"]", terrain);
            }
        }

    }

    private TerrainTexturePack createTexturePack(Loader loader) {
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("game/terrain/golfTerrain"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("game/terrain/sand"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("game/terrain/grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("game/terrain/sand"));
        return new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public float getHeightOfTerrain(float x, float z) {
        int gridX = (int) Math.floor(x / Terrain.getSIZE());
        int gridZ = (int) Math.floor(z / Terrain.getSIZE());
        return gridNumToTerrain.get("["+gridX+","+gridZ+"]").getHeightOfTerrain(x, z);
    }

}
