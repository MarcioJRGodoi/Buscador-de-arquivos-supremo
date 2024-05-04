package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuscaThreadTesouro implements Busca {
    private final JTextArea textArea;

    public BuscaThreadTesouro(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void buscarNome(File[] files, String nome) {
        ExecutorService executorService = Executors.newFixedThreadPool(files.length);
        for (File file : files) {
            executorService.execute(new Buscador(file, nome));
        }
        executorService.shutdown();
    }

    private class Buscador implements Runnable {
        private final File arquivo;
        private final String palavraDesejada;
        private boolean palavraEncontrada;
        private int palavraEncontradaIndex;

        public Buscador(File arquivo, String palavraDesejada) {
            this.arquivo = arquivo;
            this.palavraDesejada = palavraDesejada;
            this.palavraEncontrada = false;
        }

        @Override
        public void run() {
            if (buscarPalavraArquivo()) {
                buscarPalavraPista();
            }
        }

        private boolean buscarPalavraArquivo() {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                ArrayList<String> palavras = extrairPalavras(arquivo);
                if (palavras.contains(palavraDesejada)) {
                    int index = palavras.indexOf(palavraDesejada);
                    if (index != -1) {
                        palavraEncontrada = true;
                        palavraEncontradaIndex = index;
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        private void buscarPalavraPista() {
            if (palavraEncontrada) {
                textArea.append("Começando busca pelo Tesouro no Arquivo: " + arquivo.getName() + "\n\n");

                ArrayList<String> palavrasOriginais = extrairPalavras(arquivo);
                ArrayList<String> palavras = new ArrayList<>(palavrasOriginais); // Cópia das palavras originais
                int indexInicial = palavras.size() / 2; // Começa na palavra do meio
                int indexAtual = indexInicial;
                int deslocamento = indexAtual; // Inicializa o deslocamento
                boolean buscarDireita = true; // Inicia buscando para a direita
                double porcentagem = 0.0; // Inicializa a porcentagem de proximidade

                while (true) {
                    palavraEncontradaIndex = palavras.indexOf(palavraDesejada);
                    String palavraAtual = palavras.get(indexAtual);

                    if (palavraEncontradaIndex != -1) {
                        porcentagem = calcularPorcentagemDistancia(indexAtual, palavraEncontradaIndex,
                                palavrasOriginais.size());
                    }

                    textArea.append("Palavra atual: " + palavraAtual + " - Posição: " + deslocamento +
                            " - Porcentagem de proximidade: " + porcentagem + "%\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());

                    if (palavraAtual.equals(palavraDesejada)) {
                        textArea.append("\nPalavra '" + palavraDesejada + "' encontrada na posição: " + (deslocamento) + "\n");
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                        break; // Achou a palavra, então sai do loop
                    } else {
                        buscarDireita = verificarDirecao(indexAtual, palavraEncontradaIndex);

                        if (buscarDireita) {
                            palavras.subList(0, indexAtual + 1).clear();
                            deslocamento += palavras.size() / 2;
                        } else {
                            palavras.subList(indexAtual, palavras.size()).clear();
                            deslocamento -= palavras.size() / 2;
                        }

                        indexInicial = palavras.size() / 2;
                        indexAtual = indexInicial;

                        if (palavras.size() <= 0) {
                            break;
                        }
                    }
                }
            }
        }

        private ArrayList<String> extrairPalavras(File arquivo) {
            ArrayList<String> palavras = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] palavrasLinha = linha.split("\\s+");
                    for (String palavra : palavrasLinha) {
                        palavras.add(palavra);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return palavras;
        }

        private double calcularPorcentagemDistancia(int indexAtual, int indexDesejado, int tamanhoOriginal) {
            double distancia = Math.abs(indexAtual - indexDesejado);
            double porcentagem = 100.0 - (distancia / tamanhoOriginal) * 100.0;
            return Math.round(porcentagem * 100.0) / 100.0; // Arredonda para duas casas decimais
        }

        public static boolean verificarDirecao(int indexAtual, int indexDesejado) {
            if (indexAtual < indexDesejado) {
                return true;
            } else {
                return false;
            }
        }
    }
}
