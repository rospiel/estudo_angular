package com.example.algamoney.api.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Usuario;

/**
 * Classe de envio dos e-mails
 * @author Rodrigo
 *
 */
@Component
public class Mailer {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine trymeleaf;
	
	/*
	@Autowired
	private LancamentoRepository lancamentoRepository; 
	
	
	@EventListener
	private void teste(ApplicationReadyEvent evento) {
		String template = "mail/aviso-lancamentos-vencidos";
		
		List<Lancamento> lista = lancamentoRepository.findAll();
		
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", lista);
		
		this.enviarEmail("emailAqui@gmail.com", Arrays.asList("emailEnvioAqu@hotmail.com"), "Testando", template, variaveis);
		System.out.println("terminado o envio de email");
	}*/
	
	/**
	 * Configura o email e concretiza seu envio
	 * @param remetente
	 * @param destinatarios
	 * @param assunto
	 * @param mensagem
	 */
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException erro) {
			throw new RuntimeException("Problemas com o envio de e-mail.", erro);
		}
	}
	
	/**
	 * Processa o template html da mensagem e procede com o envio
	 * @param remetente
	 * @param destinatarios
	 * @param assunto
	 * @param template
	 * @param variaveis
	 */
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = trymeleaf.process(template, context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	/**
	 * Recupera a relação de destinatarios e os respectivos lançamentos vencidos e prossegue com o envio
	 * @param vencidos
	 * @param destinatarios
	 */
	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<String, Object>();
		variaveis.put("lancamentos", vencidos);
		
		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		
		this.enviarEmail("emailAqui@gmail.com", emails, "Lançamentos Vencidos", "mail/aviso-lancamentos-vencidos", variaveis);
	}
}
