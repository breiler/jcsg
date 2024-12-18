package eu.mihosoft.vrl.v3d.javafx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.FileUtil;
import eu.mihosoft.vrl.v3d.STL;

public class StlExportTest {

	public void makeBadSTL() throws IOException {
		//Plane.setEPSILON(1.0e-11);
		//Vector3d.setEXPORTEPSILON(10);
		CSG.setUseGPU(false);
		CSG.setPreventNonManifoldTriangles(true);
		CSG badExport2 = CSG.text(" A QUICK BROWN FOX JUMPS OVER THE LAZY DOG", 10,30,"Serif Regular").movey(30);
		System.out.println("First text loaded");
		CSG badExport = CSG.text("THis is some text a quick brown fox jumps over the lazy dog.", 10);
		System.out.println("Second text loaded");
//		badExport2=new Cube(20).toCSG().movey(30);
//		badExport=new Cube(20).toCSG();
		
		badExport=badExport.union(badExport2);
		//String filename ="TextStl.stl";
		FileUtil.write(Paths.get("TextStl.stl"),
				badExport.toStlString());
		System.out.println("Load saved stl");
		File file = new File("TextStl.stl");
		CSG loaded = STL.file(file.toPath());
		System.out.println("Perform difference");
		badExport=loaded.scaleToMeasurmentX(160).scaleToMeasurmentY(30);
		badExport=new Cube(180,40,10).toCSG().toZMin().toXMin().toYMin().movey(-5).difference(badExport).rotx(35).roty(45);
		FileUtil.write(Paths.get("TextStl2.stl"),
				badExport.toStlString());
	}

}
