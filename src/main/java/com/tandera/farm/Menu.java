/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tandera.farm;

import com.tandera.farm.config.ClienteFachada;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.mdi.client.ClientFacade;
import org.openswing.swing.mdi.client.MDIController;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.permissions.client.LoginController;


import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.internationalization.java.BrazilianPortugueseOnlyResourceFactory;
import org.openswing.swing.internationalization.java.Language;
import org.openswing.swing.mdi.client.Clock;
import org.openswing.swing.mdi.client.GenericStatusPanel;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.permissions.client.LoginDialog;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;

/**
 *
 * @author sergio.feitosa
 */
public class Menu implements MDIController, LoginController {

    private ClienteFachada clientFacade = null;
    private Connection conn = null;
    private Hashtable domains = new Hashtable();
    private String username = null;
    private Properties idiomas = new Properties();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Menu();
            }
        });

    }

    public Menu() {
        createConnection();
        clientFacade = new ClienteFachada(conn);

        Hashtable xmlFiles = new Hashtable();
        //xmlFiles.put("EN", "recursos/Resources_en.xml");
        //xmlFiles.put("IT", "recursos/Resources_it.xml");
        //xmlFiles.put("PT_BR", "recursos/Resources_pt_br.xml");

        idiomas = System.getProperties();

        ClientSettings clientSettings = new ClientSettings(new BrazilianPortugueseOnlyResourceFactory(idiomas, false), new Hashtable());
        /*ClientSettings clientSettings = new ClientSettings(
                new XMLResourcesFactory(xmlFiles, false),
                domains);
        
        new ClientSettings(new BrazilianPortugueseOnlyResourceFactory(idiomas, false), new Hashtable());
        */
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ClientSettings.BACKGROUND = "tandera.jpg";
        ClientSettings.BACK_IMAGE_DISPOSITION = Consts.BACK_IMAGE_CENTERED;
        //ClientSettings.TREE_BACK = "tandera.jpg";
        ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
        ClientSettings.VIEW_MANDATORY_SYMBOL = true;
        ClientSettings.ALLOW_OR_OPERATOR = false;
        ClientSettings.INCLUDE_IN_OPERATOR = false;
        ClientSettings.SHOW_SCROLLBARS_IN_MDI = false;
        ClientSettings.FILTER_PANEL_ON_GRID = true;

        ClientSettings.getInstance().setLanguage("PT_BR");
/*
        idiomas.setProperty("EN", "English");
        idiomas.setProperty("IT", "Italiano");
        idiomas.setProperty("PT_BR", "Português do Brasil");
         */
        LoginDialog d = new LoginDialog(
                null,
                false,
                this,
                "Autenticação",
                "Login",
                'L',
                "Sair",
                'S',
                "Armazenar Informações",
                "Fazenda",
                null
                //idiomas,
               //"PT_BR"
        );


    }

    /**
     * Método chamado após a criação da janela MDI
     *
     * @param frame
     */
    @Override
    public void afterMDIcreation(MDIFrame frame) {
        GenericStatusPanel userPanel = new GenericStatusPanel();
        userPanel.setColumns(12);
        MDIFrame.addStatusComponent(userPanel);
        userPanel.setText(username);
        MDIFrame.addStatusComponent(new Clock());
    }

    /**
     * Veja o método getExtendedState do JFrame
     */
    public int getExtendedState() {
        return JFrame.MAXIMIZED_BOTH;
    }

    /**
     * retorna a fachada (facade) do cliente invocada pelos itens de menu
     */
    @Override
    public ClientFacade getClientFacade() {
        return clientFacade;

    }

    /**
     * Método usado para finalizar a aplicação
     */
    @Override
    public void stopApplication() {
        System.exit(0);
    }

    /**
     * Define se as funções da aplicação devem ser visualizadas dentro de um
     * menu em árvore na janela MDI.
     */
    @Override
    public boolean viewFunctionsInTreePanel() {
        return true;
    }

    /**
     * Define se as funções da aplicação devem ser visualizadas dentro de uma
     * barra de menu na janela MDI.
     */
    @Override
    public boolean viewFunctionsInMenuBar() {
        return true;
    }

    /**
     * Define se deve existir uma opção para mudar o usuário no menu Arquivo
     */
    @Override
    public boolean viewLoginInMenuBar() {
        return true;
    }

    /**
     * Define o título da aplicação
     */
    public String getMDIFrameTitle() {
        return "Tandera-Farm - Sistema para Administração de Fazendas";
    }

    /**
     * Define o texto exibido na janela "sobre"
     */
    @Override
    public String getAboutText() {
        return "Tandera-Farm - Sistema para Administração de Fazendas\n"
                + "\n"
                + "Copyright: Copyright (C) 2018 \n"
                + "Autor: Sergio/Alex \n"
                + "slifebr@hotmail.com";
    }

    /**
     * Define a imagem que aparece na janela "sobre"
     */
    @Override
    public String getAboutImage() {
        return "sobre.jpg";
    }

    /**
     * Define a janela de login que aparece quando a aplicação é iniciada.
     */
    @Override
    public JDialog viewLoginDialog(JFrame parentFrame) {

        JDialog d = new LoginDialog(
                parentFrame,
                true,
                this,
                "Autenticação",
                "Login",
                'L',
                "Sair",
                'S',
                "Armazenar Informação",
                "fazenda",
                null,
                idiomas,
                ClientSettings.getInstance().getResources().getLanguageId()
        );
        return d;
    }

    /**
     * Define o número máximo de tentativas na janela de login.
     */
    @Override
    public int getMaxAttempts() {
        return 3;
    }

    /**
     * Método chamado para autenticar o usuário. A definição abaixo está
     * estática Como exercício você deve implementar essa verificação com dados
     * armazenados no banco de dados, numa tabela de usuários
     */
    @Override
    public boolean authenticateUser(Map loginInfo) throws Exception {
        if ("ADMIN".equalsIgnoreCase((String) loginInfo.get("username"))
                && "ADMIN".equalsIgnoreCase((String) loginInfo.get("password"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método chamado por LoginDialog para notificar o sucesso no login;
     */
    @Override
    public void loginSuccessful(Map loginInfo) {
        username = loginInfo.get("username").toString().toUpperCase();
        if (username.equals("ADMIN")) {
            //ClientSettings.getInstance().setLanguage("PT_BR");
        } else if (username.equals("SLIFE")) {
            //ClientSettings.getInstance().setLanguage("PT_BR");
        }
        MDIFrame mdi = new MDIFrame(this);

        //configura os botões da barra de ferramentas
        mdi.addButtonToToolBar("fazenda.png", "Cadastro de Fazendas");
        mdi.addButtonToToolBar("fornecedor.png", "Cadastro de Fornecedores");
        mdi.addButtonToToolBar("banco.png", "Cadastro de Bancos");
        mdi.addButtonToToolBar("funcionario.png", "Cadastro de Funcionarios");
        /*
        mdi.addSeparatorToToolBar();
        mdi.addSeparatorToToolBar();
        mdi.addButtonToToolBar("plano_conta.png", "Cadastro do Centro de Custo (Contas)");
        mdi.addButtonToToolBar("contrato.png", "Cadastro dos Contratos com Fornecedores");
        mdi.addSeparatorToToolBar();
        mdi.addSeparatorToToolBar();
        mdi.addButtonToToolBar("boleto.png", "Emissão de Boletos");
        mdi.addButtonToToolBar("gas.png", "Controle do Gás");
        mdi.addSeparatorToToolBar();
        mdi.addSeparatorToToolBar();
        mdi.addButtonToToolBar("pagar.png", "Contas a Pagar");
        mdi.addButtonToToolBar("receber.png", "Confirma Recebimentos");
        mdi.addButtonToToolBar("cheque.png", "Conciliação de Cheques");
        mdi.addButtonToToolBar("bancario.png", "Movimento Bancário");
        mdi.addSeparatorToToolBar();
        mdi.addButtonToToolBar("ata.png", "Controle de Atas");
        mdi.addSeparatorToToolBar();
        mdi.addSeparatorToToolBar();
        */
        mdi.addSeparatorToToolBar();
        mdi.addButtonToToolBar("sair.png", "Sair da Aplicação");
    }

    /**
     * Define se deve existir uma opção para mudar o idioma no menu Arquivo
     */
    @Override
    public boolean viewChangeLanguageInMenuBar() {
        return false;
    }

    /**
     * Define a lista de idiomas suportados pela aplicação
     */
    @Override
    public ArrayList getLanguages() {
        ArrayList list = new ArrayList();
        // list.add(new Language("EN", "English"));
        // list.add(new Language("IT", "Italiano"));
        
        list.add(new Language("PT_BR", "Portugês do Brasil"));
        return list;
    }

    /**
     * Define as funções da aplicação organizadas como uma árvore
     */
    public DefaultTreeModel getApplicationFunctions() {
        DefaultMutableTreeNode root = new OpenSwingTreeNode();
        DefaultTreeModel model = new DefaultTreeModel(root);

        ApplicationFunction n1 = new ApplicationFunction("Cadastro", null);
        ApplicationFunction n2 = new ApplicationFunction("Movimento", null);
        ApplicationFunction n11 = new ApplicationFunction("Fazendas", "fazenda", null,
                "getCondominio");
        ApplicationFunction n12 = new ApplicationFunction("Funcionarios", "funcionario", null, "getInquilino");
       // ApplicationFunction n13 = new ApplicationFunction("Bancos", "banco", null, "getBanco");
        ApplicationFunction n14 = new ApplicationFunction("Fornecedores", "fornecedor", null,
                "getFornecedor");
        ApplicationFunction n15 = new ApplicationFunction(true);
        //ApplicationFunction n16 = new ApplicationFunction("Centro de Custo", "centro_custo", null,
        //        "getCentroCusto");
        //ApplicationFunction n17 = new ApplicationFunction("Contratos", "contrato", null, "getContrato");
        //ApplicationFunction n21 = new ApplicationFunction("Contas a Receber", null);
       // ApplicationFunction n211 = new ApplicationFunction("Emissao de Boletos", "boleto", null,
       //         "getBoleto");
       // ApplicationFunction n212 = new ApplicationFunction("Confirma Recebimentos", "receber", null,
       //         "getReceber");
       // ApplicationFunction n22 = new ApplicationFunction("Contas a Pagar", "pagar", null, "getPagar");
       // ApplicationFunction n23 = new ApplicationFunction("Controle do Gás", "gas", null, "getGas");
       // ApplicationFunction n24 = new ApplicationFunction("Conciliação de Cheques", "cheque", null,
       //         "getCheque");
       // ApplicationFunction n25 = new ApplicationFunction("Movimento Bancário", "bancario", null,
       //         "getBancario");
       // ApplicationFunction n26 = new ApplicationFunction(true);
      //  ApplicationFunction n27 = new ApplicationFunction("Controle de Atas", "ata", null, "getAta");
       // ApplicationFunction n28 = new ApplicationFunction("Cartas de Cobrança", "cobranca", null,
       //         "getCobranca");
        n1.add(n11);
        n1.add(n12);
       // n1.add(n13);
        n1.add(n14);
        n1.add(n15);
        /*
        n1.add(n16);
        n1.add(n17);
        n2.add(n21);
        n21.add(n211);
        n21.add(n212);
        n2.add(n22);
        n2.add(n23);
        n2.add(n24);
        n2.add(n25);
        n2.add(n26);
        n2.add(n27);
        n2.add(n28);
        */
        root.add(n1);
        root.add(n2);
        return model;
    }

    /**
     * Define se deve aparecer item File no menu
     *
     */
    @Override
    public boolean viewFileMenu() {
        return true;
    }

    /**
     * Define se deve aparecer um painel na parte inferior da janela contendo
     * botões referentes às janelas abertas dendo da janela mãe
     */
    @Override
    public boolean viewOpenedWindowIcons() {
        return true;
    }

//==============================================================================
//==============================================================================
//==============================================================================    
    /**
     * Cria a conexão com o banco de dados
     */
    private void createConnection() {
        String strConexao = "jdbc:mysql://localhost/farm?user=root&password=slife&useTimezone=true&serverTimezone=UTC";
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(strConexao);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
