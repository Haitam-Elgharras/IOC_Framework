# Rapport - Partie 1: Injection de Dépendances avec Spring

Ce rapport couvre la mise en œuvre de l'injection de dépendances dans une application Java en utilisant différents mécanismes, y compris l'injection statique, l'injection dynamique, et l'utilisation du framework Spring dans ses versions XML et annotations.

## Contenu du Projet

Le projet est divisé en plusieurs parties, chacune correspondant à une méthode spécifique d'injection de dépendances.

### 1. Interface IDao et Implémentation

L'interface `IDao` est créée avec une méthode `getDate` pour interagir avec la source de données. Une implémentation de cette interface est fournie pour accéder aux données de manière concrète.

### 2. Interface IMetier et Implémentation

L'interface `IMetier` est définie avec une méthode `calcul` qui encapsule la logique métier de l'application. Une implémentation de cette interface est fournie, mettant en œuvre le calcul en utilisant un couplage faible pour améliorer la modularité et la maintenance du code.

### 3. Injection de Dépendances

#### a. Instanciation Statique

L'injection de dépendances par instanciation statique consiste à créer et à fournir les dépendances directement dans les classes qui en ont besoin, généralement via des constructeurs ou des méthodes d'initialisation.

#### b. Instanciation Dynamique

L'injection de dépendances par instanciation dynamique utilise des mécanismes tels que la réflexion et la configuration externe pour créer et fournir les dépendances au moment de l'exécution. Dans ce cas, les dépendances sont chargées dynamiquement en fonction des besoins de l'application.

#### c. Utilisation du Framework Spring

##### - Version XML

L'utilisation de Spring avec une configuration XML permet de définir les beans et leurs dépendances dans des fichiers de configuration XML distincts. Spring charge et gère ces beans pour fournir l'injection de dépendances à l'application.

##### - Version Annotations

Spring offre également la possibilité de configurer les beans et leurs dépendances en utilisant des annotations telles que `@Component`, `@Autowired`, et d'autres. Cette approche basée sur les annotations simplifie la configuration et réduit la quantité de code de configuration nécessaire.

## Structure du Projet

Le projet est organisé en plusieurs commits, chacun se concentrant sur une partie spécifique de l'injection de dépendances. Chaque commit comprend des fichiers de présentation dans le package `presentation` pour illustrer l'utilisation des fonctionnalités implémentées.


# Rapport - Partie 2: Injection de Dépendances avec Les Annotations.

Ce rapport présente la deuxième partie du projet, se concentrant sur l'injection de dépendances en utilisant des annotations et la réflexion dans une application Java.

## Contenu du Projet

La partie 2 du projet explore différentes méthodes d'injection de dépendances, en mettant l'accent sur l'utilisation d'annotations telles que `@Autowired`, `@Service`, et `@Repository`, ainsi que sur la réflexion pour analyser et gérer les dépendances.

### 1. Annotation `@Autowired` et Annotations de Service et Repository

Les annotations `@Autowired`, `@Service`, et `@Repository` sont utilisées pour marquer les composants de l'application, indiquer les dépendances à injecter, et identifier les classes de service et de repository.

### 2. Mécanismes d'Injection de Dépendances
La classe `CustomAnnotationConfigApplicationContext` utilise la réflexion pour scanner les classes dans un package donné, identifier les composants annotés, et gérer l'instantiation des beans et l'injection de dépendances.

#### a. Injection de Dépendances par Constructeur et par Setter
Les dépendances peuvent être injectées soit par constructeur, soit par méthode setter, en fonction des annotations présentes dans les classes et des paramètres de leurs constructeurs et méthodes.

#### b. Gestion des Dépendances par Annotations
Les annotations telles que `@Autowired` sont analysées pour déterminer les dépendances à injecter, et la réflexion est utilisée pour localiser les beans correspondants et les fournir aux classes requises.

## Structure du Projet

La structure du projet reste organisée avec des packages distincts pour les composants, les interfaces, et les annotations. Chaque commit est associé à une fonctionnalité spécifique, avec des fichiers de présentation détaillant l'utilisation des annotations et des mécanismes d'injection de dépendances.

## Utilisation

Le projet peut être cloné localement et exécuté pour explorer les différentes méthodes d'injection de dépendances mises en œuvre.

---

## Auteur

- **Haitam ELGHARRAS** - [GitHub](hhtps://github.com/haitam-elgharras)
- **Email** -  [Email](elgharras.haitam@gmail.com)
