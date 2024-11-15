package eu.mihosoft.vrl.v3d.ext.quickhull3d;

// TODO: Auto-generated Javadoc
/**
 * Simple example usage of QuickHull3D. Run as the command
 *  
 *   java quickhull3d.SimpleExample
 *  
 */
class SimpleExample
{
	
	/**
	 * Run for a simple demonstration of QuickHull3D.
	 *
	 * @param args the arguments
	 */
	public static void main (String[] args)
	 {
           // x y z coordinates of 6 points
	   Point3d[] points = new Point3d[] 
	      { new Point3d (0.0,  0.0,  0.0),
		new Point3d (1.0,  0.5,  0.0),
		new Point3d (2.0,  0.0,  0.0),
		new Point3d (0.5,  0.5,  0.5),
		new Point3d (0.0,  0.0,  2.0),
		new Point3d (0.1,  0.2,  0.3),
		new Point3d (0.0,  2.0,  0.0),
	      };

	   QuickHull3D hull = new QuickHull3D();
	   hull.build (points);

	   //com.neuronrobotics.sdk.common.Log.error ("Vertices:");
	   Point3d[] vertices = hull.getVertices();
	   for (int i=0; i<vertices.length; i++)
	    { Point3d pnt = vertices[i];
	      //com.neuronrobotics.sdk.common.Log.error (pnt.x + " " + pnt.y + " " + pnt.z);
	    }

	   //com.neuronrobotics.sdk.common.Log.error ("Faces:");
	   int[][] faceIndices = hull.getFaces();
	   for (int i=0; i<vertices.length; i++)
	    { for (int k=0; k<faceIndices[i].length; k++)
	       { System.out.print (faceIndices[i][k] + " ");
	       }
	      //com.neuronrobotics.sdk.common.Log.error ("");
	    }
	 }
}
