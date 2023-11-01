package br.com.acaboumony.account.dto.response;

import java.util.UUID;

public record EmailDTO (UUID userId, String emailTo, String code2FA){}
