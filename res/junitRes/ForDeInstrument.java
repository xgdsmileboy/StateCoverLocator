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
	@Test
	public void testSinFunction() {
		if (true) {
			auxiliary.Dumper.write("[INST]T#0#36");
		}
		UnivariateFunction f = new Sin();
		auxiliary.Dumper.write("[INST]T#0#37");
		BaseAbstractUnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(5, 1.0e-14, 1.0e-10, 2, 15);
		auxiliary.Dumper.write("[INST]T#0#39");
		double min, max, expected, result, tolerance;
		auxiliary.Dumper.write("[INST]T#0#41");
		min = 0;
		auxiliary.Dumper.write("[INST]T#0#41");
		max = FastMath.PI;
		auxiliary.Dumper.write("[INST]T#0#41");
		expected = 2;
		auxiliary.Dumper.write("[INST]T#0#42");
		tolerance = FastMath.max(integrator.getAbsoluteAccuracy(),
				FastMath.abs(expected * integrator.getRelativeAccuracy()));
		auxiliary.Dumper.write("[INST]T#0#44");
		result = integrator.integrate(10000, f, min, max);
		auxiliary.Dumper.write("[INST]T#0#45");
		Assert.assertEquals(expected, result, tolerance);
		auxiliary.Dumper.write("[INST]T#0#47");
		min = -FastMath.PI / 3;
		auxiliary.Dumper.write("[INST]T#0#47");
		max = 0;
		auxiliary.Dumper.write("[INST]T#0#47");
		expected = -0.5;
		auxiliary.Dumper.write("[INST]T#0#48");
		tolerance = FastMath.max(integrator.getAbsoluteAccuracy(),
				FastMath.abs(expected * integrator.getRelativeAccuracy()));
		auxiliary.Dumper.write("[INST]T#0#50");
		result = integrator.integrate(10000, f, min, max);
		auxiliary.Dumper.write("[INST]T#0#51");
		Assert.assertEquals(expected, result, tolerance);
	}

	@Test
	public void testQuinticFunction() {
		auxiliary.Dumper.write("[INST]T#1#56");
		UnivariateFunction f = new QuinticFunction();
		auxiliary.Dumper.write("[INST]T#1#57");
		UnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(3,
				BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
				BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
				BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT, 64);
		auxiliary.Dumper.write("[INST]T#1#63");
		double min, max, expected, result;
		auxiliary.Dumper.write("[INST]T#1#65");
		min = 0;
		auxiliary.Dumper.write("[INST]T#1#65");
		max = 1;
		auxiliary.Dumper.write("[INST]T#1#65");
		expected = -1.0 / 48;
		auxiliary.Dumper.write("[INST]T#1#66");
		result = integrator.integrate(10000, f, min, max);
		auxiliary.Dumper.write("[INST]T#1#67");
		Assert.assertEquals(expected, result, 1.0e-16);
		auxiliary.Dumper.write("[INST]T#1#69");
		min = 0;
		auxiliary.Dumper.write("[INST]T#1#69");
		max = 0.5;
		auxiliary.Dumper.write("[INST]T#1#69");
		expected = 11.0 / 768;
		auxiliary.Dumper.write("[INST]T#1#70");
		result = integrator.integrate(10000, f, min, max);
		auxiliary.Dumper.write("[INST]T#1#71");
		Assert.assertEquals(expected, result, 1.0e-16);
		auxiliary.Dumper.write("[INST]T#1#73");
		min = -1;
		auxiliary.Dumper.write("[INST]T#1#73");
		max = 4;
		auxiliary.Dumper.write("[INST]T#1#73");
		expected = 2048 / 3.0 - 78 + 1.0 / 48;
		auxiliary.Dumper.write("[INST]T#1#74");
		result = integrator.integrate(10000, f, min, max);
		auxiliary.Dumper.write("[INST]T#1#75");
		Assert.assertEquals(expected, result, 1.0e-16);
	}

	private double exactIntegration(PolynomialFunction p, double a, double b) {
		final double[] coeffs = p.getCoefficients();
		double yb = coeffs[coeffs.length - 1] / coeffs.length;
		double ya = yb;
		for (int i = coeffs.length - 2; i >= 0; --i) {
			yb = yb * b + coeffs[i] / (i + 1);
			ya = ya * a + coeffs[i] / (i + 1);
		}
		return yb * b - ya * a;
	}
}
