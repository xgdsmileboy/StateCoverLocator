package code;

import polyglot.ast.Case;

public class SideEffect {
	
	public void method_if_1() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		for(int i = 0; i < a.length; i++) {
			if(a[i] > 5)
				b[i] = a[i];
			else
				b[i] = 5;
		}
		
	}
	
	public void method_if_2() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		for(int i = 0; i < a.length; i++) {
			if(a[i] > 5){
				b[i] = a[i];
			} else {
				b[i] = 5;
			}
		}
	}
	
	public void method_for_1(){
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		for(int i = 0; i < a.length; i++)
			if(a[i] > 5){
				b[i] = a[i];
			} else {
				b[i] = 5;
			}
	}
	
	public void method_do_1() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		int i = 0;
		do
			b[i] = a[i];
		while (i++ < a.length);
	}
	
	public void method_do_2() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		int i = 0;
		do{
			b[i] = a[i];
		}while (i++ < a.length);
	}
	
	public void method_while_1() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		int i = 0;
		while(i++ < a.length)
			b[i] = a[i];
	}
	
	public void method_while_2() {
		int[] a = new int[]{1,2,3,4,5,6};
		int[] b = new int[a.length];
		int i = 0;
		while(i < a.length){
			b[i] = a[i];
			i ++;
		}
	}
	
	public int method_switch(String name) {
		int a = 0;
		switch(name) {
		case "name1":
			a = 1;
			break;
		case "name2":
			a = 2;
			break;
		default:
			break;
		}
		return a;
	}
	
	public void withoutInitializer(String string) {
		int i,m = 5;
		float f1,f2;
		byte b;
		short s;
		long l;
		char c;
		double d;
		boolean bl;
		Integer integer;
		String other;
		
		if(string ==null || (i=string.length()) <= 0) {
			return;
		}
		for(int j = 0; i > j; j++) {
			
		}
		
	}
	
	public Long method_insert_return() {
		return 0;
	}
	
	public Number method_not_insert_return() {
		return 0;
	}
	
}