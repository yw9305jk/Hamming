/*	Kou Vang
 * 	ICS 460.01
 *  PA 1 - Hamming Codes
 *  http://adityamandhare.blogspot.com/2012/08/hamming-codeerror-correction-detection.html
 *  http://darshangajara.com/tag/program-to-find-hamming-code-in-java/
 *  ^ These websites were used as a reference for this program
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Hamming {
	public static void main (String[] args) throws IOException{

		int bitCode[] = new int[8];
		int parity[] = new int[4];
		int finalBitCode[] = new int[8];
		int finalParity[] = new int[4];
		int receivedCode[] = new int[12];
		int completeCode[] = new int[12];
		String[] bitTemp = new String[8];
		String[] receivedTemp = new String[12];
		ArrayList<String> hammingCode = new ArrayList<String>();
		ArrayList<String> finalHammingCode = new ArrayList<String>();
		FileWriter writer = new FileWriter("output.csv");
		File file = null;
		//File file = new File ("D:\\downloads\\eclipse\\workspace\\PA1\\input.csv");
		

		//Load input file with command line
		if (0 < args.length){
			file = new File(args[0]);
		}
		else{
			System.err.println("Invalid arguments count:" + args.length);
			System.exit(0);
		}
		
		//Delimit the CSV into smaller array list elements
		Scanner scannerTest = new Scanner(file);
		scannerTest.useDelimiter("\n");
		while(scannerTest.hasNext()){
			hammingCode.add(scannerTest.next());
		}
		
		//Create two new arrays to hold the initial bit code and the received code
		String[] bitCodeString = new String[hammingCode.size()];
		String[] receivedString = new String[hammingCode.size()];

		//Split the two codes into their own arrays
		for (int i = 0; i < hammingCode.size(); i++){
			String[] pieces = hammingCode.get(i).split(",");
			bitCodeString[i] = pieces[0];
			receivedString[i] = pieces[1];
		}
		
		//Loop through the number of codes in the input file and apply hamming code
		for (int i = 0; i < hammingCode.size(); i++){
			//Turn the bitCode into integers so we can work with them
			for(int j = 0; j < 8; j++){
				bitTemp = bitCodeString[i].split("");
				bitCode[j] = Integer.parseInt(bitTemp[j]);
				System.out.print(bitCode[j]);
				writer.write(Integer.toString(bitCode[j]));
			}
			
			//Using the use-skip method, we calculate the parity bits from the position and bit code
			parity[0] = bitCode[0]^bitCode[1]^bitCode[3]^bitCode[4]^bitCode[6];
			parity[1] = bitCode[0]^bitCode[2]^bitCode[3]^bitCode[5]^bitCode[6];
			parity[2] = bitCode[1]^bitCode[2]^bitCode[3]^bitCode[7];
			parity[3] = bitCode[4]^bitCode[5]^bitCode[6]^bitCode[7];
			
			//The code that includes the parity and regular bit code
			completeCode[0] = parity[0];
			completeCode[1] = parity[1];
			completeCode[2] = bitCode[0];
			completeCode[3] = parity[2];
			completeCode[4] = bitCode[1];
			completeCode[5] = bitCode[2];
			completeCode[6] = bitCode[3];
			completeCode[7] = parity[3];
			completeCode[8] = bitCode[4];
			completeCode[9] = bitCode[5];
			completeCode[10] = bitCode[6];
			completeCode[11] = bitCode[7];
			
			System.out.print(",");
			writer.write(",");
			
			//Turn the receivedCode into integers so we can work with them
			for (int j = 0; j < 12; j++){
				receivedTemp = receivedString[i].split("");
				receivedCode[j] = Integer.parseInt(receivedTemp[j]);
				System.out.print(receivedCode[j]);
				writer.write(Integer.toString(receivedCode[j]));
			}
			
			System.out.print(",");
			writer.write(",");
			
			//Print-write the complete code to console-output.csv
			for(int j = 0; j < 12; j++){
				System.out.print(completeCode[j]);
				writer.write(Integer.toString(completeCode[j]));
			}
			
			System.out.print(",");
			writer.write(",");
			
			//Find parity and bit code locations
			finalParity[0] = receivedCode[0];
			finalParity[1] = receivedCode[1];
			finalBitCode[0] = receivedCode[2];
			finalParity[2] = receivedCode[3];
			finalBitCode[1] = receivedCode[4];
			finalBitCode[2] = receivedCode[5];
			finalBitCode[3] = receivedCode[6];
			finalParity[3] = receivedCode[7];
			finalBitCode[4] = receivedCode[8];
			finalBitCode[5] = receivedCode[9];
			finalBitCode[6] = receivedCode[10];
			finalBitCode[7] = receivedCode[11];
			
			//Using the use-skip method, check the parity bits for the received message
			int errorCheck[] = new int[4];
			errorCheck[0] = finalParity[0]^finalBitCode[0]^finalBitCode[1]^finalBitCode[3]^finalBitCode[4]^finalBitCode[6];
			errorCheck[1] = finalParity[1]^finalBitCode[0]^finalBitCode[2]^finalBitCode[3]^finalBitCode[5]^finalBitCode[6];
			errorCheck[2] = finalParity[2]^finalBitCode[1]^finalBitCode[2]^finalBitCode[3]^finalBitCode[7];
			errorCheck[3] = finalParity[3]^finalBitCode[4]^finalBitCode[5]^finalBitCode[6]^finalBitCode[7];
			
			//Find the location of the error
			int check = (errorCheck[0]*1) + (errorCheck[1]*2) + (errorCheck[2]*4) + (errorCheck[3]*8);
			if(check == 0){
			System.out.println("0");
			writer.write("0" + System.lineSeparator());
			}
			
			//Print the location of the error
			else{
				System.out.println(check);
				writer.write(Integer.toString(check ) + System.lineSeparator());
				if (receivedCode[check-1] == 0){
					receivedCode[check-1] = 1;
				}
				else{
					receivedCode[check-1] = 0;
				}
			}
			
		}
		//Closes the writer
		writer.close();
	}
}
