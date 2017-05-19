package org.jfree.chart.renderer.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.data.DomainOrder;
import org.jfree.data.xy.DefaultXYDataset;
/** 
 * Some checks for the             {@link RendererUtilities} class.
 */
public class RendererUtilitiesTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(RendererUtilitiesTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public RendererUtilitiesTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the findLiveItemsLowerBound() method when the dataset is unordered.
 */
  public void testFindLiveItemsLowerBound_Unordered(){
    DefaultXYDataset d=new DefaultXYDataset();
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,0,10.0,11.0));
    d.addSeries("S2",new double[][]{{0.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,2.0,3.3));
    d.addSeries("S3",new double[][]{{0.0,1.0},{9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,0.0,1.1));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,2.0,3.3));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,3.0,4.4));
    d.addSeries("S4",new double[][]{{1.0,2.0,1.5},{9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,3,2.0,3.3));
    assertEquals(2,RendererUtilities.findLiveItemsLowerBound(d,3,3.0,4.4));
    d.addSeries("S5",new double[][]{{1.0,2.0,1.5,1.8},{9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,4,2.0,3.3));
    assertEquals(3,RendererUtilities.findLiveItemsLowerBound(d,4,3.0,4.4));
    assertEquals(3,RendererUtilities.findLiveItemsLowerBound(d,4,4.0,5.5));
  }
  /** 
 * Some checks for the findLiveItemsLowerBound() method when the dataset is ASCENDING.
 */
  public void testFindLiveItemsLowerBound_Ascending(){
    DefaultXYDataset d=new DefaultXYDataset(){
      public DomainOrder getDomainOrder(){
        return DomainOrder.ASCENDING;
      }
    }
;
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,0,10.0,11.1));
    d.addSeries("S2",new double[][]{{1.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,2.0,2.2));
    d.addSeries("S3",new double[][]{{1.0,2.0},{9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,2.0,3.3));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,3.0,4.4));
    d.addSeries("S4",new double[][]{{1.0,2.0,3.0},{9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,3,2.0,3.3));
    assertEquals(2,RendererUtilities.findLiveItemsLowerBound(d,3,3.0,4.4));
    d.addSeries("S5",new double[][]{{1.0,2.0,3.0,4.0},{9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,4,2.0,3.3));
    assertEquals(2,RendererUtilities.findLiveItemsLowerBound(d,4,3.0,4.4));
    assertEquals(3,RendererUtilities.findLiveItemsLowerBound(d,4,4.0,5.5));
    d.addSeries("S5",new double[][]{{1.0,2.0,2.0,2.0,3.0},{9.9,9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,0.0,4.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,1.0,4.0));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,4,2.0,4.0));
    assertEquals(4,RendererUtilities.findLiveItemsLowerBound(d,4,3.0,4.0));
  }
  /** 
 * Some checks for the findLiveItemsLowerBound() method when the dataset is DESCENDING.
 */
  public void testFindLiveItemsLowerBound_Descending(){
    DefaultXYDataset d=new DefaultXYDataset(){
      public DomainOrder getDomainOrder(){
        return DomainOrder.DESCENDING;
      }
    }
;
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,0,10.0,11.0));
    d.addSeries("S2",new double[][]{{1.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,0.0,1.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,1,1.1,2.0));
    d.addSeries("S3",new double[][]{{2.0,1.0},{9.9,9.9}});
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,0.1,0.5));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,2,0.1,1.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,1.1,2.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,2.2,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,2,3.3,4.0));
    d.addSeries("S4",new double[][]{{3.0,2.0,1.0},{9.9,9.9,9.9}});
    assertEquals(2,RendererUtilities.findLiveItemsLowerBound(d,3,0.0,1.0));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,3,1.0,2.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,2.0,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,3,3.0,4.0));
    d.addSeries("S5",new double[][]{{4.0,3.0,2.0,1.0},{9.9,9.9,9.9,9.9}});
    assertEquals(3,RendererUtilities.findLiveItemsLowerBound(d,4,0.1,0.5));
    assertEquals(3,RendererUtilities.findLiveItemsLowerBound(d,4,0.1,1.0));
    assertEquals(2,RendererUtilities.findLiveItemsLowerBound(d,4,1.1,2.0));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,4,2.2,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,3.3,4.0));
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,4,4.4,5.0));
    d.addSeries("S6",new double[][]{{3.0,2.0,2.0,2.0,1.0},{9.9,9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsLowerBound(d,5,0.0,3.0));
    assertEquals(1,RendererUtilities.findLiveItemsLowerBound(d,5,0.0,2.0));
    assertEquals(4,RendererUtilities.findLiveItemsLowerBound(d,5,0.0,1.0));
    assertEquals(4,RendererUtilities.findLiveItemsLowerBound(d,5,0.0,0.5));
  }
  /** 
 * Some checks for the findLiveItemsUpperBound() method when the dataset is unordered.
 */
  public void testFindLiveItemsUpperBound_Unordered(){
    DefaultXYDataset d=new DefaultXYDataset();
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,0,10.0,11.0));
    d.addSeries("S2",new double[][]{{1.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,2.0,3.3));
    d.addSeries("S3",new double[][]{{1.0,2.0},{9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,0.0,1.1));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,2.0,3.3));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,3.0,4.4));
    d.addSeries("S4",new double[][]{{1.0,2.0,1.5},{9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,3,0.0,1.1));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,3,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,3,2.0,3.3));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,3,3.0,4.4));
    d.addSeries("S5",new double[][]{{1.0,2.0,1.5,1.8},{9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,1.1));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,4,2.0,3.3));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,3.0,4.4));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,4.0,5.5));
  }
  /** 
 * Some checks for the findLiveItemsUpperBound() method when the dataset is ASCENDING.
 */
  public void testFindLiveItemsUpperBound_Ascending(){
    DefaultXYDataset d=new DefaultXYDataset(){
      public DomainOrder getDomainOrder(){
        return DomainOrder.ASCENDING;
      }
    }
;
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,0,10.0,11.1));
    d.addSeries("S2",new double[][]{{1.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,0.0,1.1));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,2.0,2.2));
    d.addSeries("S3",new double[][]{{1.0,2.0},{9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,0.0,1.0));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,1.0,2.2));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,2.0,3.3));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,3.0,4.4));
    d.addSeries("S4",new double[][]{{1.0,2.0,3.0},{9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,3,0.0,1.1));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,3,1.0,2.2));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,3,2.0,3.3));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,3,3.0,4.4));
    d.addSeries("S5",new double[][]{{1.0,2.0,3.0,4.0},{9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,1.1));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,4,1.0,2.2));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,4,2.0,3.3));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,3.0,4.4));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,4.0,5.5));
    d.addSeries("S5",new double[][]{{1.0,2.0,2.0,2.0,3.0},{9.9,9.9,9.9,9.9,9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,1.0));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,2.0));
    assertEquals(4,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,3.0));
    assertEquals(4,RendererUtilities.findLiveItemsUpperBound(d,4,0.0,4.0));
  }
  /** 
 * Some checks for the findLiveItemsUpperBound() method when the dataset is DESCENDING.
 */
  public void testFindLiveItemsUpperBound_Descending(){
    DefaultXYDataset d=new DefaultXYDataset(){
      public DomainOrder getDomainOrder(){
        return DomainOrder.DESCENDING;
      }
    }
;
    d.addSeries("S1",new double[][]{{},{}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,0,10.0,11.0));
    d.addSeries("S2",new double[][]{{1.0},{9.9}});
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,0.0,1.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,1,1.1,2.0));
    d.addSeries("S3",new double[][]{{2.0,1.0},{9.9,9.9}});
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,0.1,0.5));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,2,0.1,1.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,1.1,2.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,2.2,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,2,3.3,4.0));
    d.addSeries("S4",new double[][]{{3.0,2.0,1.0},{9.9,9.9,9.9}});
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,3,0.0,1.0));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,3,1.0,2.0));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,3,2.0,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,3,3.0,4.0));
    d.addSeries("S5",new double[][]{{4.0,3.0,2.0,1.0},{9.9,9.9,9.9,9.9}});
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,0.1,0.5));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,4,0.1,1.0));
    assertEquals(2,RendererUtilities.findLiveItemsUpperBound(d,4,1.1,2.0));
    assertEquals(1,RendererUtilities.findLiveItemsUpperBound(d,4,2.2,3.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,3.3,4.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,4,4.4,5.0));
    d.addSeries("S6",new double[][]{{3.0,2.0,2.0,2.0,1.0},{9.9,9.9,9.9,9.9,9.9}});
    assertEquals(4,RendererUtilities.findLiveItemsUpperBound(d,5,0.0,5.0));
    assertEquals(4,RendererUtilities.findLiveItemsUpperBound(d,5,1.0,5.0));
    assertEquals(3,RendererUtilities.findLiveItemsUpperBound(d,5,2.0,5.0));
    assertEquals(0,RendererUtilities.findLiveItemsUpperBound(d,5,3.0,5.0));
  }
}
