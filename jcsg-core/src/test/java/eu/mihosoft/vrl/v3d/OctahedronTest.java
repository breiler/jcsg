package eu.mihosoft.vrl.v3d;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OctahedronTest {

	@Test
	public void test() throws IOException {
		double radius = 10;
		
		CSG octahedron = new Octahedron(radius).toCSG();
		CSG box = new Cube(3*radius).toCSG().difference(new Cube(2*radius).toCSG());
		CSG insphere = new Sphere(Math.sqrt(6)/6*radius).toCSG();
		
		assertTrue(octahedron.intersect(box).getPolygons().size() == 0);
		assertTrue(insphere.difference(octahedron).getPolygons().size() == 0);
		
		FileUtil.write(Paths.get("octahedron.stl"),
			octahedron.toStlString());
	}

}
