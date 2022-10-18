package engineTester;

import org.lwjgl.opengl.Display;

import models.*;
import textures.*;
import renderEngine.*;
import shaders.StaticShader;;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
				-0.5f, 0.5f, 0f,//v0
				-0.5f, -0.5f, 0f,//v1
				0.5f, -0.5f, 0f,//v2
				0.5f, 0.5f, 0f,//v3
		};
		
		int[] indices = {
				0,1,3,//top left triangle (v0, v1, v3)
				3,1,2//bottom right triangle (v3, v1, v2)
		};
		
		float[] textureCoords = {
			0,0, //v0
			0,1, //v1
			1,1, //v2
			1,0  //v3
		};
		
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("textures/minos"));
		TexturedModel texturedModel = new TexturedModel(model, texture);

		while(!Display.isCloseRequested()) {
			renderer.prepare();
			//   [game logic here]
			shader.start();
			renderer.render(texturedModel);
			//if you dont stop it, it gives this trippy lsd effect
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
}
