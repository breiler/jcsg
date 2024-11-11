package eu.mihosoft.vrl.v3d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;



import java.io.IOException;
import java.nio.file.Paths;


public class TetrahedronTest {

	@Test
	public void test() throws IOException {
		double radius = 10;
		
		CSG tetrahedron = new Tetrahedron(radius).toCSG();
		CSG box = new Cube(3*radius).toCSG().difference(new Cube(2*radius).toCSG());
		CSG insphere = new Sphere(1.0/3.0).toCSG();
		
		assertTrue(tetrahedron.intersect(box).getPolygons().size() == 0);
		assertTrue(insphere.difference(tetrahedron).getPolygons().size() == 0);
		
		FileUtil.write(Paths.get("tetrahedron.stl"),
			tetrahedron.toStlString());
	}

}
