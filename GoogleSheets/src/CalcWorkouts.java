import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
		List<Integer> score = new ArrayList<Integer>();

		
		/*WritableWorkbook copy = Workbook.createWorkbook(new File("temp.xls"), workbook);
		WritableSheet sh = copy.createSheet("First Sheet", 0);*/
		WritableWorkbook wb = Workbook.createWorkbook(new File("assaas.xls"));
		WritableSheet sh = wb.createSheet("First Sheet", 0);
		for (int i=1; i<cols; i=i+2){
			
			List<Integer> initScore = new ArrayList<Integer>();
			for (int j=1; j<rows; j++){
				
				Cell cell = sheet.getCell(i, j);
				initScore.add(Integer.parseInt(cell.getContents()));
			}
			
			score = getScoresToSheet(initScore);
			
			for (int k=0; k<initScore.size(); k++){
				//System.out.println(initScore.get(k) + " - " + score.get(k));
				
				Label l = new Label(i+1, k+1, score.get(k).toString());
				
				sh.addCell(l);
				l = new Label(0, 0, "sadsd");
				sh.addCell(l);
			}
			System.out.println("----");
			
		}
		
		for (int i=1; i<cols; i++){
			
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
;					break;
				}
			}
		}
		
		return res;
	}

}
