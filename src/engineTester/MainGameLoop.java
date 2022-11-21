package engineTester;

import java.io.IOException;
import java.util.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import models.*;
import renderEngine.*;
import terrains.*;
import textures.*;
import toolbox.*;

public class MainGameLoop {

	public static void main(String[] args) throws LWJGLException, IOException {
		//INITIALIZE BEFORE EVERYTHING ELSE
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//MODEL
		RawModel treeTexture = OBJLoader.loadObjModel("models/tree", loader);
		RawModel grassTexture = OBJLoader.loadObjModel("models/grassModel", loader);
		TexturedModel treeModel = new TexturedModel(treeTexture,new ModelTexture(loader.loadTexture("textures/tree")));
		TexturedModel grassModel = new TexturedModel(grassTexture,new ModelTexture(loader.loadTexture("textures/grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);

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
		
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("textures/grass")));
		Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("textures/grass")));
		
		MasterRenderer renderer = new MasterRenderer();
		Discord.details = "Developing J3D";
		Discord.main(args);
		
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
