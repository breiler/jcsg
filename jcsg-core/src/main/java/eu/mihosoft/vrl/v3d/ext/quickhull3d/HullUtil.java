/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.v3d.ext.quickhull3d;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Vector3d;
import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.PropertyStorage;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class HullUtil.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class HullUtil {

	/**
	 * Instantiates a new hull util.
	 */
	private HullUtil() {
		throw new AssertionError("Don't instantiate me!", null);
	}

	/**
	 * Hull.
	 *
	 * @param points
	 *            the points
	 * @return the csg
	 */
	public static CSG hull(List<?> points) {
		List<Vector3d> plist = new ArrayList<>();
		if (Vector3d.class.isInstance(points.get(0))) {
			points.stream().forEach((pobj) -> plist.add((Vector3d) pobj));
			return hull(plist, new PropertyStorage());
		}
		if (CSG.class.isInstance(points.get(0))) {
			for (Object csg : points)
				((CSG) csg).getPolygons().forEach((p) -> p.vertices.forEach((v) -> plist.add(v.pos)));

			return hull(plist, new PropertyStorage());
		}
		throw new RuntimeException("Objects in list are of unknown type: " + points.get(0).getClass().getName()+"\r\nExpected CSG or Vector3d ");
	}

	/**
	 * Hull.
	 *
	 * @param points
	 *            the points
	 * @param storage
	 *            the storage
	 * @return the csg
	 */
	public static CSG hull(List<Vector3d> points, PropertyStorage storage) {

		Point3d[] hullPoints = points.stream().map((vec) -> new Point3d(vec.x, vec.y, vec.z)).toArray(Point3d[]::new);

		QuickHull3D hull = new QuickHull3D();
		hull.build(hullPoints);
		hull.triangulate();

		int[][] faces = hull.getFaces();

		List<Polygon> polygons = new ArrayList<>();

		List<Vector3d> vertices = new ArrayList<>();

		for (int[] verts : faces) {

			for (int i : verts) {
				vertices.add(points.get(hull.getVertexPointIndices()[i]));
			}

			polygons.add(Polygon.fromPoints(vertices, storage));

			vertices.clear();
		}

		return CSG.fromPolygons(polygons);
	}

	/**
	 * Hull.
	 *
	 * @param csg
	 *            the csg
	 * @param storage
	 *            the storage
	 * @return the csg
	 */
	public static CSG hull(CSG csg, PropertyStorage storage) {

		List<Vector3d> points = new ArrayList<>(csg.getPolygons().size() * 3);

		csg.getPolygons().forEach((p) -> p.vertices.forEach((v) -> points.add(v.pos)));

		return hull(points, storage);
	}

	/**
	 * Hull.
	 *
	 * @param csgList
	 *            a list of csg
	 * @return the csg
	 */
	public static CSG hull(CSG... csgList) {

		List<Vector3d> points = new ArrayList<>();
		for (CSG csg : csgList)
			csg.getPolygons().forEach((p) -> p.vertices.forEach((v) -> points.add(v.pos)));

		return hull(points, new PropertyStorage());
	}
}
