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
  public BigFraction(  BigInteger num,  BigInteger den){
    auxiliary.Dumper.write("[INST]M#0#21");
    MathUtils.checkNotNull(num,LocalizedFormats.NUMERATOR);
    auxiliary.Dumper.write("[INST]M#0#22");
    MathUtils.checkNotNull(den,LocalizedFormats.DENOMINATOR);
    auxiliary.Dumper.write("[INST]M#0#23");
    if (BigInteger.ZERO.equals(den)) {
      auxiliary.Dumper.write("[INST]M#0#24");
      throw new ZeroException(LocalizedFormats.ZERO_DENOMINATOR);
    }
    auxiliary.Dumper.write("[INST]M#0#26");
    if (BigInteger.ZERO.equals(num)) {
      auxiliary.Dumper.write("[INST]M#0#27");
      numerator=BigInteger.ZERO;
      auxiliary.Dumper.write("[INST]M#0#28");
      denominator=BigInteger.ONE;
    }
 else {
      auxiliary.Dumper.write("[INST]M#0#30");
      final BigInteger gcd=num.gcd(den);
      auxiliary.Dumper.write("[INST]M#0#31");
      if (BigInteger.ONE.compareTo(gcd) < 0) {
        auxiliary.Dumper.write("[INST]M#0#32");
        num=num.divide(gcd);
        auxiliary.Dumper.write("[INST]M#0#33");
        den=den.divide(gcd);
      }
      auxiliary.Dumper.write("[INST]M#0#35");
      if (BigInteger.ZERO.compareTo(den) > 0) {
        auxiliary.Dumper.write("[INST]M#0#36");
        num=num.negate();
        auxiliary.Dumper.write("[INST]M#0#37");
        den=den.negate();
      }
      auxiliary.Dumper.write("[INST]M#0#39");
      numerator=num;
      auxiliary.Dumper.write("[INST]M#0#40");
      denominator=den;
    }
  }
class TroubleClazz {
    public void method1(){
    }
    public boolean method2(){
      auxiliary.Dumper.write("[INST]M#2#49");
      return true;
    }
  }
  public BigFraction(  final double value) throws MathIllegalArgumentException {
    auxiliary.Dumper.write("[INST]M#3#54");
    if (Double.isNaN(value)) {
      auxiliary.Dumper.write("[INST]M#3#55");
      throw new MathIllegalArgumentException(LocalizedFormats.NAN_VALUE_CONVERSION);
    }
    auxiliary.Dumper.write("[INST]M#3#57");
    if (Double.isInfinite(value)) {
      auxiliary.Dumper.write("[INST]M#3#58");
      throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_VALUE_CONVERSION);
    }
    auxiliary.Dumper.write("[INST]M#3#60");
    final long bits=Double.doubleToLongBits(value);
    auxiliary.Dumper.write("[INST]M#3#61");
    final long sign=bits & 0x8000000000000000L;
    auxiliary.Dumper.write("[INST]M#3#62");
    final long exponent=bits & 0x7ff0000000000000L;
    auxiliary.Dumper.write("[INST]M#3#63");
    long m=bits & 0x000fffffffffffffL;
    auxiliary.Dumper.write("[INST]M#3#64");
    if (exponent != 0) {
      auxiliary.Dumper.write("[INST]M#3#65");
      m|=0x0010000000000000L;
    }
    auxiliary.Dumper.write("[INST]M#3#67");
    if (sign != 0) {
      auxiliary.Dumper.write("[INST]M#3#68");
      m=-m;
    }
    auxiliary.Dumper.write("[INST]M#3#70");
    int k=((int)(exponent >> 52)) - 1075;
    while (((m & 0x001ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
      auxiliary.Dumper.write("[INST]M#3#71");
      auxiliary.Dumper.write("[INST]M#3#72");
      m=m >> 1;
      auxiliary.Dumper.write("[INST]M#3#73");
      ++k;
    }
    auxiliary.Dumper.write("[INST]M#3#75");
    if (k < 0) {
      auxiliary.Dumper.write("[INST]M#3#76");
      numerator=BigInteger.valueOf(m);
      auxiliary.Dumper.write("[INST]M#3#77");
      denominator=BigInteger.ZERO.flipBit(-k);
    }
 else {
      auxiliary.Dumper.write("[INST]M#3#79");
      numerator=BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
      auxiliary.Dumper.write("[INST]M#3#80");
      denominator=BigInteger.ONE;
    }
  }
  public BigFraction(  final double value,  final double epsilon,  final int maxIterations) throws FractionConversionException {
    this(value,epsilon,Integer.MAX_VALUE,maxIterations);
    auxiliary.Dumper.write("[INST]M#4#86");
  }
  public BigFraction abs(){
    auxiliary.Dumper.write("[INST]M#5#90");
    return (BigInteger.ZERO.compareTo(numerator) <= 0) ? this : negate();
  }
  public BigFraction divide(  final BigInteger bg){
    auxiliary.Dumper.write("[INST]M#6#94");
    if (bg == null) {
      auxiliary.Dumper.write("[INST]M#6#95");
      throw new NullArgumentException(LocalizedFormats.FRACTION);
    }
    auxiliary.Dumper.write("[INST]M#6#97");
    if (BigInteger.ZERO.equals(bg)) {
      auxiliary.Dumper.write("[INST]M#6#98");
      throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR);
    }
    auxiliary.Dumper.write("[INST]M#6#100");
    return new BigFraction(numerator,denominator.multiply(bg));
  }
  public BigFraction divide(  final int i){
    auxiliary.Dumper.write("[INST]M#7#104");
    return divide(BigInteger.valueOf(i));
  }
}
class AnotherTroubleClazz {
  public void method1(){
  }
  public boolean method2(){
    auxiliary.Dumper.write("[INST]M#9#113");
    return true;
  }
}
