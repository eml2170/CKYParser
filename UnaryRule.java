
public class UnaryRule {

	String x, w;
	
	public UnaryRule(String x, String w) {
		super();
		this.x = x;
		this.w = w;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(x);
		buffer.append("->");
		buffer.append(w);
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
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
		UnaryRule other = (UnaryRule) obj;
		if (w == null) {
			if (other.w != null)
				return false;
		} else if (!w.equals(other.w))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		return true;
	}
	
	
	
	
}
