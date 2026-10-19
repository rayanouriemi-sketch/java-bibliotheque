package fr.rayan.bibliotheque;

import java.util.Objects;

public record Book(String isbn, String title, String author, int year) {
    public Book {
        if (isbn == null || !isbn.matches("\\d{10}|\\d{13}")) {
            throw new IllegalArgumentException("ISBN invalide (10 ou 13 chiffres attendus) : " + isbn);
        }
        Objects.requireNonNull(title, "titre");
        Objects.requireNonNull(author, "auteur");
        if (title.isBlank() || author.isBlank()) throw new IllegalArgumentException("titre et auteur obligatoires");
    }
}
