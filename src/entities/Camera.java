package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 10;
	private float yaw;
	private float roll;
	private float speed = 0.02f;
	
	public Camera(Vector3f position) {
		this.position = position;
	}
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			position.y -= speed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			speed = 0.08f;
		} else {
			speed = 0.06f;
		}
		
		
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
