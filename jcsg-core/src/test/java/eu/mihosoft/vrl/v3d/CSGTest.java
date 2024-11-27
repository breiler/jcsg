package eu.mihosoft.vrl.v3d;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CSGTest {
    private static final Color BLUE = Color.color(0, 0, 1);
    private static final Color RED = Color.color(1, 0, 0);

    @Test
    public void setColor_ShouldSetColorToAllPolygons() {
        CSG cube = new Cube()
                .toCSG()
                .setColor(BLUE);
        assertEquals(BLUE, cube.getColor());

        cube.getPolygons().forEach(polygon -> {
            assertEquals(BLUE, polygon.getColor(), "Expected the polygon to get the same color as the CSG");
        });
    }

    @Test
    public void setColor_OnUnionCSGShouldRetainColorsOnPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2);
        assertEquals(RED, union.getColor());

        union.getPolygons().forEach(polygon -> {
            boolean isLeftCube = polygon.getPoints().stream().allMatch(p -> p.x <= 5);
            if (isLeftCube) {
                assertEquals(BLUE, polygon.getColor(), "Expected the left cube polygons to be blue");
            } else {
                assertEquals(RED, polygon.getColor(), "Expected the right cube polygons to be red");
            }
        });
    }

    @Test
    public void setColor_OnUnionedAndTriangulatedCSGShouldRetainColorsOnPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2).triangulate();
        assertEquals(RED, union.getColor());

        union.getPolygons().forEach(polygon -> {
            boolean isLeftCube = polygon.getPoints().stream().allMatch(p -> p.x <= 5);
            if (isLeftCube) {
                assertEquals(BLUE, polygon.getColor(), "Expected the left cube polygons to be blue");
            } else {
                assertEquals(RED, polygon.getColor(), "Expected the right cube polygons to be red");
            }
        });
    }

    @Test
    public void setColor_OnCSGShouldChangeColorsOfAllPolygons() {
        CSG cube = new Cube(10).toCSG()
                .setColor(BLUE);
        assertEquals(BLUE, cube.getColor());

        cube.setColor(RED);

        cube.getPolygons().forEach(polygon -> {
            assertEquals(RED, polygon.getColor(), "Expected the cube polygons to be another color");
        });
    }

    @Test
    public void setColor_OnUnionedCSGShouldChangeColorsOfAllPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2);
        assertEquals(RED, union.getColor(), "Expected the new object to get the default color");

        union.setColor(BLUE);
        union.getPolygons().forEach(polygon -> {
            assertEquals(BLUE, polygon.getColor(), "Expected the cube polygons to be another color");
        });
    }

    @Test
    public void setColor_OnDifferenceWithOptTypeBoundsShouldChangeColorsOfIntersectingPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .move(0, 0, 0)
                .setColor(BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .move(9, 0, 0)
                .setColor(RED);

        assertEquals(CSG.OptType.CSG_BOUND, cube1.getOptType());

        CSG difference = cube1.difference(cube2);
        assertEquals(6, difference.getPolygons().size(), "Unexpected number of faces");
        difference.getPolygons().forEach(polygon -> {
            Vector3d center = polygon.getBounds().getCenter();
            if (center.equals(Vector3d.xyz(-5, 0, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the left face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, -5, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the front face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 5, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the back face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 0, -5))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the bottom face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 0, 5))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the top face to have another color");
            } else if (center.equals(Vector3d.xyz(4, 0, 0))) {
                assertNull(polygon.getColor(), "Expected the right face to have no color");
            } else {
                fail("Unknown face with center" + center);
            }
        });
    }

    @Test
    public void setColor_OnDifferenceWithOptTypePolygonShouldChangeColorsOfIntersectingPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .move(0, 0, 0)
                .setColor(BLUE)
                .setOptType(CSG.OptType.POLYGON_BOUND);

        CSG cube2 = new Cube(10).toCSG()
                .move(9, 0, 0)
                .setColor(RED);

        assertEquals(CSG.OptType.POLYGON_BOUND, cube1.getOptType());

        CSG difference = cube1.difference(cube2);
        assertEquals(6, difference.getPolygons().size(), "Unexpected number of faces");
        difference.getPolygons().forEach(polygon -> {
            Vector3d center = polygon.getBounds().getCenter();
            if (center.equals(Vector3d.xyz(-5, 0, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the left face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, -5, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the front face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 5, 0))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the back face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 0, -5))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the bottom face to have another color");
            } else if (center.equals(Vector3d.xyz(-0.5, 0, 5))) {
                assertEquals(BLUE, polygon.getColor(), "Expected the top face to have another color");
            } else if (center.equals(Vector3d.xyz(4, 0, 0))) {
                assertEquals(RED, polygon.getColor(), "Expected the right face to have another color");
            } else {
                fail("Unknown face with center" + center);
            }
        });
    }

    @Test
    public void difference_ShouldContainEnoughPolygonsForEachFace() throws IOException {
        CSG cube1 = new Cube(10).toCSG()
                .move(0, 0, 0)
                .triangulate(false);

        CSG cube2 = new Cube(10).toCSG()
                .move(1, 0, 0)
                .triangulate(false);

        CSG difference = cube1.difference(cube2);

        assertTrue(difference.getPolygons().size() >= 6, "Must contain at least 12 polygons (one for each face) but was " + difference.getPolygons().size() );
        List<Vector3d> points = difference.getPolygons().stream().flatMap(polygon -> polygon.getPoints().stream()).collect(Collectors.toList());
        points.forEach(point -> {
            assertTrue(point.getX() <= -3.99, "Expected X to be smaller " + point);
            assertTrue(point.getX() >= -5.01, "Expected X to be larger " + point);
            assertTrue(point.getY() >= -5.01, "Expected Y to be larger " + point);
            assertTrue(point.getY() <= 5.01, "Expected Y to be smaller " + point);
            assertTrue(point.getZ() >= -5.01, "Expected Z to be larger " + point);
            assertTrue(point.getZ() <= 5.01, "Expected Z to be smaller " + point);
        });
    }

    @Test
    public void textTriangulateShouldCreateSixFaces() {
        CSG text = CSG.text("e", 1);
        assertFalse( text.getPolygons().isEmpty(), "Should be able to generate a shape for default text");
    }
}
