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
    auxiliary.Dumper.write("[INST]T#0#17");
    UnivariateFunction f=new Sin();
    auxiliary.Dumper.write("[INST]T#0#18");
    BaseAbstractUnivariateIntegrator integrator=new IterativeLegendreGaussIntegrator(5,1.0e-14,1.0e-10,2,15);
    auxiliary.Dumper.write("[INST]T#0#19");
    double min, max, expected, result, tolerance;
    auxiliary.Dumper.write("[INST]T#0#21");
    min=0;
    auxiliary.Dumper.write("[INST]T#0#22");
    max=FastMath.PI;
    auxiliary.Dumper.write("[INST]T#0#23");
    expected=2;
    auxiliary.Dumper.write("[INST]T#0#24");
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    auxiliary.Dumper.write("[INST]T#0#26");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#0#27");
    Assert.assertEquals(expected,result,tolerance);
    auxiliary.Dumper.write("[INST]T#0#29");
    min=-FastMath.PI / 3;
    auxiliary.Dumper.write("[INST]T#0#30");
    max=0;
    auxiliary.Dumper.write("[INST]T#0#31");
    expected=-0.5;
    auxiliary.Dumper.write("[INST]T#0#32");
    tolerance=FastMath.max(integrator.getAbsoluteAccuracy(),FastMath.abs(expected * integrator.getRelativeAccuracy()));
    auxiliary.Dumper.write("[INST]T#0#34");
    result=integrator.integrate(10000,f,min,max);
    auxiliary.Dumper.write("[INST]T#0#35");
    Assert.assertEquals(expected,result,tolerance);
  }
  @Test public void testNormalDistributionWithLargeSigma(){
    auxiliary.Dumper.write("[INST]T#1#40");
    final double sigma=1000;
    auxiliary.Dumper.write("[INST]T#1#41");
    final double mean=0;
    auxiliary.Dumper.write("[INST]T#1#42");
    final double factor=1 / (sigma * FastMath.sqrt(2 * FastMath.PI));
    auxiliary.Dumper.write("[INST]T#1#43");
    final UnivariateFunction normal=new Gaussian(factor,mean,sigma);
    auxiliary.Dumper.write("[INST]T#1#45");
    final double tol=1e-2;
    auxiliary.Dumper.write("[INST]T#1#46");
    final IterativeLegendreGaussIntegrator integrator=new IterativeLegendreGaussIntegrator(5,tol,tol);
    auxiliary.Dumper.write("[INST]T#1#48");
    final double a=-5000;
    auxiliary.Dumper.write("[INST]T#1#49");
    final double b=5000;
    auxiliary.Dumper.write("[INST]T#1#50");
    final double s=integrator.integrate(50,normal,a,b);
    auxiliary.Dumper.write("[INST]T#1#51");
    Assert.assertEquals(1,s,1e-5);
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
