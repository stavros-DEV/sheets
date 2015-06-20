import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class CalcWorkouts {

	@SuppressWarnings("unused")
	private static class Team {
		private String Name;
		private List<Integer> score;
		private int finalScore;
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public List<Integer> getScore() {
			return score;
		}
		public void setScore(List<Integer> score) {
			this.score = score;
		}
		public int getFinalScore() {
			return finalScore;
		}
		public void setFinalScore(int finalScore) {
			this.finalScore = finalScore;
		}
		
	}

	public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException {
		
		Workbook workbook = Workbook.getWorkbook(new File("hello.xls"));
		Sheet sheet = workbook.getSheet(0);
		int cols = sheet.getColumns();
		int rows = sheet.getRows();
		
		System.out.println("LOG::INFO - Spreadsheet Info (Rows): " + rows);
		System.out.println("LOG::INFO - Spreadsheet Info (Columns): " + cols);
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		List<Integer> score = new ArrayList<Integer>();
		
		
		for (int i=1; i<cols; i=i+2){
			
			List<Integer> initScore = new ArrayList<Integer>();
			for (int j=1; j<rows; j++){
				
				Cell cell = sheet.getCell(i, j);
				initScore.add(Integer.parseInt(cell.getContents()));
			}
			
			score = getScoresToSheet(initScore);
			
			System.out.println("LOG::INFO - WO"+i);
			for (int k=0; k<initScore.size(); k++){
				Cell cell = sheet.getCell(0, k+1);
				System.out.println(cell.getContents()+": "+initScore.get(k)+" - "+score.get(k));
			}
			list.add(score);
			System.out.println("----");
		}
		
		List<Integer> rvlist = new ArrayList<Integer>();
		
		for (int i=0; i<list.get(0).size();i++){//3 times(teams)
			
			int scrPerTeam = 0;
			for (int j=0;j<list.size();j++){
				scrPerTeam += list.get(j).get(i);
			}
			rvlist.add(scrPerTeam);
		}
		for (int k=0; k<rvlist.size(); k++){
			Cell cl = sheet.getCell(0, k+1);
			System.out.println("Team "+cl.getContents()+": "+rvlist.get(k));
		}
	}

	private static List<Integer> getScoresToSheet(List<Integer> initScore) {
		List<Integer> temp = new ArrayList<Integer>();
		for (int i=0; i<initScore.size(); i++){
			temp.add(initScore.get(i));
		}
		Collections.sort(temp);
		
		List<Integer> res = new ArrayList<Integer>();
		for (int i=0; i<initScore.size(); i++){
			int init = initScore.get(i);
			for (int j=0; j<temp.size(); j++){
				int tmp = temp.get(j);
				if (j+1<temp.size() && init == temp.get(j+1)) {
					continue;
				}
				if (tmp == init){
					res.add(j+1);
					break;
				}
			}
		}
		return res;
	}
}