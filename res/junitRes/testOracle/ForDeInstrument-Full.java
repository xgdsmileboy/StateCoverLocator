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
    UnivariateFunction f=new QuinticFunction();
    UnivariateIntegrator integrator=new IterativeLegendreGaussIntegrator(3,BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,64);
    double min, max, expected, result;
    min=0;
    max=1;
    expected=-1.0 / 48;
    result=integrator.integrate(10000,f,min,max);
    Assert.assertEquals(expected,result,1.0e-16);
    min=0;
    max=0.5;
    expected=11.0 / 768;
    result=integrator.integrate(10000,f,min,max);
    Assert.assertEquals(expected,result,1.0e-16);
    min=-1;
    max=4;
    expected=2048 / 3.0 - 78 + 1.0 / 48;
    result=integrator.integrate(10000,f,min,max);
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
