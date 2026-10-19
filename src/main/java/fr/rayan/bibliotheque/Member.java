package fr.rayan.bibliotheque;

public record Member(int id, String name) {
    public Member {
        if (id <= 0) throw new IllegalArgumentException("identifiant de membre invalide : " + id);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("nom obligatoire");
    }
}
