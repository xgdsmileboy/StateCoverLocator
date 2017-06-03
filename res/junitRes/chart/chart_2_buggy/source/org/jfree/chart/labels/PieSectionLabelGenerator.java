package org.jfree.chart.labels;
import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import org.jfree.data.pie.PieDataset;
/** 
 * Interface for a label generator for plots that use data from a                                                                                                                                                                     {@link PieDataset}.
 */
public interface PieSectionLabelGenerator {
  /** 
 * Generates a label for a pie section.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param key  the section key (<code>null</code> not permitted).
 * @return The label (possibly <code>null</code>).
 */
  public String generateSectionLabel(  PieDataset dataset,  Comparable key);
  /** 
 * Generates an attributed label for the specified series, or <code>null</code> if no attributed label is available (in which case, the string returned by                                                                                                                                                                    {@link #generateSectionLabel(PieDataset,Comparable)} willprovide the fallback).  Only certain attributes are recognised by the code that ultimately displays the labels: <ul> <li> {@link TextAttribute#FONT}: will set the font;</li> <li>                                                                                                                                                                    {@link TextAttribute#POSTURE}: a value of                                                                                                                                                                    {@link TextAttribute#POSTURE_OBLIQUE} will add {@link Font#ITALIC} tothe current font;</li> <li> {@link TextAttribute#WEIGHT}: a value of                                                                                                                                                                    {@link TextAttribute#WEIGHT_BOLD} will add {@link Font#BOLD} to thecurrent font;</li> <li> {@link TextAttribute#FOREGROUND}: this will set the                                                                                                                                                                     {@link Paint}for the current</li> <li>                                                                                                                                                                    {@link TextAttribute#SUPERSCRIPT}: the values                                                                                                                                                                    {@link TextAttribute#SUPERSCRIPT_SUB} and{@link TextAttribute#SUPERSCRIPT_SUPER} are recognised.</li></ul>
 * @param dataset  the dataset.
 * @param key  the key.
 * @return An attributed label (possibly <code>null</code>).
 */
  public AttributedString generateAttributedSectionLabel(  PieDataset dataset,  Comparable key);
}
