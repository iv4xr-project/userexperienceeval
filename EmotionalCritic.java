package emotions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import agents.LabRecruitsTestAgent;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.BasicAgent;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Time;

public class EmotionalCritic {
	
	// The value that defines the value interval for the PAD dimensions: [-limit, limit]
	private double limit = 5;
	
	// The initialization of the PAD dimensions
	private double pleasure = 0.0;
	private double dominance = 0.0;
	private double arousal = 0.0;
	
	// The tick counters for each dimension, recording how many ticks passed since the dimension was last altered
	private int P_ticker = 0;
	private int D_ticker = 0;
	private int A_ticker = 0;
	
	// The maximum 'ticks without alteration' for each dimension
	private int P_treshold = 10;
	private int D_treshold = 10;
	private int A_treshold = 10;
	
	// The number of known entities
	public int known_entities = 0;
	
	
	
	public Vector<Vector<Double>> PDA_vector = new Vector<Vector<Double>>();
	
	public Vector<LocalDateTime> time_stamps = new Vector<LocalDateTime>();
	
	public Vector<Vec3> position_stamps = new Vector<Vec3>();
	
	public LabRecruitsTestAgent agent;
	
	private GoalStructure last_assessed_goal = null;
	
	private int goal_success = 0;

	public EmotionalCritic(LabRecruitsTestAgent agent) {
		
		this.agent = agent;
		
	}
	
	
	
	public void update() {
		
		this.P_ticker++; 
		this.D_ticker++;
		this.A_ticker++;

		var last_goal = this.agent.getCurrentGoal();
		
		
		if(last_goal == null) {
			System.out.println("No goal!");
		}
		else {
			System.out.println("Status:" + last_goal.getStatus());
		}
		
		
		//Assessing changes in Pleasure
		
		
		if ((last_goal == this.last_assessed_goal || this.last_assessed_goal == null)) {
			if (this.P_ticker > this.P_treshold) {
				this.pleasure -= 0.4;
				this.P_ticker = 0;
			}
		}
		else {
			this.P_ticker = 0;
			
			if(this.last_assessed_goal.getStatus().success()){
				
				this.pleasure += 1;		
			}
			else if (this.last_assessed_goal.getStatus().failed()) {
				
				this.pleasure -= 1;		
			}
		}
		
		
		//Assessing changes in Arousal
		
		
		if (this.A_ticker > this.A_treshold) {
			this.arousal -= 0.4;
			this.A_ticker = 0;
		}
		
		var prev_known_entities = this.known_entities;
		
		this.known_entities = agent.getState().worldmodel.elements.size();
		
		System.out.println(this.known_entities);
		
		if (this.known_entities > prev_known_entities) {
			
			this.arousal += 1;
			this.A_ticker = 0;
			
		}
		
		
		//Assessing changes in Dominance
		
		//This is yet to be implemented
		
		
		
		enforceLimit();
		
		Vector<Double> PDA_vals = new Vector<Double>(3);
		
		PDA_vals.add(this.pleasure);
		PDA_vals.add(this.dominance);
		PDA_vals.add(this.arousal);
		
		this.PDA_vector.add(PDA_vals);
		
		this.time_stamps.add(LocalDateTime.now());
		
		this.position_stamps.add(this.agent.getState().worldmodel.position);
		
		System.out.println("P: " + this.pleasure);
		System.out.println("D: " + this.dominance);
		System.out.println("A: " + this.arousal);
		System.out.println("P_ticker: " + this.P_ticker);
		System.out.println("D_ticker: " + this.D_ticker);
		System.out.println("A_ticker: " + this.A_ticker);
		
		this.last_assessed_goal = last_goal;
		
	}
	
	public void saveVectorsInFile() {
		
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss");
		
		var filename = "./src/test/resources/emotions/emotion_vectors_" + myDateObj.format(myFormatObj) + ".txt";
		
		try {
			File file = new File(filename);
			FileWriter myWriter = new FileWriter(filename);
			myWriter.write("E|");
			myWriter.write(this.PDA_vector.toString());
			myWriter.write("\nT|");
			myWriter.write(this.time_stamps.toString());
			myWriter.write("\nP|");
			myWriter.write(this.position_stamps.toString());
			myWriter.close();
		} catch (IOException e) {
			System.out.println("Error while trying to write to file.");
			e.printStackTrace();
		}
	   
		
		
	}
	
	private void enforceLimit() {
		
		if (this.pleasure > this.limit) {
			this.pleasure = this.limit;
		}
		else if(this.pleasure < -this.limit) {
			this.pleasure = -this.limit;
		}
		
		if (this.dominance > this.limit) {
			this.dominance = this.limit;
		}
		else if(this.dominance < -this.limit) {
			this.dominance = -this.limit;
		}
		
		if (this.arousal > this.limit) {
			this.arousal = this.limit;
		}
		else if(this.arousal < -this.limit) {
			this.arousal = -this.limit;
		}
		
	}
	

	
	public double getP() {
		return this.pleasure;
	}
	
	public double getD() {
		return this.dominance;
	}
	
	
	public double getA() {
		return this.arousal;
	}
	
	
	

}
