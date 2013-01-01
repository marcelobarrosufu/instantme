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
        "InstantME V1.0.0. Desenolvido por Shining Bits.\nSuporte: support@shiningbits.com",
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
        "Atenção",
        "Esse aplicativo acessa suas fotos, comentários, curtidas e amigos em seu nome mas" +
            " seu usuário e senha não serão armazenados neste telefone." +
            "\nVocê também deve ter uma conta Instagram válida antes de usar este programa. " +
            "Use um telefone Android ou iPhone para criá-la.",
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
        "abrir"
            
        };
    
    public String getStr(int id) {
        if(id < 0 || id >= texts.length)
            return "INDEF";  
        else
            return texts[id];
    }
}
