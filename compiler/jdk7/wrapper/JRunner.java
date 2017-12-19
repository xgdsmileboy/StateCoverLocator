package jdk7.wrapper;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class JRunner {

	/** run class from the compiled byte code file by URLClassloader */
	public static boolean runMethod(List<String> classpath, String clazz, String methodName, Class<?>[] params, Object[] objects ) {
		if(params.length != objects.length) {
			return false;
		}
		for(int i = 0; i < params.length; i++) {
			if(objects[i].getClass() != params[i]) {
				return false;
			}
		}
		
		try {
			int size = classpath.size();
			URL[] urls = new URL[size];
			for(int i = 0; i < size; i++) {
				urls[i] = new File(classpath.get(i)).toURI().toURL();
			}
			// Create a new class loader with the directory
			ClassLoader loader = new URLClassLoader(urls);

			// Load in the class;
			Class<?> thisClass = loader.loadClass(methodName);

			Object instance = thisClass.newInstance();
			Method thisMethod = thisClass.getDeclaredMethod(methodName, params);

			// run the testAdd() method on the instance:
			thisMethod.invoke(instance, objects);
			
		} catch (MalformedURLException e) {
		} catch (ClassNotFoundException e) {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
}
