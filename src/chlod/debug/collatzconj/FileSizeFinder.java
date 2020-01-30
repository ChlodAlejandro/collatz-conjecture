package chlod.debug.collatzconj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileSizeFinder implements Runnable {

	@Override
	public void run() {
		final long starttime = System.currentTimeMillis();
		long lastloop = System.currentTimeMillis();
		long primesize = CollatzConjecture.ccfile.length();
		long benchsize = CollatzConjecture.benchfile.length();
		BufferedWriter benchwr;
		try {
			benchwr = new BufferedWriter(new FileWriter(CollatzConjecture.benchfile));
			while ((CollatzConjecture.passes <= CollatzConjecture.limit) && (CollatzConjecture.ccfile.length() <= CollatzConjecture.bytelimit)) {
				if (System.currentTimeMillis() - lastloop >= 1000) {
					//(correct * 100.0f) / questionNum
					double primesfiles = (Double.parseDouble(String.valueOf(CollatzConjecture.ccfile.length()))*100.0D) / Double.parseDouble(String.valueOf(CollatzConjecture.bytelimit));
					double primesdone = (Double.parseDouble(String.valueOf(CollatzConjecture.passes))*100.0D) / Double.parseDouble(String.valueOf(CollatzConjecture.limit));
					CollatzConjecture.output("CC FILE SIZE: " + CollatzConjecture.ccfile.length() + "B, " + String.format("%.2f", primesfiles) + "% byte limit reached. " + (CollatzConjecture.bytelimit-CollatzConjecture.ccfile.length()) + "B left. " + 
					"PASSED NUMBERS: " + CollatzConjecture.passes + ", " + String.format("%.2f", primesdone) + "% number limit reached. " + (CollatzConjecture.limit-CollatzConjecture.passes) + " left.");
					
					//file writing
					benchwr.append(CollatzConjecture.passes + " numbers scanned, " + (CollatzConjecture.violated ? "VIOLATION DETECTED." : "No violations found.")); benchwr.newLine();
					benchwr.append(System.currentTimeMillis() - lastloop + "ms since last loop"); benchwr.newLine();
					benchwr.append(System.currentTimeMillis() - starttime + "ms since bench start"); benchwr.newLine();
					benchwr.newLine();
					benchwr.append("Benchmark file size: " + CollatzConjecture.benchfile.length() + "B (" + (CollatzConjecture.benchfile.length() - benchsize) + " B this cycle)"); benchwr.newLine();
					benchwr.append("Primes file size: " + CollatzConjecture.ccfile.length() + "B (" + (CollatzConjecture.ccfile.length() - primesize) + " B this cycle)"); benchwr.newLine();
					benchwr.newLine();
					benchwr.flush();
					//console
		//			output(passes + " numbers scanned");
		//			output(primes + " found. " + (primes-lastprimes) + " primes found this second.");
		//			output(System.currentTimeMillis() - lastloop + "ms since last cycle"); 
		//			output(System.currentTimeMillis() - starttime + "ms since bench start"); 
		//			output("");
		//			output("Benchmark file size: " + benchfile.length() + "B (" + (benchfile.length() - benchsize) + " B this cycle)"); 
		//			output("Primes file size: " + ccfile.length() + "B (" + (ccfile.length() - primesize) + " B this cycle)"); 
		//			output("");
					//re value
					primesize = CollatzConjecture.ccfile.length();
					benchsize = CollatzConjecture.benchfile.length();
					lastloop = System.currentTimeMillis();
				}
			}
			
		} catch (IOException e) {
			CollatzConjecture.output("Error!");
			e.printStackTrace();
		}
	}

	
	
}
