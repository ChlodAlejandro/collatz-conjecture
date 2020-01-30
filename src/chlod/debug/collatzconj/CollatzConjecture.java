package chlod.debug.collatzconj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollatzConjecture {
	
	public static boolean violated = false;
	public static File benchfile = new File("benchmark.txt");
	public static File ccfile = new File("calculations.txt");
	public static long passes = 0;
	public static double current = 1.0D;
	public static boolean debug = false;
	public static long limit = 0;
	public static long bytelimit = Long.MAX_VALUE;
	public static boolean verbose;
	public static boolean output = true;
	public static boolean bench = true;
	
	public static void main(String[] args) {
		if (args.length >= 2) {
			try {
				for (String arg : args) {
					switch(arg) {
						case "-v":
							verbose = true;
							break;
						case "-c":
							output = false;
							break;
						case "-l":
							bench = false;
						break;
					}
				}
				limit = Long.parseLong(args[0]);
				bytelimit = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				output("Invalid arguments! Follow the correct arrangement!");
				output("java -jar <filename>.jar <long : computation limit> <long : file size limit (B)> [-v verbose] [-c disable output file] [-l disable benchmark file]");
				System.exit(0);
			}
		} else {
			output("Wrong argument count!");
			output("java -jar <filename>.jar <long : computation limit> <long : file size limit (B)> [-v verbose] [-c disable output file] [-l disable benchmark file]");
			System.exit(0);
		}
		try {
			BufferedWriter benchwr = new BufferedWriter(new FileWriter(benchfile));
			BufferedWriter ccwr = new BufferedWriter(new FileWriter(ccfile));
			if (benchfile.isDirectory()) crash("Benchmark file (benchmark.txt) is a directory! Delete this before restarting.");
			if (ccfile.isDirectory()) crash("Calculation file (calculations.txt) is a directory! Delete this before restarting.");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			benchwr.append("COLLATZ CONJECTURE COMPUTATION - run " + dateFormat.format(date)); benchwr.newLine();
			ccwr.append("COLLATZ CONJECTURE COMPUTATION - run " + dateFormat.format(date)); ccwr.newLine();
			benchwr.flush();
			ccwr.flush();

			if (bench) (new Thread(new FileSizeFinder())).start();
			while (current <= limit || ccfile.length() <= bytelimit) {
				ccwr.write(current + " | ");
				double currloop = current;
				while (currloop != 1) {
					if (currloop % 2 == 0) {
						currloop = currloop / 2;
						ccwr.write(currloop + " ");
						if (currloop == 1) break;
					}
					else {
						currloop--;
						ccwr.write(currloop + " ");
						if (currloop == 1) break;
					}
					if (currloop < 1) {
						violated = true;
					}
					if (currloop == 1) break;
				}
				ccwr.write(Double.toString(currloop));
				ccwr.newLine();
				if (output) ccwr.flush();
				passes++;
				current += 1.0D;
				if (verbose) output("Done: " + current);
			}
			output("LIMIT(S) REACHED! Passed " + passes + " numbers, used " + ccfile.length() + "B storage.");
			benchwr.close();
			ccwr.close();
		} catch (IOException e) {
			output("Error!");
			e.printStackTrace();
		}
		
	}
	
	public static void crash(String s) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("[" + dateFormat.format(date) + "] ERROR!");
		System.out.println("[" + dateFormat.format(date) + "] " + s);
		Exception e = new Exception(s);
		e.printStackTrace();
		System.exit(1);
	}
	
	public static void output(String s) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("[" + dateFormat.format(date) + "] " + s);
	}
}
