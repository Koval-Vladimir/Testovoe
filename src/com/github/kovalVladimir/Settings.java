package com.github.kovalVladimir;

import java.nio.file.Path;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

class Settings {
	String pathForResults      = Path.of(".").toAbsolutePath().normalize().toString();
	String prefixFileName      = "";
	String fileNameForIntegers = "integers.txt";
	String fileNameForFloats   = "floats.txt";
	String fileNameForStrings  = "strings.txt";
	boolean hasOptionA		   = false;
	OpenOption[] openOption	   = { StandardOpenOption.CREATE, 
								   StandardOpenOption.TRUNCATE_EXISTING,
								   StandardOpenOption.WRITE }; 
}
