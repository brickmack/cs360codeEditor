package adamat01_lab1;

import java.text.DecimalFormat;

public class LandCalculation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int area = 534521;//square footage
		double acres; //acreage
		final double CONVERSION = 43560;
		
		acres = area/CONVERSION;
		
		System.out.println("The area of the land in square feet is: "+area+" square feet");
		System.out.println("The area of the land in acres is: "+acres+" acres\n");
		
		DecimalFormat df;
		df = new DecimalFormat("##.000");
		
		System.out.println("The area of the land in acres is: "+df.format(acres)+" acres");
		
		df = new DecimalFormat("##.#####");
		
		System.out.println("The area of the land in acres is: "+df.format(acres)+" acres");
	}

}
