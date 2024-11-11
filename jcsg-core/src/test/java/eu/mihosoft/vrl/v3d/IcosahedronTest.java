package eu.mihosoft.vrl.v3d;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class IcosahedronTest {

	@Test
	public void test() throws IOException {
		double radius = 10;
		
		CSG icosahedron = new Icosahedron(radius).toCSG();
		CSG box = new Cube(3*radius).toCSG().difference(new Cube(1.7013016167*radius).toCSG());
		CSG insphere = new Sphere(0.794654472292*radius).toCSG();
		
		assertTrue(icosahedron.intersect(box).getPolygons().size() == 0);
		assertTrue(insphere.difference(icosahedron).getPolygons().size() == 0);
		
		FileUtil.write(Paths.get("icosahedron.stl"),
			icosahedron.toStlString());
	}

}
