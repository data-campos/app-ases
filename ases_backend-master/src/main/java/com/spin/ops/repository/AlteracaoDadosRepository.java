package com.spin.ops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spin.ops.model.InterfacesJPA.AlteracaoDados;
import com.spin.ops.model.InterfacesJPA.ListaAlteracao;
import com.spin.ops.model.Usuario;

public interface AlteracaoDadosRepository extends JpaRepository<Usuario, Long> {

	@Query(value = 	"select     \r\n" + 
			"        p.nm_pessoa_fisica,     \r\n" + 
			"        p.dt_nascimento,  \r\n" + 
			" 		 p.ie_estado_civil, "+
			"        substr(tasy.obter_valor_dominio(5,p.ie_estado_civil),1,80) as ds_estado_civil,\r\n" + 
			"        p.nr_cpf,\r\n" + 
			"        p.nr_cartao_nac_sus,  \r\n" + 
			"        tasy.pls_obter_email_pessoa(p.cd_estabelecimento, p.cd_pessoa_fisica, null, 'TASY') as ds_email,  \r\n" + 
			"        p.nr_identidade,\r\n" + 
			"        p.dt_emissao_ci as dt_emissao_identidade,\r\n" + 
			"        p.sg_emissora_ci,\r\n" + 
			"        p.ds_orgao_emissor_ci,\r\n" + 
			"        p.nr_reg_geral_estrang as nr_cartao_estrangeiro,\r\n" + 
			"        p.nr_cep_cidade_nasc,\r\n" + 
			"        tasy.obter_desc_muni_cep(p.nr_cep_cidade_nasc) nm_cidade_nasc, "+
			"        p.ie_sexo, "+
			"        substr(tasy.obter_valor_dominio(4,p.ie_sexo),1,80) as ds_sexo,\r\n" + 
			"        \r\n" + 
			"        compl.cd_cep,\r\n" + 
			"        compl.nr_endereco,\r\n" + 
			"        compl.ds_endereco,\r\n" + 
			"        compl.ds_bairro,\r\n" + 
			"        compl.cd_municipio_ibge, "+
			"        tasy.obter_desc_municipio_ibge(compl.cd_municipio_ibge) as ds_cidade,\r\n" + 
			"        compl.sg_estado, "+
			"        substr(tasy.obter_valor_dominio(50,compl.sg_estado),1,80) as ds_uf,\r\n" + 
			"        \r\n" + 
			"        compl.nr_ddi_telefone,\r\n" + 
			"        compl.nr_ddd_telefone,\r\n" + 
			"        compl.nr_telefone,\r\n" + 
			"        \r\n" + 
			"        p.nr_telefone_celular,\r\n" + 
			"        p.cd_declaracao_nasc_vivo,\r\n" + 
			"        \r\n" + 
			"        (select pai.nm_contato from tasy.compl_pessoa_fisica pai where pai.ie_tipo_complemento = 4 and pai.cd_pessoa_fisica = p.cd_pessoa_fisica) as nm_pai,\r\n" + 
			"        (select mae.nm_contato from tasy.compl_pessoa_fisica mae where mae.ie_tipo_complemento = 5 and mae.cd_pessoa_fisica = p.cd_pessoa_fisica) as nm_mae\r\n" + 
			"\r\n" + 
			"from    tasy.pessoa_fisica p,  \r\n" + 
			"        tasy.compl_pessoa_fisica compl\r\n" + 
			"where   p.cd_pessoa_fisica = :cd_pessoa_fisica_p \r\n" + 
			"and     compl.cd_pessoa_fisica = p.cd_pessoa_fisica\r\n" + 
			"and     compl.nr_sequencia = (\r\n" + 
			"    select  max(x.nr_sequencia) \r\n" + 
			"    from    tasy.compl_pessoa_fisica x\r\n" + 
			"    where   x.cd_pessoa_fisica = p.cd_pessoa_fisica\r\n" + 
			"    and     x.ie_tipo_complemento = 1\r\n" + 
			")", nativeQuery = true)
	public AlteracaoDados buscarDados(@Param("cd_pessoa_fisica_p") String cd_pessoa_fisica_p);
	
	@Query(value = 	"select  a.nr_sequencia,\r\n" + 
			"    a.dt_atualizacao_nrec,\r\n" + 
			"    decode(a.ie_status, 'A', 'Aguardando ação','N','Negada','L','Liberada') as ds_status,\r\n" + 
			"    c.nm_atributo,\r\n" + 
			"    nvl(c.ds_valor_old,'') as ds_antes,\r\n" + 
			"    nvl(c.ds_valor_new,'') as ds_depois,\r\n" + 
			"    decode(c.ie_status, 'P', 'Pendente','A','Aprovado','R','Reprovado') as ds_status_campo        \r\n" + 
			"from    tasy.tasy_solic_alteracao a, tasy.tasy_solic_alt_campo c\r\n" + 
			"where   a.nr_sequencia = c.nr_seq_solicitacao\r\n" + 
			"and     a.cd_pessoa_fisica = :cd_pessoa_fisica_p \r\n" + 
			"and     a.nm_usuario = 'TASY' \r\n" + 
			"order   by 2 desc, 4 ", nativeQuery = true)
	public List<ListaAlteracao> listar(@Param("cd_pessoa_fisica_p") String cd_pessoa_fisica_p);
	
	
	
}
