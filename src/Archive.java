import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;


public class Archive {
	private List<String> archive;
	private String archiveName = null;
	private Boolean opened;
	
	public Archive(String archiveName) {
		this.archiveName = archiveName + ".txt";
		this.opened = false;
		this.archive = new ArrayList<String>();
		try {
			this.validate();
		}catch(RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean getOpened() {
		return(this.opened);
	}
	
	private void validate(){
		if(!this.archiveName.contains(".ed2") && !this.archiveName.contains(".ED2")) {
			throw new RuntimeException("Formato inválido!");
		}
	}
	
	public List<String> readArchive() throws IOException{
		BufferedReader buffRead = new BufferedReader(new FileReader(this.archiveName));
		String line = "";
	    while ((line = buffRead.readLine()) != null) {
	        this.archive.add(line);
	    }
		buffRead.close();
		this.opened = true;
		return(this.archive);
	}
	
	public void saveArchive(List<String> archive) throws IOException {
		Scanner input = new Scanner(System.in);
		String name;
		System.out.println("Digite o nome do arquivo .ED2:");
		name = input.nextLine();
		name = name + ".ED2";
		File file = new File(name);
		
		if(name.contains(" ")) {
			input.close();
			throw new IOException("Arquivo não pode conter espaços no nome!");
		}
		
		if(file.exists()) {
			input.close();
			throw new IOException("Arquivo já existente no diretório");
		}
		
		if(this.opened==true) {
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(name+".ED2"));
			for(int i=0;i<archive.size();i++) {
				buffWrite.append(archive.get(i) + "\n");
			}
			buffWrite.close();
		}else {
			input.close();
			throw new IOException("Nenhum arquivo aberto até o momento!");
		}
		input.close();
	}
}
