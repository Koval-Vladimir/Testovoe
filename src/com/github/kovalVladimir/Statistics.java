package com.github.kovalVladimir;

class Statistics {
	int counterIntegers 	= 0;
	long minInt 			= Long.MAX_VALUE;
	long maxInt 			= Long.MIN_VALUE;
	long totalInt 			= 0L;
	
	int counterFloats 		= 0;
	double minFloat 		= Double.MAX_VALUE;
	double maxFloat 		= Double.MIN_VALUE;
	double totalFloat 		= 0.0;
	
	int counterStrings 		= 0;
	String shortestStr 		= "";
	int shortestStrLength 	= Integer.MAX_VALUE;
	String longestStr 		= "";
	int longestStrLength 	= Integer.MIN_VALUE;
	
	enum Mode { NO, SHORT, FULL };
	Mode mode = Mode.NO;
	
	void showStatistics(Settings settings) {
		if(counterIntegers != 0 || counterFloats != 0 || counterStrings != 0) {
			System.out.println("\nОбщее количество элементов записанных в файлы результатов: " + 
							(counterIntegers + counterFloats + counterStrings) + "\n");
			if(counterIntegers != 0) {
				System.out.print("Количество элементов записанных в \"" + settings.fileNameForIntegers);
				System.out.println("\": " + counterIntegers);
				if(mode == Mode.FULL) {
					System.out.println("Минимальное  целое число: " + minInt);
					System.out.println("Максимальное целое число: " + maxInt);
					System.out.println("Сумма целых чисел.......: " + totalInt);
					System.out.println("Среднее целых чисел.....: " + (double)totalInt / counterIntegers);
					System.out.println();
				}
			}
			if(counterFloats != 0) {
				System.out.print("Количество элементов записанных в \"" + settings.fileNameForFloats);
				System.out.println("\"  : " + counterFloats);
				if(mode == Mode.FULL) {
					System.out.println("Минимальное  вещественное число: " + minFloat);
					System.out.println("Максимальное вещественное число: " + maxFloat);
					System.out.println("Сумма вещественных чисел.......: " + totalFloat);
					System.out.println("Среднее вещественных чисел.....: " + totalFloat / counterFloats);
					System.out.println();
				}
			}
			if(counterStrings != 0) {
				System.out.print("Количество элементов записанных в \"" + settings.fileNameForStrings);
				System.out.println("\" : " + counterStrings);
				if(mode == Mode.FULL) {
					System.out.print("Размер самой короткой строки: ");
					System.out.println("\"" + shortestStr + "\" - " + shortestStrLength);
					System.out.print("Размер самой  длинной строки: ");
					System.out.println("\"" + longestStr + "\" - " + longestStrLength);
					System.out.println();
				}
			}
		}
	}
}
