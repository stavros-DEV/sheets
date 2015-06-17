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
		private int number;
		private List<People> getPeople() {
			return people;
		}
		private void setPeople(List<People> people) {
			this.people = people;
		}
		private int getNumber() {
			return number;
		}
		private void setNumber(int number) {
			this.number = number;
		}
	}

	private static class People {
		private String Name;
		private String Box;
		private String Sex;
		
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
	
	private static int number = 6;
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
    	  }
    	  people.add(athlet);
      }
      int athletes = people.size();
      System.out.println("LOG::INFO - Athletes to be processed: " + athletes);
    
      int teams = athletes / number;
      
      getRestPeople(athletes % number);
      
      int ladiesNum = 0;
      
      for (int i=0; i<athletes; i++) {
    	  if (people.get(i).getSex().equals("0"))
    		  ladiesNum++;
      }
      Iterator<People> iter;
      int ladiesPerTeam = ladiesNum / teams;
      System.out.println("LOG::INFO - Teams: " + athletes + " Ladies per team: " + ladiesPerTeam);
      
      List<List<People>> glList = new ArrayList<List<People>>();

      for (int i=0; i<teams; i++) {
    	  iter = people.iterator();
    	  List<People> list = new ArrayList<People>();
    	  String prevBox = "";
    	  
    	  /*Enter the ladies first. */
    	  while(iter.hasNext()){
    		  People p = iter.next();
        	  if (p.getSex().equals("0")){
        		  if (!prevBox.equals(p.getBox())) {
        			  prevBox = p.getBox();
        			  list.add(p);
        			  iter.remove();
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
        		  }
        	  }
        	  
        	  if (list.size() == number)
        		  break;
    	  }
    	  
    	  
    	  
    	 printLists(list);
    	  System.out.println("++++++++++++++");
    	  
    	  glList.add(list);
      }
      System.out.println("--PEOPLESTART--");
	  for (int l=0;l<people.size();l++)
	  {System.out.println(people.get(l).Name +" "+people.get(l).Box +" "+people.get(l).Sex);
	  }
	  System.out.println("--PEOPLEEND--");
      workbook.close();
   }
   private static void printLists(List<People> list) {
	   System.out.println("------TEAMSTART-------");
	   for(int i=0;i<list.size();i++){
		   People p = list.get(i);
		   System.out.println(p.getName() + " - " + p.getBox() + " - " + p.getSex());
	   }
	   System.out.println("------TEAMEND-------");
   	}
private static void getRestPeople(int num) {
	   
   }
   
}