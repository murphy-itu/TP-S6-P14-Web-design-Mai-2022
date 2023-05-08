create database examenwebdesign;

\c examenwebdesign;

create table utilisateur (
    id serial primary key,
    nom varchar(255),
    prenom varchar(255),
    email varchar(255),
    pseudo varchar(255),
    motdepasse varchar(255),
    type varchar(255)
);

create table photo (
    id serial primary key,
    nom varchar(255),
    base64 text
);

create table categorie (
    id serial primary key,
    libelle varchar(255)
);

create table article (
    id serial primary key,
    idphoto integer references photo(id),
    titre varchar(255),
    idcategorie integer references categorie(id),
    resume text,
    contenu text
);

create table token (
    id serial primary key,
    idutilisateur integer references utilisateur(id),
    token text,
    delai timestamp
);


insert into categorie values (default,'Apprentissage automatique');
insert into categorie values (default,'Reseaux de neurones');
insert into categorie values (default,'Traitement du language naturel');
insert into categorie values (default,'Robotique');
insert into categorie values (default,'Vision par ordinateur');
insert into categorie values (default,'Systeme experts');
insert into categorie values (default,'Ethique de IA');
insert into categorie values (default,'Santé');

insert into article values (default,1,'Les avancees de l''intelligence artificielle dans le secteur de la sante',8,'L''intelligence artificielle est en train de revolutionner le secteur de la santé grâce à ses capacités de diagnostic et de traitement','<p>L''<strong>IA</strong> est capable de détecter les signes précoces de <strong>maladies</strong> et de fournir <strong>des traitements personnalisés</strong> en fonction des caractéristiques de chaque patient. Les <strong>algorithmes d''IA</strong> peuvent également aider les <strong>médecins</strong> à prendre des décisions plus éclairées en matière de traitement. Les applications de <strong>l''IA</strong> dans le <strong>domaine de la santé</strong> sont prometteuses et pourraient changer la façon dont nous prenons soin de notre <strong>santé</strong> à l''avenir</p>');
