package org.casadeguara.utilitarios;

import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;

public class Backup {

	private static final String POSTGRES_PATH = "C:\\Program Files\\PostgreSQL\\10\\bin\\pg_dump.exe";

	public void generateOnBackground(String filename, String extension) {
		Task<Void> backgroundProcess = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {
					File diretorio = criarDiretorioBackup();
					File arquivo = criarArquivoBackup(diretorio, filename, extension);
					gerarBackup(arquivo);
				} catch (IOException ex) {
					// TODO Inserir Log
				}
				return null;
			}
		};

		new Thread(backgroundProcess).start();
	}

	public File criarDiretorioBackup() {
		File diretorio = new File("backup" + File.separator);
		diretorio.mkdir();
		return diretorio;
	}

	public File criarArquivoBackup(File parent, String nomeArquivo, String extensao) {
		return new File(parent, nomeArquivo + extensao);
	}

	public int gerarBackup(File arquivo) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(POSTGRES_PATH, "--host=localhost", "--port=5432", "--username=postgres",
				"--no-password", "--format=custom", "--blobs", "--verbose", "--file=" + arquivo.getAbsolutePath(),
				"biblioteca");

		pb.environment().put("PGPASSWORD", "j3RdOTZ9");
		pb.redirectErrorStream(true);
		Process process = pb.start();

		return process.exitValue();
	}

}
