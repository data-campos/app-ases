package com.spin.ops.security;

public class SecurityConstants {
	
	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 dias
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/login";
    public static final String SIGN_UP_PASSWORD_SOLICITA = "/beneficiario/solicitar-alteracao-senha";
    public static final String VALIDATE_HASH = "/beneficiario/validar-hash";
    public static final String EXCLUDE_ACCOUNT_VALIDATE_HASH = "/usuario/excluir-conta/validar-hash";
    public static final String SIGN_UP_PASSWORD = "/beneficiario/alterar-senha";
    public static final String SIGN_UP_BOLETO = "/boleto/{nr_titulo}/gerar";
	public static final String SIGN_UP_CREATE = "/beneficiario/novo";
    public static final String SIGN_UP_GUIA_MEDICO_GET = "/guia/lista-especialidade";
    public static final String SIGN_UP_GUIA_MEDICO_POST = "/guia/listar";
}
