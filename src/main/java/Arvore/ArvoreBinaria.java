package Arvore;


import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ArvoreBinaria {
    public No root;
    JTextArea textArea;

    public ArvoreBinaria(String[] lines) {
        this.textArea = textArea;
        root = constructTree(lines, 0, lines.length - 1);
    }

    private No constructTree(String[] lines, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        No No = new No(lines[mid]);

        No.left = constructTree(lines, start, mid - 1);
        No.right = constructTree(lines, mid + 1, end);

        return No;
    }

    public void buscarNome(No No, String nome) {
        if (No == null) {
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            buscarNome(No.left, nome);
            SwingUtilities.invokeLater(() -> {
                textArea.append("Thread finalizada em: " + System.currentTimeMillis() + "\n");
            });
        });
        executor.submit(() -> {
            buscarNome(No.right, nome);
            SwingUtilities.invokeLater(() -> {
                textArea.append("Thread finalizada em: " + System.currentTimeMillis() + "\n");
            });
        });

        if (No.getLine().contains(nome)) {
            SwingUtilities.invokeLater(() -> {
                textArea.append("Nome encontrado: " + nome + "\n");
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}