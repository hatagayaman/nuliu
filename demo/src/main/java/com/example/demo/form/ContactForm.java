package com.example.demo.form;

import org.hibernate.validator.constraints.NotBlank;

public class ContactForm {

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@NotBlank(message="名前を入力してください")
	private String name = "";

	@NotBlank(message="メールアドレスを入力してください")
	private String email = "";

	@NotBlank(message="内容を入力してください")
	private String body = "";

	@NotBlank(message="件名を入力してください")
	private String subject = "";
}
