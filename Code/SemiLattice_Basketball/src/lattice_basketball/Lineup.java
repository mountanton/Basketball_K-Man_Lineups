/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lattice_basketball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author micha
 */
public class Lineup {

    TreeSet<String> lineup = new TreeSet<String>();
    String lineupStr="";
    double points = 0, gamesPlayed = 0, PTM_2 = 0, PTA_2 = 0, PPER_2 = 0;
    double PTM_3 = 0, PTA_3 = 0, PPER_3 = 0;
    double FGM = 0, FGA = 0, FGPER = 0;
    double FTM = 0, FTA = 0, FTPER = 0;
    double OR = 0, DR = 0, TR = 0, assists = 0, TO = 0, STEAL = 0, BLK = 0, PF = 0, DF = 0, RANKING = 0, PLUSMINUS = 0, POINTS_OPP = 0;
    double wins = 0, loses = 0;
    String team = "";

    ;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
    HashMap<String, Double> allStats = new HashMap<>();

    
    
    
    
    
    public void setStats(String[] names, String[] stats) {
        for (int i = 0; i <= 27; i++) {
            if (!names[i].equals("TM") && !names[i].equals("LINEUP")) {
                if (i != 27) {
                    allStats.put(names[i], Double.parseDouble(stats[i].replace("%", "").replace("-", "0")));
                } else {
                    allStats.put(names[i], Double.parseDouble(stats[i].replace("%", "")));
                }
            }
        }
        if (allStats.get("+ / -") >= 0) {
            allStats.put("OPP_PTS", allStats.get("PTS") - allStats.get("+ / -"));
        } else {
            allStats.put("OPP_PTS", allStats.get("PTS") + Math.abs(allStats.get("+ / -")));
        }
       
    }

    public void printStats(ArrayList<String> toPrint) {
        System.out.print(lineup.toString().replace("[","").replace("]","")+"\t"+this.team+"\t");
        //System.out.println(toPrint);
     //   System.out.println("");
        for (String key : toPrint) {
            System.out.print(allStats.get(key) + "\t");
        }
        System.out.println("");
    }
    
    
    public String returnStats(ArrayList<String> toPrint) {
        String output="";
        output+=lineup.toString().replace("[","").replace("]","")+"\t"+this.team+"\t";
        for (String key : toPrint) {
           output+=allStats.get(key) + "\t";
        }
        output+="\n";
        return output;
    }

    public void setLineupStats(Lineup y) {
        this.allStats = new HashMap<String, Double>(y.allStats);
    }

    public void decreaseStats(HashMap<String, Double> otherStats) {
        for (String key : otherStats.keySet()) {
            if (!key.contains("%")) {
                this.allStats.put(key, this.allStats.get(key) - otherStats.get(key));
            }
        }

    }
    
    public void setBasicPercentages(){
          if (this.allStats.get("2PTA")!=null && this.allStats.get("2PTA") > 0) {
            this.allStats.put("2PT%", this.allStats.get("2PTM") / this.allStats.get("2PTA"));
        }
        else{
              this.allStats.put("2PT%",0.0);
        }
        if (this.allStats.get("3PTA")!=null && this.allStats.get("3PTA") > 0) {
            this.allStats.put("3PT%", this.allStats.get("3PTM") / this.allStats.get("3PTA"));
           // System.out.println(this.allStats.get("3PTM") / this.allStats.get("3PTA"));
        }
        else{
              this.allStats.put("3PT%",0.0);
        }
        
        if (this.allStats.get("FTA")!=null && this.allStats.get("FTA") > 0) {
            
              this.allStats.put("FT%", this.allStats.get("FTM") / this.allStats.get("FTA"));
        }
        else{
            this.allStats.put("FT%",0.0);
        }
        if (this.allStats.get("FGA")!=null && this.allStats.get("FGA") > 0) {
             this.allStats.put("FG%", this.allStats.get("FGM") / this.allStats.get("FGA"));
        }
        else{
              this.allStats.put("FG%",0.0);
        }
    }

    public void setPercentages() {
        if (this.allStats.get("2PTA")!=null && this.allStats.get("2PTA") > 0) {
            this.allStats.put("2PT%", this.allStats.get("2PTM") / this.allStats.get("2PTA"));
        }
        else{
              this.allStats.put("2PT%",0.0);
        }
        if (this.allStats.get("3PTA")!=null && this.allStats.get("3PTA") > 0) {
            this.allStats.put("3PT%", this.allStats.get("3PTM") / this.allStats.get("3PTA"));
           // System.out.println(this.allStats.get("3PTM") / this.allStats.get("3PTA"));
        }
        else{
              this.allStats.put("3PT%",0.0);
        }
         if(allStats.get("TO")>0){
              allStats.put("AST_TO", allStats.get("AST")/ allStats.get("TO"));
        }
         else
              allStats.put("AST_TO",0.0);
        if (this.allStats.get("FTA")!=null && this.allStats.get("FTA") > 0) {
            
              this.allStats.put("FT%", this.allStats.get("FTM") / this.allStats.get("FTA"));
        }
        else{
            this.allStats.put("FT%",0.0);
        }
        if (this.allStats.get("FGA")!=null && this.allStats.get("FGA") > 0) {
             this.allStats.put("FG%", this.allStats.get("FGM") / this.allStats.get("FGA"));
        }
        else{
              this.allStats.put("FG%",0.0);
        }
        this.allStats.put("PTS_MIN",this.allStats.get("PTS")/this.allStats.get("MIN"));
        this.allStats.put("OPPPTS_MIN",this.allStats.get("OPP_PTS")/this.allStats.get("MIN"));
        this.allStats.put("TR_MIN",this.allStats.get("TR")/this.allStats.get("MIN"));
        this.allStats.put("DR_MIN",this.allStats.get("DR")/this.allStats.get("MIN"));
        this.allStats.put("OR_MIN",this.allStats.get("OR")/this.allStats.get("MIN"));
        this.allStats.put("AST_MIN",this.allStats.get("AST")/this.allStats.get("MIN"));
        this.allStats.put("TO_MIN",this.allStats.get("TO")/this.allStats.get("MIN"));
        this.allStats.put("ST_MIN",this.allStats.get("ST")/this.allStats.get("MIN"));
        this.allStats.put("VAL_MIN",this.allStats.get("VAL")/this.allStats.get("MIN"));
        this.allStats.put("BLK_MIN",this.allStats.get("OR")/this.allStats.get("MIN"));
        this.allStats.put("PLMIN_MIN",this.allStats.get("+ / -")/this.allStats.get("MIN"));
    }

    public void increaseStats(HashMap<String, Double> otherStats) {
        for (String key : otherStats.keySet()) {
            if (allStats.containsKey(key)) {
                this.allStats.put(key, this.allStats.get(key) + otherStats.get(key));
            } else {
                this.allStats.put(key, otherStats.get(key));
            }
        }

    }
    
    
    
    
    
    

}
