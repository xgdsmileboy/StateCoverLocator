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
		UnivariateFunction f = new Sin();
		BaseAbstractUnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(5, 1.0e-14, 1.0e-10, 2, 15);
		double min, max, expected, result, tolerance;

		min = 0;
		max = FastMath.PI;
		expected = 2;
		tolerance = FastMath.max(integrator.getAbsoluteAccuracy(),
				FastMath.abs(expected * integrator.getRelativeAccuracy()));
		result = integrator.integrate(10000, f, min, max);
		Assert.assertEquals(expected, result, tolerance);

		min = -FastMath.PI / 3;
		max = 0;
		expected = -0.5;
		tolerance = FastMath.max(integrator.getAbsoluteAccuracy(),
				FastMath.abs(expected * integrator.getRelativeAccuracy()));
		result = integrator.integrate(10000, f, min, max);
		Assert.assertEquals(expected, result, tolerance);
	}

	@Test
	public void testNormalDistributionWithLargeSigma() {
		final double sigma = 1000;
		final double mean = 0;
		final double factor = 1 / (sigma * FastMath.sqrt(2 * FastMath.PI));
		final UnivariateFunction normal = new Gaussian(factor, mean, sigma);

		final double tol = 1e-2;
		final IterativeLegendreGaussIntegrator integrator = new IterativeLegendreGaussIntegrator(5, tol, tol);

		final double a = -5000;
		final double b = 5000;
		final double s = integrator.integrate(50, normal, a, b);
		Assert.assertEquals(1, s, 1e-5);
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
