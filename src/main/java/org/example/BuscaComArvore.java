package org.example;

import Arvore.ArvoreBinaria;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class BuscaComArvore implements Busca {
    private JTextArea textArea;

    public BuscaComArvore(JTextArea textArea) {
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

                    ArvoreBinaria tree = new ArvoreBinaria(lines.toArray(new String[0]));
                    tree.buscarNome(tree.root, nome);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
