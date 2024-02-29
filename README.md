JCSG
=======

[![Join the chat at https://gitter.im/NeuronRobotics/JCSG](https://badges.gitter.im/NeuronRobotics/JCSG.svg)](https://gitter.im/NeuronRobotics/JCSG?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Documentation

[Javadoc Generated Documentation](https://neuronrobotics.github.io/JCSG/annotated.html) 

Spike Examples [Part of BowlerStudio](http://commonwealthrobotics.com/JavaCAD/Overview/)

# Build Status

[![Build Status](https://travis-ci.org/NeuronRobotics/JCSG.png?branch=master)](https://travis-ci.org/NeuronRobotics/JCSG)

# Overview

Java implementation of BSP based CSG (Constructive Solid Geometry). It is the only simple and free Java implementation I am aware of. This implementation uses an optimized CSG algorithm based on [csg.js](https://github.com/evanw/csg.js) (see `CSG` and `Node` classes). Thanks to the author for creating the [csg.js](https://github.com/evanw/csg.js) library.

In addition to CSG this library provides the following features:

- optimized `difference()` and `union()` operations (many thanks to Sebastian Reiter)
- extrusion of concave, non-intersecting polygons (uses [Poly2Tri](https://code.google.com/p/poly2tri/) for triangulation)
- convex hull (uses [QuickHull3D](https://www.cs.ubc.ca/~lloyd/java/quickhull3d.html))
- weighted transformations (Scale, Rotation, Translation and Mirror)
- STL import and export (STLLoader from [Fiji](https://github.com/fiji/fiji/blob/master/src-plugins/3D_Viewer/src/main/java/customnode/STLLoader.java))
- OBJ export including material information (see screenshot below)
- supports conversion of CSG's to `JavaFX 3D` nodes
- 3d text support (using [FXyz](https://github.com/FXyz/FXyz))

**JCSG** on [stackoverflow](http://stackoverflow.com/search?q=jcsg).

![](/resources/screenshot2.png)



## Maven
![](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.neuronrobotics/JavaCad.svg?style=flat)

```
<dependency>
  <groupId>com.neuronrobotics</groupId>
  <artifactId>JavaCad</artifactId>
  <version>VERSION_FROM_BADGE</version>
  <type>zip</type>
</dependency>
```

## Gradle
![](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.neuronrobotics/JavaCad.svg?style=flat)

```
repositories {
	//com.neuronrobotics hosting point
	maven { url 'https://oss.sonatype.org/content/repositories/staging/' }
}
dependencies {
	compile "com.neuronrobotics:JavaCad:VERSION_FROM_BADGE"
}

```

## How to Build JCSG

### Requirements

- Java >= 1.8
- Internet connection (dependencies are downloaded automatically)
- IDE: [Gradle](http://www.gradle.org/) Plugin (not necessary for command line usage)

### IDE

Open the `JCSG` [Gradle](http://www.gradle.org/) project in your favourite IDE (tested with NetBeans 7.4) and build it
by calling the `assemble` task.

### Command Line

Navigate to the [Gradle](http://www.gradle.org/) project (e.g., `path/to/JCSG`) and enter the following command

#### Bash (Linux/OS X/Cygwin/other Unix-like shell)
    
    sudo update-alternatives --config java # select Java 8
    sudo apt-get install libopenjfx-java
    bash gradlew assemble
    
#### Windows (CMD)

    gradlew assemble

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


# CI Publish HOWTO set up

1. Export your gpg private key from the system that you have created it.
    1. Find your key-id (using `gpg --list-secret-keys --keyid-format=long`)
    2. Put the GPG id into a variable `OSSRH_GPG_SECRET_KEY_ID` 
    3. Export the gpg secret key to an ASCII file using `gpg --export-secret-keys -a <key-id> > secret.txt`
    4. Edit `secret.txt` using a plain text editor, and replace all newlines with a literal "\n" until everything is on a single line
2. Set up [GitHub Actions secrets](https://github.com/organizations/CommonWealthRobotics/settings/secrets/actions)
    1. Create a secret called `OSSRH_GPG_SECRET_KEY` using the text from your edited `secret.txt` file (the whole text should be in a single line)
    2. Create a secret called `OSSRH_GPG_SECRET_KEY_PASSWORD` containing the password for your gpg secret key
3. Add Maven Credentials
   1. In ~/gradle.properties, osshUsername `MAVEN_USERNAME`
   2. In ~/gradle.properties, osshPassword `MAVEN_PASSWORD`
	
5. Create a GitHub Actions step to install the gpg secret key
    1. Add an action similar to:
        ```yaml
        - id: install-secret-key
          name: Install gpg secret key
          run: |
            cat <(echo -e "${{ secrets.OSSRH_GPG_SECRET_KEY }}") | gpg --batch --import
            gpg --list-secret-keys --keyid-format LONG
        ```
    2. Verify that the secret key is shown in the GitHub Actions logs
    3. You can remove the output from list secret keys if you are confident that this action will work, but it is better to leave it in there
