package org.jfree.data.gantt;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.general.Series;
/** 
 * A series that contains zero, one or many                               {@link Task} objects.<P> This class is used as a building block for the  {@link TaskSeriesCollection}class that can be used to construct basic Gantt charts.
 */
public class TaskSeries extends Series {
  /** 
 * Storage for the tasks in the series. 
 */
  private List tasks;
  /** 
 * Constructs a new series with the specified name.
 * @param name  the series name (<code>null</code> not permitted).
 */
  public TaskSeries(  String name){
    super(name);
    this.tasks=new java.util.ArrayList();
  }
  /** 
 * Adds a task to the series and sends a                              {@link org.jfree.data.general.SeriesChangeEvent} to all registeredlisteners.
 * @param task  the task (<code>null</code> not permitted).
 */
  public void add(  Task task){
    if (task == null) {
      throw new IllegalArgumentException("Null 'task' argument.");
    }
    this.tasks.add(task);
    fireSeriesChanged();
  }
  /** 
 * Removes a task from the series and sends a                               {@link org.jfree.data.general.SeriesChangeEvent}to all registered listeners.
 * @param task  the task.
 */
  public void remove(  Task task){
    this.tasks.remove(task);
    fireSeriesChanged();
  }
  /** 
 * Removes all tasks from the series and sends a                               {@link org.jfree.data.general.SeriesChangeEvent}to all registered listeners.
 */
  public void removeAll(){
    this.tasks.clear();
    fireSeriesChanged();
  }
  /** 
 * Returns the number of items in the series.
 * @return The item count.
 */
  public int getItemCount(){
    return this.tasks.size();
  }
  /** 
 * Returns a task from the series.
 * @param index  the task index (zero-based).
 * @return The task.
 */
  public Task get(  int index){
    return (Task)this.tasks.get(index);
  }
  /** 
 * Returns the task in the series that has the specified description.
 * @param description  the name (<code>null</code> not permitted).
 * @return The task (possibly <code>null</code>).
 */
  public Task get(  String description){
    Task result=null;
    int count=this.tasks.size();
    for (int i=0; i < count; i++) {
      Task t=(Task)this.tasks.get(i);
      if (t.getDescription().equals(description)) {
        result=t;
        break;
      }
    }
    return result;
  }
  /** 
 * Returns an unmodifialble list of the tasks in the series.
 * @return The tasks.
 */
  public List getTasks(){
    return Collections.unmodifiableList(this.tasks);
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the object to test against (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof TaskSeries)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    TaskSeries that=(TaskSeries)obj;
    if (!this.tasks.equals(that.tasks)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns an independent copy of this series.
 * @return A clone of the series.
 * @throws CloneNotSupportedException if there is some problem cloningthe dataset.
 */
  public Object clone() throws CloneNotSupportedException {
    TaskSeries clone=(TaskSeries)super.clone();
    clone.tasks=(List)ObjectUtilities.deepClone(this.tasks);
    return clone;
  }
}
