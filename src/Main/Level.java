package Main;

public class Level {
	public int level;
	public int map;
	public String path;

	public Level(int level, int map, String path) {
		this.level = level;
		this.map = map;
		this.path = path;
	}

	@Override
	public String toString() {
		return "level:"+level + " map:"+map + " path:"+path;
	}
}