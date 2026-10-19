package fr.rayan.bibliotheque;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Library {
    public static final int MAX_LOANS_PER_MEMBER = 3;
    public static final int LOAN_DAYS = 14;
    public static final int LATE_FEE_CENTS_PER_DAY = 20;

    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<Integer, Member> members = new HashMap<>();
    private final Map<String, Loan> loansByIsbn = new HashMap<>();

    public void addBook(Book book) {
        if (books.putIfAbsent(book.isbn(), book) != null) {
            throw new LibraryException("Un livre avec l'ISBN " + book.isbn() + " existe déjà");
        }
    }

    public void addMember(Member member) {
        if (members.putIfAbsent(member.id(), member) != null) {
            throw new LibraryException("Le membre #" + member.id() + " existe déjà");
        }
    }

    public Loan borrow(String isbn, int memberId, LocalDate today) {
        Book book = findBook(isbn);
        Member member = findMember(memberId);
        if (loansByIsbn.containsKey(isbn)) {
            throw new LibraryException("« " + book.title() + " » est déjà emprunté");
        }
        if (loansOf(memberId).size() >= MAX_LOANS_PER_MEMBER) {
            throw new LibraryException(member.name() + " a déjà " + MAX_LOANS_PER_MEMBER + " emprunts en cours");
        }
        if (!overdueLoans(today).stream().filter(l -> l.member().id() == memberId).toList().isEmpty()) {
            throw new LibraryException(member.name() + " doit d'abord rendre ses livres en retard");
        }
        Loan loan = new Loan(book, member, today, today.plusDays(LOAN_DAYS));
        loansByIsbn.put(isbn, loan);
        return loan;
    }

    /** Rend un livre et retourne la pénalité de retard en centimes. */
    public long returnBook(String isbn, LocalDate today) {
        Loan loan = loansByIsbn.remove(isbn);
        if (loan == null) throw new LibraryException("Ce livre n'est pas emprunté : " + isbn);
        return loan.daysLate(today) * LATE_FEE_CENTS_PER_DAY;
    }

    public List<Book> search(String query) {
        String q = query.toLowerCase(Locale.ROOT);
        return books.values().stream()
                .filter(b -> b.title().toLowerCase(Locale.ROOT).contains(q)
                        || b.author().toLowerCase(Locale.ROOT).contains(q))
                .sorted(Comparator.comparing(Book::title))
                .toList();
    }

    public List<Book> availableBooks() {
        return books.values().stream().filter(b -> !loansByIsbn.containsKey(b.isbn())).toList();
    }

    public List<Loan> loansOf(int memberId) {
        return loansByIsbn.values().stream().filter(l -> l.member().id() == memberId).toList();
    }

    public List<Loan> overdueLoans(LocalDate today) {
        return loansByIsbn.values().stream()
                .filter(l -> l.isOverdue(today))
                .sorted(Comparator.comparing(Loan::dueOn))
                .toList();
    }

    private Book findBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) throw new LibraryException("Livre introuvable : " + isbn);
        return book;
    }

    private Member findMember(int id) {
        Member member = members.get(id);
        if (member == null) throw new LibraryException("Membre introuvable : #" + id);
        return member;
    }
}
