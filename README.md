JCSG
=======

Java implementation of BSP based CSG (Constructive Solid Geometry). It is the only simple and free Java implementation I am aware of. This implementation uses an optimized CSG algorithm based on [csg.js](https://github.com/evanw/csg.js) (see `CSG` and `Node` classes). Thanks to the author for creating the [csg.js](https://github.com/evanw/csg.js) library.

This package is based on Kevin Harringtons work from https://github.com/NeuronRobotics/JCSG and Michael Hoffer from https://github.com/miho/JCSG with the goal to remove the dependency to JavaFX.

In addition to CSG this library provides the following features:

- optimized `difference()` and `union()` operations (many thanks to Sebastian Reiter)
- extrusion of concave, non-intersecting polygons (uses [Poly2Tri](https://code.google.com/p/poly2tri/) for triangulation)
- convex hull (uses [QuickHull3D](https://www.cs.ubc.ca/~lloyd/java/quickhull3d.html))
- weighted transformations (Scale, Rotation, Translation and Mirror)
- STL import and export (STLLoader from [Fiji](https://github.com/fiji/fiji/blob/master/src-plugins/3D_Viewer/src/main/java/customnode/STLLoader.java))
- OBJ export including material information (see screenshot below)
- 3d text support

![](/resources/screenshot2.png)



## Maven
![](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.breiler/jcsg.svg?style=flat)

```
<dependency>
  <groupId>com.breiler</groupId>
  <artifactId>jcsg-core</artifactId>
  <version>VERSION_FROM_BADGE</version>
</dependency>
```

## Code Sample:


```java

// we use cube and sphere as base geometries
CSG cube = new Cube(2).toCSG();
CSG sphere = new Sphere(1.25).toCSG();

// perform union, difference and intersection
CSG cubePlusSphere = cube.union(sphere);
CSG cubeMinusSphere = cube.difference(sphere);
CSG cubeIntersectSphere = cube.intersect(sphere);
        
// translate geometries to prevent overlapping 
CSG union = cube.
        union(sphere.transformed(Transform.unity().translateX(3))).
        union(cubePlusSphere.transformed(Transform.unity().translateX(6))).
        union(cubeMinusSphere.transformed(Transform.unity().translateX(9))).
        union(cubeIntersectSphere.transformed(Transform.unity().translateX(12)));
        
// save union as stl
try {
    FileUtil.write(
            Paths.get("sample.stl"),
            union.toStlString()
    );
} catch (IOException ex) {
    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
}
```