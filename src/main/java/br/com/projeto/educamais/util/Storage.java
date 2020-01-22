package br.com.projeto.educamais.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.exception.FalhaUploadArquivoException;

@Component
public class Storage {

	public static final String DIR_UPLOADS = "/arquivos/";
	
	public List<Arquivo> upload(MultipartFile[] arquivos) {
		
		List<Arquivo> arquivosSalvos = new ArrayList<Arquivo>();
		
		for (MultipartFile arquivo : arquivos) {
			
			Arquivo arquivoSalvo = salvar(arquivo);
			
			if(arquivoSalvo != null) {
				arquivosSalvos.add(arquivoSalvo);
			}
		}
		
		return arquivosSalvos;
	}
	
	public String getNomeArquivo(MultipartFile arquivo) {
		return String.format("%d-%s", new Date().getTime(), arquivo.getOriginalFilename());
    }

	
	public Arquivo salvar(MultipartFile arquivo) {
		if (arquivo != null && arquivo.getSize() > 0) {
		
		    File diretorio = new File(System.getProperty("user.home") + DIR_UPLOADS);
		    String nomeArquivo = getNomeArquivo(arquivo);
		
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			
			try {
			
				File serverFile = new File(diretorio + File.separator + nomeArquivo);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(arquivo.getBytes());
				stream.close();
				return new Arquivo(nomeArquivo);
				
			} catch (IllegalStateException | IOException e) {
				throw new FalhaUploadArquivoException();
		    }
		}
		
		return null;
	}


}
