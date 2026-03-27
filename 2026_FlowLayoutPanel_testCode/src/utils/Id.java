package utils;

public class Id{
	public int id;
	public String name;
	public Id(int id, String name){
		this.id = id; this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
}
