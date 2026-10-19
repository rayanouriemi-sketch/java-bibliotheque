package fr.rayan.bibliotheque;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("9782070408504", "Le Petit Prince", "Antoine de Saint-Exupéry", 1943));
        library.addBook(new Book("9782070360024", "L'Étranger", "Albert Camus", 1942));
        library.addBook(new Book("9782253004226", "Germinal", "Émile Zola", 1885));
        library.addBook(new Book("9782070413119", "La Peste", "Albert Camus", 1947));
        library.addMember(new Member(1, "Alice"));
        library.addMember(new Member(2, "Bob"));

        LocalDate today = LocalDate.of(2024, 9, 1);
        Loan loan = library.borrow("9782070360024", 1, today);
        System.out.println("Alice emprunte « " + loan.book().title() + " », à rendre le " + loan.dueOn());

        System.out.println("\nLivres de Camus :");
        library.search("camus").forEach(b -> System.out.println("  - " + b.title() + " (" + b.year() + ")"));

        System.out.println("\nDisponibles :");
        library.availableBooks().forEach(b -> System.out.println("  - " + b.title()));

        try {
            library.borrow("9782070360024", 2, today);
        } catch (LibraryException e) {
            System.out.println("\nRefusé : " + e.getMessage());
        }

        long fee = library.returnBook("9782070360024", today.plusDays(20));
        System.out.printf("%nRetour avec 6 jours de retard : %.2f € de pénalité%n", fee / 100.0);
    }
}
