package eu.mihosoft.vrl.v3d;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

public class StlExportTest {

	@Test
	public void makeBadSTL() throws IOException {
		CSG badExport2 = CSG.text(" A QUICK BROWN FOX JUMPS OVER THE LAZY DOG", 10).movey(30);
		CSG badExport = CSG.text("THis is some text a quick brown fox jumps over the lazy dog.", 10);
//		badExport2=new Cube(20).toCSG().movey(30);
//		badExport=new Cube(20).toCSG();
		
		badExport=badExport.union(badExport2);
		String filename ="TextStl.stl";
		FileUtil.write(Paths.get(filename),
				badExport.toStlString());
		System.out.println("Load saved stl");
		File file = new File(filename);
		CSG loaded = STL.file(file.toPath());
		badExport=loaded.union(badExport2);
		FileUtil.write(Paths.get(filename),
				badExport.toStlString());
	}

}
