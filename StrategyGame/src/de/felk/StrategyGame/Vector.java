package de.felk.StrategyGame;

public class Vector {

	private float x;
	private float y;
	private float z;
	private float value;
	private boolean valueDirty = true;

	public Vector(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public Vector(float xyz) {
		this.setX(xyz);
		this.setY(xyz);
		this.setZ(xyz);
	}

	public Vector() {
	}

	// Constructor for cloning
	private Vector(float x, float y, float z, float value, boolean valueDirty) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = value;
		this.valueDirty = valueDirty;
	}

	public Vector addX(float x) {
		setX(getX() + x);
		return this;
	}

	public Vector addY(float y) {
		setY(getY() + y);
		return this;
	}

	public Vector addZ(float z) {
		setZ(getZ() + z);
		return this;
	}

	public Vector add(Vector v) {
		addX(v.getX());
		addY(v.getY());
		addZ(v.getZ());
		return this;
	}

	public Vector subtract(Vector v) {
		addX(-v.getX());
		addY(-v.getY());
		addZ(-v.getZ());
		return this;
	}

	public Vector multiply(float r) {
		setX(getX() * r);
		setY(getY() * r);
		setZ(getZ() * r);
		return this;
	}

	public Vector normalize() {
		return setValue(1f);
	}

	public Vector added(Vector v) {
		return clone().add(v);
	}

	public Vector subtracted(Vector v) {
		return clone().subtract(v);
	}

	public Vector multiplied(float r) {
		return clone().multiply(r);
	}

	public Vector normalized() {
		return clone().normalize();
	}

	public Vector set(Vector v) {
		setX(v.getX());
		setY(v.getY());
		setZ(v.getZ());
		return this;
	}

	public float getX() {
		return x;
	}

	public Vector setX(float x) {
		this.x = x;
		valueDirty = true;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vector setY(float y) {
		this.y = y;
		valueDirty = true;
		return this;
	}

	public float getZ() {
		return z;
	}

	public Vector setZ(float z) {
		this.z = z;
		valueDirty = true;
		return this;
	}

	public float getValue() {
		if (valueDirty)
			recalculateValue();
		return value;
	}

	public float getSquaredValue() {
		return x * x + y * y + z * z;
	}

	public Vector setValue(float value) {
		multiply(value / getValue());
		this.value = value;
		valueDirty = false;
		return this;
	}

	private void recalculateValue() {
		this.value = (float) Math.sqrt(getSquaredValue());
	}

	@Override
	public Vector clone() {
		return new Vector(this.x, this.y, this.z, this.value, this.valueDirty);
	}

	@Override
	public String toString() {
		return "(" + x + "|" + y + "|" + z + ")";
	}

	public Position toPosition() {
		return new Position((int) getX(), (int) getZ());
	}

}
