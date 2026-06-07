# TP 20 - Number Book : Android Contacts et API distante avec Retrofit

## Description

Ce projet est une application Android développée en Java dans le cadre du cours **Programmation Mobile : Android avec Java**.

Le laboratoire porte sur la création d’une application mobile nommée **Number Book**. Cette application permet de lire les contacts enregistrés dans le téléphone, de les afficher dans une interface Android, puis de les synchroniser vers une API distante développée en PHP avec une base de données MySQL.

L’application permet également d’effectuer une recherche distante par nom ou par numéro de téléphone grâce à Retrofit.

Ce TP met en relation plusieurs notions importantes du développement mobile moderne :

* accès aux contacts Android ;
* gestion des permissions runtime ;
* affichage avec RecyclerView ;
* communication HTTP avec Retrofit ;
* conversion JSON avec Gson ;
* backend PHP ;
* base de données MySQL ;
* recherche distante ;
* synchronisation mobile vers serveur.

## Objectifs du laboratoire

Ce lab permet de mettre en pratique les notions suivantes :

* Comprendre le rôle de `ContentResolver` dans Android
* Lire les contacts du téléphone avec `ContactsContract`
* Demander et gérer la permission `READ_CONTACTS`
* Afficher une liste de contacts avec `RecyclerView`
* Créer un modèle Java représentant un contact
* Créer un adapter RecyclerView
* Créer une API REST simple en PHP
* Créer une base de données distante MySQL
* Envoyer des contacts vers un serveur distant
* Consommer une API REST avec Retrofit
* Convertir automatiquement JSON vers objets Java avec Gson
* Rechercher des contacts distants par nom ou numéro
* Structurer un projet Android connecté à un backend
* Comprendre le lien entre application mobile, API et base de données

## Résultat attendu

À la fin du TP, l’application permet de :

* demander l’autorisation d’accès aux contacts ;
* charger les contacts du téléphone ;
* afficher les contacts dans une liste ;
* synchroniser les contacts vers le serveur ;
* stocker les contacts dans une base MySQL ;
* afficher les contacts stockés côté serveur ;
* rechercher un contact distant par nom ou numéro ;
* afficher les résultats de recherche dans l’application.

## Fonctionnalités réalisées

L’application contient les fonctionnalités suivantes :

* bouton pour charger les contacts locaux ;
* demande de permission `READ_CONTACTS` ;
* lecture des noms et numéros de téléphone ;
* nettoyage simple des numéros ;
* suppression des doublons locaux par numéro ;
* affichage des contacts avec RecyclerView ;
* bouton de synchronisation vers l’API distante ;
* insertion des contacts dans MySQL ;
* bouton pour afficher les contacts serveur ;
* champ de recherche ;
* recherche distante par nom ou numéro ;
* affichage des résultats retournés par l’API ;
* gestion des erreurs réseau ;
* messages utilisateur avec Toast.

## Technologies utilisées

### Côté Android

* Java
* Android Studio
* Android SDK
* XML
* AppCompat
* Material Components
* RecyclerView
* Retrofit
* Gson Converter
* ContentResolver
* ContactsContract
* ActivityResultLauncher
* Runtime Permissions

### Côté backend

* PHP
* PDO
* MySQL
* JSON
* XAMPP
* phpMyAdmin

## Architecture globale

Le flux général de l’application est le suivant :

```
Utilisateur
    |
    | ouvre l’application
    v
MainActivity
    |
    | demande READ_CONTACTS
    v
ContentResolver
    |
    | lit les contacts Android
    v
Liste de Contact
    |
    | affichage local
    v
RecyclerView
    |
    | synchronisation
    v
Retrofit
    |
    | requêtes HTTP
    v
API PHP
    |
    | requêtes SQL
    v
Base MySQL
```

Pour la recherche distante :

```
Utilisateur
    |
    | saisit un mot-clé
    v
MainActivity
    |
    | appelle Retrofit
    v
searchContact.php
    |
    | recherche dans MySQL
    v
JSON
    |
    | conversion Gson
    v
List<Contact>
    |
    | affichage RecyclerView
    v
Résultats visibles dans l’application
```

## Structure du projet Android

```
NumberBook/
│
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           │
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── numberbook/
│           │               │
│           │               ├── model/
│           │               │   ├── Contact.java
│           │               │   └── ApiResponse.java
│           │               │
│           │               ├── network/
│           │               │   ├── ContactApi.java
│           │               │   └── RetrofitClient.java
│           │               │
│           │               └── ui/
│           │                   ├── MainActivity.java
│           │                   └── ContactAdapter.java
│           │
│           └── res/
│               ├── layout/
│               │   └── activity_main.xml
│               │
│               ├── values/
│               │   ├── strings.xml
│               │   └── themes.xml
│               │
│               └── xml/
│                   └── network_security_config.xml
│
├── README.md
└── .gitignore
```

## Structure du backend

```
numberbook-api/
│
├── config/
│   └── Database.php
│
├── model/
│   └── Contact.php
│
├── service/
│   └── ContactService.php
│
└── api/
    ├── insertContact.php
    ├── getAllContacts.php
    └── searchContact.php
```

## Package Android utilisé

Le package principal du projet est :

```
com.example.numberbook
```

Les sous-packages utilisés sont :

```
com.example.numberbook.model
com.example.numberbook.network
com.example.numberbook.ui
```

## Base de données distante

La base de données utilisée s’appelle :

```
numberbook
```

La table principale s’appelle :

```
contact
```

Structure de la table :

```
id
name
phone
source
created_at
```

## Script SQL utilisé

```
CREATE DATABASE IF NOT EXISTS numberbook
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE numberbook;

CREATE TABLE IF NOT EXISTS contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    source VARCHAR(50) DEFAULT 'mobile',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_phone (phone)
);
```

## Rôle des colonnes de la table contact

### id

Identifiant unique du contact dans la base de données.

Il est généré automatiquement avec :

```
AUTO_INCREMENT
```

### name

Nom du contact.

Le type utilisé est :

```
VARCHAR(150)
```

### phone

Numéro de téléphone du contact.

Le type utilisé est :

```
VARCHAR(50)
```

Le numéro est stocké sous forme de chaîne de caractères, car un numéro peut contenir :

* un indicatif ;
* des espaces ;
* des parenthèses ;
* des tirets ;
* le signe plus.

### source

Origine du contact.

Dans ce TP, la valeur par défaut est :

```
mobile
```

### created_at

Date et heure d’insertion du contact.

La valeur est générée automatiquement avec :

```
CURRENT_TIMESTAMP
```

### unique_phone

Une contrainte d’unicité est ajoutée sur le numéro de téléphone.

Cela permet d’éviter l’insertion de doublons avec le même numéro.

## Backend PHP

Le backend est développé en PHP.

Il contient trois couches principales :

* `config` : connexion à la base de données ;
* `service` : logique d’accès aux données ;
* `api` : points d’entrée HTTP appelés par Android.

## Description des fichiers backend

### Database.php

Chemin :

```
numberbook-api/config/Database.php
```

Ce fichier contient la classe responsable de la connexion à MySQL avec PDO.

Son rôle est de :

* définir l’hôte MySQL ;
* définir le nom de la base ;
* définir l’utilisateur ;
* créer une connexion PDO ;
* activer le mode exception ;
* retourner l’objet de connexion.

La connexion utilise :

```
charset=utf8mb4
```

Cela permet de gérer correctement les caractères accentués et spéciaux.

### Contact.php

Chemin :

```
numberbook-api/model/Contact.php
```

Ce fichier contient une classe simple représentant un contact côté serveur.

Elle contient les attributs suivants :

```
id
name
phone
source
created_at
```

Cette classe améliore l’organisation du backend.

### ContactService.php

Chemin :

```
numberbook-api/service/ContactService.php
```

Ce fichier contient la logique d’accès à la base.

Il contient les méthodes suivantes :

```
insert()
getAll()
search()
```

### insert()

Cette méthode insère un contact dans la base.

Elle utilise une requête préparée afin d’éviter une concaténation SQL directe.

La requête utilisée permet aussi d’éviter les doublons grâce à :

```
ON DUPLICATE KEY UPDATE
```

### getAll()

Cette méthode retourne tous les contacts stockés dans la base.

Les contacts sont triés par nom.

### search()

Cette méthode cherche un contact par nom ou par numéro.

Elle utilise :

```
LIKE
```

avec le format :

```
%keyword%
```

Cela permet une recherche partielle.

### insertContact.php

Chemin :

```
numberbook-api/api/insertContact.php
```

Ce fichier reçoit une requête POST contenant un contact au format JSON.

Il vérifie :

* que la méthode HTTP est POST ;
* que le JSON est valide ;
* que les champs `name` et `phone` sont présents ;
* que les valeurs ne sont pas vides.

Puis il appelle :

```
ContactService::insert()
```

La réponse est renvoyée en JSON.

### getAllContacts.php

Chemin :

```
numberbook-api/api/getAllContacts.php
```

Ce fichier retourne tous les contacts enregistrés dans MySQL.

Il renvoie une liste JSON exploitable par Android.

### searchContact.php

Chemin :

```
numberbook-api/api/searchContact.php
```

Ce fichier reçoit un paramètre GET :

```
keyword
```

Exemple :

```
searchContact.php?keyword=ali
```

Il retourne les contacts dont le nom ou le numéro contient ce mot-clé.

## Endpoints API

### Insertion d’un contact

Méthode :

```
POST
```

URL :

```
/insertContact.php
```

Corps JSON attendu :

```
{
  "name": "Ali",
  "phone": "+212600000000"
}
```

Réponse JSON possible :

```
{
  "success": true,
  "message": "Contact inséré avec succès"
}
```

### Récupération de tous les contacts

Méthode :

```
GET
```

URL :

```
/getAllContacts.php
```

Réponse JSON possible :

```
[
  {
    "id": 1,
    "name": "Ali",
    "phone": "+212600000000",
    "source": "mobile",
    "created_at": "2026-01-01 10:00:00"
  }
]
```

### Recherche de contacts

Méthode :

```
GET
```

URL :

```
/searchContact.php?keyword=ali
```

Réponse JSON possible :

```
[
  {
    "id": 1,
    "name": "Ali",
    "phone": "+212600000000",
    "source": "mobile",
    "created_at": "2026-01-01 10:00:00"
  }
]
```

## Configuration Android

### Permissions utilisées

Dans `AndroidManifest.xml`, les permissions suivantes sont utilisées :

```
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.INTERNET" />
```

### READ_CONTACTS

Cette permission permet à l’application de lire les contacts enregistrés dans le téléphone.

Elle doit être demandée à l’exécution, car c’est une permission sensible.

### INTERNET

Cette permission permet à l’application de communiquer avec l’API distante.

Sans elle, Retrofit ne peut pas effectuer les requêtes HTTP.

## Configuration réseau

Le projet utilise un fichier :

```
network_security_config.xml
```

Ce fichier autorise le trafic HTTP non chiffré dans le cadre du TP.

Cela est nécessaire car le backend local XAMPP utilise souvent :

```
http://
```

au lieu de :

```
https://
```

En environnement réel, il faudrait utiliser HTTPS.

## Adresse du backend

Sur l’émulateur Android, l’adresse utilisée est :

```
http://10.0.2.2/numberbook-api/api/
```

Pourquoi `10.0.2.2` ?

Dans un émulateur Android, `localhost` désigne l’émulateur lui-même, pas le PC.

L’adresse spéciale `10.0.2.2` permet à l’émulateur d’accéder au serveur local du PC.

Pour un téléphone réel, il faut utiliser l’adresse IP du PC, par exemple :

```
http://192.168.1.10/numberbook-api/api/
```

Le téléphone et le PC doivent être connectés au même réseau Wi-Fi.

## Dépendances Android

Les dépendances principales sont :

```
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.constraintlayout:constraintlayout:2.2.0")
implementation("androidx.recyclerview:recyclerview:1.4.0")
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
```

## Rôle des dépendances

### AppCompat

Permet d’utiliser une Activity compatible avec plusieurs versions Android.

### Material Components

Permet d’utiliser un thème moderne basé sur Material Design.

### ConstraintLayout

Dépendance classique dans les projets Android récents.

### RecyclerView

Permet d’afficher efficacement une liste de contacts.

### Retrofit

Permet de consommer une API REST avec un code Java clair.

### Gson Converter

Permet à Retrofit de convertir automatiquement :

```
JSON -> objets Java
objets Java -> JSON
```

## Description des fichiers Android

### Contact.java

Chemin :

```
app/src/main/java/com/example/numberbook/model/Contact.java
```

Cette classe représente un contact côté Android.

Elle contient :

```
id
name
phone
source
created_at
```

Elle possède :

* un constructeur vide ;
* un constructeur avec nom et téléphone ;
* des getters ;
* des setters.

Le constructeur vide est utile pour la désérialisation automatique par Gson.

### ApiResponse.java

Chemin :

```
app/src/main/java/com/example/numberbook/model/ApiResponse.java
```

Cette classe représente la réponse de l’API lors de l’insertion.

Elle contient :

```
success
message
```

Exemple de réponse correspondante :

```
{
  "success": true,
  "message": "Contact inséré avec succès"
}
```

### ContactApi.java

Chemin :

```
app/src/main/java/com/example/numberbook/network/ContactApi.java
```

Cette interface décrit les routes de l’API utilisées par Retrofit.

Elle contient :

```
insertContact()
getAllContacts()
searchContacts()
```

### insertContact()

Cette méthode appelle :

```
insertContact.php
```

Elle utilise :

```
@POST
```

et envoie un objet Contact dans le corps de la requête avec :

```
@Body
```

### getAllContacts()

Cette méthode appelle :

```
getAllContacts.php
```

Elle récupère une liste de contacts.

### searchContacts()

Cette méthode appelle :

```
searchContact.php
```

Elle envoie le mot-clé dans l’URL avec :

```
@Query("keyword")
```

### RetrofitClient.java

Chemin :

```
app/src/main/java/com/example/numberbook/network/RetrofitClient.java
```

Cette classe crée une instance unique de Retrofit.

Elle configure :

* l’URL de base ;
* le convertisseur Gson ;
* le singleton Retrofit.

### ContactAdapter.java

Chemin :

```
app/src/main/java/com/example/numberbook/ui/ContactAdapter.java
```

Cette classe permet d’afficher les contacts dans le RecyclerView.

Elle contient :

* une liste de contacts ;
* un ViewHolder ;
* une méthode `updateData()` ;
* `onCreateViewHolder()` ;
* `onBindViewHolder()` ;
* `getItemCount()`.

Le layout utilisé pour chaque ligne est :

```
android.R.layout.simple_list_item_2
```

Ce layout contient deux lignes :

* le nom ;
* le numéro de téléphone.

### MainActivity.java

Chemin :

```
app/src/main/java/com/example/numberbook/ui/MainActivity.java
```

Cette classe gère toute l’interface principale.

Elle contient :

* les boutons ;
* le champ de recherche ;
* le RecyclerView ;
* la liste locale des contacts ;
* l’Adapter ;
* l’objet Retrofit API ;
* la demande de permission ;
* le chargement des contacts ;
* la synchronisation ;
* l’affichage des contacts serveur ;
* la recherche distante.

## Interface utilisateur

Le layout principal est :

```
activity_main.xml
```

Il contient :

* un titre ;
* bouton `CHARGER LES CONTACTS` ;
* bouton `SYNCHRONISER VERS LE SERVEUR` ;
* bouton `AFFICHER CONTACTS SERVEUR` ;
* champ de recherche ;
* bouton `RECHERCHER` ;
* RecyclerView.

## Fonctionnement détaillé du chargement des contacts

Quand l’utilisateur clique sur :

```
CHARGER LES CONTACTS
```

L’application exécute :

```
checkPermissionAndLoadContacts()
```

Cette méthode vérifie si la permission `READ_CONTACTS` est déjà accordée.

Si oui :

```
loadContacts()
```

est appelée directement.

Si non :

```
requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
```

demande l’autorisation à l’utilisateur.

Si l’utilisateur accepte, les contacts sont chargés.

Si l’utilisateur refuse, un message est affiché.

## Fonctionnement de ContentResolver

Android stocke les contacts dans une base système.

L’application ne lit pas directement cette base.

Elle passe par :

```
ContentResolver
```

La requête utilisée est :

```
getContentResolver().query(...)
```

La source utilisée est :

```
ContactsContract.CommonDataKinds.Phone.CONTENT_URI
```

Cette URI permet de récupérer les contacts qui possèdent un numéro de téléphone.

## Fonctionnement du Cursor

La méthode `query()` retourne un objet :

```
Cursor
```

Le Cursor permet de parcourir les résultats ligne par ligne.

La boucle utilisée est :

```
while (cursor.moveToNext())
```

Pour chaque ligne, l’application récupère :

```
DISPLAY_NAME
NUMBER
```

Puis elle crée un objet :

```
new Contact(name, phone)
```

À la fin, le Cursor est fermé avec :

```
cursor.close()
```

C’est important pour libérer les ressources.

## Nettoyage des données locales

Le projet applique un nettoyage simple :

### Nettoyage du nom

```
trim()
```

supprime les espaces inutiles au début et à la fin.

### Nettoyage du numéro

Le numéro est nettoyé en supprimant :

* les espaces ;
* les tirets ;
* les parenthèses.

Cela permet de réduire les doublons.

## Suppression des doublons locaux

Le projet utilise :

```
LinkedHashMap<String, Contact>
```

La clé est le numéro de téléphone.

Cela permet de garder un seul contact par numéro tout en conservant l’ordre d’insertion.

## Fonctionnement de la synchronisation

Quand l’utilisateur clique sur :

```
SYNCHRONISER VERS LE SERVEUR
```

L’application vérifie d’abord si la liste locale est vide.

Si elle est vide, un Toast affiche qu’il n’y a aucun contact à synchroniser.

Sinon, l’application parcourt la liste :

```
for (Contact contact : contactList)
```

Pour chaque contact, elle appelle :

```
contactApi.insertContact(contact).enqueue(...)
```

La méthode `enqueue()` exécute l’appel réseau de façon asynchrone.

Cela évite de bloquer l’interface Android.

## Gestion du résultat de synchronisation

Le projet compte :

* le nombre total de contacts ;
* le nombre de contacts traités ;
* le nombre de succès ;
* le nombre d’erreurs.

À la fin, un Toast affiche :

```
Synchronisation terminée : X succès, Y erreurs
```

## Fonctionnement de l’affichage des contacts serveur

Quand l’utilisateur clique sur :

```
AFFICHER CONTACTS SERVEUR
```

L’application appelle :

```
contactApi.getAllContacts()
```

Le serveur retourne la liste complète des contacts stockés dans MySQL.

Retrofit et Gson convertissent automatiquement le JSON en :

```
List<Contact>
```

Puis l’Adapter met à jour le RecyclerView avec :

```
adapter.updateData(serverContacts)
```

## Fonctionnement de la recherche distante

Quand l’utilisateur saisit un mot-clé puis clique sur :

```
RECHERCHER
```

L’application lit le champ :

```
etKeyword.getText().toString().trim()
```

Si le champ est vide, un message demande de saisir un nom ou un numéro.

Sinon, l’application appelle :

```
contactApi.searchContacts(keyword)
```

Retrofit construit une URL comme :

```
searchContact.php?keyword=ali
```

Le serveur cherche dans MySQL les lignes dont :

```
name LIKE %keyword%
phone LIKE %keyword%
```

Les résultats sont retournés en JSON, convertis en `List<Contact>` puis affichés dans le RecyclerView.

## Installation du backend avec XAMPP

1. Installer XAMPP.

2. Démarrer :

   ```
    Apache
    MySQL
   ```

3. Créer le dossier suivant :

   ```
    C:\xampp\htdocs\numberbook-api\
   ```

4. Copier les dossiers backend :

   ```
    config
    model
    service
    api
   ```

5. Ouvrir phpMyAdmin :

   ```
    http://localhost/phpmyadmin
   ```

6. Importer le fichier SQL ou exécuter les requêtes de création de base.

7. Tester l’API dans le navigateur.

## URLs de test backend

Afficher tous les contacts :

```
http://localhost/numberbook-api/api/getAllContacts.php
```

Rechercher un contact :

```
http://localhost/numberbook-api/api/searchContact.php?keyword=ali
```

Pour tester l’API depuis l’émulateur Android :

```
http://10.0.2.2/numberbook-api/api/getAllContacts.php
```

## Installation et exécution de l’application Android

1. Ouvrir Android Studio.

2. Ouvrir le projet :

   ```
    NumberBook
   ```

3. Vérifier que XAMPP est lancé.

4. Vérifier que Apache et MySQL sont actifs.

5. Vérifier que l’URL de Retrofit est correcte :

   ```
    http://10.0.2.2/numberbook-api/api/
   ```

6. Synchroniser Gradle.

7. Lancer l’application sur un émulateur Android.

8. Cliquer sur :

   ```
    CHARGER LES CONTACTS
   ```

9. Accepter la permission.

10. Cliquer sur :

    ```
    SYNCHRONISER VERS LE SERVEUR
    ```

11. Vérifier la table `contact` dans phpMyAdmin.

12. Cliquer sur :

    ```
    AFFICHER CONTACTS SERVEUR
    ```

13. Tester la recherche avec un nom ou un numéro.

## Tests de validation

### Test 1 : backend accessible

Action :

```
ouvrir getAllContacts.php dans le navigateur.
```

Résultat attendu :

```
une réponse JSON s’affiche.
```

### Test 2 : base créée

Action :

```
ouvrir phpMyAdmin et vérifier la base numberbook.
```

Résultat attendu :

```
la table contact existe.
```

### Test 3 : chargement des contacts

Action :

```
cliquer sur CHARGER LES CONTACTS.
```

Résultat attendu :

```
la permission est demandée et les contacts s’affichent.
```

### Test 4 : refus de permission

Action :

```
refuser la permission READ_CONTACTS.
```

Résultat attendu :

```
un message indique que la permission est refusée.
```

### Test 5 : synchronisation

Action :

```
charger les contacts puis cliquer sur SYNCHRONISER VERS LE SERVEUR.
```

Résultat attendu :

```
les contacts sont envoyés vers MySQL.
```

### Test 6 : affichage distant

Action :

```
cliquer sur AFFICHER CONTACTS SERVEUR.
```

Résultat attendu :

```
les contacts stockés côté serveur s’affichent dans l’application.
```

### Test 7 : recherche par nom

Action :

```
saisir un nom puis cliquer sur RECHERCHER.
```

Résultat attendu :

```
les contacts correspondants s’affichent.
```

### Test 8 : recherche par numéro

Action :

```
saisir une partie d’un numéro puis cliquer sur RECHERCHER.
```

Résultat attendu :

```
les contacts correspondants s’affichent.
```

### Test 9 : recherche vide

Action :

```
cliquer sur RECHERCHER sans saisir de mot-clé.
```

Résultat attendu :

```
un message demande de saisir un nom ou un numéro.
```

### Test 10 : doublons

Action :

```
synchroniser plusieurs fois les mêmes contacts.
```

Résultat attendu :

```
les numéros identiques ne sont pas dupliqués grâce à la contrainte unique.
```


## Dépôt GitHub

Lien du dépôt :

```
https://github.com/wiiam8/NumberBook
```

## Dépannage

### Problème : l’application ne se connecte pas au serveur

Causes possibles :

* Apache n’est pas lancé ;
* l’URL Retrofit est incorrecte ;
* l’émulateur utilise `localhost` au lieu de `10.0.2.2` ;
* le firewall bloque la connexion ;
* le backend n’est pas dans `htdocs`.

Solution :

Vérifier que l’URL utilisée dans `RetrofitClient.java` est :

```
http://10.0.2.2/numberbook-api/api/
```

### Problème : erreur cleartext HTTP

Cause possible :

```
Android bloque le trafic HTTP non chiffré.
```

Solution :

Vérifier dans `AndroidManifest.xml` :

```
android:usesCleartextTraffic="true"
```

Vérifier aussi :

```
network_security_config.xml
```

### Problème : les contacts ne s’affichent pas

Causes possibles :

* permission refusée ;
* téléphone sans contacts ;
* émulateur sans contacts ;
* erreur dans `ContentResolver`.

Solution :

Ajouter des contacts dans l’émulateur ou tester sur un vrai téléphone.

### Problème : permission non demandée

Causes possibles :

* permission absente du Manifest ;
* mauvaise logique de demande runtime.

Solution :

Vérifier :

```
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

Vérifier aussi l’appel :

```
requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
```

### Problème : table introuvable

Cause possible :

```
la table contact n’a pas été créée.
```

Solution :

Exécuter le script SQL dans phpMyAdmin.

### Problème : JSON invalide

Causes possibles :

* erreur PHP affichée avant le JSON ;
* mauvaise configuration de la base ;
* warning PHP visible dans la réponse.

Solution :

Vérifier les fichiers PHP et la connexion PDO.

### Problème : erreur 404

Causes possibles :

* mauvais chemin ;
* dossier mal placé ;
* fichier PHP absent.

Solution :

Vérifier que le chemin existe :

```
C:\xampp\htdocs\numberbook-api\api\getAllContacts.php
```

Tester :

```
http://localhost/numberbook-api/api/getAllContacts.php
```

### Problème : Retrofit retourne onFailure

Causes possibles :

* serveur inaccessible ;
* mauvaise IP ;
* Internet permission absente ;
* endpoint incorrect ;
* téléphone et PC pas sur le même Wi-Fi.

Solution :

Vérifier :

```
<uses-permission android:name="android.permission.INTERNET" />
```

Tester l’URL dans le navigateur du PC.

### Problème : les données sont dupliquées

Cause possible :

```
pas de contrainte unique sur phone.
```

Solution :

Ajouter :

```
UNIQUE KEY unique_phone (phone)
```

ou vider la table puis recréer la structure.

### Problème : Cannot resolve symbol Retrofit

Cause possible :

```
dépendances Retrofit absentes.
```

Solution :

Ajouter :

```
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
```

Puis synchroniser Gradle.

### Problème : Cannot resolve symbol RecyclerView

Cause possible :

```
dépendance RecyclerView absente.
```

Solution :

Ajouter :

```
implementation("androidx.recyclerview:recyclerview:1.4.0")
```

### Problème : Cannot resolve symbol R

Causes possibles :

* erreur XML ;
* fichier ressource invalide ;
* mauvais package ;
* Gradle non synchronisé.

Solution :

Vérifier :

* `activity_main.xml`
* `strings.xml`
* `themes.xml`
* `network_security_config.xml`

Puis faire :

```
Build > Clean Project
Build > Rebuild Project
```

## Bonnes pratiques appliquées

Ce projet applique plusieurs bonnes pratiques :

* demande runtime de permission sensible ;
* séparation entre modèle, réseau et interface ;
* utilisation de Retrofit au lieu de requêtes HTTP manuelles ;
* conversion JSON automatique avec Gson ;
* requêtes serveur préparées avec PDO ;
* fermeture du Cursor après usage ;
* nettoyage simple des numéros ;
* suppression des doublons locaux ;
* contrainte unique côté base ;
* gestion des erreurs réseau ;
* affichage utilisateur avec Toast ;
* utilisation d’une configuration réseau dédiée pour le TP.

## Limites du projet

Ce projet reste un TP pédagogique.

Ses limites principales sont :

* pas d’authentification ;
* pas de HTTPS ;
* pas de chiffrement côté transport en local ;
* pas de pagination ;
* pas de suppression distante ;
* pas de modification distante ;
* pas de synchronisation bidirectionnelle ;
* pas de stockage local Room ;
* pas de gestion avancée des doublons ;
* pas de ProgressBar ;
* pas de gestion complète des erreurs HTTP ;
* pas de design avancé pour les lignes de contacts.

## Améliorations possibles

Pour aller plus loin, il est possible d’ajouter :

* authentification utilisateur ;
* API sécurisée avec token ;
* HTTPS ;
* suppression distante d’un contact ;
* mise à jour distante d’un contact ;
* recherche instantanée ;
* ProgressBar pendant la synchronisation ;
* compteur de succès et d’échecs plus détaillé ;
* stockage local avec Room ;
* synchronisation bidirectionnelle ;
* détection avancée des doublons ;
* normalisation internationale des numéros ;
* affichage avec layout personnalisé ;
* photo du contact ;
* filtre local ;
* pagination côté serveur ;
* architecture MVVM côté Android ;
* Repository Android ;
* ViewModel et LiveData ;
* backend Laravel ou Spring Boot.

## Questions de compréhension

1. Quel est le rôle de `ContentResolver` dans Android ?
2. Pourquoi faut-il demander la permission `READ_CONTACTS` ?
3. Quelle différence existe entre une permission normale et une permission dangereuse ?
4. Pourquoi utiliser `RecyclerView` au lieu de `ListView` ?
5. Quel est le rôle de Retrofit ?
6. Pourquoi utiliser `enqueue()` au lieu d’un appel synchrone ?
7. Pourquoi le backend renvoie-t-il du JSON ?
8. Pourquoi le numéro de téléphone est-il stocké sous forme de chaîne ?
9. Quel est l’intérêt des requêtes préparées en PHP ?
10. Pourquoi `10.0.2.2` est utilisé avec l’émulateur Android ?
11. Pourquoi faut-il fermer un Cursor ?
12. Pourquoi éviter le HTTP en production ?
13. Quel est le rôle de Gson Converter ?
14. Pourquoi utiliser une contrainte unique sur le numéro ?
15. Comment améliorer la synchronisation dans une vraie application ?

## Synthèse pédagogique

Ce TP montre comment construire une application Android connectée à une API distante.

La partie Android permet de comprendre :

* l’accès aux contacts ;
* la gestion des permissions ;
* l’affichage d’une liste ;
* la communication réseau ;
* la conversion JSON ;
* la gestion des réponses.

La partie backend permet de comprendre :

* la structure d’une API PHP ;
* la connexion à MySQL ;
* l’insertion de données ;
* la recherche SQL ;
* le retour JSON ;
* la séparation entre configuration, service et API.

Le projet relie donc directement le monde mobile et le monde backend.

## Conclusion

L’application **Number Book** est une application complète et réaliste permettant de lire les contacts d’un téléphone, de les afficher, de les synchroniser vers une base distante et de les rechercher via une API.

Ce laboratoire est important car il combine plusieurs compétences essentielles du développement mobile :

* accès aux données système Android ;
* permissions ;
* RecyclerView ;
* Retrofit ;
* API REST ;
* backend PHP ;
* MySQL ;
* JSON ;
* synchronisation client-serveur.

Ce projet constitue une excellente base pour aller vers des applications plus avancées, notamment avec Room, MVVM, authentification, synchronisation bidirectionnelle et API sécurisée.


## Lab

TP 20 : Application Number Book avec Android, Contacts et API distante via Retrofit
