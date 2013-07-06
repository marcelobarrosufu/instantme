/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.locales;

public class LocalePT_BR implements ILocale {
    
    private String texts[] = { 
        "ok", 
        "voltar", 
        "sair",
        "sobre",
        "InstantME V1.0.5. Desenvolvido por Shining Bits.\nSuporte: support@shiningbits.com",
        "atualizar",
        "Aguarde",
        "Por favor, aguarde o final da operação.",
        "Info. do Usuário",
        "Atualiz. seguidos ...",
        "Falha",
        "Impossível atualizar. Por favor, tente novamente.",
        "Atualizando seguidores ...",
        "Logando - Passo 1/4 ...",
        "Logando - Passo 2/4 ...",
        "Logando - Passo 3/4 ...",
        "Logando - Passo 4/4 ...",
        "Requisitando ...",
        "Baixando info. usuário ...",
        "Baixando imagem usuário ...",
        "Baixando foto ",
        "s",
        "m",
        "h",
        "d",    
        "Usuário",
        "Senha",
        "Política de Privacidade",
        "Esse aplicativo acessa suas fotos, comentários, curtidas e amigos em seu nome mas" +
            " seu usuário e senha não serão armazenados neste telefone." +
            "\nVocê também deve ter uma conta Instagram válida antes de usar este programa. " +
            "Use um telefone Android ou iPhone/iPad para criá-la.",
        "cancelar",
        "logar",
        "Erro",
        "Usuário ou senha não podem ser vazios!",
        "Logando ...",
        "Sucesso",
        "Logado. Use o menu atualizar para atualizar a linha do tempo",
        "Impossível logar. Verifique seu usuário/senha e conexão de rede.",
        "Erro ao baixar",
        "Curtidas",
        "Curtidas (Você)",
        "Comentários",
        "curtir",
        "adic. coment.",
        "apagar coment.",
        "apagar",
        "enviar",
        "limpar",
        "Você não pode apagar nenhum comentário neste post.",
        "Nenhum comentário para apagar",
        "Comentário",
        "Atualiz. comentários ...",
        "Apag. comentários ...",
        "Adicionar curtir ...",
        "Remover curtir ...",
        "Adic. comentário",
        "Impossível carregar comentários. Tente de novo.",
        "Erro ao adiconar comentário. Tente de novo.",
        "Erro ao apagar comentário. Tente de novo.",
        "mais antigas",
        "mais novas",
        "eu",
        "amigos",
        "Sobre",
        "Por favor, logue primeiro.",
        "Minha Info.",
        "Por favor, use o menu login para colocar suas credenciais de acesso ao Instagram.",
        "Falha no login. Reinicie a aplicação ou coloque suas credenciais de acesso no menu login.",
        "Atualizando ...",
        "Amigos",
        "seguindo",
        "seguidores",
        "Seguindo",
        "Seguidores",  
        "Bio",
        "Site",
        "Imagens",
        "Impossível atualizar (dados privados?). Por favor, tente de novo.",
        "Carregando ...",
        "Analisando ...",
        "InstantME",
        "Shining Bits",
        "abrir",
        "ajuda",
        "Ajuda",
        "Você deve ter uma conta Instagram válida para usar este aplicativo. " +
            "Ela pode ser criada através de um telefone Android ou iPhone.\n\n" +
            "Com sua informação de conta, use o menu 'logar' para entrar na rede do Instagram.\n\n" +
            "Você pode navegar usando as opções 'mais antigas' e 'mais novas' e ver os seguidores/seguindo " +
            "usando a opção 'amigos'.\n\nOs detalhes da sua conta podem ser vistos através da opção 'eu'.\n\n" +
            "Finalmente, a opção 'atualizar' mostra o que existe de mais novo na sua linha do tempo."
        };
    
    public String getStr(int id) {
        if(id < 0 || id >= texts.length)
            return "INDEF";  
        else
            return texts[id];
    }
}
