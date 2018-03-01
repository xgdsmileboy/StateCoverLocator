package code;

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
	
}