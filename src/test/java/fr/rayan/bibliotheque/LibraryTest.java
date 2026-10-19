package fr.rayan.bibliotheque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest {
    private static final LocalDate DAY = LocalDate.of(2024, 1, 10);
    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(new Book("1111111111", "Le Petit Prince", "Saint-Exupéry", 1943));
        library.addBook(new Book("2222222222", "La Peste", "Camus", 1947));
        library.addBook(new Book("3333333333", "L'Étranger", "Camus", 1942));
        library.addBook(new Book("4444444444", "Germinal", "Zola", 1885));
        library.addMember(new Member(1, "Alice"));
        library.addMember(new Member(2, "Bob"));
    }

    @Test
    void borrowSetsDueDateTwoWeeksLater() {
        Loan loan = library.borrow("1111111111", 1, DAY);
        assertEquals(DAY.plusDays(14), loan.dueOn());
        assertEquals(3, library.availableBooks().size());
    }

    @Test
    void cannotBorrowABookTwice() {
        library.borrow("1111111111", 1, DAY);
        assertThrows(LibraryException.class, () -> library.borrow("1111111111", 2, DAY));
    }

    @Test
    void memberIsLimitedToThreeLoans() {
        library.borrow("1111111111", 1, DAY);
        library.borrow("2222222222", 1, DAY);
        library.borrow("3333333333", 1, DAY);
        LibraryException e = assertThrows(LibraryException.class, () -> library.borrow("4444444444", 1, DAY));
        assertTrue(e.getMessage().contains("3 emprunts"));
    }

    @Test
    void memberWithOverdueBookCannotBorrow() {
        library.borrow("1111111111", 1, DAY);
        assertThrows(LibraryException.class, () -> library.borrow("2222222222", 1, DAY.plusDays(15)));
    }

    @Test
    void lateReturnCostsTwentyCentsPerDay() {
        library.borrow("1111111111", 1, DAY);
        assertEquals(0, library.returnBook("1111111111", DAY.plusDays(14)));
        library.borrow("1111111111", 1, DAY);
        assertEquals(5 * 20, library.returnBook("1111111111", DAY.plusDays(19)));
        assertThrows(LibraryException.class, () -> library.returnBook("1111111111", DAY));
    }

    @Test
    void overdueLoansAreListedByDueDate() {
        library.borrow("2222222222", 1, DAY.plusDays(1));
        library.borrow("1111111111", 2, DAY);
        var overdue = library.overdueLoans(DAY.plusDays(30));
        assertEquals("1111111111", overdue.get(0).book().isbn());
        assertEquals(2, overdue.size());
        assertTrue(library.overdueLoans(DAY.plusDays(14)).isEmpty());
    }

    @Test
    void searchIsCaseInsensitiveOnTitleAndAuthor() {
        assertEquals(2, library.search("CAMUS").size());
        assertEquals("Germinal", library.search("germ").get(0).title());
        assertTrue(library.search("inconnu").isEmpty());
    }

    @Test
    void rejectsInvalidDataAndUnknownIds() {
        assertThrows(IllegalArgumentException.class, () -> new Book("123", "Titre", "Auteur", 2000));
        assertThrows(IllegalArgumentException.class, () -> new Member(0, "X"));
        assertThrows(LibraryException.class, () -> library.addBook(new Book("1111111111", "Doublon", "X", 2000)));
        assertThrows(LibraryException.class, () -> library.borrow("9999999999", 1, DAY));
        assertThrows(LibraryException.class, () -> library.borrow("1111111111", 99, DAY));
    }
}
