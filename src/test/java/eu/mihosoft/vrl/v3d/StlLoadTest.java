package eu.mihosoft.vrl.v3d;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class StlLoadTest {

	@Test
	public void test() throws IOException {
		String filename = "brokenSTL.STL";
		CSG loaded = STL.file(new File(filename).toPath());
	}

}
