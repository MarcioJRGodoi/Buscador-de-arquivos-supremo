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
                long openStartTime = System.currentTimeMillis();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    long openEndTime = System.currentTimeMillis();

                    long startTime = System.currentTimeMillis();

                    String finalLine = line;
                    Thread thread1 = new Thread(() -> {
                        for (int i = 0; i < lines.size() / 2; i++) {
                            if (lines.get(i).contains(nome)) {
                                int lineNumber = i + 1;
                                SwingUtilities.invokeLater(() -> {
                                    textArea.append("Arquivo: " + file.getName() + "Nome: " + finalLine + ", Linha: " + lineNumber + "\n");
                                });
                            }
                        }
                        long threadEndTime = System.currentTimeMillis();
                        SwingUtilities.invokeLater(() -> {
                            textArea.append("Thread 1 terminou em: " + threadEndTime + "ms\n");
                        });
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
                        long threadEndTime = System.currentTimeMillis();
                        SwingUtilities.invokeLater(() -> {
                            textArea.append("Thread 2 terminou em: " + threadEndTime + "ms\n");
                        });
                    });

                    thread1.start();
                    thread2.start();

                    // Aguarda as threads terminarem
                    thread1.join();
                    thread2.join();

                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    long openElapsedTime = openEndTime - openStartTime;
                    SwingUtilities.invokeLater(() -> {
                        textArea.append("Tempo para abrir o arquivo: "+ file.getName() + " " + openElapsedTime + "ms\n");
                        textArea.append("Tempo de execução para o arquivo : "+ file.getName() + " " + elapsedTime + "ms\n");
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
