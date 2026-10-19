# Gestion de bibliothèque en Java

Modélisation orientée objet d'une bibliothèque : livres, membres, emprunts, retards et pénalités.

## Règles métier

- Un emprunt dure **14 jours**
- Un membre peut avoir au maximum **3 emprunts** en cours
- Un membre qui a un livre en retard ne peut plus emprunter
- Pénalité de **0,20 € par jour** de retard
- Recherche insensible à la casse par titre ou auteur

## Lancer

```bash
mvn package
java -jar target/bibliotheque-1.0.0.jar
```

```
Alice emprunte « L'Étranger », à rendre le 2024-09-15

Livres de Camus :
  - L'Étranger (1942)
  - La Peste (1947)
...
Retour avec 6 jours de retard : 1,20 € de pénalité
```

## Tests

```bash
mvn test
```

## Notions abordées

Records Java 17 avec validation dans le constructeur compact, collections (`Map`, `List`), Streams, `java.time`, exceptions métier, tests JUnit 5.
