package org.J3D.entities;

import org.J3D.models.TexturedModel;
import org.J3D.renderEngine.DisplayManager;
import org.J3D.terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {

	private static final float RUN_SPEED = 20;
	private static final float HORZ_RUN_SPEED = 15;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 15;
	
	private static final float TERRAIN_HEIGHT = 0;	
	private float currentSpeed = 0;
	private float horzCurrentSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	public boolean isFor, isStrafe;
	public boolean isFPS = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(float camY, Terrain terrain) {
		//System.out.println(isFPS);
		checkInputs();
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float distanceStrafe;
		float dx;
		float dz;
		float dx1;
		float dz1;
		
		if(isFPS) {
			super.setRotY(-camY);
			distanceStrafe = horzCurrentSpeed * DisplayManager.getFrameTimeSeconds();
			dx = (float) (distance * Math.sin(Math.toRadians(-camY)));
			dz = (float) (distance * Math.cos(Math.toRadians(-camY)));
			dx1 = (float) (distanceStrafe * Math.sin(Math.toRadians(-camY + 90)));
			dz1 = (float) (distanceStrafe * Math.cos(Math.toRadians(-camY + 90)));
			
			if(isFor) {
				//super.increasePosition(Maths.getOffset(dx, 0.5f), 0, Maths.getOffset(dx, 0.5f));
				super.getPosition().x += dx;
				super.getPosition().z += dz;
			}
			if(isStrafe) {
				super.increasePosition(dx1, 0, dz1);
			}
		} 
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		//jump
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		if(super.getPosition().y<terrainHeight) {
			isInAir = false;
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir && isFPS) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
		
	}
	
	private void checkInputs() {
		if(isFPS) {
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
		} 
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			if(!isFor && !isStrafe) {
				isFPS = !isFPS;
			}
			
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
