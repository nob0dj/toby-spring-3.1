package springbook.user.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService implements UserLevelUpgradePolicy {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * PlatformTransactionManager란 긴 이름이 붙은 이유는 JTA에 있는 TransactionManager인터페이스와 혼동을
	 * 방지하기 위함이다. 너무 긴 이름이라 관레적으로 짧게 줄여 setter를 설정한다.
	 */
	private PlatformTransactionManager txManager;

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	/**
	 * Spring이 지원하는 MailSender인터페이스 구현체
	 */
	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 서비스 추상화 적용
	 * 
	 * 특정 트랜잭션 방법에 의존적이지 않고 독립적으로 작동해야한다. JDBC, JTA, Hibernate, JPA, JDO, JMS의 공통적으로
	 * PlatformTransactionManager를 구현하고 있다. - JDBC : DataSourceTxManager -
	 * Connection - JTA : DataSourceTxManager - UserTransaction - Hibernate :
	 * HibernateTxManager - Transaction
	 * 
	 * 
	 * <img src="https://d.pr/i/Lqn88C+" alt="" width="100%"/>
	 * 
	 */
	public void upgradeLevels() throws Exception {
//		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource); // JDBC
//		PlatformTransactionManager txManager = new JTATransactionManager(dataSource); // JTA

		// 트랜잭션 경계시작 : DB Connection객체 가져오는 작업역시 자동으로 처리된다.
		// DefaultTransactionDefinition : 트랜잭션 속성 정보
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

		try {
			List<User> users = userDao.getAll();
			for (User user : users) {
				if (canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status);
			throw e;
		}
	}

	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER:
			return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);

		sendUpgradeEmail(user);
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	/**
	 * DI버젼 mail 관련 설정은 xml에서 처리
	 * 
	 * @param user
	 */
	private void sendUpgradeEmail(User user) {
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setTo(user.getEmail());
//		mailMessage.setFrom("shqkel1863@gmail.com");
//		mailMessage.setSubject("등급 업그레이드 공지");
//		mailMessage.setText("사용자의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
//		this.mailSender.send(mailMessage);

		
		//JavaMailSender타입은 MimeMessage객체를 생성할 수 있다. html메세지 전송가능
		JavaMailSender mailSender = (JavaMailSender) this.mailSender;
		MimeMessage mailMessage = mailSender.createMimeMessage();
		try {
			mailMessage.setSubject("등급 업그레이드 공지", "UTF-8");
			mailMessage.setText("사용자의 등급이 <strong>" + user.getLevel().name() + "</strong>로 업그레이드 되었습니다.", "UTF-8", "html");
			mailMessage.setFrom(new InternetAddress("shqkel1863@gmail"));
			mailMessage.addRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		mailSender.send(mailMessage);
	}

	/**
	 * Spring이 제공하는 JavaMail추상화 인터페이스 MailSender 이용버젼(DI사용안함)
	 * 
	 * MailSender(interface) - JavaMailSender(interface) -
	 * JavaMailSenderImpl(class)구현객체 사용
	 * https://gangzzang.tistory.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81Spring-MainlSender-JavaMailSender-%EB%A9%94%EC%9D%BC-%EB%B0%9C%EC%86%A1
	 * https://gist.github.com/ihoneymon/56dd964336322eea04dc
	 * 
	 * org.springframework.context.support-3.0.7.REALEASE.jar 참조
	 * 
	 * @param user
	 */
	private void sendUpgradeEmailWithJavaMailSenderImpl(User user) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(25);
		mailSender.setDefaultEncoding("utf-8");
		mailSender.setUsername("shqkel1863@gmail.com");
		mailSender.setPassword("wa0a9a0a7a");

		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.starttls.enable", "true"); // 오류방지 com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must
														// issue a STARTTLS command first. ha8sm14871508pjb.6 - gsmtp
		mailSender.setJavaMailProperties(prop);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("shqkel1863@gmail.com"); // 관리자 이메일
		mailMessage.setSubject("등급 업그레이드 공지");
		mailMessage.setText("사용자의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

		mailSender.send(mailMessage);
	}

	/**
	 * JavaMail 이용버젼 JavaMail은 확장, 지원이 불가한 악명높은 api중 하나이다. 상속을 통한 테스트대역을 만들수 없다.
	 * 
	 * 아래 jar 참조설정할 것. com.springsource.javax.mail-1.4.0.jar
	 * com.springsource.javax.activation-1.1.0.jar
	 * 
	 * Properties - Session - MimeMessage - Transport 흐륾으로 전개된다.
	 * 
	 * @param user
	 */
	private void sendUpgradeEmailWithJavaMail(User user) {
		final String sender = "shqkel1863@gmail.com"; // 네이버일 경우 네이버 계정, gmail경우 gmail 계정
		final String password = "wa0a9a0a7a"; // 패스워드

		// SMTP 서버 정보를 설정한다.
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// 오류방지 com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a
		// STARTTLS command first. ha8sm14871508pjb.6 - gsmtp
		prop.put("mail.smtp.starttls.enable", "true");

		// final class javax.mail.Session 상속불가
		// 생성자는 모두 private이라 static 메소드를 통한 객체 생성만 가능
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			// 수신자메일주소
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			// 제목
			message.setSubject("등급 업그레이드 공지");
			// 내용
			message.setText("사용자의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
			// 전송
			Transport.send(message);
			System.out.println("message sent successfully...");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
