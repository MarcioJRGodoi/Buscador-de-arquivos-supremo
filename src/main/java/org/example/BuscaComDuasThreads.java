package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuscaComDuasThreads implements Busca {
    private JTextArea textArea;

    public BuscaComDuasThreads(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void buscarNome(File[] files, String nome) {
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }

                    long startTime = System.currentTimeMillis();

                    Thread thread1 = new Thread(() -> {
                        for (int i = 0; i < lines.size() / 2; i++) {
                            if (lines.get(i).contains(nome)) {
                                int lineNumber = i + 1;
                                SwingUtilities.invokeLater(() -> {
                                    textArea.append("Arquivo: " + file.getName() + ", Linha: " + lineNumber + "\n");
                                });
                            }
                        }
                    });

                    Thread thread2 = new Thread(() -> {
                        for (int i = lines.size() - 1; i >= lines.size() / 2; i--) {
                            if (lines.get(i).contains(nome)) {
                                int lineNumber = i + 1;
                                SwingUtilities.invokeLater(() -> {
                                    textArea.append("Arquivo: " + file.getName() + ", Linha: " + lineNumber + "\n");
                                });
                            }
                        }
                    });

                    thread1.start();
                    thread2.start();

                    // Aguarda as threads terminarem
                    thread1.join();
                    thread2.join();

                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    SwingUtilities.invokeLater(() -> {
                        textArea.append("Tempo de execução para o arquivo : "+ file.getName() + " " + elapsedTime + "ms\n");
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
