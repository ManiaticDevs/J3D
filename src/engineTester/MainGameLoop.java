package engineTester;

import org.lwjgl.opengl.Display;

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
		
		
		RawModel model = loader.loadToVAO(vertices, indices);

		while(!Display.isCloseRequested()) {
			renderer.prepare();
			//   [game logic here]
			shader.start();
			renderer.render(model);
			//if you dont stop it, it gives this trippy lsd effect
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
}
