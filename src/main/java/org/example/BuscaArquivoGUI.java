package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

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
        frame.setBounds(100, 100, 850, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setViewportView(textArea);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(inputPanel);

        JLabel lblNewLabel = new JLabel("Nome para busca:");
        inputPanel.add(lblNewLabel);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 20)); // Reduzindo a largura do campo de texto
        inputPanel.add(textField);

        comboBox = new JComboBox<>();
        comboBox.addItem(new BuscaTradicional(textArea));
        comboBox.addItem(new BuscaComUmaThread(textArea));
        comboBox.addItem(new BuscaComDuasThreads(textArea));
        // comboBox.addItem(new BuscaComArvore(textArea));//BuscaComArvore Perigoso ISSO
        // KKKKKKKK
        comboBox.addItem(new BuscaHashSemThread(textArea));// BuscaHashSemThread
        comboBox.addItem(new BuscaComTrie(textArea));// BuscaComTrie
        comboBox.addItem(new BuscaThreadExplorador(textArea));
        comboBox.addItem(new BuscaThreadTesouro(textArea));

        inputPanel.add(comboBox);
        frame.setLocationRelativeTo(null);
        JButton btnNewButton = new JButton("Selecionar Arquivos");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Limpar a tela antes de cada busca
                textArea.setText("");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    if (selectedFolder != null && selectedFolder.isDirectory()) {
                        File[] files = selectedFolder.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.toLowerCase().endsWith(".txt");
                            }
                        });

                        Busca busca = (Busca) comboBox.getSelectedItem();
                        busca.buscarNome(files, textField.getText());
                    } else {
                        System.out.println("Nenhum diretório selecionado.");
                    }
                }
            }

        });
        inputPanel.add(btnNewButton);
    }
}
