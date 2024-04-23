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
        inputPanel.add(comboBox);
        frame.setLocationRelativeTo(null);
        JButton btnNewButton = new JButton("Selecionar Arquivos");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Limpar a tela antes de cada busca
                textArea.setText("");

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
        inputPanel.add(btnNewButton);
    }
}
