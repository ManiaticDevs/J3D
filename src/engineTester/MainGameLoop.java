package engineTester;

import java.io.*;
import java.util.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import models.*;
import objConverter.*;
import renderEngine.*;
import terrains.*;
import textures.*;
import toolbox.*;

public class MainGameLoop {

	public static void main(String[] args) throws LWJGLException, IOException {
		//DISCORD
		Discord.details = "Developing J3D";
		Discord.state = "";
		Discord.main(args);
		
		//INITIALIZE BEFORE EVERYTHING ELSE
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//TERRAIN TEXTURES
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/blendMap"));
		
		//MODELDATA
		ModelData dataTree = OBJFileLoader.loadOBJ("models/tree");
		ModelData dataGrass = OBJFileLoader.loadOBJ("models/grassModel");
		RawModel tree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
		RawModel grass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
		
		//TEXTURES
		TexturedModel treeModel = new TexturedModel(tree, new ModelTexture(loader.loadTexture("textures/tree")));
		TexturedModel grassModel = new TexturedModel(grass, new ModelTexture(loader.loadTexture("textures/grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);
		
		//RANDOM PLACER
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(treeModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
		}
		
		for(int i=0;i<200;i++){
			entities.add(new Entity(grassModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
		}
		
		//ENTITIES
		Light light = new Light(new Vector3f(20000,20000,20000), new Vector3f(1,1,1));
		Camera camera = new Camera(new Vector3f(0,5,0));
		
		Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1,-1,loader, texturePack, blendMap);
		
		MasterRenderer renderer = new MasterRenderer();
		
		//MAINGAMELOOP
		while(!Display.isCloseRequested()) {
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities) {renderer.processEntity(entity);}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		Discord.isOn = false;
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
}
