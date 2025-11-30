package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
	private double x;
	private double y;

	public FunctionPoint(double _x, double _y) { // создаёт объект точки с заданными координатами
		this.x = _x;
		this.y = _y;
	}

	public FunctionPoint(FunctionPoint point) { // создаёт объект точки с теми же координатами, что у указанной точки
		this.x = point.x;
		this.y = point.y;
	}
	
	public FunctionPoint() { // создаёт точку с координатами (0; 0)
		this.x = 0;
		this.y = 0;
	}

	public double getX () { // возвращаем значение x
		return x;
	}

	public double getY () { // возвращаем значение y
		return y;
	}

	public void setX (double _x) { // изменяем значение x
		x = _x;
	}
	
	public void setY(double _y) { // изменяем значение y
		y = _y;
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FunctionPoint)) return false;

		FunctionPoint that = (FunctionPoint) o;
		return (Math.abs(this.x - that.x) < 1e-10) && (Math.abs(this.y - that.y) < 1e-10);
	}

	@Override
	public int hashCode() {
		long xBits = Double.doubleToLongBits(x);
		long yBits = Double.doubleToLongBits(y);

		int xHash = (int)(xBits ^ (xBits >>> 32));
		int yHash = (int)(yBits ^ (yBits >>> 32));

		return xHash ^ yHash;
	}

	@Override
	public Object clone() {
		return new FunctionPoint(this);
	}
}