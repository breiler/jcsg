package eu.mihosoft.vrl.v3d;

import javafx.scene.paint.Color;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CSGTest {

    @Test
    public void setColor_ShouldSetColorToAllPolygons() {
        CSG cube = new Cube()
                .toCSG()
                .setColor(Color.BLUE);
        assertEquals(Color.BLUE, cube.getColor());

        cube.getPolygons().forEach(polygon -> {
            assertEquals("Expected the polygon to get the same color as the CSG", Color.BLUE, polygon.getColor());
        });
    }

    @Test
    public void setColor_OnUnionCSGShouldRetainColorsOnPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(Color.BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(Color.RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2);
        assertEquals(CSG.getDefaultColor(), union.getColor());

        union.getPolygons().forEach(polygon -> {
            boolean isLeftCube = polygon.getPoints().stream().allMatch(p -> p.x <= 5);
            if (isLeftCube) {
                assertEquals("Expected the left cube polygons to be blue", Color.BLUE, polygon.getColor());
            } else {
                assertEquals("Expected the right cube polygons to be red", Color.RED, polygon.getColor());
            }
        });
    }

    @Test
    public void setColor_OnUnionedAndTriangulatedCSGShouldRetainColorsOnPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(Color.BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(Color.RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2).triangulate();
        assertEquals(CSG.getDefaultColor(), union.getColor());

        union.getPolygons().forEach(polygon -> {
            boolean isLeftCube = polygon.getPoints().stream().allMatch(p -> p.x <= 5);
            if (isLeftCube) {
                assertEquals("Expected the left cube polygons to be blue", Color.BLUE, polygon.getColor());
            } else {
                assertEquals("Expected the right cube polygons to be red", Color.RED, polygon.getColor());
            }
        });
    }

    @Test
    public void setColor_OnCSGShouldChangeColorsOfAllPolygons() {
        CSG cube = new Cube(10).toCSG()
                .setColor(Color.BLUE);
        assertEquals(Color.BLUE, cube.getColor());

        cube.setColor(Color.RED);

        cube.getPolygons().forEach(polygon -> {
            assertEquals("Expected the cube polygons to be another color", Color.RED, polygon.getColor());
        });
    }

    @Test
    public void setColor_OnUnionedCSGShouldChangeColorsOfAllPolygons() {
        CSG cube1 = new Cube(10).toCSG()
                .setColor(Color.BLUE);

        CSG cube2 = new Cube(10).toCSG()
                .setColor(Color.RED)
                .transformed(new Transform().translate(10, 0, 0));

        CSG union = cube1.union(cube2);
        assertEquals("Expected the new object to inherit the color from the latest unioned object", CSG.getDefaultColor(), union.getColor());

        union.setColor(Color.BLUE);
        union.getPolygons().forEach(polygon -> {
            assertEquals("Expected the cube polygons to be another color", Color.BLUE, polygon.getColor());
        });
    }
}
