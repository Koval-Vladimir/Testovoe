package com.github.kovalVladimir;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import com.github.kovalVladimir.exceptions.*;

public class Utility {
	
	private final Settings settings = new Settings();
	private final Statistics statistics = new Statistics();
	private final ArrayList<Path> listInputFiles = new ArrayList<>();	
	
	public static void main(String[] args) {
		Utility utility = new Utility();
		try {
			utility.parseArgs(args);
			utility.processingFiles();
			if(utility.statistics.mode != Statistics.Mode.NO) {
				utility.statistics.showStatistics(utility.settings);
			}
		} catch(NotFoundInputFilesException | BadOptionException e) {
			System.out.println(e.getMessage());
			showInfoForUsingUtility();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
		
	private void parseArgs(String[] args) throws NotFoundInputFilesException, BadOptionException {
		for(int i = 0; i < args.length; i++) {			
			if(args[i].startsWith("-")) {
				switch(args[i]) {
					case "-o" : 
						i = getPathForResults(args, ++i);
						break;
					case "-p" : 
						i = getPrefixFileName(args, ++i);
						break;
					case "-a" :
						settings.hasOptionA = true;
						settings.openOption = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}; 
						break;
					case "-s" : 
						setModeStatistics(Statistics.Mode.SHORT);
						break;
					case "-f" : 
						setModeStatistics(Statistics.Mode.FULL);
						break;
					default: 
						throw new BadOptionException("Неизвестная опция: " + args[i]);
				}
			} else {
				getFile(args[i]);				
			}
		}
		if(args.length == 0 || listInputFiles.isEmpty()) {
			throw new NotFoundInputFilesException("Не указаны входные файлы для обработки.");
		}
	}
	
	// Обработка опции -o
	private int getPathForResults(String[] args, int i) throws NotFoundInputFilesException {
		if(i == args.length) {
			if(listInputFiles.isEmpty()) {
				throw new NotFoundInputFilesException("Не указаны входные файлы для обработки.");	
			} else {
				System.out.println("Не указана директория для файлов с результатами для опции -o. ");
				System.out.println("Директория установлена по умолчанию (текущая директория).");
			}
		} else {
			try {
				Path directory = Path.of(args[i]).toAbsolutePath().normalize();
				if(Files.isDirectory(directory)) {
					settings.pathForResults = directory.toString();
				} else if(args[i].startsWith("-") || Files.isRegularFile(directory)) {
					i--;
					System.out.println("Не указана директория для файлов с результатами для опции -o. ");
					System.out.println("Директория установлена по умолчанию (текущая директория).");
				} else {
					System.out.print("Директории \"" + args[i] + "\" не существует. ");
					System.out.println("Директория установлена по умолчанию (текущая директория).");
				}
			} catch(InvalidPathException e) {
				System.out.print("Директория для результата \"" + args[i] + "\" не может существовать, ");
				System.out.print(", поскольку имя директории содержит недопустимые символы, или имя ");
				System.out.println("недопустимо по другим причинам, зависящим от файловой системы.");
				System.out.println("Директория установлена по умолчанию (текущая директория).");
			}
		}
		return i;
	}
	
	// Обработка опции -p
	private int getPrefixFileName(String[] args, int i) throws NotFoundInputFilesException {
		if(i == args.length) {
			if(listInputFiles.isEmpty()) {
				throw new NotFoundInputFilesException("Не указаны входные файлы для обработки.");	
			} else {
				System.out.println("Не указан префикс для имен файлов с результатами для опции -p. ");
				System.out.println("Префикс установлен по умолчанию (отсутствует).");
			}	
		} else {
			try {
				Path fileName = Path.of(args[i] + settings.fileNameForIntegers);
				Path checkPrefix = Path.of(args[i]).toAbsolutePath().normalize();
				if(!args[i].startsWith("-") && !Files.isDirectory(checkPrefix) && !Files.isRegularFile(checkPrefix)) {
					Files.createFile(fileName);
					Files.delete(fileName);
					settings.prefixFileName = args[i];
				} else {
					System.out.println("Не указан префикс для имен файлов с результатами для опции -p. ");
					System.out.println("Префикс установлен по умолчанию (отсутствует).");
					i--;
				}				
			} catch(IOException e) {
				System.out.print("Недопустимый префикс \"" + args[i] + "\" ");
				System.out.println("Префикс установлен по умолчанию (отсутствует).");
			} catch(InvalidPathException e) {
				System.out.print("Имена выходных файлов не могут содержать префикс \"" + args[i]); 
				System.out.print(", поскольку префикс содержит недопустимые символы, или префикс ");
				System.out.println("недопустим по другим причинам, зависящим от файловой системы.");
				System.out.println("Префикс имен выходных файлов установлен по умолчанию (отсутствует).");
			}
		}
		return i;
	}
	
	private void setModeStatistics(Statistics.Mode mode) {
		if(statistics.mode != Statistics.Mode.FULL) {
			statistics.mode = mode;
		}
	}
	
	// Проверка входного файла
	private void getFile(String userFile) {
		try {
			Path file = Path.of(userFile).toAbsolutePath().normalize();
			if(Files.isRegularFile(file)) {
				if(Files.isReadable(file)) {
					listInputFiles.add(file);
				} else {
					System.out.print("Файл \"" + file + "\" не доступен для чтения.");
				}
			} else if(Files.isDirectory(file)) {
				System.out.println("\"" + file + "\" является директорией, но не файлом.");
			} else {
				System.out.println("Файла \"" + file + "\" не существует.");
			}
		} catch(InvalidPathException e) {
			System.out.print("Файл с именем \"" + userFile + "\" не может существовать, ");
			System.out.print("поскольку имя содержит недопустимые символы, или имя ");
			System.out.println("недопустимо по другим причинам, зависящим от файловой системы.");
		}
	}
		
	private void processingFiles() throws IOException {
		ArrayList<BufferedReader> openFiles = new ArrayList<>();
		Path pathInt = Path.of(settings.pathForResults, (settings.prefixFileName + settings.fileNameForIntegers));
		Path pathFlt = Path.of(settings.pathForResults, (settings.prefixFileName + settings.fileNameForFloats));
		Path pathStr = Path.of(settings.pathForResults, (settings.prefixFileName + settings.fileNameForStrings));
		
		BufferedWriter integers = Files.newBufferedWriter(pathInt, settings.openOption);
		BufferedWriter floats   = Files.newBufferedWriter(pathFlt, settings.openOption);
		BufferedWriter strings  = Files.newBufferedWriter(pathStr, settings.openOption);
		
		for(var path : listInputFiles) {
			openFiles.add(Files.newBufferedReader(path));
		}	
		while(!openFiles.isEmpty()) {
			for(int i = 0; i < openFiles.size(); i++) {
				var file = openFiles.get(i);
				String str = file.readLine();
				if(str == null) {
					file.close();
					openFiles.remove(file);
				} else {
					processingString(str, integers, floats, strings);
				}
			}
		}	
		closeOutputFiles(integers, floats, strings, pathInt, pathFlt, pathStr);
	}
	
	void closeOutputFiles(BufferedWriter integers, BufferedWriter floats, BufferedWriter strings, 
							Path... paths) throws IOException {
		integers.close();	
		floats.close();
		strings.close();
		for(int i = 0; i < paths.length; i++) {
			if(Files.size(paths[i]) == 0) {
				Files.delete(paths[i]);
			} else {
				System.out.println("Файл \"" + paths[i].toString() + "\" " + (settings.hasOptionA ? "дополнен." : "создан."));
			}
		}
	}
	
	private void processingString(String str, BufferedWriter integers,
							        BufferedWriter floats, BufferedWriter strings) throws IOException
	{ 
		try { 				                    // Проверка на целое число (восьмеричное,
			Long currentInt = Long.decode(str); // десятичное и шестнадцатеричное представления)
			integers.write(str + System.lineSeparator());
			statistics.counterIntegers++;
			if(currentInt < statistics.minInt) statistics.minInt = currentInt;
			if(currentInt > statistics.maxInt) statistics.maxInt = currentInt;
			statistics.totalInt += currentInt;
		} catch(NumberFormatException nfeOne) {
			try {
				Double currentFloat = Double.parseDouble(str); // Проверка на вещественное число
				floats.write(str + System.lineSeparator());
				statistics.counterFloats++;
				if(currentFloat < statistics.minFloat) statistics.minFloat = currentFloat;
				if(currentFloat > statistics.maxFloat) statistics.maxFloat = currentFloat;
				statistics.totalFloat += currentFloat;
			} catch(NumberFormatException nfeTwo) {
				int length = str.length();
				if(length != 0) { // Пропускаем пустые строки в файле
					strings.write(str + System.lineSeparator());   // Если не целое и не вещественное, значит строка
					statistics.counterStrings++;
					if(length < statistics.shortestStrLength) {
						statistics.shortestStrLength = length;
						statistics.shortestStr = str;
					}
					if(length > statistics.longestStrLength) {
						statistics.longestStrLength = length;
						statistics.longestStr = str;
					}
				}
			}
		}
	}
	
	private static void showInfoForUsingUtility() {
		System.out.print("Использование: java -jar util.jar [опции] <входной_файл_1>");
		System.out.println(" [<дополнительные входные_файлы>...]");
	}
}
