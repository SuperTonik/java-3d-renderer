package engineTester;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.joml.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import texture.ModelTexture;

public class Main {
	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		texture.setShineDamper(3);
		texture.setReflectivity(0.2f);
		Entity dragon1 = new Entity(texturedModel, new Vector3f(0, -6, -20), 0, 0, 0, 1);
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(-20, -7, -10), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		MasterRenderer renderer = new MasterRenderer();
		
		while (!glfwWindowShouldClose(DisplayManager.window)) {
			dragon1.increaseRotation(0, 0.2f, 0);
			camera.move();
			renderer.processEntity(dragon1);
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
