package org.jfree.chart.labels;
import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.text.AttributedString;
import java.text.NumberFormat;
import java.util.Locale;
import org.jfree.chart.util.ObjectList;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.pie.PieDataset;
/** 
 * A standard item label generator for plots that use data from a                                                                                              {@link PieDataset}. <p> For the label format, use {0} where the pie section key should be inserted, {1} for the absolute section value and {2} for the percent amount of the pie section, e.g. <code>"{0} = {1} ({2})"</code> will display as <code>apple = 120 (5%)</code>.
 */
public class StandardPieSectionLabelGenerator extends AbstractPieItemLabelGenerator implements PieSectionLabelGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=3064190563760203668L;
  /** 
 * The default section label format. 
 */
  public static final String DEFAULT_SECTION_LABEL_FORMAT="{0}";
  /** 
 * An optional list of attributed labels (instances of AttributedString).
 */
  private ObjectList attributedLabels;
  /** 
 * Creates a new section label generator using                                                                                              {@link #DEFAULT_SECTION_LABEL_FORMAT} as the label format string, andplatform default number and percentage formatters.
 */
  public StandardPieSectionLabelGenerator(){
    this(DEFAULT_SECTION_LABEL_FORMAT,NumberFormat.getNumberInstance(),NumberFormat.getPercentInstance());
  }
  /** 
 * Creates a new instance for the specified locale.
 * @param locale  the local (<code>null</code> not permitted).
 * @since 1.0.7
 */
  public StandardPieSectionLabelGenerator(  Locale locale){
    this(DEFAULT_SECTION_LABEL_FORMAT,locale);
  }
  /** 
 * Creates a new section label generator using the specified label format string, and platform default number and percentage formatters.
 * @param labelFormat  the label format (<code>null</code> not permitted).
 */
  public StandardPieSectionLabelGenerator(  String labelFormat){
    this(labelFormat,NumberFormat.getNumberInstance(),NumberFormat.getPercentInstance());
  }
  /** 
 * Creates a new instance for the specified locale.
 * @param labelFormat  the label format (<code>null</code> not permitted).
 * @param locale  the local (<code>null</code> not permitted).
 * @since 1.0.7
 */
  public StandardPieSectionLabelGenerator(  String labelFormat,  Locale locale){
    this(labelFormat,NumberFormat.getNumberInstance(locale),NumberFormat.getPercentInstance(locale));
  }
  /** 
 * Creates an item label generator using the specified number formatters.
 * @param labelFormat  the label format string (<code>null</code> notpermitted).
 * @param numberFormat  the format object for the values (<code>null</code>not permitted).
 * @param percentFormat  the format object for the percentages(<code>null</code> not permitted).
 */
  public StandardPieSectionLabelGenerator(  String labelFormat,  NumberFormat numberFormat,  NumberFormat percentFormat){
    super(labelFormat,numberFormat,percentFormat);
    this.attributedLabels=new ObjectList();
  }
  /** 
 * Returns the attributed label for a section, or <code>null</code> if none is defined.
 * @param section  the section index.
 * @return The attributed label.
 */
  public AttributedString getAttributedLabel(  int section){
    return (AttributedString)this.attributedLabels.get(section);
  }
  /** 
 * Sets the attributed label for a section.
 * @param section  the section index.
 * @param label  the label (<code>null</code> permitted).
 */
  public void setAttributedLabel(  int section,  AttributedString label){
    this.attributedLabels.set(section,label);
  }
  /** 
 * Generates a label for a pie section.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the section key (<code>null</code> not permitted).
 * @return The label (possibly <code>null</code>).
 */
  public String generateSectionLabel(  PieDataset dataset,  Comparable key){
    return super.generateSectionLabel(dataset,key);
  }
  /** 
 * Generates an attributed label for the specified series, or <code>null</code> if no attributed label is available (in which case, the string returned by                                                                                              {@link #generateSectionLabel(PieDataset,Comparable)} willprovide the fallback).  Only certain attributes are recognised by the code that ultimately displays the labels: <ul> <li> {@link TextAttribute#FONT}: will set the font;</li> <li>                                                                                              {@link TextAttribute#POSTURE}: a value of                                                                                              {@link TextAttribute#POSTURE_OBLIQUE} will add {@link Font#ITALIC} tothe current font;</li> <li> {@link TextAttribute#WEIGHT}: a value of                                                                                              {@link TextAttribute#WEIGHT_BOLD} will add {@link Font#BOLD} to thecurrent font;</li> <li> {@link TextAttribute#FOREGROUND}: this will set the                                                                                               {@link Paint}for the current</li> <li>                                                                                              {@link TextAttribute#SUPERSCRIPT}: the values                                                                                              {@link TextAttribute#SUPERSCRIPT_SUB} and{@link TextAttribute#SUPERSCRIPT_SUPER} are recognised.</li></ul>
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the key.
 * @return An attributed label (possibly <code>null</code>).
 */
  public AttributedString generateAttributedSectionLabel(  PieDataset dataset,  Comparable key){
    return getAttributedLabel(dataset.getIndex(key));
  }
  /** 
 * Tests the generator for equality with an arbitrary object.
 * @param obj  the object to test against (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof StandardPieSectionLabelGenerator)) {
      return false;
    }
    StandardPieSectionLabelGenerator that=(StandardPieSectionLabelGenerator)obj;
    if (!this.attributedLabels.equals(that.attributedLabels)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException  should not happen.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
