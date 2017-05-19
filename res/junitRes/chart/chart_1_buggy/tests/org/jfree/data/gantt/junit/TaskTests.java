package org.jfree.data.gantt.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;
/** 
 * Tests for the             {@link Task} class.
 */
public class TaskTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TaskTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TaskTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    Task t1=new Task("T",new Date(1),new Date(2));
    Task t2=new Task("T",new Date(1),new Date(2));
    assertTrue(t1.equals(t2));
    assertTrue(t2.equals(t1));
    t1.setDescription("X");
    assertFalse(t1.equals(t2));
    t2.setDescription("X");
    assertTrue(t1.equals(t2));
    t1.setDuration(new SimpleTimePeriod(new Date(2),new Date(3)));
    assertFalse(t1.equals(t2));
    t2.setDuration(new SimpleTimePeriod(new Date(2),new Date(3)));
    assertTrue(t1.equals(t2));
    t1.setPercentComplete(0.5);
    assertFalse(t1.equals(t2));
    t2.setPercentComplete(0.5);
    assertTrue(t1.equals(t2));
    t1.addSubtask(new Task("T",new Date(22),new Date(33)));
    assertFalse(t1.equals(t2));
    t2.addSubtask(new Task("T",new Date(22),new Date(33)));
    assertTrue(t1.equals(t2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    Task t1=new Task("T",new Date(1),new Date(2));
    Task t2=null;
    try {
      t2=(Task)t1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(t1 != t2);
    assertTrue(t1.getClass() == t2.getClass());
    assertTrue(t1.equals(t2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    Task t1=new Task("T",new Date(1),new Date(2));
    Task t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(Task)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(t1,t2);
  }
  /** 
 * Check the getSubTaskCount() method.
 */
  public void testGetSubTaskCount(){
    Task t1=new Task("T",new Date(100),new Date(200));
    assertEquals(0,t1.getSubtaskCount());
    t1.addSubtask(new Task("S1",new Date(100),new Date(110)));
    assertEquals(1,t1.getSubtaskCount());
    Task s2=new Task("S2",new Date(111),new Date(120));
    t1.addSubtask(s2);
    assertEquals(2,t1.getSubtaskCount());
    t1.addSubtask(new Task("S3",new Date(121),new Date(130)));
    assertEquals(3,t1.getSubtaskCount());
    t1.removeSubtask(s2);
    assertEquals(2,t1.getSubtaskCount());
  }
}
