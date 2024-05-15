import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Archive {
	private List<String> archive;
	private String archiveName = null;
	private Boolean opened;
	
	public Archive(String archiveName) {
		this.archiveName = archiveName;
		this.opened = false;
		this.archive = new ArrayList<String>();
		try {
			this.validate(this.archiveName);
		}catch(RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getOpened() {
		return(this.opened);
	}
	
	private void validate(String archiveName){
		if(!archiveName.contains(".ed2") || !archiveName.contains(".ED2")) {
			throw new RuntimeException("Formato inválido!");
		};
	}
	
	public List<String> readArchive(String archiveName) throws IOException{
		BufferedReader buffRead = new BufferedReader(new FileReader(archiveName));
		String line = "";
		while (true) {
			if (line != null) {
				line = buffRead.readLine();
				this.archive.add(line);
			} else
				break;
		}
		buffRead.close();
		this.opened = true;
		return(this.archive);
	}
	
	public void saveArchive(List<String> archive) throws IOException {
		if(this.opened==true) {
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(this.archiveName));
			for(int i=0;i<archive.size();i++) {
				buffWrite.append(archive.get(i) + "\n");
			}
			buffWrite.close();
		}
	}
}
