package entities;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import tools.Keyboard;

public class Camera {

	private Vector3f position = new Vector3f(0f, 10f, 0f);
	private float pitch;
	private float yaw = 180f;
	private float roll;
	private float speed = 0.2f;
	
	public Camera() {}

	public void move() {
		if (Keyboard.isKeyDown(GLFW_KEY_W)) position.z -= speed;
		if (Keyboard.isKeyDown(GLFW_KEY_S)) position.z += speed;
		if (Keyboard.isKeyDown(GLFW_KEY_D)) position.x += speed;
		if (Keyboard.isKeyDown(GLFW_KEY_A)) position.x -= speed;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
}
