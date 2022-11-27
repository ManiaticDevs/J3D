package org.J3D.engineTester;

import java.io.*;
import java.util.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import org.J3D.entities.*;
import org.J3D.models.*;
import org.J3D.objConverter.*;
import org.J3D.renderEngine.*;
import org.J3D.terrains.*;
import org.J3D.textures.*;
import org.J3D.toolbox.*;

public class MainGameLoop {

	public static void main(String[] args) throws LWJGLException, IOException {
		
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
		//List<Terrain> terrains = new ArrayList<Terrain>();
		//Terrain terrain2 = new Terrain(-1,-1,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		//terrains.add(terrain1);
		//terrains.add(terrain2);
		
		Terrain terrain1 = new Terrain(-1,-1,loader, texturePack, blendMap, "textures/terrain/maps/heightMap");
		
		//MODELDATA
		ModelData dataTree = OBJFileLoader.loadOBJ("models/tree");
		ModelData dataGrass = OBJFileLoader.loadOBJ("models/grassModel");
		ModelData dataPlayer = OBJFileLoader.loadOBJ("models/spartan_remade");
		RawModel treeRaw = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
		RawModel grassRaw = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
		RawModel playerRaw = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
		
		//TEXTURES
		TexturedModel treeModel = new TexturedModel(treeRaw, new ModelTexture(loader.loadTexture("textures/models/tree", true)));
		TexturedModel grassModel = new TexturedModel(grassRaw, new ModelTexture(loader.loadTexture("textures/models/grassTexture", false)));
		TexturedModel playerModel = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("textures/models/spartan", false)));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);
		playerModel.getTexture().setUseFakeLighting(true);
		//RANDOM PLACER
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(2345);
		
		
		
		for(int i=0;i<500;i++){
			if(i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain1.getHeightOfTerrain(x, z);
				entities.add(new Entity(treeModel, new Vector3f(x,y,z),0,random.nextFloat() * 360,random.nextFloat() * 0.1f + 0.6f,5));
			}
			
			if(i % 20 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain1.getHeightOfTerrain(x, z);
				entities.add(new Entity(grassModel, new Vector3f(0,y,0),0,random.nextFloat() * 360,0,1));
			}
			
		}
		
		System.out.println(entities.get(0).getPosition());
		
		//ENTITIES
		
		Light light = new Light(new Vector3f(0,20000,20000), new Vector3f(1,1,1));
		Camera cam = new Camera();
		Player player = new Player(playerModel, new Vector3f(74,0,-19), 0, 0, 0, 0.075f);
		
		cam = new Camera(player);
		cam.getPosition().x = 74;
		cam.getPosition().y = 12;
		cam.getPosition().z = 5;
		entities.add(player);
		//MAINGAMELOOP
		while(!Display.isCloseRequested()) {
			cam.update();
			player.move(cam.getYaw(), terrain1);
			//System.out.println(cam.getPosition());
			renderer.processTerrain(terrain1);
			for(Entity entity:entities) {renderer.processEntity(entity);}
			//for(Terrain terrain:terrains) {renderer.processTerrain(terrain);}
			
			renderer.render(light, cam);
			DisplayManager.logFrame();
			DisplayManager.updateDisplay();
		}
		//ON WINDOW CLOSE
		Discord.isOn = false;
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
}
