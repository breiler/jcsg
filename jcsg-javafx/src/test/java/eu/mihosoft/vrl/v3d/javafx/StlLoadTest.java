package eu.mihosoft.vrl.v3d.javafx;



import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.STL;
import eu.mihosoft.vrl.v3d.javafx.thumbnail.ThumbnailImage;
import javafx.scene.shape.CullFace;
import org.junit.jupiter.api.Test;

public class StlLoadTest {

	@Test
	public void test() throws IOException {
		String filename = "brokenSTL.STL";
		File file = new File(filename);
		CSG loaded = STL.file(file.toPath());
		try {
			ThumbnailImage.setCullFaceValue(CullFace.NONE);
			ThumbnailImage.writeImage(List.of(loaded),new File(file.getAbsolutePath()+".png")).join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
