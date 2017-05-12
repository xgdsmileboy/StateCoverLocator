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
    if (true) {
    }
    UnivariateFunction f=new Sin();
    BaseAbstractUnivariateIntegrator integrator=new IterativeLegendreGaussIntegrator(5,1.0e-14,1.0e-10,2,15);
    double min, max, expected, result, tolerance;
    min=0;
    max=FastMath.PI;
    expected=2;
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    result=integrator.integrate(10000,f,min,max);
    Assert.assertEquals(expected,result,tolerance);
    min=-FastMath.PI / 3;
    max=0;
    expected=-0.5;
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    result=integrator.integrate(10000,f,min,max);
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
