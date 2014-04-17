import java.util.ArrayList;
import junit.framework.TestCase;

/**
 * @version Autumn 2013
 */
public class BuilderTest extends TestCase {
    
	String s;
	Builder builder;
	
    /**
     * Test Builder
     */
	public void testBuilder() 
    {
	   // Single simple rule, no dependencies
    	this.s = "Class1.java::Edit Class1.java";
        boolean flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Two simple rules, no dependencies
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class2.java::Edit Class2.java\n";
        flag = false;
        try 
        {
	    	new Builder(this.s);
	        flag = true;
	    } 
        catch (ParseException e) 
	    {
	        // do nothing
	    } 
	    catch (UnknownTargetException e)
	    {
	        // do nothing
	    } 
	    catch (CycleDetectedException e)
	    {
	        // do nothing
	    }
	    assertTrue(flag);
	    
	    // Two rules, one dependency
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n";
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing  
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
	    // Flip rules
        this.s =  "Class1.class:Class1.java:javac Class1.java\n" +
        	"Class1.java::Edit Class1.java\n";
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing  
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Lab writeup example
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class1.class Class2.class:jar cvf *.class\n";
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing
        } 
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Another complex example
        this.s = "Class1.java:Class2.java:Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class1.class Class2.class:jar cvf *.class\n";
        
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Another complex example
        this.s = "Class1.java:Class2.java:Edit Class1.java\n" +
        "Class1.class:Class1.java Class2.class:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class2.class Class1.class:jar cvf *.class\n";
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Leading whitespace
        this.s = "  Class1.java  ::Edit Class1.java\n" +
        "Class2.java::  Edit Class2.java\n";
        flag = false;
        try 
        {
	    	new Builder(this.s);
	        flag = true;
	    } 
        catch (ParseException e) 
	    {
	        // do nothing
	    } 
	    catch (UnknownTargetException e)
	    {
	        // do nothing
	    } 
	    catch (CycleDetectedException e)
	    {
	        // do nothing
	    }
	    assertTrue(flag);
	    
	    // Leading whitespace
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class1.class:Class1.java  :javac Class1.java\n";
        flag = false;
        try 
        {
            new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing  
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
    }
    
    public void testBuilderParseException()
    {
    	// null input
        boolean flag = false;
        try 
        {
            new Builder(null);
        } 
        catch (ParseException e) 
        {
        	flag = true;
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        // zero colons
    	this.s = "Class1.java";
    	flag = false;
    	try 
    	{
            new Builder(this.s);    
        } 
        catch (ParseException e) 
        {
            flag = true;
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
    	// two colons
    	this.s = "Class1.java::";
    	flag = false;
    	try 
    	{
            new Builder(this.s);    
        } 
        catch (ParseException e) 
        {
            flag = true;
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        // three colons
    	this.s = "Class1.java:Edit Class1.java:Edit Class1.java:Edit Class1.java";
    	flag = false;
    	try 
    	{
            new Builder(this.s);    
        } 
        catch (ParseException e) 
        {
            flag = true;
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        // three colons, multiple lines
    	this.s = "Class1.java::Edit Class1.java" +
    			"Class2.java::Edit Class2.java:Edit Class2.java";
    	flag = false;
    	try 
    	{
            new Builder(this.s);    
        } 
        catch (ParseException e) 
        {
            flag = true;
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        // Duplicate targets
        this.s = "Class1.java::Edit Class1.java\n" +
        	"Class1.java::Edit Class1.java";
        flag = false;
        try 
        {
        	new Builder(this.s);
        } 
        catch (ParseException e)
        {
        	flag = true;
        }
        catch (UnknownTargetException e)
        {
        	// do nothing
        }
        catch (CycleDetectedException e)
        {
        	// do nothing
        }
        assertTrue(flag);
        
        // Duplicate targets
        this.s = "Class1.java::Edit Class1.java\n" +
        		"Class2.java::Edit Class2.java\n" +
        		"Class1.java::Edit Class1.java";
        flag = false;
        try 
        {
        	new Builder(this.s);
        } 
        catch (ParseException e)
        {
        	flag = true;
        }
        catch (UnknownTargetException e)
        {
        	// do nothing
        }
        catch (CycleDetectedException e)
        {
        	// do nothing
        }
        assertTrue(flag);
    }
    
    public void testUnknownTargetException()
    {
    	// single line
    	this.s = "Class1.java:Class2.java:Edit Class1.java";
        boolean flag = false;
        try 
        {
            new Builder(this.s);
        } 
        catch (ParseException e) 
        {
        	// do nothing
        } 
        catch (UnknownTargetException e) 
        {
            flag = true;
        }
        catch (CycleDetectedException e) 
        {
        	// do nothing
        }
        assertTrue(flag);
        
        // multiple lines
    	this.s = "Class3.java:Class4.java:Edit Class3.java\n"+ 
    			"Class1.java:Class2.java:Edit Class1.java\n" +
    			"Class2.java::Edit Class2.java";
    			
        flag = false;
        try 
        {
            new Builder(this.s);
        } 
        catch (ParseException e) 
        {
        	// do nothing
        } 
        catch (UnknownTargetException e) 
        {
            flag = true;
        }
        catch (CycleDetectedException e) 
        {
        	// do nothing
        }
        assertTrue(flag);
    }

    public void testMakeTarget() 
    {
    	// null input
    	this.s = "Class1.java::Edit Class1.java";
        boolean flag = false;
        try 
        {
            this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        ArrayList<String> list = this.builder.makeTarget(null);
        assertNotNull(list);
        assertEquals(0, list.size());
        
        // simple single rule
    	this.s = "Class1.java::Edit Class1.java";
        flag = false;
        try 
        {
            this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        list = this.builder.makeTarget("Class1.java");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        
        // Two simple rules, no dependencies
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class2.java::Edit Class2.java";
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
	        flag = true;
	    } 
        catch (ParseException e) 
	    {
	        // do nothing
	    } 
	    catch (UnknownTargetException e)
	    {
	        // do nothing
	    } 
	    catch (CycleDetectedException e)
	    {
	        // do nothing
	    }
	    assertTrue(flag);
        list = this.builder.makeTarget("Class1.java");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        list = this.builder.makeTarget("Class2.java");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Edit Class2.java", list.get(0));
        list = this.builder.makeTarget(null);
        assertNotNull(list);
        assertEquals(0, list.size());
        list = this.builder.makeTarget("Class3.java");
        assertNotNull(list);
        assertEquals(0, list.size());
	    
	    // Two rules, one dependency
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n";
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing  
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        list = this.builder.makeTarget("Class1.java");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        list = this.builder.makeTarget("Class1.class");
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        assertEquals("javac Class1.java", list.get(1)); 
        
	    // Flip rules
        this.s =  "Class1.class:Class1.java:javac Class1.java\n" +
        	"Class1.java::Edit Class1.java\n";
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing  
        }
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        list = this.builder.makeTarget("Class1.java");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        list = this.builder.makeTarget("Class1.class");
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Edit Class1.java", list.get(0));
        assertEquals("javac Class1.java", list.get(1)); 
        
        // Lab write-up example
        this.s = "Class1.java::Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class1.class Class2.class:jar cvf *.class\n";
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        }
        catch (UnknownTargetException e)
        {
            // do nothing
        } 
        catch (CycleDetectedException e)
        {
            // do nothing
        }
        assertTrue(flag);
        
        list = this.builder.makeTarget("MyApp.jar");
        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains("Edit Class1.java"));
        assertTrue(list.contains("Edit Class2.java"));
        assertTrue(list.contains("javac Class1.java"));
        assertTrue(list.contains("javac Class2.java"));
        assertTrue(list.contains("jar cvf *.class"));
      
        assertEquals("jar cvf *.class", list.get(4));
        assertTrue(list.indexOf("Edit Class1.java") < list.indexOf("javac Class1.java"));
        assertTrue(list.indexOf("Edit Class2.java") < list.indexOf("javac Class2.java"));
        
        // Another complex example
        this.s = "Class1.java:Class2.java:Edit Class1.java\n" +
        "Class1.class:Class1.java:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class1.class Class2.class:jar cvf *.class\n";
        
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e) 
        {
            // do nothing
        }
        catch (UnknownTargetException e) 
        {
            // do nothing
        } 
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        list = this.builder.makeTarget("MyApp.jar");
        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains("Edit Class1.java"));
        assertTrue(list.contains("Edit Class2.java"));
        assertTrue(list.contains("javac Class1.java"));
        assertTrue(list.contains("javac Class2.java"));
        assertTrue(list.contains("jar cvf *.class"));
     
        assertEquals("jar cvf *.class", list.get(4));
        assertEquals("Edit Class2.java", list.get(0));
        assertTrue(list.indexOf("Edit Class1.java") < list.indexOf("javac Class1.java"));
        assertTrue(list.indexOf("Edit Class2.java") < list.indexOf("javac Class2.java"));
        assertTrue(list.indexOf("Edit Class2.java") < list.indexOf("Edit Class1.java"));
        
        // Another complex example
        this.s = "Class1.java:Class2.java:Edit Class1.java\n" +
        "Class1.class:Class1.java Class2.class:javac Class1.java\n" +
        "Class2.java::Edit Class2.java\n" +
        "Class2.class:Class2.java:javac Class2.java\n" +
        "MyApp.jar:Class2.class Class1.class:jar cvf *.class\n";
        flag = false;
        try 
        {
        	this.builder = new Builder(this.s);
            flag = true;
        } 
        catch (ParseException e)
        {
            // do nothing
        } 
        catch (UnknownTargetException e) 
        {
            // do nothing
        }
        catch (CycleDetectedException e) 
        {
            // do nothing
        }
        assertTrue(flag);
        
        list = this.builder.makeTarget("MyApp.jar");
        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains("Edit Class1.java"));
        assertTrue(list.contains("Edit Class2.java"));
        assertTrue(list.contains("javac Class1.java"));
        assertTrue(list.contains("javac Class2.java"));
        assertTrue(list.contains("jar cvf *.class"));
        
        assertEquals("jar cvf *.class", list.get(4));
        assertEquals("Edit Class2.java", list.get(0));
        assertEquals("javac Class1.java", list.get(3));
        assertTrue(list.contains("Edit Class1.java"));
        assertTrue(list.contains("javac Class2.java"));
    }
   
    public void testCycle() 
    {
        
        this.s = "Class1.java:Class2.java:Edit Class1.java\n" +
                "Class2.java:Class1.java:Edit Class2.java\n";
        boolean flag = false;
        try 
        {
            new Builder(this.s);
        } 
        catch (ParseException e) 
        {
        	// do nothing
        } 
        catch (UnknownTargetException e)
        {
        	// do nothing
        } 
        catch (CycleDetectedException e) 
        {
            flag = true;
        }
        assertTrue(flag); 
    }
	
	
}
