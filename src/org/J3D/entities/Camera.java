package org.J3D.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	private float angleAroundPlayer;

	private Player player;
	
	public Camera() {}
	
	public Camera(Player player) {
		this.player = player;
	}
	
	
	public void update() {
		cAAP();
	}
	
	//calculateAngleAroundPlayer = cAAP
	private void cAAP() {
		position.x = player.getPosition().x;
		if(!player.isCrouch) {
			position.y = player.getPosition().y + 5; //5 to match with head 
		} else {
			position.y = player.getPosition().y + 2.5f; //to make look crouch

		}
		
		position.z = player.getPosition().z;

		yaw = angleAroundPlayer;
		if(Mouse.isButtonDown(0) && !Mouse.isGrabbed()) {
			Mouse.setGrabbed(true);
		}
		if(Mouse.isGrabbed()) {
			float pitchChange = Mouse.getDY() * 0.1f;
			float angleChange = Mouse.getDX() * 0.2f;
			angleAroundPlayer += angleChange;
			pitch -= pitchChange;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed()) {
			Mouse.setGrabbed(false);
		}
		
		if(pitch >= 90) {
			pitch = 90;
		}
		if(pitch <= -90) {
			pitch = -90;
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
