package org.J3D.entities;

import org.J3D.renderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	private float angleAroundPlayer;

	
	private static float RUN_SPEED = 20;
	private static float HORZ_RUN_SPEED = 20;
	private float currentSpeed = 0;
	private float horzCurrentSpeed = 0;
	private boolean isFor, isStrafe;
	
	private Player player;
	
	public Camera() {}
	
	public Camera(Player player) {
		this.player = player;
	}
	
	
	public void update() {
		cAAP();
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float distanceStrafe = horzCurrentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx1 = (float) (distanceStrafe * Math.sin(Math.toRadians(-yaw + 90)));
		float dz1 = (float) (distanceStrafe * Math.cos(Math.toRadians(-yaw + 90)));
		float dx = (float) (distance * Math.sin(Math.toRadians(-yaw)));
		float dz = (float) (distance * Math.cos(Math.toRadians(-yaw)));
		if(!player.isFPS) {
			move();
			if(isFor) {
				position.x += dx;
				position.z += dz;
			}
			if(isStrafe) {
				position.x += dx1;
				position.z += dz1;
			}
		}
		
		
	}
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			isFor = true;
			this.currentSpeed = -RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			isFor = true;
			this.currentSpeed = RUN_SPEED;
		} else {
			isFor = false;
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			isStrafe = true;
			this.horzCurrentSpeed = -HORZ_RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			isStrafe = true;
			this.horzCurrentSpeed = HORZ_RUN_SPEED;
		} else {
			isStrafe = false;
			this.horzCurrentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.2;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			position.y -= 0.2;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			RUN_SPEED = 30;
			HORZ_RUN_SPEED = 30;
		} else {
			RUN_SPEED = 20;
			HORZ_RUN_SPEED = 20;
		}
		
	}
	
	//calculateAngleAroundPlayer = cAAP
	private void cAAP() {
		if(player.isFPS) {
			position.x = player.getPosition().x;
			position.y = player.getPosition().y + 4;
			position.z = player.getPosition().z;
			
			
		}
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
