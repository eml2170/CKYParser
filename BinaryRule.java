
public class BinaryRule {

	String x, y1, y2;
	

	public BinaryRule(String x, String y1, String y2) {
		super();
		this.x = x;
		this.y1 = y1;
		this.y2 = y2;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(x);
		buffer.append("->");
		buffer.append(y1);
		buffer.append(" ");
		buffer.append(y2);
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y1 == null) ? 0 : y1.hashCode());
		result = prime * result + ((y2 == null) ? 0 : y2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryRule other = (BinaryRule) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y1 == null) {
			if (other.y1 != null)
				return false;
		} else if (!y1.equals(other.y1))
			return false;
		if (y2 == null) {
			if (other.y2 != null)
				return false;
		} else if (!y2.equals(other.y2))
			return false;
		return true;
	}
	
	
	
}
