create table swe2Filiale (
  id int auto_increment primary key,
  bezeichnung varchar(99) not null,
  adresse varchar(99) not null
);

create table swe2Mitarbeiter (
  id int auto_increment primary key,
  name varchar(99) not null,
  vorname varchar(99) not null,
  rolle varchar(99) not null,
  email varchar(99) unique,
  telefon varchar(99),
  swe2Filiale_id int not null,

  constraint fk_swe2Mitarbeiter_swe2Filiale
  foreign key (swe2Filiale_id)
  references swe2Filiale(id)
  on delete restrict
  on update cascade
);

create table swe2Verlag (
  id int auto_increment primary key,
  name varchar(99) not null,
  adresse varchar(99),
  telefon varchar(99),
  email varchar(99)
);

create table swe2Buch (
  id int auto_increment primary key,
  titel varchar(99) not null,
  isbn varchar(99) unique not null,
  erscheinungsjahr year,
  genre varchar(99),
  seiten int,
  sprache varchar(99),
  swe2Verlag_id int not null,

  constraint fk_swe2Buch_swe2Verlag
  foreign key (swe2Verlag_id)
  references swe2Verlag(id)
  on delete restrict
  on update cascade
);

create table swe2Author (
  id int auto_increment primary key,
  vorname varchar(99) not null,
  nachname varchar(99) not null,
  ident varcher(99)
);

create table swe2Author_swe2Buch (
  swe2Author_id int not null,
  swe2Buch_id int not null,

  primary key (swe2Author_id, swe2Buch_id),

  constraint fk_swe2Authorbuch_swe2Author
  foreign key (swe2Author_id)
  references swe2Author(id)
  on delete cascade
  on update cascade,

  constraint fk_swe2Authorbuch_swe2Buch
  foreign key (swe2Buch_id)
  references swe2Buch(id)
  on delete cascade
  on update cascade
);

create table swe2Lagerort (
  id int auto_increment primary key,
  regal varchar(99) not null,
  fach varchar(99),
  etage varchar(99),
  swe2Filiale_id int not null,

  constraint fk_swe2Lagerort_swe2Filiale
  foreign key (swe2Filiale_id)
  references swe2Filiale(id)
  on delete cascade
  on update cascade
);

create table swe2Exemplar (
  id int auto_increment primary key,
  inventarnummer varchar(99) unique not null,
  status varchar(99),
  zustand varchar(99),
  notitz varchar(99),

  swe2Buch_id int not null,
  swe2Lagerort_id int not null,

  constraint fk_swe2Exemplar_swe2Buch
  foreign key (swe2Buch_id)
  references swe2Buch(id)
  on delete restrict
  on update cascade,

  constraint fk_swe2Exemplar_swe2Lagerort
  foreign key (swe2Lagerort_id)
  references swe2Lagerort(id)
  on delete restrict
  on update cascade
);

create table swe2Kunde (
  id int auto_increment primary key,
  vorname varchar(99) not null,
  nachname varchar(99) not null,
  email varchar(99) unique,
  telefon varchar(99),
  adresse varchar(99),
  status varchar(99),
  registriert_am date default (current_date)
);

create table swe2Ausleihe (
  id int auto_increment primary key,
  swe2Kunde_id int not null,
  swe2Mitarbeiter_id int not null,
  ausleihdatum date default (current_date),
  status varchar(99) default 'aktiv',

  constraint fk_swe2Ausleihe_swe2Kunde
  foreign key (swe2Kunde_id)
  references swe2Kunde(id)
  on delete restrict
  on update cascade,
  
  constraint fk_swe2Ausleihe_swe2Mitarbeiter
  foreign key (swe2Mitarbeiter_id)
  references swe2Mitarbeiter(id)
  on delete restrict
  on update cascade
);

create table swe2Ausleihe_position (
  id int auto_increment primary key,

  swe2Ausleihe_id int not null,
  swe2Exemplar_id int not null,

  rueckgabeDatum date,

  constraint fk_swe2Ausleiheposition_swe2Ausleihe
  foreign key (swe2Ausleihe_id)
  references swe2Ausleihe(id)
  on delete cascade
  on update cascade,

  constraint fk_swe2Ausleiheposition_swe2Exemplar
  foreign key (swe2Exemplar_id)
  references swe2Exemplar(id)
  on delete restrict
  on update cascade
);
