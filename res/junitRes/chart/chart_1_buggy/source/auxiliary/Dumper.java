package auxiliary;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
public class Dumper {
  private static final long MAX_OUTPUT_FILE_SIZE=1000;
  private static final String OUT_AND_LIB_PATH="/home/jiajun/code/space/StateCoverLocator";
  private static final String OUT_FILE_NAME=OUT_AND_LIB_PATH + "/out/path.out";
  private static Dumper instance=new Dumper();
  protected static Dumper getInstance(){
    return instance;
  }
  public static boolean write(  String message){
    if (message == null) {
      return false;
    }
    File file=new File(OUT_FILE_NAME);
    if (!file.exists()) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }
 catch (      IOException e) {
        return false;
      }
    }
    if ((file.length() >> 20) > MAX_OUTPUT_FILE_SIZE) {
      return true;
    }
    BufferedWriter bufferedWriter=null;
    try {
      bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8"));
    }
 catch (    IOException e) {
      return false;
    }
    try {
      bufferedWriter.write(message);
      bufferedWriter.write("\n");
      bufferedWriter.close();
    }
 catch (    IOException e) {
      e.printStackTrace();
    }
 finally {
      if (bufferedWriter != null) {
        try {
          bufferedWriter.close();
        }
 catch (        IOException e) {
          e.printStackTrace();
        }
      }
    }
    return true;
  }
}
