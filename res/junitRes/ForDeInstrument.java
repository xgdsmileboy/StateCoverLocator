package org.apache.commons.math3.analysis.integration;
import java.util.Random;
import org.apache.commons.math3.analysis.QuinticFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Sin;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;
public class IterativeLegendreGaussIntegratorTest {
  @Test public void testSinFunction(){
    auxiliary.Dumper.write("[INST]T#0#36");
    UnivariateFunction f=new Sin();
    auxiliary.Dumper.write("[INST]T#0#37");
    BaseAbstractUnivariateIntegrator integrator=new IterativeLegendreGaussIntegrator(5,1.0e-14,1.0e-10,2,15);
    auxiliary.Dumper.write("[INST]T#0#39");
    double min, max, expected, result, tolerance;
    auxiliary.Dumper.write("[INST]T#0#41");
    min=0;
    auxiliary.Dumper.write("[INST]T#0#41");
    max=FastMath.PI;
    auxiliary.Dumper.write("[INST]T#0#41");
    expected=2;
    auxiliary.Dumper.write("[INST]T#0#42");
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    auxiliary.Dumper.write("[INST]T#0#44");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#0#45");
    Assert.assertEquals(expected,result,tolerance);
    auxiliary.Dumper.write("[INST]T#0#47");
    min=-FastMath.PI / 3;
    auxiliary.Dumper.write("[INST]T#0#47");
    max=0;
    auxiliary.Dumper.write("[INST]T#0#47");
    expected=-0.5;
    auxiliary.Dumper.write("[INST]T#0#48");
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    auxiliary.Dumper.write("[INST]T#0#50");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#0#51");
    Assert.assertEquals(expected,result,tolerance);
  }
  @Test public void testQuinticFunction(){
    auxiliary.Dumper.write("[INST]T#1#56");
    UnivariateFunction f=new QuinticFunction();
    auxiliary.Dumper.write("[INST]T#1#57");
    UnivariateIntegrator integrator=new IterativeLegendreGaussIntegrator(3,BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,64);
    auxiliary.Dumper.write("[INST]T#1#63");
    double min, max, expected, result;
    auxiliary.Dumper.write("[INST]T#1#65");
    min=0;
    auxiliary.Dumper.write("[INST]T#1#65");
    max=1;
    auxiliary.Dumper.write("[INST]T#1#65");
    expected=-1.0 / 48;
    auxiliary.Dumper.write("[INST]T#1#66");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#1#67");
    Assert.assertEquals(expected,result,1.0e-16);
    auxiliary.Dumper.write("[INST]T#1#69");
    min=0;
    auxiliary.Dumper.write("[INST]T#1#69");
    max=0.5;
    auxiliary.Dumper.write("[INST]T#1#69");
    expected=11.0 / 768;
    auxiliary.Dumper.write("[INST]T#1#70");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#1#71");
    Assert.assertEquals(expected,result,1.0e-16);
    auxiliary.Dumper.write("[INST]T#1#73");
    min=-1;
    auxiliary.Dumper.write("[INST]T#1#73");
    max=4;
    auxiliary.Dumper.write("[INST]T#1#73");
    expected=2048 / 3.0 - 78 + 1.0 / 48;
    auxiliary.Dumper.write("[INST]T#1#74");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#1#75");
    Assert.assertEquals(expected,result,1.0e-16);
  }
  @Test public void testExactIntegration(){
    auxiliary.Dumper.write("[INST]T#2#80");
    Random random=new Random(86343623467878363l);
    for (int n=2; n < 6; ++n) {
      auxiliary.Dumper.write("[INST]T#2#81");
      auxiliary.Dumper.write("[INST]T#2#82");
      IterativeLegendreGaussIntegrator integrator=new IterativeLegendreGaussIntegrator(n,BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,64);
      for (int degree=0; degree <= 2 * n - 1; ++degree) {
        auxiliary.Dumper.write("[INST]T#2#90");
        for (int i=0; i < 10; ++i) {
          auxiliary.Dumper.write("[INST]T#2#91");
          auxiliary.Dumper.write("[INST]T#2#92");
          double[] coeff=new double[degree + 1];
          for (int k=0; k < coeff.length; ++k) {
            auxiliary.Dumper.write("[INST]T#2#93");
            auxiliary.Dumper.write("[INST]T#2#94");
            coeff[k]=2 * random.nextDouble() - 1;
          }
          auxiliary.Dumper.write("[INST]T#2#96");
          PolynomialFunction p=new PolynomialFunction(coeff);
          auxiliary.Dumper.write("[INST]T#2#97");
          double result=integrator.integrate(10000,p,-5.0,15.0);
          auxiliary.Dumper.write("[INST]T#2#98");
          double reference=exactIntegration(p,-5.0,15.0);
          auxiliary.Dumper.write("[INST]T#2#99");
          Assert.assertEquals(n + " " + degree+ " "+ i,reference,result,1.0e-12 * (1.0 + FastMath.abs(reference)));
        }
      }
    }
  }
  @Test public void testNormalDistributionWithLargeSigma(){
    auxiliary.Dumper.write("[INST]T#3#109");
    final double sigma=1000;
    auxiliary.Dumper.write("[INST]T#3#110");
    final double mean=0;
    auxiliary.Dumper.write("[INST]T#3#111");
    final double factor=1 / (sigma * FastMath.sqrt(2 * FastMath.PI));
    auxiliary.Dumper.write("[INST]T#3#112");
    final UnivariateFunction normal=new Gaussian(factor,mean,sigma);
    auxiliary.Dumper.write("[INST]T#3#114");
    final double tol=1e-2;
    auxiliary.Dumper.write("[INST]T#3#115");
    final IterativeLegendreGaussIntegrator integrator=new IterativeLegendreGaussIntegrator(5,tol,tol);
    auxiliary.Dumper.write("[INST]T#3#118");
    final double a=-5000;
    auxiliary.Dumper.write("[INST]T#3#119");
    final double b=5000;
    auxiliary.Dumper.write("[INST]T#3#120");
    final double s=integrator.integrate(50,normal,a,b);
    auxiliary.Dumper.write("[INST]T#3#121");
    Assert.assertEquals(1,s,1e-5);
  }
  @Test public void testIssue464(){
    auxiliary.Dumper.write("[INST]T#4#126");
    final double value=0.2;
    auxiliary.Dumper.write("[INST]T#4#127");
    UnivariateFunction f=new UnivariateFunction(){
      public double value(      double x){
        return (x >= 0 && x <= 5) ? value : 0.0;
      }
    }
;
    auxiliary.Dumper.write("[INST]T#4#132");
    IterativeLegendreGaussIntegrator gauss=new IterativeLegendreGaussIntegrator(5,3,100);
    auxiliary.Dumper.write("[INST]T#4#136");
    double maxX=0.32462367623786328;
    auxiliary.Dumper.write("[INST]T#4#137");
    Assert.assertEquals(maxX * value,gauss.integrate(Integer.MAX_VALUE,f,-10,maxX),1.0e-7);
    auxiliary.Dumper.write("[INST]T#4#138");
    Assert.assertTrue(gauss.getEvaluations() > 37000000);
    auxiliary.Dumper.write("[INST]T#4#139");
    Assert.assertTrue(gauss.getIterations() < 30);
    try {
      auxiliary.Dumper.write("[INST]T#4#143");
      gauss.integrate(1000,f,-10,maxX);
      auxiliary.Dumper.write("[INST]T#4#144");
      Assert.fail("expected TooManyEvaluationsException");
    }
 catch (    TooManyEvaluationsException tmee) {
      auxiliary.Dumper.write("[INST]T#4#147");
      Assert.assertEquals(1000,tmee.getMax());
    }
    auxiliary.Dumper.write("[INST]T#4#151");
    double sum1=gauss.integrate(1000,f,-10,0);
    auxiliary.Dumper.write("[INST]T#4#152");
    int eval1=gauss.getEvaluations();
    auxiliary.Dumper.write("[INST]T#4#153");
    double sum2=gauss.integrate(1000,f,0,maxX);
    auxiliary.Dumper.write("[INST]T#4#154");
    int eval2=gauss.getEvaluations();
    auxiliary.Dumper.write("[INST]T#4#155");
    Assert.assertEquals(maxX * value,sum1 + sum2,1.0e-7);
    auxiliary.Dumper.write("[INST]T#4#156");
    Assert.assertTrue(eval1 + eval2 < 200);
  }
  private double exactIntegration(  PolynomialFunction p,  double a,  double b){
    final double[] coeffs=p.getCoefficients();
    double yb=coeffs[coeffs.length - 1] / coeffs.length;
    double ya=yb;
    for (int i=coeffs.length - 2; i >= 0; --i) {
      yb=yb * b + coeffs[i] / (i + 1);
      ya=ya * a + coeffs[i] / (i + 1);
    }
    return yb * b - ya * a;
  }
}

