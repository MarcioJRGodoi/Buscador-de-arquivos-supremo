package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class BuscaThreadExplorador implements Busca {
    private static JTextArea textArea;

    public BuscaThreadExplorador(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void buscarNome(File[] files, String nome) {
        for (File file : files) {
            Thread explorer = new Thread(new Explorer(file, nome));
            explorer.start();
        }
    }

    static class Explorer implements Runnable {
        private final File arquivo;
        private final String palavraObjetivo;
        private final Random random;

        public Explorer(File arquivo, String palavraObjetivo) {
            this.arquivo = arquivo;
            this.palavraObjetivo = palavraObjetivo;
            this.random = new Random();
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String texto = sb.toString();
                int tamanhoTexto = texto.length();
                int tamanhoPalavra = palavraObjetivo.length();

                long startTime = System.currentTimeMillis();

                StringBuilder path = new StringBuilder();

                while (true) {
                    int startPos = random.nextInt(Math.max(1, tamanhoTexto - tamanhoPalavra));

                    int direction = random.nextInt(4);

                    int currentPosition = startPos;
                    boolean found = true;
                    for (int i = 0; i < tamanhoPalavra; i++) {
                        char currentChar = texto.charAt(currentPosition);
                        char targetChar = palavraObjetivo.charAt(i);
                        String direcao = "";
                        if (currentChar != targetChar) {
                            found = false;
                            break;
                        }
                        switch (direction) {
                            case 0:
                                currentPosition++;
                                direcao = "Cima";
                                break;
                            case 1:
                                currentPosition--;
                                direcao = "Baixo";
                                break;
                            case 2:
                                currentPosition -= 1;
                                direcao = "Esquerda";
                                break;
                            case 3:
                                currentPosition += 1;
                                direcao = "Direita";
                                break;
                        }
                        if (currentPosition < 0 || currentPosition >= tamanhoTexto) {
                            found = false;
                            break;
                        }

                        if (path.length() > 0) {
                            path.append(" -> ").append(currentPosition + " ("+ direcao +")" + "\n");
                        } else {
                            path.append("\n" + currentPosition + "\n");
                        }
                        
                    }
                    if (found) {
                        String message = "Palavra '" + palavraObjetivo + "' encontrada em '" + arquivo.getName()
                                + "' na posição " + startPos;
                        message += "\nCaminho percorrido: " + path.toString();
                        textArea.append(message + "\n");

                        long endTime = System.currentTimeMillis();
                        long executionTime = endTime - startTime;
                        String timeMessage = "Tempo de execução: " + executionTime + " ms";
                        textArea.append(timeMessage + "\n");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
