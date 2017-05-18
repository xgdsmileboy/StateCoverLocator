package org.jfree.chart;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.Timeline;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.WaferMapPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.renderer.WaferMapRenderer;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.category.WaterfallBarRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.GradientXYBarPainter;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.WindItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.pie.DefaultPieDataset;
import org.jfree.data.pie.PieDataset;
import org.jfree.data.general.WaferMapDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.WindDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
/** 
 * A collection of utility methods for creating some standard charts with JFreeChart.
 */
public abstract class ChartFactory {
  /** 
 * The chart theme. 
 */
  private static ChartTheme currentTheme=new StandardChartTheme("JFree");
  /** 
 * Returns the current chart theme used by the factory.
 * @return The chart theme.
 * @see #setChartTheme(ChartTheme)
 * @see ChartUtilities#applyCurrentTheme(JFreeChart)
 * @since 1.0.11
 */
  public static ChartTheme getChartTheme(){
    return currentTheme;
  }
  /** 
 * Sets the current chart theme.  This will be applied to all new charts created via methods in this class.
 * @param theme  the theme (<code>null</code> not permitted).
 * @see #getChartTheme()
 * @see ChartUtilities#applyCurrentTheme(JFreeChart)
 * @since 1.0.11
 */
  public static void setChartTheme(  ChartTheme theme){
    if (theme == null) {
      throw new IllegalArgumentException("Null 'theme' argument.");
    }
    currentTheme=theme;
    if (theme instanceof StandardChartTheme) {
      StandardChartTheme sct=(StandardChartTheme)theme;
      if (sct.getName().equals("Legacy")) {
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        XYBarRenderer.setDefaultBarPainter(new StandardXYBarPainter());
      }
 else {
        BarRenderer.setDefaultBarPainter(new GradientBarPainter());
        XYBarRenderer.setDefaultBarPainter(new GradientXYBarPainter());
      }
    }
  }
  /** 
 * Creates a pie chart with default settings.  The chart object returned by this method uses a                                                                                               {@link PiePlot} instance as the plot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A pie chart.
 */
  public static JFreeChart createPieChart(  String title,  PieDataset dataset,  boolean legend){
    return createPieChart(title,dataset,legend,Locale.getDefault());
  }
  /** 
 * Creates a pie chart with default settings. <P> The chart object returned by this method uses a                                                                                               {@link PiePlot} instanceas the plot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @param locale  the locale (<code>null</code> not permitted).
 * @return A pie chart.
 * @since 1.0.7
 */
  public static JFreeChart createPieChart(  String title,  PieDataset dataset,  boolean legend,  Locale locale){
    PiePlot plot=new PiePlot(dataset);
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(locale));
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator(locale));
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a pie chart with default settings that compares 2 datasets. The colour of each section will be determined by the move from the value for the same key in <code>previousDataset</code>. ie if value1 > value2 then the section will be in green (unless <code>greenForIncrease</code> is <code>false</code>, in which case it would be <code>red</code>). Each section can have a shade of red or green as the difference can be tailored between 0% (black) and percentDiffForMaxScale% (bright red/green). <p> For instance if <code>percentDiffForMaxScale</code> is 10 (10%), a difference of 5% will have a half shade of red/green, a difference of 10% or more will have a maximum shade/brightness of red/green. <P> The chart object returned by this method uses a                                                                                               {@link PiePlot} instanceas the plot. <p> Written by <a href="mailto:opensource@objectlab.co.uk">Benoit Xhenseval</a>.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param previousDataset  the dataset for the last run, this will be usedto compare each key in the dataset
 * @param percentDiffForMaxScale scale goes from bright red/green to black,percentDiffForMaxScale indicate the change required to reach top scale.
 * @param greenForIncrease  an increase since previousDataset will bedisplayed in green (decrease red) if true.
 * @param legend  a flag specifying whether or not a legend is required.
 * @param locale  the locale (<code>null</code> not permitted).
 * @param subTitle displays a subtitle with colour scheme if true
 * @param showDifference  create a new dataset that will show the %difference between the two datasets.
 * @return A pie chart.
 * @since 1.0.7
 */
  public static JFreeChart createPieChart(  String title,  PieDataset dataset,  PieDataset previousDataset,  int percentDiffForMaxScale,  boolean greenForIncrease,  boolean legend,  Locale locale,  boolean subTitle,  boolean showDifference){
    PiePlot plot=new PiePlot(dataset);
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(locale));
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator(locale));
    List keys=dataset.getKeys();
    DefaultPieDataset series=null;
    if (showDifference) {
      series=new DefaultPieDataset();
    }
    double colorPerPercent=255.0 / percentDiffForMaxScale;
    for (Iterator it=keys.iterator(); it.hasNext(); ) {
      Comparable key=(Comparable)it.next();
      Number newValue=dataset.getValue(key);
      Number oldValue=previousDataset.getValue(key);
      if (oldValue == null) {
        if (greenForIncrease) {
          plot.setSectionPaint(key,Color.green);
        }
 else {
          plot.setSectionPaint(key,Color.red);
        }
        if (showDifference) {
          series.setValue(key + " (+100%)",newValue);
        }
      }
 else {
        double percentChange=(newValue.doubleValue() / oldValue.doubleValue() - 1.0) * 100.0;
        double shade=(Math.abs(percentChange) >= percentDiffForMaxScale ? 255 : Math.abs(percentChange) * colorPerPercent);
        if (greenForIncrease && newValue.doubleValue() > oldValue.doubleValue() || !greenForIncrease && newValue.doubleValue() < oldValue.doubleValue()) {
          plot.setSectionPaint(key,new Color(0,(int)shade,0));
        }
 else {
          plot.setSectionPaint(key,new Color((int)shade,0,0));
        }
        if (showDifference) {
          series.setValue(key + " (" + (percentChange >= 0 ? "+" : "")+ NumberFormat.getPercentInstance().format(percentChange / 100.0)+ ")",newValue);
        }
      }
    }
    if (showDifference) {
      plot.setDataset(series);
    }
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    if (subTitle) {
      TextTitle subtitle=null;
      subtitle=new TextTitle("Bright " + (greenForIncrease ? "red" : "green") + "=change >=-"+ percentDiffForMaxScale+ "%, Bright "+ (!greenForIncrease ? "red" : "green")+ "=change >=+"+ percentDiffForMaxScale+ "%",new Font("Tahoma",Font.PLAIN,10));
      chart.addSubtitle(subtitle);
    }
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a pie chart with default settings that compares 2 datasets. The colour of each section will be determined by the move from the value for the same key in <code>previousDataset</code>. ie if value1 > value2 then the section will be in green (unless <code>greenForIncrease</code> is <code>false</code>, in which case it would be <code>red</code>). Each section can have a shade of red or green as the difference can be tailored between 0% (black) and percentDiffForMaxScale% (bright red/green). <p> For instance if <code>percentDiffForMaxScale</code> is 10 (10%), a difference of 5% will have a half shade of red/green, a difference of 10% or more will have a maximum shade/brightness of red/green. <P> The chart object returned by this method uses a                                                                                               {@link PiePlot} instanceas the plot. <p> Written by <a href="mailto:opensource@objectlab.co.uk">Benoit Xhenseval</a>.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param previousDataset  the dataset for the last run, this will be usedto compare each key in the dataset
 * @param percentDiffForMaxScale scale goes from bright red/green to black,percentDiffForMaxScale indicate the change required to reach top scale.
 * @param greenForIncrease  an increase since previousDataset will bedisplayed in green (decrease red) if true.
 * @param legend  a flag specifying whether or not a legend is required.
 * @param subTitle displays a subtitle with colour scheme if true
 * @param showDifference  create a new dataset that will show the %difference between the two datasets.
 * @return A pie chart.
 */
  public static JFreeChart createPieChart(  String title,  PieDataset dataset,  PieDataset previousDataset,  int percentDiffForMaxScale,  boolean greenForIncrease,  boolean legend,  boolean subTitle,  boolean showDifference){
    PiePlot plot=new PiePlot(dataset);
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator());
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator());
    List keys=dataset.getKeys();
    DefaultPieDataset series=null;
    if (showDifference) {
      series=new DefaultPieDataset();
    }
    double colorPerPercent=255.0 / percentDiffForMaxScale;
    for (Iterator it=keys.iterator(); it.hasNext(); ) {
      Comparable key=(Comparable)it.next();
      Number newValue=dataset.getValue(key);
      Number oldValue=previousDataset.getValue(key);
      if (oldValue == null) {
        if (greenForIncrease) {
          plot.setSectionPaint(key,Color.green);
        }
 else {
          plot.setSectionPaint(key,Color.red);
        }
        if (showDifference) {
          series.setValue(key + " (+100%)",newValue);
        }
      }
 else {
        double percentChange=(newValue.doubleValue() / oldValue.doubleValue() - 1.0) * 100.0;
        double shade=(Math.abs(percentChange) >= percentDiffForMaxScale ? 255 : Math.abs(percentChange) * colorPerPercent);
        if (greenForIncrease && newValue.doubleValue() > oldValue.doubleValue() || !greenForIncrease && newValue.doubleValue() < oldValue.doubleValue()) {
          plot.setSectionPaint(key,new Color(0,(int)shade,0));
        }
 else {
          plot.setSectionPaint(key,new Color((int)shade,0,0));
        }
        if (showDifference) {
          series.setValue(key + " (" + (percentChange >= 0 ? "+" : "")+ NumberFormat.getPercentInstance().format(percentChange / 100.0)+ ")",newValue);
        }
      }
    }
    if (showDifference) {
      plot.setDataset(series);
    }
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    if (subTitle) {
      TextTitle subtitle=null;
      subtitle=new TextTitle("Bright " + (greenForIncrease ? "red" : "green") + "=change >=-"+ percentDiffForMaxScale+ "%, Bright "+ (!greenForIncrease ? "red" : "green")+ "=change >=+"+ percentDiffForMaxScale+ "%",new Font("Tahoma",Font.PLAIN,10));
      chart.addSubtitle(subtitle);
    }
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a ring chart with default settings. The chart object returned by this method uses a                                                                                               {@link RingPlot} instance as the plot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A ring chart.
 */
  public static JFreeChart createRingChart(  String title,  PieDataset dataset,  boolean legend){
    return createRingChart(title,dataset,legend,Locale.getDefault());
  }
  /** 
 * Creates a ring chart with default settings. <P> The chart object returned by this method uses a                                                                                               {@link RingPlot}instance as the plot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @param locale  the locale (<code>null</code> not permitted).
 * @return A ring chart.
 * @since 1.0.7
 */
  public static JFreeChart createRingChart(  String title,  PieDataset dataset,  boolean legend,  Locale locale){
    RingPlot plot=new RingPlot(dataset);
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator(locale));
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator(locale));
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a chart that displays multiple pie plots.  The chart object returned by this method uses a                                                                                               {@link MultiplePiePlot} instance as theplot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset (<code>null</code> permitted).
 * @param order  the order that the data is extracted (by row or by column)(<code>null</code> not permitted).
 * @param legend  include a legend?
 * @return A chart.
 */
  public static JFreeChart createMultiplePieChart(  String title,  CategoryDataset dataset,  TableOrder order,  boolean legend){
    if (order == null) {
      throw new IllegalArgumentException("Null 'order' argument.");
    }
    MultiplePiePlot plot=new MultiplePiePlot(dataset);
    plot.setDataExtractOrder(order);
    plot.setBackgroundPaint(null);
    plot.setOutlineStroke(null);
    PieToolTipGenerator tooltipGenerator=new StandardPieToolTipGenerator();
    PiePlot pp=(PiePlot)plot.getPieChart().getPlot();
    pp.setToolTipGenerator(tooltipGenerator);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a 3D pie chart using the specified dataset.  The chart object returned by this method uses a                                                                                               {@link PiePlot3D} instance as theplot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @param locale  the locale (<code>null</code> not permitted).
 * @return A pie chart.
 * @since 1.0.7
 */
  public static JFreeChart createPieChart3D(  String title,  PieDataset dataset,  boolean legend,  Locale locale){
    PiePlot3D plot=new PiePlot3D(dataset);
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator(locale));
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a 3D pie chart using the specified dataset.  The chart object returned by this method uses a                                                                                               {@link PiePlot3D} instance as theplot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A pie chart.
 */
  public static JFreeChart createPieChart3D(  String title,  PieDataset dataset,  boolean legend){
    PiePlot3D plot=new PiePlot3D(dataset);
    plot.setInsets(new RectangleInsets(0.0,5.0,5.0,5.0));
    plot.setToolTipGenerator(new StandardPieToolTipGenerator());
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a chart that displays multiple pie plots.  The chart object returned by this method uses a                                                                                               {@link MultiplePiePlot} instance as theplot.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset (<code>null</code> permitted).
 * @param order  the order that the data is extracted (by row or by column)(<code>null</code> not permitted).
 * @param legend  include a legend?
 * @return A chart.
 */
  public static JFreeChart createMultiplePieChart3D(  String title,  CategoryDataset dataset,  TableOrder order,  boolean legend){
    if (order == null) {
      throw new IllegalArgumentException("Null 'order' argument.");
    }
    MultiplePiePlot plot=new MultiplePiePlot(dataset);
    plot.setDataExtractOrder(order);
    plot.setBackgroundPaint(null);
    plot.setOutlineStroke(null);
    JFreeChart pieChart=new JFreeChart(new PiePlot3D(null));
    TextTitle seriesTitle=new TextTitle("Series Title",new Font("Tahoma",Font.BOLD,12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    pieChart.setTitle(seriesTitle);
    pieChart.removeLegend();
    pieChart.setBackgroundPaint(null);
    plot.setPieChart(pieChart);
    PieToolTipGenerator tooltipGenerator=new StandardPieToolTipGenerator();
    PiePlot pp=(PiePlot)plot.getPieChart().getPlot();
    pp.setToolTipGenerator(tooltipGenerator);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a bar chart.  The chart object returned by this method uses a                                                                                              {@link CategoryPlot} instance as the plot, with a {@link CategoryAxis}for the domain axis, a                                                                                               {@link NumberAxis} as the range axis, and a{@link BarRenderer} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis(<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A bar chart.
 */
  public static JFreeChart createBarChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    ValueAxis valueAxis=new NumberAxis(valueAxisLabel);
    BarRenderer renderer=new BarRenderer();
    ItemLabelPosition position1=new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.BOTTOM_CENTER);
    renderer.setBasePositiveItemLabelPosition(position1);
    ItemLabelPosition position2=new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6,TextAnchor.TOP_CENTER);
    renderer.setBaseNegativeItemLabelPosition(position2);
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a stacked bar chart with default settings.  The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as theplot, with a  {@link CategoryAxis} for the domain axis, a{@link NumberAxis} as the range axis, and a {@link StackedBarRenderer}as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param domainAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param rangeAxisLabel  the label for the value axis(<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A stacked bar chart.
 */
  public static JFreeChart createStackedBarChart(  String title,  String domainAxisLabel,  String rangeAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(domainAxisLabel);
    ValueAxis valueAxis=new NumberAxis(rangeAxisLabel);
    StackedBarRenderer renderer=new StackedBarRenderer();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a bar chart with a 3D effect. The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as the plot, with a{@link CategoryAxis3D} for the domain axis, a {@link NumberAxis3D} asthe range axis, and a  {@link BarRenderer3D} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A bar chart with a 3D effect.
 */
  public static JFreeChart createBarChart3D(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis3D(categoryAxisLabel);
    ValueAxis valueAxis=new NumberAxis3D(valueAxisLabel);
    BarRenderer3D renderer=new BarRenderer3D();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    plot.setForegroundAlpha(0.75f);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a stacked bar chart with a 3D effect and default settings. The chart object returned by this method uses a                                                                                               {@link CategoryPlot}instance as the plot, with a                                                                                               {@link CategoryAxis3D} for the domain axis,a  {@link NumberAxis3D} as the range axis, and a{@link StackedBarRenderer3D} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A stacked bar chart with a 3D effect.
 */
  public static JFreeChart createStackedBarChart3D(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis3D(categoryAxisLabel);
    ValueAxis valueAxis=new NumberAxis3D(valueAxisLabel);
    CategoryItemRenderer renderer=new StackedBarRenderer3D();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates an area chart with default settings.  The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as the plot, with a{@link CategoryAxis} for the domain axis, a {@link NumberAxis} as therange axis, and an  {@link AreaRenderer} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return An area chart.
 */
  public static JFreeChart createAreaChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    categoryAxis.setCategoryMargin(0.0);
    ValueAxis valueAxis=new NumberAxis(valueAxisLabel);
    AreaRenderer renderer=new AreaRenderer();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a stacked area chart with default settings.  The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as theplot, with a  {@link CategoryAxis} for the domain axis, a{@link NumberAxis} as the range axis, and a {@link StackedAreaRenderer}as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A stacked area chart.
 */
  public static JFreeChart createStackedAreaChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    categoryAxis.setCategoryMargin(0.0);
    ValueAxis valueAxis=new NumberAxis(valueAxisLabel);
    StackedAreaRenderer renderer=new StackedAreaRenderer();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a line chart with default settings.  The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as the plot, with a{@link CategoryAxis} for the domain axis, a {@link NumberAxis} as therange axis, and a  {@link LineAndShapeRenderer} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A line chart.
 */
  public static JFreeChart createLineChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    ValueAxis valueAxis=new NumberAxis(valueAxisLabel);
    LineAndShapeRenderer renderer=new LineAndShapeRenderer(true,false);
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a line chart with default settings. The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as the plot, with a{@link CategoryAxis3D} for the domain axis, a {@link NumberAxis3D} asthe range axis, and a  {@link LineRenderer3D} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A line chart.
 */
  public static JFreeChart createLineChart3D(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis3D(categoryAxisLabel);
    ValueAxis valueAxis=new NumberAxis3D(valueAxisLabel);
    LineRenderer3D renderer=new LineRenderer3D();
    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a Gantt chart using the supplied attributes plus default values where required.  The chart object returned by this method uses a                                                                                              {@link CategoryPlot} instance as the plot, with a {@link CategoryAxis}for the domain axis, a                                                                                               {@link DateAxis} as the range axis, and a{@link GanttRenderer} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param dateAxisLabel  the label for the date axis(<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A Gantt chart.
 */
  public static JFreeChart createGanttChart(  String title,  String categoryAxisLabel,  String dateAxisLabel,  IntervalCategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    DateAxis dateAxis=new DateAxis(dateAxisLabel);
    CategoryItemRenderer renderer=new GanttRenderer();
    renderer.setBaseToolTipGenerator(new IntervalCategoryToolTipGenerator("{3} - {4}",DateFormat.getDateInstance()));
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,dateAxis,renderer);
    plot.setOrientation(PlotOrientation.HORIZONTAL);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a waterfall chart.  The chart object returned by this method uses a                                                                                               {@link CategoryPlot} instance as the plot, with a{@link CategoryAxis} for the domain axis, a {@link NumberAxis} as therange axis, and a  {@link WaterfallBarRenderer} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  the label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  the label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A waterfall chart.
 */
  public static JFreeChart createWaterfallChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  CategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    categoryAxis.setCategoryMargin(0.0);
    ValueAxis valueAxis=new NumberAxis(valueAxisLabel);
    WaterfallBarRenderer renderer=new WaterfallBarRenderer();
    ItemLabelPosition position=new ItemLabelPosition(ItemLabelAnchor.CENTER,TextAnchor.CENTER,TextAnchor.CENTER,0.0);
    renderer.setBasePositiveItemLabelPosition(position);
    renderer.setBaseNegativeItemLabelPosition(position);
    StandardCategoryToolTipGenerator generator=new StandardCategoryToolTipGenerator();
    renderer.setBaseToolTipGenerator(generator);
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    plot.clearRangeMarkers();
    Marker baseline=new ValueMarker(0.0);
    baseline.setPaint(Color.black);
    plot.addRangeMarker(baseline,Layer.FOREGROUND);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a polar plot for the specified dataset (x-values interpreted as angles in degrees).  The chart object returned by this method uses a                                                                                              {@link PolarPlot} instance as the plot, with a {@link NumberAxis} forthe radial axis.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset (<code>null</code> permitted).
 * @param legend  legend required?
 * @return A chart.
 */
  public static JFreeChart createPolarChart(  String title,  XYDataset dataset,  boolean legend){
    PolarPlot plot=new PolarPlot();
    plot.setDataset(dataset);
    NumberAxis rangeAxis=new NumberAxis();
    rangeAxis.setAxisLineVisible(false);
    rangeAxis.setTickMarksVisible(false);
    rangeAxis.setTickLabelInsets(new RectangleInsets(0.0,0.0,0.0,0.0));
    plot.setAxis(rangeAxis);
    plot.setRenderer(new DefaultPolarItemRenderer());
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a scatter plot with default settings.  The chart object returned by this method uses an                                                                                               {@link XYPlot} instance as the plot,with a  {@link NumberAxis} for the domain axis, a  {@link NumberAxis}as the range axis, and an                                                                                               {@link XYLineAndShapeRenderer} as therenderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A scatter plot.
 */
  public static JFreeChart createScatterPlot(  String title,  String xAxisLabel,  String yAxisLabel,  XYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    yAxis.setAutoRangeIncludesZero(false);
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,null);
    XYItemRenderer renderer=new XYLineAndShapeRenderer(false,true);
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    plot.setRenderer(renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of an XY bar chart. <P> The chart object returned by this method uses an                                                                                               {@link XYPlot} instanceas the plot, with a  {@link DateAxis} for the domain axis, a{@link NumberAxis} as the range axis, and a {@link XYBarRenderer} as therenderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param dateAxis  make the domain axis display dates?
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return An XY bar chart.
 */
  public static JFreeChart createXYBarChart(  String title,  String xAxisLabel,  boolean dateAxis,  String yAxisLabel,  IntervalXYDataset dataset,  boolean legend){
    ValueAxis domainAxis=null;
    if (dateAxis) {
      domainAxis=new DateAxis(xAxisLabel);
    }
 else {
      NumberAxis axis=new NumberAxis(xAxisLabel);
      axis.setAutoRangeIncludesZero(false);
      domainAxis=axis;
    }
    ValueAxis valueAxis=new NumberAxis(yAxisLabel);
    XYBarRenderer renderer=new XYBarRenderer();
    XYToolTipGenerator tt;
    if (dateAxis) {
      tt=StandardXYToolTipGenerator.getTimeSeriesInstance();
    }
 else {
      tt=new StandardXYToolTipGenerator();
    }
    renderer.setBaseToolTipGenerator(tt);
    XYPlot plot=new XYPlot(dataset,domainAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates an area chart using an                                                                                               {@link XYDataset}. <P> The chart object returned by this method uses an                                                                                               {@link XYPlot} instanceas the plot, with a  {@link NumberAxis} for the domain axis, a{@link NumberAxis} as the range axis, and a {@link XYAreaRenderer} asthe renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return An XY area chart.
 */
  public static JFreeChart createXYAreaChart(  String title,  String xAxisLabel,  String yAxisLabel,  XYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,null);
    plot.setForegroundAlpha(0.5f);
    XYAreaRenderer renderer=new XYAreaRenderer(XYAreaRenderer.AREA);
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    plot.setRenderer(renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a stacked XY area plot.  The chart object returned by this method uses an                                                                                               {@link XYPlot} instance as the plot, with a{@link NumberAxis} for the domain axis, a {@link NumberAxis} as therange axis, and a  {@link StackedXYAreaRenderer2} as the renderer.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A stacked XY area chart.
 */
  public static JFreeChart createStackedXYAreaChart(  String title,  String xAxisLabel,  String yAxisLabel,  TableXYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    xAxis.setLowerMargin(0.0);
    xAxis.setUpperMargin(0.0);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    StackedXYAreaRenderer2 renderer=new StackedXYAreaRenderer2();
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    renderer.setOutline(true);
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,renderer);
    plot.setRangeAxis(yAxis);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a line chart (based on an                                                                                               {@link XYDataset}) with default settings.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return The chart.
 */
  public static JFreeChart createXYLineChart(  String title,  String xAxisLabel,  String yAxisLabel,  XYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    XYItemRenderer renderer=new XYLineAndShapeRenderer(true,false);
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,renderer);
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a stepped XY plot with default settings.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A chart.
 */
  public static JFreeChart createXYStepChart(  String title,  String xAxisLabel,  String yAxisLabel,  XYDataset dataset,  boolean legend){
    DateAxis xAxis=new DateAxis(xAxisLabel);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    XYItemRenderer renderer=new XYStepRenderer();
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,null);
    plot.setRenderer(renderer);
    plot.setDomainCrosshairVisible(false);
    plot.setRangeCrosshairVisible(false);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a filled stepped XY plot with default settings.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A chart.
 */
  public static JFreeChart createXYStepAreaChart(  String title,  String xAxisLabel,  String yAxisLabel,  XYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    XYItemRenderer renderer=new XYStepAreaRenderer(XYStepAreaRenderer.AREA_AND_SHAPES);
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,null);
    plot.setRenderer(renderer);
    plot.setDomainCrosshairVisible(false);
    plot.setRangeCrosshairVisible(false);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a time series chart.  A time series chart is an                                                                                              {@link XYPlot} with a {@link DateAxis} for the x-axis and a{@link NumberAxis} for the y-axis.  The default renderer is an{@link XYLineAndShapeRenderer}. A convenient dataset to use with this chart is a                                                                                               {@link TimeSeriesCollection}.
 * @param title  the chart title (<code>null</code> permitted).
 * @param timeAxisLabel  a label for the time axis (<code>null</code>permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A time series chart.
 */
  public static JFreeChart createTimeSeriesChart(  String title,  String timeAxisLabel,  String valueAxisLabel,  XYDataset dataset,  boolean legend){
    ValueAxis timeAxis=new DateAxis(timeAxisLabel);
    timeAxis.setLowerMargin(0.02);
    timeAxis.setUpperMargin(0.02);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    valueAxis.setAutoRangeIncludesZero(false);
    XYPlot plot=new XYPlot(dataset,timeAxis,valueAxis,null);
    XYToolTipGenerator toolTipGenerator=null;
    toolTipGenerator=StandardXYToolTipGenerator.getTimeSeriesInstance();
    XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer(true,false);
    renderer.setBaseToolTipGenerator(toolTipGenerator);
    plot.setRenderer(renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of a candlesticks chart.
 * @param title  the chart title (<code>null</code> permitted).
 * @param timeAxisLabel  a label for the time axis (<code>null</code>permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A candlestick chart.
 */
  public static JFreeChart createCandlestickChart(  String title,  String timeAxisLabel,  String valueAxisLabel,  OHLCDataset dataset,  boolean legend){
    ValueAxis timeAxis=new DateAxis(timeAxisLabel);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    XYPlot plot=new XYPlot(dataset,timeAxis,valueAxis,null);
    plot.setRenderer(new CandlestickRenderer());
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of a high-low-open-close chart.
 * @param title  the chart title (<code>null</code> permitted).
 * @param timeAxisLabel  a label for the time axis (<code>null</code>permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A high-low-open-close chart.
 */
  public static JFreeChart createHighLowChart(  String title,  String timeAxisLabel,  String valueAxisLabel,  OHLCDataset dataset,  boolean legend){
    ValueAxis timeAxis=new DateAxis(timeAxisLabel);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    HighLowRenderer renderer=new HighLowRenderer();
    renderer.setBaseToolTipGenerator(new HighLowItemLabelGenerator());
    XYPlot plot=new XYPlot(dataset,timeAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of a high-low-open-close chart with a special timeline. This timeline can be a                                                                                              {@link org.jfree.chart.axis.SegmentedTimeline} such as the Mondaythrough Friday timeline that will remove Saturdays and Sundays from the axis.
 * @param title  the chart title (<code>null</code> permitted).
 * @param timeAxisLabel  a label for the time axis (<code>null</code>permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param timeline  the timeline.
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A high-low-open-close chart.
 */
  public static JFreeChart createHighLowChart(  String title,  String timeAxisLabel,  String valueAxisLabel,  OHLCDataset dataset,  Timeline timeline,  boolean legend){
    DateAxis timeAxis=new DateAxis(timeAxisLabel);
    timeAxis.setTimeline(timeline);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    HighLowRenderer renderer=new HighLowRenderer();
    renderer.setBaseToolTipGenerator(new HighLowItemLabelGenerator());
    XYPlot plot=new XYPlot(dataset,timeAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a bubble chart with default settings.  The chart is composed of an                                                                                               {@link XYPlot}, with a                                                                                               {@link NumberAxis} for the domain axis,a  {@link NumberAxis} for the range axis, and an {@link XYBubbleRenderer}to draw the data items.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the X-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the Y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A bubble chart.
 */
  public static JFreeChart createBubbleChart(  String title,  String xAxisLabel,  String yAxisLabel,  XYZDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    NumberAxis yAxis=new NumberAxis(yAxisLabel);
    yAxis.setAutoRangeIncludesZero(false);
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,null);
    XYItemRenderer renderer=new XYBubbleRenderer(XYBubbleRenderer.SCALE_ON_RANGE_AXIS);
    renderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator());
    plot.setRenderer(renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a histogram chart.  This chart is constructed with an                                                                                              {@link XYPlot} using an {@link XYBarRenderer}.  The domain and range axes are                                                                                               {@link NumberAxis} instances.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  the x axis label (<code>null</code> permitted).
 * @param yAxisLabel  the y axis label (<code>null</code> permitted).
 * @param dataset  the dataset (<code>null</code> permitted).
 * @param legend  create a legend?
 * @return The chart.
 */
  public static JFreeChart createHistogram(  String title,  String xAxisLabel,  String yAxisLabel,  IntervalXYDataset dataset,  boolean legend){
    NumberAxis xAxis=new NumberAxis(xAxisLabel);
    xAxis.setAutoRangeIncludesZero(false);
    ValueAxis yAxis=new NumberAxis(yAxisLabel);
    XYItemRenderer renderer=new XYBarRenderer();
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of a box and whisker chart based on data from a                                                                                               {@link BoxAndWhiskerCategoryDataset}.
 * @param title  the chart title (<code>null</code> permitted).
 * @param categoryAxisLabel  a label for the category axis(<code>null</code> permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A box and whisker chart.
 * @since 1.0.4
 */
  public static JFreeChart createBoxAndWhiskerChart(  String title,  String categoryAxisLabel,  String valueAxisLabel,  BoxAndWhiskerCategoryDataset dataset,  boolean legend){
    CategoryAxis categoryAxis=new CategoryAxis(categoryAxisLabel);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    valueAxis.setAutoRangeIncludesZero(false);
    BoxAndWhiskerRenderer renderer=new BoxAndWhiskerRenderer();
    renderer.setBaseToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
    CategoryPlot plot=new CategoryPlot(dataset,categoryAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates and returns a default instance of a box and whisker chart.
 * @param title  the chart title (<code>null</code> permitted).
 * @param timeAxisLabel  a label for the time axis (<code>null</code>permitted).
 * @param valueAxisLabel  a label for the value axis (<code>null</code>permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag specifying whether or not a legend is required.
 * @return A box and whisker chart.
 */
  public static JFreeChart createBoxAndWhiskerChart(  String title,  String timeAxisLabel,  String valueAxisLabel,  BoxAndWhiskerXYDataset dataset,  boolean legend){
    ValueAxis timeAxis=new DateAxis(timeAxisLabel);
    NumberAxis valueAxis=new NumberAxis(valueAxisLabel);
    valueAxis.setAutoRangeIncludesZero(false);
    XYBoxAndWhiskerRenderer renderer=new XYBoxAndWhiskerRenderer(10.0);
    XYPlot plot=new XYPlot(dataset,timeAxis,valueAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a wind plot with default settings.
 * @param title  the chart title (<code>null</code> permitted).
 * @param xAxisLabel  a label for the x-axis (<code>null</code> permitted).
 * @param yAxisLabel  a label for the y-axis (<code>null</code> permitted).
 * @param dataset  the dataset for the chart (<code>null</code> permitted).
 * @param legend  a flag that controls whether or not a legend is created.
 * @return A wind plot.
 */
  public static JFreeChart createWindPlot(  String title,  String xAxisLabel,  String yAxisLabel,  WindDataset dataset,  boolean legend){
    ValueAxis xAxis=new DateAxis(xAxisLabel);
    ValueAxis yAxis=new NumberAxis(yAxisLabel);
    yAxis.setRange(-12.0,12.0);
    WindItemRenderer renderer=new WindItemRenderer();
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot=new XYPlot(dataset,xAxis,yAxis,renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
  /** 
 * Creates a wafer map chart.
 * @param title  the chart title (<code>null</code> permitted).
 * @param dataset  the dataset (<code>null</code> permitted).
 * @param legend  display a legend?
 * @return A wafer map chart.
 */
  public static JFreeChart createWaferMapChart(  String title,  WaferMapDataset dataset,  boolean legend){
    WaferMapPlot plot=new WaferMapPlot(dataset);
    WaferMapRenderer renderer=new WaferMapRenderer();
    plot.setRenderer(renderer);
    JFreeChart chart=new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,plot,legend);
    currentTheme.apply(chart);
    return chart;
  }
}
