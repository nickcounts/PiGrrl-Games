import acm.graphics.GLabel;
import acm.program.GraphicsProgram;

public class TestProp extends GraphicsProgram {
	
	
	public static void main(String[] args) {
		
		ConfigLoader.load(HardwareConstants.class, "hardwareConfig.properties");
		
	}
	
	public void run() {
		GLabel l = new GLabel("This is a long string that hopefully will be wrapped", 0, 50);
		add(l);
		
		
	}
	
	
}
