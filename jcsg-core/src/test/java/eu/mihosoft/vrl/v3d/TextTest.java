package eu.mihosoft.vrl.v3d;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TextTest {

	@Test
	public void test() throws IOException {
		TextExtrude.text(10.0, "Hello", new Font("Helvetica",  Font.PLAIN, 18));
		TextExtrude.text(10.0, "Hello World!", new Font("Times New Roman", Font.PLAIN, 18));
		
		Text3d text = new Text3d("Hello world", 10);
		FileUtil.write(Paths.get("exampleText.stl"),
				text.toCSG().toStlString());
	}

}
