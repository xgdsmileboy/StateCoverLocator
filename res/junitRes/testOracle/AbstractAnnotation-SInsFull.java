package org.jfree.chart.annotations;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.jfree.chart.event.AnnotationChangeEvent;
import org.jfree.chart.event.AnnotationChangeListener;
public abstract class AbstractAnnotation implements Annotation, Cloneable, Serializable {
  private transient EventListenerList listenerList;
  private boolean notify=true;
  protected AbstractAnnotation(){
    auxiliary.Dumper.write("[INST]M#0#19");
    this.listenerList=new EventListenerList();
  }
  public void addChangeListener(  AnnotationChangeListener listener){
    auxiliary.Dumper.write("[INST]M#1#23");
    this.listenerList.add(AnnotationChangeListener.class,listener);
  }
  public void removeChangeListener(  AnnotationChangeListener listener){
    auxiliary.Dumper.write("[INST]M#2#27");
    this.listenerList.remove(AnnotationChangeListener.class,listener);
  }
  public boolean hasListener(  EventListener listener){
    auxiliary.Dumper.write("[INST]M#3#31");
    List list=Arrays.asList(this.listenerList.getListenerList());
    auxiliary.Dumper.write("[INST]M#3#32");
    return list.contains(listener);
  }
  protected void fireAnnotationChanged(){
    auxiliary.Dumper.write("[INST]M#4#36");
    if (notify) {
      auxiliary.Dumper.write("[INST]M#4#37");
      notifyListeners(new AnnotationChangeEvent(this,this));
    }
  }
  protected void notifyListeners(  AnnotationChangeEvent event){
    auxiliary.Dumper.write("[INST]M#5#42");
    Object[] listeners=this.listenerList.getListenerList();
    for (int i=listeners.length - 2; i >= 0; i-=2) {
      auxiliary.Dumper.write("[INST]M#5#43");
      auxiliary.Dumper.write("[INST]M#5#44");
      if (listeners[i] == AnnotationChangeListener.class) {
        auxiliary.Dumper.write("[INST]M#5#45");
        ((AnnotationChangeListener)listeners[i + 1]).annotationChanged(event);
      }
    }
  }
  public boolean getNotify(){
    auxiliary.Dumper.write("[INST]M#6#51");
    return this.notify;
  }
  public void setNotify(  boolean flag){
    auxiliary.Dumper.write("[INST]M#7#55");
    this.notify=flag;
    auxiliary.Dumper.write("[INST]M#7#56");
    if (notify) {
      auxiliary.Dumper.write("[INST]M#7#57");
      fireAnnotationChanged();
    }
  }
  public Object clone() throws CloneNotSupportedException {
    auxiliary.Dumper.write("[INST]M#8#62");
    AbstractAnnotation clone=(AbstractAnnotation)super.clone();
    auxiliary.Dumper.write("[INST]M#8#63");
    clone.listenerList=new EventListenerList();
    auxiliary.Dumper.write("[INST]M#8#64");
    return clone;
  }
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    auxiliary.Dumper.write("[INST]M#9#68");
    stream.defaultWriteObject();
  }
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    auxiliary.Dumper.write("[INST]M#10#72");
    stream.defaultReadObject();
    auxiliary.Dumper.write("[INST]M#10#73");
    this.listenerList=new EventListenerList();
  }
}
