package org.theya.sustain.util;

public class Position {
	
	private int x, y ,z;
	
	public Position() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
}
