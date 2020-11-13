package agents;

import agents.tactics.*;
import emotions.EmotionalCritic;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;
import world.LabEntity;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public class EmotionTest_3 {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
       	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(USE_SERVER_FOR_TEST) labRecruitsTestServer.close(); }

    
    /**
     * 
     */
    @Test
    public void test_random_exploration() throws InterruptedException {

    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("emotionalTest_3"));
    	
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("agent1")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        
        EmotionalCritic emo = new EmotionalCritic(agent);
        
        
        var g = GoalLib.entityIsInteracted("button1");
        
        agent.setGoal(g);
        

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    	
        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int i = 0 ;
        agent.update();
        emo.known_entities = agent.getState().worldmodel.elements.size();
        while (g.getStatus().inProgress()) {
            agent.update();
            emo.update();
            System.out.println(agent.getState().worldmodel.elements) ;
            Thread.sleep(1000);
            
            i++ ;
            if (i>1000) {
            	break ;
            }
        }
        
        g.printGoalStructureStatus();
        
        System.out.println(emo.PDA_vector);
        System.out.println(emo.time_stamps);
        
        emo.saveVectorsInFile();
        
        assertTrue(g.getStatus().success()) ;
        var agent_p  = agent.getState().worldmodel.getFloorPosition() ;
        var button_p = ((LabEntity) agent.getState().worldmodel.getElement("button1")).getFloorPosition() ;
        assertTrue(agent_p.distance(button_p) < 0.5) ;

        if (!environment.close())
            throw new InterruptedException("Unity refuses to start the Simulation!");

    }
    
}
