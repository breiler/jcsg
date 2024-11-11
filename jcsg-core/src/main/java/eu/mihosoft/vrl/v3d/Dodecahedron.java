/**
 * Dodecahedron.java
 */
package eu.mihosoft.vrl.v3d;

import java.util.ArrayList;
import java.util.List;

import eu.mihosoft.vrl.v3d.ext.quickhull3d.HullUtil;

public class Dodecahedron extends Primitive {

    /**
     * Center of this dodecahedron.
     */
    private Vector3d center;
    /**
     * Dodecahedron circumscribed radius.
     */
    private double radius;

    /** The centered. */
    private boolean centered = true;

    /** The properties. */
    private final PropertyStorage properties = new PropertyStorage();
    /**
     * Constructor. Creates a new dodecahedron with center {@code [0,0,0]} and
     * dimensions {@code [1,1,1]}.
     */
    public Dodecahedron() {
        center = new Vector3d(0, 0, 0);
        radius = 1;
    }

    /**
     * Constructor. Creates a new dodecahedron with center {@code [0,0,0]} and
     * radius {@code size}.
     * 
     * @param size size
     */
    public Dodecahedron(double size) {
        center = new Vector3d(0, 0, 0);
        radius = size;
    }

    /**
     * Constructor. Creates a new dodecahedron with the specified center and
     * radius.
     *
     * @param center center of the dodecahedron
     * @param size of the dodecahedron
     */
    public Dodecahedron(Vector3d center, double size) {
        this.center = center;
        this.radius = size;
    }
    
    /* (non-Javadoc)
     * @see eu.mihosoft.vrl.v3d.Primitive#toPolygons()
     */
    @Override
    public List<Polygon> toPolygons() {
    	if(radius<=0)
    		throw new NumberFormatException("radius can not be negative");

    	double phi = (Math.sqrt(5)+1)/2;
    	
    	List<Vector3d> points = new ArrayList<>();
	    	points.add(new Vector3d(-1,-1,-1));
	    	points.add(new Vector3d(-1,-1,+1));
	    	points.add(new Vector3d(-1,+1,-1));
	    	points.add(new Vector3d(-1,+1,+1));
	    	points.add(new Vector3d(+1,-1,-1));
	    	points.add(new Vector3d(+1,-1,+1));
	    	points.add(new Vector3d(+1,+1,-1));
	    	points.add(new Vector3d(+1,+1,+1));
    		points.add(new Vector3d(0,1/phi,phi));
			points.add(new Vector3d(0,-1/phi,phi));
			points.add(new Vector3d(phi,0,1/phi));
			points.add(new Vector3d(1/phi,phi,0));
			points.add(new Vector3d(-1/phi,phi,0));
			points.add(new Vector3d(-phi,0,1/phi));
			points.add(new Vector3d(1/phi,-phi,0));
			points.add(new Vector3d(phi,0,-1/phi));
			points.add(new Vector3d(0,1/phi,-phi));
			points.add(new Vector3d(-phi,0,-1/phi));
			points.add(new Vector3d(-1/phi,-phi,0));
			points.add(new Vector3d(0,-1/phi,-phi));
    	
		List<Polygon> polygons = HullUtil.hull(points).scale(radius*(phi-1)).getPolygons();

        return polygons;
    }

    /**
     * Gets the center.
     *
     * @return the center
     */
    public Vector3d getCenter() {
        return center;
    }

    /**
     * Sets the center.
     *
     * @param center the center to set
     */
    public Dodecahedron setCenter(Vector3d center) {
        this.center = center;
        return this;
    }

    /**
     * Gets the radius.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius.
     *
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /* (non-Javadoc)
     * @see eu.mihosoft.vrl.v3d.Primitive#getProperties()
     */
    @Override
    public PropertyStorage getProperties() {
        return properties;
    }

    /**
     * Defines that this dodecahedron will not be centered.
     * @return this dodecahedron
     */
    public Dodecahedron noCenter() {
        centered = false;
        return this;
    }

}
