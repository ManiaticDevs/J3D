package org.J3D.engineTester;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.*;
import org.J3D.entities.*;
import org.J3D.models.*;
import org.J3D.objConverter.*;
import org.J3D.renderEngine.*;
import org.J3D.renderEngine.renderers.*;
import org.J3D.terrains.*;
import org.J3D.textures.terrain.*;
import org.J3D.textures.gui.GuiTexture;
import org.J3D.textures.model.*;
import org.J3D.toolbox.*;

public class MainGameLoop {

	public static void main(String[] args) throws Exception {
		//DISCORD
		Discord.details = "Developing J3D";
		Discord.state = "";
		Discord.main(args);
		
		//INITIALIZE BEFORE EVERYTHING ELSE THAT USES OPENGL
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		//TERRAIN TEXTURES
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/terrain/grassy", true));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/terrain/mud", true));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/terrain/grassFlowers", true));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/terrain/path", true));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/terrain/maps/blendMap", false));
		
		//TERRAIN 
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain1 = new Terrain(0,-1,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		Terrain terrain2 = new Terrain(0,0,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		Terrain terrain3 = new Terrain(-1,0,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		Terrain terrain4 = new Terrain(-1,-1,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		terrains.add(terrain1);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);
		
		//MODELDATA
		ModelData dataTree = OBJFileLoader.loadOBJ("models/tree");
		ModelData dataFern = OBJFileLoader.loadOBJ("models/fern");
		ModelData dataPlayer = OBJFileLoader.loadOBJ("models/spartan_remade");
		RawModel treeRaw = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
		RawModel grassRaw = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
		RawModel playerRaw = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
		
		
		//TEXTURES
		TexturedModel treeModel = new TexturedModel(treeRaw, new ModelTexture(loader.loadTexture("textures/models/tree", true)));
		TexturedModel fernModel = new TexturedModel(grassRaw, new ModelTexture(loader.loadTexture("textures/models/atlas/fern", true)));
		TexturedModel playerModel = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("textures/flat/blank", false)));
		
		fernModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(false);
		fernModel.getTexture().setNumberOfRows(2);
		playerModel.getTexture().setUseFakeLighting(true);
		//RANDOM PLACER
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(2345);
		
		for(Terrain terrain : terrains) {
			for(int i=0;i<500;i++){
				if(i % 5 == 0) {
					float x = random.nextInt((int) terrain.getSize()) + terrain.getX();
					float z = random.nextInt((int) terrain.getSize() ) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(treeModel, new Vector3f(x,y,z),0,random.nextFloat() * 360,random.nextFloat() * 0.1f + 0.6f,5));
				}
				
				if(i % 10 == 0) {
					float x = random.nextInt((int) terrain.getSize()) + terrain.getX();
					float z = random.nextInt((int) terrain.getSize() ) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x,y,z),0,random.nextFloat() * 360,0,1));
				}
				
			}
		}	
		
		//ENTITIES
		Light light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1.1f,1.1f,1.1f));
		Camera cam = new Camera();
		Player player = new Player(playerModel, new Vector3f(74,0,-19), 0, 0, 0, 0.075f);
		
		cam = new Camera(player);
		entities.add(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("textures/flat/minos", false), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		//MAINGAMELOOP
		while(!Display.isCloseRequested()) {
			for(Entity entity:entities) {renderer.processEntity(entity);}
			for (Terrain terrain : terrains) {
				renderer.processTerrain(terrain);
				if (player.getPosition().x < terrain.getX() + Terrain.SIZE && player.getPosition().x >= terrain.getX() && player.getPosition().z >= terrain.getZ() && player.getPosition().z < terrain.getZ() + Terrain.SIZE) {
					player.move(cam.getYaw(), terrain);
				}
			}
			
			cam.update();
			renderer.render(light, cam);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
			//System.out.println("fps: " + DisplayManager.logFrame());
		}
		//ON WINDOW CLOSE
		Discord.isOn = false;
		renderer.cleanUp();
		loader.cleanUp();
		guiRenderer.cleanUp();
		DisplayManager.closeDisplay();
	}
}
