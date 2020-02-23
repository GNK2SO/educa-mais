package br.com.projeto.educamais.util.messages;

public class AtividadeErrors {

	public static final String NOT_FOUND = "Falha ao obter atividade. Atividade não está cadastrada.";
	public static final String FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_TURMA = "A atividade informada não pertence a turma informada.";
	public static final String FORBIDDEN_ATIVIDADE_DESABILITADA = "Esta atividade não está habilitada.";
	public static final String FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_ALUNO = "A atividade não pertence ao aluno informado.";
	public static final String FORBIDDEN_SALVAR_ATIVIDADE = "Apenas o professor tem permissão para cadastrar atividades.";
	public static final String FORBIDDEN_PROFESSOR_SUBMIT_RESPOSTA = "Apenas o aluno tem permissão para submeter as respostas da atividade.";
	
}
