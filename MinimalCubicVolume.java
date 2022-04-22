// Liam Downs
// 11/30/2021

import java.util.Arrays;
import java.util.Scanner;

public class Packaging {

	private static final double ROOT2 = Math.sqrt(2); //constants
	private static final double PIFOURTH = Math.PI / 4;
	private static final double MAXRADIANS = Math.atan(ROOT2);
	private static final double EPSILON = 0.000000000000001; //15 decimal places
	
	private static boolean tiltCheck(int[] dimensions) { //used to check if necessary to calculate tilt/diagonal
		return (2 * Math.atan((double) dimensions[0] / dimensions[2])) <= PIFOURTH;
	}
	
	private static double tiltSide(int[] dimensions) { //computes minimum cube with tilt orientation
		return (dimensions[0] + dimensions[2]) / ROOT2;
	}
	
	private static double[] piecesXZ(double r, double atanZX) { //computations of function that are reused to compute cube
		return new double[] {Math.cos(r + atanZX), Math.sin(r + atanZX)};
	}
	
	private static double function(int[] dimensions, double r, double pythagoreanXZ, double[] piecesXZ) { //function to compute roots
		return dimensions[0] * Math.cos(r) + (dimensions[1] - pythagoreanXZ * (piecesXZ[0] + piecesXZ[1] * ROOT2)) / 2;
	}
	
	private static double[] diagonalSide(int[] dimensions) { //computes minimum cube with diagonal orientation
		double atanZX = Math.atan((double) dimensions[2] / dimensions[0]); //constants computed with dimensions
		double pythagoreanXZ = Math.sqrt(dimensions[0] * dimensions[0] + dimensions[2] * dimensions[2]);
		
		//bisection method
		double r0 = 0; //starting bounds
		double r1 = MAXRADIANS;
		if(function(dimensions, r0, pythagoreanXZ, piecesXZ(r0, atanZX))
				* function(dimensions, r1, pythagoreanXZ, piecesXZ(r1, atanZX))
				>= 0) { // check if function has no roots at within bounds
			return new double[] {-1};
		}
		
		double r2 = r0;
		double[] pieces2 = piecesXZ(r2, atanZX); //stores computations for computing cube
		while(r1 - r0 >= EPSILON) { //loop until acceptable error
			r2 = (r0 + r1) / 2;
			pieces2 = piecesXZ(r2, atanZX);
			double fun2 = function(dimensions, r2, pythagoreanXZ, pieces2);
			if(fun2 == 0) { //check if root, otherwise check if higher or lower
				break;
			} else if(fun2 * function(dimensions, r0, pythagoreanXZ, piecesXZ(r0, atanZX)) < 0) {
				r1 = r2;
			} else {
				r0 = r2;
			}
		}
		return new double[] {(2 * dimensions[0] * Math.cos(r2) + dimensions[1] + pythagoreanXZ * (pieces2[1] * ROOT2 - pieces2[0])) / (2 * ROOT2), r2};
	}
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		while(true) { //program loop
			System.out.print("input dimensions: ");
			int[] dimensions = new int[3];
			
			int i;
			for(i = 0; i < dimensions.length; i++) { //input loop
				if(!input.hasNextInt()) { //break if not int
					System.out.println("invalid\n");
					break;
				}
				dimensions[i] = input.nextInt();
				if(dimensions[i] <= 0) { //break if not positive
					System.out.println("only positive integers\n");
					break;
				} else if(dimensions[i] > 32767) { //break if too large
					System.out.println("no values over 32767\n");
					break;
				}
			}
			if(i < 3) { //restart if inputs are incorrect
				input.next();
				continue;
			}
			
			Arrays.sort(dimensions); //[a, b, c]; a<=b<=c
			
			double[] data = new double[3];
			double sideP;
			int orientation;
			boolean tiltC = tiltCheck(dimensions);
			String tiltComparison = "≥";
			if(!tiltC) { //checks if tilt is optimal
				sideP = dimensions[2]; //otherwise compute flat
				orientation = 0;
			} else {
				sideP = data[0] = tiltSide(dimensions); //compute tilt
				if(sideP < dimensions[1]) { //check if y is greater than tilt side
					sideP = dimensions[1]; //replace side with y as minimal side
					tiltComparison = "<";
				}
				double[] temp = diagonalSide(dimensions); //compute diagonal
				data[1] = temp[0];
				if(temp.length == 1) { //check if diagonal is optimal
					orientation = 1; //otherwise use tilt
				} else if(data[0] < data[1]){
					orientation = 1; //use tilt but save diagonal
					data[2] = temp[1];
				} else {
					data[2] = temp[1];
					sideP = data[1]; //use diagonal
					orientation = 2;
				}
			}
			String[] orientations = new String[] {"flat", "tilt", "diagonal"};
			System.out.println("orientation: " + orientations[orientation]); //print resulting minimal cube
			System.out.println("output cube: " + String.format("%.0f", sideP) + "³");
			
			System.out.print(">");
			input.nextLine(); //enter to continue
			String command = input.nextLine().trim();
			if(command.equals("show")) {
				if(args.length > 0 && args[0].equals("-a")) { //check arg for accessing command permissions
					System.out.println("all computes");
					System.out.println(orientations[0] + ": " + dimensions[2] + "³");
					if(!tiltC) {
						data[0] = tiltSide(dimensions); //compute tilt
						if(sideP < dimensions[1]) { //check if y is greater than tilt side
							tiltComparison = "<";
						}
						double[] temp = diagonalSide(dimensions);
						for(int j = 0; j < temp.length; j++) {
							data[j + 1] = temp[j];
						}
					}
					System.out.println(orientations[1] + ": " + data[0] + "³ " + tiltComparison + " y³");
					if(data[1] == -1) {
						System.out.println(orientations[2] + ": none optimal");
					} else {
						System.out.println(orientations[2] + ": " + data[1] + "³ @ " + data[2] + "rad");
					}
				} else {
					System.out.println("not in admin mode");
				}
				System.out.print(">");
				command = input.nextLine().trim(); //takes second command
			}
			if(command.equals("exit")) { //type exit to terminate
				break;
			}
		}
		input.close();

	}

}
