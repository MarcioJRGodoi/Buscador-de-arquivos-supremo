package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BuscaTradicional implements Busca{
    private JTextArea textArea;

    public BuscaTradicional(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void buscarNome(File[] files, String nome) {
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineNumber = 0;
                    while ((line = br.readLine()) != null) {
                        lineNumber++;
                        if (line.contains(nome)) {
                            int finalLineNumber = lineNumber;
                            String finalLine = line;
                            SwingUtilities.invokeLater(() -> {
                                textArea.append("Arquivo: " + file.getName() + "Nome: " + finalLine + ", Linha: " + finalLineNumber + "\n");
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        SwingUtilities.invokeLater(() -> {
            textArea.append("Tempo de execução: " + elapsedTime + "ms\n");
        });
    }
}
