/*
   Copyright 2008 Simon Mieth

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.kabeja.math;

import org.junit.jupiter.api.Test;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.dxf.helpers.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParametricPlaneTest {
    @Test
    public void testDirectionY() {
        ParametricPlane p = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Vector(0, 0, 1));
        Vector y = p.getDirectionY();
        assertEquals(0.0, y.getX(), 0.001);
        assertEquals(1.0, y.getY(), 0.001);
        assertEquals(0.0, y.getZ(), 0.001);
    }

    @Test
    public void testDirectionX() {
        ParametricPlane p = new ParametricPlane(new Point(0, 0, 0),
                new Point(0, 1, 0), new Vector(0, 0, 1));
        Vector y = p.getDirectionY();
        assertEquals(-1.0, y.getX(), 0.001);
        assertEquals(0.0, y.getY(), 0.001);
        assertEquals(0.0, y.getZ(), 0.001);
    }

    @Test
    public void testPoint1() {
        ParametricPlane plane = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Vector(0, 0, 1));

        Point p = plane.getPoint(2.0, 3.0);

        assertEquals(2.0, p.getX(), 0.001);
        assertEquals(3.0, p.getY(), 0.001);
        assertEquals(0.0, p.getZ(), 0.001);
    }

    @Test
    public void testParameters() {
        ParametricPlane plane = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Vector(0, 0, 1));

        Point p = new Point(2.0, 3.0, 0.0);
        double[] paras = plane.getParameter(p);
        assertEquals(2.0, paras[0], 0.001);
        assertEquals(3.0, paras[1], 0.001);
    }

    @Test
    public void testIsOnPlane() {
        ParametricPlane plane = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Vector(0, 0, 1));

        Point p = new Point(2.0, 3.0, 0.0);
        assertEquals(true, plane.isOnPlane(p));
    }

    @Test
    public void testIsNotPlane() {
        ParametricPlane plane = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Vector(0, 0, 1));

        Point p = new Point(2.0, 3.0, -0.01);
        assertEquals(false, plane.isOnPlane(p));
    }

    @Test
    public void testNormal() {
        ParametricPlane plane = new ParametricPlane(new Point(0, 0, 0),
                new Point(1, 0, 0), new Point(1.0, 0.001, 0));

        Vector n = plane.getNormal();
        assertEquals(0.0, n.getX(), 0.001);
        assertEquals(0.0, n.getY(), 0.001);
        assertEquals(1.0, n.getZ(), 0.001);
    }
}
