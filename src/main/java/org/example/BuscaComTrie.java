package org.example;

import javax.swing.*;
import java.io.*;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean endOfWord;
}

class Trie {
    TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.endOfWord = true;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            TrieNode node = current.children.get(c);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.endOfWord;
    }
}

public class BuscaComTrie implements Busca {
    private JTextArea textArea;

    public BuscaComTrie(JTextArea textArea) {
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

                    for (String l : lines) {
                        Trie trie = new Trie();
                        trie.insert(l);
                        if (trie.search(nome)) {
                            String finalLine = line;
                            SwingUtilities.invokeLater(() -> {
                                textArea.append("Nome encontrado: " + finalLine + "\n");
                            });
                        }
                    }

                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    long openElapsedTime = openEndTime - openStartTime;
                    SwingUtilities.invokeLater(() -> {
                        textArea.append("Tempo para abrir o arquivo: "+ file.getName() + " " + openElapsedTime + "ms\n");
                        textArea.append("Tempo de execução para a busca no arquivo : "+ file.getName() + " " + elapsedTime + "ms\n");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
