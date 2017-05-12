package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class BigFraction extends Number implements FieldElement<BigFraction>, Comparable<BigFraction>, Serializable {
	private final BigInteger numerator;
	private final BigInteger denominator;

	public BigFraction(BigInteger num, BigInteger den) {
		MathUtils.checkNotNull(num, LocalizedFormats.NUMERATOR);
		MathUtils.checkNotNull(den, LocalizedFormats.DENOMINATOR);
		if (BigInteger.ZERO.equals(den)) {
			throw new ZeroException(LocalizedFormats.ZERO_DENOMINATOR);
		}
		if (BigInteger.ZERO.equals(num)) {
			numerator = BigInteger.ZERO;
			denominator = BigInteger.ONE;
		} else {
			final BigInteger gcd = num.gcd(den);
			if (BigInteger.ONE.compareTo(gcd) < 0) {
				num = num.divide(gcd);
				den = den.divide(gcd);
			}
			if (BigInteger.ZERO.compareTo(den) > 0) {
				num = num.negate();
				den = den.negate();
			}
			numerator = num;
			denominator = den;
		}
	}

	class TroubleClazz {
		public void method1() {
		}

		public boolean method2() {
			return true;
		}
	}

	public BigFraction(final double value) throws MathIllegalArgumentException {
		if (Double.isNaN(value)) {
			throw new MathIllegalArgumentException(LocalizedFormats.NAN_VALUE_CONVERSION);
		}
		if (Double.isInfinite(value)) {
			throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_VALUE_CONVERSION);
		}
		final long bits = Double.doubleToLongBits(value);
		final long sign = bits & 0x8000000000000000L;
		final long exponent = bits & 0x7ff0000000000000L;
		long m = bits & 0x000fffffffffffffL;
		if (exponent != 0) {
			m |= 0x0010000000000000L;
		}
		if (sign != 0) {
			m = -m;
		}
		int k = ((int) (exponent >> 52)) - 1075;
		while (((m & 0x001ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
			m = m >> 1;
			++k;
		}
		if (k < 0) {
			numerator = BigInteger.valueOf(m);
			denominator = BigInteger.ZERO.flipBit(-k);
		} else {
			numerator = BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
			denominator = BigInteger.ONE;
		}
	}

	public BigFraction(final double value, final double epsilon, final int maxIterations)
			throws FractionConversionException {
		this(value, epsilon, Integer.MAX_VALUE, maxIterations);
	}

	public BigFraction abs() {
		return (BigInteger.ZERO.compareTo(numerator) <= 0) ? this : negate();
	}

	public BigFraction divide(final BigInteger bg) {
		if (bg == null) {
			throw new NullArgumentException(LocalizedFormats.FRACTION);
		}
		if (BigInteger.ZERO.equals(bg)) {
			throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR);
		}
		return new BigFraction(numerator, denominator.multiply(bg));
	}

	public BigFraction divide(final int i) {
		return divide(BigInteger.valueOf(i));
	}
}

class AnotherTroubleClazz {
	public void method1() {
	}

	public boolean method2() {
		return true;
	}
}
