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
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/blendMap"));
		
		//TERRAIN 
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain1 = new Terrain(0,-1,loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1,-1,loader, texturePack, blendMap);
		terrains.add(terrain1);
		terrains.add(terrain2);
		
		//MODELDATA
		ModelData dataTree = OBJFileLoader.loadOBJ("models/tree");
		ModelData dataGrass = OBJFileLoader.loadOBJ("models/grassModel");
		ModelData dataPlayer = OBJFileLoader.loadOBJ("models/dragon");
		RawModel treeRaw = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
		RawModel grassRaw = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
		RawModel playerRaw = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
		
		//TEXTURES
		TexturedModel treeModel = new TexturedModel(treeRaw, new ModelTexture(loader.loadTexture("textures/tree")));
		TexturedModel grassModel = new TexturedModel(grassRaw, new ModelTexture(loader.loadTexture("textures/grassTexture")));
		TexturedModel playerModel = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("textures/dragon")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);
		
		//RANDOM PLACER
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(treeModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -400),0,0,0,5));
		}
			

		
		for(int i=0;i<200;i++){
			entities.add(new Entity(grassModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -400),0,0,0,1));
		}
		
		//ENTITIES
		
		Light light = new Light(new Vector3f(0,20000,20000), new Vector3f(1,1,1));
		Camera cam = new Camera();
		Player player = new Player(playerModel, new Vector3f(0,0,-10), 0, 0, 0, 1);
		
		cam = new Camera(player);
		
		
		
		entities.add(player);
		//MAINGAMELOOP
		while(!Display.isCloseRequested()) {
			/*Vector3f camPos = new Vector3f(player.getPosition().x, player.getPosition().y + 3f, player.getPosition().z);
			camera.setCamPosRot(camPos, -player.getRotX(), -player.getRotY() + 180f, -player.getRotZ());*/
			cam.update();
			player.move(cam.getYaw());
			
			for(Entity entity:entities) {renderer.processEntity(entity);}
			for(Terrain terrain:terrains) {renderer.processTerrain(terrain);}
			
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
