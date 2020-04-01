package gameelements;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;

public class Input {

	public static void main(String[] args) {
		Input input = new Input("C:\\Users\\hoang\\Project-1.2-DKE\\test");
	}
	

	private double g;
	private double m;
	private double mu;
	private double vmax;
	private double tol;

	public Input(String filePath) {
		System.out.println(this.loadCourse(filePath));
	}

	public PuttingCourse loadCourse(String filePath) {
		PuttingCourse pc;
		try {
			String[] in = new String[10];
			int i = 0;
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				in[i] = sc.nextLine(); i++;
			}
			this.g = Double.parseDouble(in[0]);
			this.m = Double.parseDouble(in[1]);
			this.mu = Double.parseDouble(in[2]);
			this.vmax = Double.parseDouble(in[3]);
			this.tol = Double.parseDouble(in[4]);
			Vector3d start = new Vector3d(Double.parseDouble(in[5]), Double.parseDouble(in[6]));
			Vector3d flag = new Vector3d(Double.parseDouble(in[7]), Double.parseDouble(in[8]));
			pc = new PuttingCourse(new FunctionParserRPN(in[9]), start, flag);
			pc.set_friction_coefficient(this.mu);
			pc.setBallMass(this.m);
			pc.setGravitationalConstant(this.g);
			pc.setHoleTolerance(this.tol);
			pc.setMaximumVelocity(this.vmax);
			sc.close();
		} catch (IOException e) {
			return null;
		}
		return pc;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public double getVmax() {
		return vmax;
	}

	public void setVmax(double vmax) {
		this.vmax = vmax;
	}

	public double getTol() {
		return tol;
	}

	public void setTol(double tol) {
		this.tol = tol;
	}

}
