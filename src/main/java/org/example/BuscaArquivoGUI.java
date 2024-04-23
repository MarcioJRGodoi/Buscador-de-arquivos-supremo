package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BuscaArquivoGUI {
    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JComboBox<Busca> comboBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BuscaArquivoGUI window = new BuscaArquivoGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BuscaArquivoGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("Nome para busca:");
        panel.add(lblNewLabel);

        textField = new JTextField();
        panel.add(textField);
        textField.setColumns(10);

        comboBox = new JComboBox<>();
        comboBox.addItem(new BuscaTradicional(textArea));
        comboBox.addItem(new BuscaComThreads());
        panel.add(comboBox);

        JButton btnNewButton = new JButton("Selecionar Arquivos");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    Busca busca = (Busca) comboBox.getSelectedItem();
                    busca.buscarNome(files, textField.getText());
                }
            }
        });
        panel.add(btnNewButton);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
    }
}
