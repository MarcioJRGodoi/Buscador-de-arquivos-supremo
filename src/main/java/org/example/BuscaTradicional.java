package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BuscaTradicional implements Busca {
    private JTextArea textArea;

    public BuscaTradicional(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void buscarNome(File[] files, String nome) {
        long startTime = System.currentTimeMillis();
        // Percorre todos os arquivos passados como parâmetro
        for (File file : files) {
            // Verifica se o arquivo é um arquivo (não um diretório) e se termina com ".txt"
            if (file.isFile() && file.getName().endsWith(".txt")) {
                long fileOpenStartTime = System.currentTimeMillis();
                // Tenta abrir o arquivo para leitura
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    long fileOpenEndTime = System.currentTimeMillis();
                    textArea.append("Tempo para abrir o arquivo: " + (fileOpenEndTime - fileOpenStartTime) + "ms\n");
                    String line;
                    int lineNumber = 0;
                    // Lê cada linha do arquivo até o fim
                    while ((line = br.readLine()) != null) {
                        lineNumber++;
                        if (line.contains(nome)) {
                            // Se o nome foi encontrado, guarda o número da linha e a linha em si
                            int finalLineNumber = lineNumber;
                            String finalLine = line;
                            // Adiciona ao textArea o nome do arquivo, o nome encontrado e a linha onde foi
                            // encontrado
                            SwingUtilities.invokeLater(() -> {
                                textArea.append("Arquivo: " + file.getName() + "Nome: " + finalLine + ", Linha: "
                                        + finalLineNumber + "\n");
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
