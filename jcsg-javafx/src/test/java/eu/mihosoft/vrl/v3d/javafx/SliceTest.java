package eu.mihosoft.vrl.v3d.javafx;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Transform;
import org.junit.jupiter.api.Test;

import java.util.List;



/**
 * Created by Ryan Benasutti on 5/30/2016.
 */

public class SliceTest {
	@Test
	public void slice() throws Exception {
		// Create a CSG to slice
		CSG carrot = new Cube(10, 10, 10).toCSG();

		// Get a slice
		List<Polygon> polygons = Slice.slice(carrot, new Transform(), 0);

		// Construct a CSG from that Polygon List
		CSG finished = CSG.fromPolygons(polygons);

		//System.out.println(finished.toStlString());
	}

	@Test
	public void sliceWithHole() throws Exception {
		// Create a CSG to slice
		CSG carrot = new Cube(10, 10, 10).toCSG().difference(new Cube(4, 4, 100).toCSG());

		// Get a slice
		List<Polygon> polygons = Slice.slice(carrot, new Transform(), 0);

		// Construct a CSG from that Polygon List
		CSG finished = CSG.fromPolygons(polygons);

		//System.out.println(finished.toStlString());
	}
}