package eu.mihosoft.vrl.v3d;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class GsonTest {
	@Test
	public void runGson() {
		Type TT_CaDoodleFile = new TypeToken<Bounds>() {
		}.getType();
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
				.create();
		Bounds b = new Cube(100).toCSG().getBounds();
		String json = gson.toJson(b);
		//System.out.println(json);
	}
}
