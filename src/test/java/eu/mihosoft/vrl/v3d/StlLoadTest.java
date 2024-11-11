package eu.mihosoft.vrl.v3d;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import eu.mihosoft.vrl.v3d.thumbnail.ThumbnailImage;
import javafx.scene.shape.CullFace;

public class StlLoadTest {

	@Test
	public void test() throws IOException {
		String filename = "brokenSTL.STL";
		File file = new File(filename);
		CSG loaded = STL.file(file.toPath());
		try {
			ThumbnailImage.setCullFaceValue(CullFace.NONE);
			ThumbnailImage.writeImage(loaded,new File(file.getAbsolutePath()+".png")).join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
