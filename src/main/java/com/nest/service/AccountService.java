package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.common.util.PasswordUtil;
import com.nest.domain.Account;
import com.nest.domain.AccountStatus;
import com.nest.domain.LoginHistory;
import com.nest.dto.JoinDto;
import com.nest.dto.ProfileDto;
import com.nest.dto.mapper.AccountMapper;
import com.nest.repository.AccountRepository;
import com.nest.dto.VerifyAccountDto;
import com.nest.repository.LoginHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class AccountService {

    private final AccountMapper accountMapper ;

    @Value("${app.base-url}")
    private String baseUrl;
    @Autowired
    private JavaMailSender mailSender;

    private final AccountRepository accountRepository;
    private final FileStorageService fileStorageService;
    private final LoginHistoryRepository loginHistoryRepository;
    private PasswordUtil passwordUtil ;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          LoginHistoryRepository loginHistoryRepository,
                          AccountMapper accountMapper,
                          PasswordUtil passwordUtil,
                          FileStorageService fileStorageService,
                          FollowService followService) {
        this.accountRepository = accountRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.passwordUtil = passwordUtil;
        this.accountMapper = accountMapper;
        this.fileStorageService = fileStorageService;
    }


    //계정 검증 및 생성
    @Transactional
    public void createAccount(JoinDto JoinDto){
        List<String> forbiddenPasswords = List.of("123456", "password", "qwerty", "abc123", "letmein", "1q2w3e4r", "123123","PassW0rd" ,"!Q2w3e4r");

        // 이메일 중복 체크
        if(isDuplicateEmail(JoinDto.getEmail())){
            throw new IllegalArgumentException(ErrorMessages.DUPLICATE_EMAIL);
        }
        // 비밀번호 검증
        String password = JoinDto.getPassword();

        if (password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*") ||
                !password.matches(".*[!@#$%^&*].*")) {
            throw new IllegalArgumentException(ErrorMessages.CONDITION_OF_PASSWORD);
        }

        if (forbiddenPasswords.contains(password.toLowerCase())) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_PASSWORD);
        }

        //사용자 닉네임 검증
        String name = JoinDto.getName();
        boolean exists = isDuplicateName(name);
        if(exists){
            throw new IllegalArgumentException(ErrorMessages.INVALID_NAME);
        }


        // 계정 생성
        Account account = new Account(JoinDto.getEmail(), passwordUtil.encode(JoinDto.getPassword()));
        account.setVerificationToken(UUID.randomUUID().toString());
        account.setVerified(false);
        account.setName(name);
        accountRepository.save(account);

        //이메일 인증 시작
        sendVerificationEmail(account);

    }

    private void sendVerificationEmail(Account account) {
        SimpleMailMessage verifyMail = new SimpleMailMessage();
        verifyMail.setTo(account.getEmail());
        verifyMail.setSubject("이메일 인증");
        verifyMail.setText(String.format(
                "이메일 인증을 하려면 링크를 클릭하세요: %s/auth/verify-email?token=%s",
                baseUrl, account.getVerificationToken()));
        try {
            mailSender.send(verifyMail);
        } catch (MailException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException(ErrorMessages.FAIL_TO_SEND_EMAIL);
        }
    }

    @Transactional
    public boolean verifyAccount(String token) {
        log.info("인증 토큰: {}", token);
        return accountRepository.findByVerificationToken(token)
                .map(account -> activateAccount(account))
                .orElse(false); // 실패 시 false 반환
    }

    private boolean activateAccount(Account account) {
        account.setVerified(true); // 계정 상태 업데이트
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account); // 변경 사항 저장
        return true;
    }


    //계정 정보 수정
    @Transactional
    public ProfileDto updateAccount(ProfileDto profileDto)  {
        Account account = getAccountById(profileDto.getId());

            if(profileDto.getName() != null ){
                account.setName(profileDto.getName());
            }
            if(profileDto.getProfileMessage() != null ){
                account.setProfileMessage(profileDto.getProfileMessage());
            }
            accountRepository.save(account);
            return accountMapper.toProfileDto(account);

    }

    @Transactional
    public ProfileDto updateProfileImage(Long accountId, MultipartFile file) {
        if(file == null && file.isEmpty()){
            throw new IllegalArgumentException(ErrorMessages.ATTACHED_FILE_ISNULL);
        }
        Account account = getAccountById(accountId);
        String savedPath = fileStorageService.saveProfileImage(account.getEmail(), file);
        account.setProfileImgPath(savedPath);
        accountRepository.save(account);

        return accountMapper.toProfileDto(account);

    }





    //회원 탈퇴
    @Transactional
    public void withDrawAccount(Long accountId) {

        Account account = getAccountById(accountId);

        log.info("계정 삭제 요청: {}", account.getEmail());

        account.setName("탈퇴한 사용자");
        account.setVerified(false);
        account.setProfileMessage("");
        account.setProfileImgPath("");
        account.setEmail("deleted_account_<"+account.getEmail()+">");
        accountRepository.save(account);

        account.setStatus(AccountStatus.DELETED);

        accountRepository.save(account);

    }

    public void loginAuthenticate(String email, String rawPassword){
        Account findAccount = accountRepository.findByEmail(email).
                orElseThrow(()-> new IllegalArgumentException(ErrorMessages.WRONG_PASSWORD));
        if(!findAccount.isVerified() || !findAccount.getStatus().equals(AccountStatus.ACTIVE)){
            log.info("계정 인증 여부 : {}", findAccount.isVerified());
            log.info("계정 상태 : {}", findAccount.getStatus());

            throw new IllegalArgumentException("인증 되지 않았거나 , 사용할 수 없는 계정입니다. ");
        }
        if(!passwordUtil.matches(rawPassword, findAccount.getPassword())){
            throw new IllegalArgumentException(ErrorMessages.WRONG_PASSWORD);
        }
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setAccount(findAccount);
        loginHistory.setLoginDateTime(LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ACCOUNT_NOT_FOUND));
    }

    public boolean isDuplicateEmail (String email){
        String lowerCaseEmail = email.toLowerCase();
        log.info("이메일 중복 확인 {}", lowerCaseEmail );
        return accountRepository.existsByEmail(email);
    }

    public boolean isDuplicateName(String name){
        log.info("닉네임 중복 확인 {}", name );
        return accountRepository.existsByName(name);
    }

    public Long getIdByEmail(String email){
        return getAccountByEmail(email)
                .getId();
    }
    public ProfileDto getProfileById (Long id){
        Account account = getAccountById(id);
        return accountMapper.toProfileDto(account);
    }

    private Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ACCOUNT_NOT_FOUND));
    }


}
