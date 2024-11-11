package eu.mihosoft.vrl.v3d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class GroupingTest {

	@Test
	public void test() {
		CSG c = new Cube(100).toCSG();
		String groupID = "testing";
		c.addGroupMembership(groupID);
		c.setName("MyName");
		c.addIsGroupResult(groupID);
		CSG copy = c.clone().syncProperties(c).setName(c.getName());
		copy.removeGroupMembership(groupID);
		copy.removeIsGroupResult(groupID);
		
		if(copy.isInGroup())
			fail("Copy should not be in a group");
		if(!c.isInGroup())
			fail("Original should  be in a group");
		
		if(copy.isGroupResult())
			fail("Copy should not be in a group");
		if(!c.isGroupResult())
			fail("Original should  be in a group");
		
	}

}
