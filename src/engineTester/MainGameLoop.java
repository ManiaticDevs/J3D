package engineTester;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import models.*;
import textures.*;
import renderEngine.*;
import shaders.*;

public class MainGameLoop {
	
	
	
	public static void main(String[] args) throws LWJGLException, IOException {
		//INITIALIZE BEFORE EVERYTHING ELSE
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		//MODEL
		RawModel model = OBJLoader.loadObjModel("models/dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("textures/dragon"));
		TexturedModel staticModel = new TexturedModel(model, texture);
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		//ENTITIES
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		Camera camera = new Camera();
		
		//CURSOR
		CursorChanger curChange = new CursorChanger();
		BufferedImage img = curChange.load("/cursor/default");
		curChange.loadCursor(img);
		
		//MAINGAMELOOP
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
}
