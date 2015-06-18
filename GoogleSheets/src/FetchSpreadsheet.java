import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class FetchSpreadsheet {
	@SuppressWarnings("unused")
	private static class Team {
		private List<People> people;
		private int level;
		public List<People> getPeople() {
			return people;
		}
		public void setPeople(List<People> people) {
			this.people = people;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
	}

	private static class People {
		private String Name;
		private String Box;
		private String Sex;
		private int Level;
		
		public int getLevel() {
			return Level;
		}
		public void setLevel(int level) {
			Level = level;
		}
		private String getName() {
			return Name;
		}
		private void setName(String name) {
			Name = name;
		}
		private String getBox() {
			return Box;
		}
		private void setBox(String box) {
			Box = box;
		}
		private String getSex() {
			return Sex;
		}
		private void setSex(String sex) {
			Sex = sex;
		}
	}
	
	private static int number = 4;
	public static void main(String[] args) throws BiffException, IOException, WriteException {
		List<People> people = new ArrayList<People>();
	   
		Workbook workbook = Workbook.getWorkbook(new File("output.xls"));
		Sheet sheet = workbook.getSheet(0);
		int cols = sheet.getColumns();
		int rows = sheet.getRows();
      
		System.out.println("LOG::INFO - Spreadsheet Info (Rows): " + rows);
		System.out.println("LOG::INFO - Spreadsheet Info (Columns): " + cols);
      
		for (int i=0; i<rows; i++){
			People athlet = new People();
			for (int j=0; j<cols; j++){
				Cell cell = sheet.getCell(j, i);
				if (j==0)
					athlet.setName(cell.getContents());
				else if (j==1)
					athlet.setBox(cell.getContents());
				else if (j==2)
					athlet.setSex(cell.getContents());
				else if (j==3)
					athlet.setLevel(Integer.parseInt(cell.getContents()));
			}
			people.add(athlet);
		}
	    int athletes = people.size();
	    System.out.println("LOG::INFO - Athletes to be processed: " + athletes);

	    int teams = athletes / number;
      
	    getRestPeople(athletes % number);
      
	    int ladiesNum = 0;
	    double glLevel = 0;
      
	    for (int i=0; i<people.size(); i++) 
	    	glLevel += people.get(i).getLevel();
	    
	    System.out.println("LOG::INFO - Level per team: " + glLevel / teams);
      
	    for (int i=0; i<athletes; i++) {
	    	if (people.get(i).getSex().equals("0"))
	    		ladiesNum++;
	    }
	    Iterator<People> iter;
	    int ladiesPerTeam = ladiesNum / teams;
	    System.out.println("LOG::INFO - Teams: " + athletes + " Ladies per team: " + ladiesPerTeam);
      
	   // List<List<People>> glList = new ArrayList<List<People>>();
	    Team glTeamobj;
	    List<Team> glTeam = new ArrayList<Team>();
	    
	    /** --------------- **/
	    /** ENTER THE TEAMS **/
	    /** --------------- **/
	    for (int i=0; i<teams; i++) {
	    	glTeamobj = new Team();
	    	iter = people.iterator();
	    	List<People> list = new ArrayList<People>();
	    	String prevBox = "";
	    	int level = 0;
    	  
	    	/*Enter the ladies first. */
	    	while(iter.hasNext()){
	    		People p = iter.next();
	    		if (p.getSex().equals("0")){
	    			if (!prevBox.equals(p.getBox())) {
	    				prevBox = p.getBox();
	    				list.add(p);
	    				iter.remove();
	    				level += p.getLevel();
	    			}
	    		}	
	    		if (list.size() == ladiesPerTeam)
	    			break;
        	}

    	  	iter = people.iterator();
    	  	/*Enter the guys.*/
    	  	while(iter.hasNext()){
    	  		People p = iter.next();
    		  	if (p.getSex().equals("1")){
    		  		if (!prevBox.equals(p.getBox())) {
    		  			prevBox = p.getBox();
    		  			list.add(p);
    		  			iter.remove();
    		  			level += p.getLevel();
    		  		}
    		  	}
        	  
    		  	if (list.size() == number)
    		  		break;
    	  	}
    	  
    	  	//printLists(list);
    	  
    	  	glTeamobj.people = list;
    	  	glTeamobj.level = level;
    	  	glTeam.add(glTeamobj);
    	  	//glList.add(list);
	    }
	    
	    /** PRINT THE RESULTS **/
	    
	    System.out.println("--PEOPLESTART--");
	  	for (int l=0;l<people.size();l++)
		  	System.out.println(people.get(l).Name +" "+people.get(l).Box +" "+people.get(l).Sex);
	  	System.out.println("--PEOPLEEND--");
	  	
	  	
	  	Iterator<Team> it = glTeam.iterator();
	  	while(it.hasNext()){
	  		Team t = it.next();
	  		printLists(t.people);
	  		System.out.println("Levelof team: " + t.getLevel());
	  	}
	  	
	  	int count = -1;
	  	Team previousTeam = null;
	  	it = glTeam.iterator();
	  	
	  	while(it.hasNext()){
	  		count++;
	  		if (count == 0) {
	  			previousTeam = it.next();
	  			continue;
	  		}
	  		Team thisTeam = it.next();
	  		
	  		
	  		while(Math.abs(thisTeam.level - previousTeam.level) >= 3){
	  			
	  			if (thisTeam.level > previousTeam.level) {
	  				
	  				People strong = getStrongestName(thisTeam.people);
	  				People weak = getWeakestName(previousTeam.people);
	  				
	  				/*Remove strong from strong team and add a weak one. */
	  				Iterator<People> itr = thisTeam.people.iterator();
	  				while(itr.hasNext()) {
	  					People p = itr.next();
	  					if (p.equals(strong)) {
	  						itr.remove();
	  					}
	  				}
	  				
	  				thisTeam.people.add(weak);
	  				thisTeam.level = calcNewLevel(thisTeam.people);
	  				
	  				/*Remove weak from weak team and add a strong one. */
	  				itr = previousTeam.people.iterator();
	  				while(itr.hasNext()) {
	  					People p = itr.next();
	  					if (p.equals(weak)) {
	  						itr.remove();
	  					}
	  				}
	  				
	  				previousTeam.people.add(strong);
	  				previousTeam.level = calcNewLevel(previousTeam.people);
	  				
	  			} else {
	  				
	  				People strong = getStrongestName(previousTeam.people);
	  				People weak = getWeakestName(thisTeam.people);
	  				
	  				/*Remove strong from strong team and add a weak one. */
	  				Iterator<People> itr = previousTeam.people.iterator();
	  				while(itr.hasNext()) {
	  					People p = itr.next();
	  					if (p.equals(strong)) {
	  						itr.remove();
	  					}
	  				}
	  				
	  				previousTeam.people.add(weak);
	  				previousTeam.level = calcNewLevel(previousTeam.people);
	  				
	  				/*Remove weak from weak team and add a strong one. */
	  				itr = thisTeam.people.iterator();
	  				while(itr.hasNext()) {
	  					People p = itr.next();
	  					if (p.equals(weak)) {
	  						itr.remove();
	  					}
	  				}
	  				
	  				thisTeam.people.add(strong);
	  				thisTeam.level = calcNewLevel(thisTeam.people);
	  				
	  			}
	  		}
	  		
	  		previousTeam = thisTeam;
	  		
	  	}
	  	/***
	  	it = glTeam.iterator();
	  	Team weakTeam = new Team();
	  	Team strongTeam = new Team();
	  	boolean weakFound = false;
	  	boolean strongFound = false;
	  	while(it.hasNext()){
	  		
	  		Team thisTeam = it.next();
	  		if (thisTeam.level - glLevel / teams < -2 ) {
	  			weakTeam = thisTeam;
	  			weakFound = true;
	  		}
	  		
	  		if (thisTeam.level - glLevel / teams > 2 ) {
	  			strongTeam = thisTeam;
	  			strongFound = true;
	  		}
	  		
	  		if (weakFound && strongFound) {
	  			People strong = getStrongestName(strongTeam.people);
  				People weak = getWeakestName(weakTeam.people);
  				
  				/*Remove strong from strong team and add a weak one. 
  				Iterator<People> itr = strongTeam.people.iterator();
  				while(itr.hasNext()) {
  					People p = itr.next();
  					if (p.equals(strong)) {
  						itr.remove();
  					}
  				}
  				
  				strongTeam.people.add(weak);
  				strongTeam.level = calcNewLevel(strongTeam.people);
  				
  				/*Remove weak from weak team and add a strong one. 
  				itr = weakTeam.people.iterator();
  				while(itr.hasNext()) {
  					People p = itr.next();
  					if (p.equals(weak)) {
  						itr.remove();
  					}
  				}
  				
  				weakTeam.people.add(strong);
  				weakTeam.level = calcNewLevel(weakTeam.people);
	  		}
	  	}***/
	  	
	  	it = glTeam.iterator();
	  	int num = 0;
	  	while(it.hasNext()){
	  		num++;
	  		Team t = it.next();
	  		System.out.println("------TEAMSTART-------"+num);
	  		printLists(t.people);
	  		System.out.println("Levelof team: " + t.getLevel());
	  	}
	  
      	workbook.close();
	}
	
	

	private static int calcNewLevel(List<People> people) {
		int level = 0;
		for (int i=0; i<people.size(); i++)
			level += people.get(i).Level;
		return level;
	}



	private static void printLists (List<People> list) {
		System.out.println("------TEAMSTART-------");
		for(int i=0;i<list.size();i++){
			People p = list.get(i);
			System.out.println(p.getName() + " - " + p.getBox() + " - " + p.getSex() + " - " + p.getLevel());
		}
		System.out.println("------TEAMEND-------");
   	}
	
	private static void getRestPeople (int num) {
	   
	}
   
	private static People getStrongestName (List<People> list) {
		for (int i=0; i<list.size(); i++)
			if (list.get(i).Level == 3)
				return list.get(i);
		return null;
	}
	
	private static People getWeakestName (List<People> list) {
		for (int i=0; i<list.size(); i++)
			if (list.get(i).Level == 1)
				return list.get(i);
		return null;
	}
}