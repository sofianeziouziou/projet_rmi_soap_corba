package com.immobilier.client;

import javax.swing.*;
import java.awt.*;
import com.immobilier.core.Utilisateur;
import Immobilier.Bien;

public class ClientApp {
    private RestClient rest;
    private CorbaClient corba;
    private SoapClient soap;

    private JFrame frame;
    private Utilisateur currentUser;

    public ClientApp() throws Exception {
        rest = new RestClient("http://localhost:8080/rest-server");
        corba = new CorbaClient(System.getProperty("user.home") + "/BienService.ior");
        soap = new SoapClient("http://localhost:8081/contracts");

        SwingUtilities.invokeLater(this::buildUI);
    }

    private void buildUI() {
        frame = new JFrame("Système Immobilier");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new CardLayout());

        JPanel login = new JPanel(new GridLayout(4, 2));
        JTextField emailF = new JTextField();
        JPasswordField pwdF = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnReg = new JButton("Register");
        login.add(new JLabel("Email:")); login.add(emailF);
        login.add(new JLabel("Password:")); login.add(pwdF);
        login.add(btnLogin); login.add(btnReg);

        JPanel agent = new JPanel(new BorderLayout());
        JPanel acheteur = new JPanel(new BorderLayout());

        frame.add(login, "login");
        frame.add(agent, "agent");
        frame.add(acheteur, "acheteur");
        frame.setVisible(true);

        // Login
        btnLogin.addActionListener(e -> {
            try {
                Utilisateur u = rest.login(emailF.getText(), new String(pwdF.getPassword()));
                if(u != null) {
                    currentUser = u;
                    if("agent".equalsIgnoreCase(u.getRole())) showAgentPanel(agent);
                    else showAcheteurPanel(acheteur);
                    ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), u.getRole());
                } else {
                    JOptionPane.showMessageDialog(frame,"Login failed");
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,"Error");
            }
        });

        // Registration
        btnReg.addActionListener(e -> {
            try {
                Utilisateur u = new Utilisateur();
                u.setEmail(emailF.getText());
                u.setPassword(new String(pwdF.getPassword()));
                u.setNom("User");
                u.setRole("acheteur");
                Utilisateur created = rest.register(u);
                JOptionPane.showMessageDialog(frame, "Registered: " + created.getEmail());
            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,"Error");
            }
        });
    }

    private void showAgentPanel(JPanel agent) {
        agent.removeAll();
        JButton refresh = new JButton("Mes Biens");
        JButton add = new JButton("Ajouter Bien");
        JTextArea text = new JTextArea();
        agent.add(new JPanel(){{
            add(refresh); add(add);
        }}, BorderLayout.NORTH);
        agent.add(new JScrollPane(text), BorderLayout.CENTER);

        refresh.addActionListener(e -> {
            try {
                Bien[] biens = corba.listBiens();
                StringBuilder sb = new StringBuilder();
                for(Bien b : biens) {
                    if(b.agentId == currentUser.getId())
                        sb.append(b.id).append(" | ").append(b.titre).append(" | ").append(b.description).append("\n");
                }
                text.setText(sb.toString());
            } catch(Exception ex){ ex.printStackTrace(); }
        });

        add.addActionListener(e -> {
            String titre = JOptionPane.showInputDialog("Titre");
            String desc = JOptionPane.showInputDialog("Description");
            try {
                Bien b = new Bien();
                b.titre = titre;
                b.description = desc;
                b.prix = 100.0;
                b.disponible = true;
                b.agentId = Math.toIntExact(currentUser.getId());
                int id = corba.addBien(b);
                JOptionPane.showMessageDialog(frame,"Bien ajouté id=" + id);
            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(frame,"Erreur"); }
        });

        agent.revalidate(); agent.repaint();
    }

    private void showAcheteurPanel(JPanel acheteur) {
        acheteur.removeAll();
        JButton refresh = new JButton("Voir Biens");
        JTextArea text = new JTextArea();
        acheteur.add(refresh, BorderLayout.NORTH);
        acheteur.add(new JScrollPane(text), BorderLayout.CENTER);

        refresh.addActionListener(e -> {
            try {
                Bien[] biens = corba.listBiens();
                StringBuilder sb = new StringBuilder();
                for(Bien b : biens) {
                    if(b.disponible)
                        sb.append(b.id).append(" | ").append(b.titre).append(" | ").append(b.description)
                                .append(" | Prix: ").append(b.prix).append("\n");
                }
                text.setText(sb.toString());

                // Achat simplifié
                if(biens.length > 0) {
                    String idS = JOptionPane.showInputDialog("Entrez id du bien à acheter (ou annuler):");
                    if(idS != null) {
                        int id = Integer.parseInt(idS);
                        int userId = (int) currentUser.getId(); // Cast long -> int si nécessaire
                        String resp = soap.createContract(id, userId);
                        JOptionPane.showMessageDialog(acheteur, "Réponse: " + resp);
                    }
                }

            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(acheteur,"Erreur"); }
        });

        acheteur.revalidate(); acheteur.repaint();
    }

    public static void main(String[] args) throws Exception {
        new ClientApp();
    }
}
