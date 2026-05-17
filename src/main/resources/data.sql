INSERT INTO KORISNIK (email, lozinka, ime, prezime) VALUES
('josip.hajpek@fer.hr', '$2a$12$EsPIWD/otVDzOHVGFoR9vehktCuCixwpr4VGB.893oUF1PSn32NT2', 'Josip', 'Hajpek'),
('ivo.ivic@gmail.com', '$2a$12$6r2IcxUX3z/HXewUPneKxempbv0TEJkhb2n2BRCQqPk7ee7UM4hdO', 'Ivo', 'Ivić'),
('ana.anic@gmail.com', '$2a$12$6Awd.h4VCeSqkSuzwvq.GuO8VlklBVVdNYkXhW14xnbJosOiQpbJ6', 'Ana', 'Anić'),
('marija.maric@gmail.com', '$2a$12$IjvwlOM5ApbbaIvV9mAcSOBSyTfkGOQzEzi6/L9wWB0DAdlRol2HW', 'Marija', 'Marić');

INSERT INTO ADMINISTRATOR (korisnik_id) VALUES (1);

INSERT INTO KUPAC (korisnik_id) VALUES (2), (3), (4);

INSERT INTO LOKACIJA (ulica, kucni_broj, postanski_broj, mjesto) VALUES
('Ulica Vice Vukova', 8, '10000', 'Zagreb'),
('Trg Stjepana Radića', 4, '10000', 'Zagreb'),
('Tončićeva ulica', 1, '21000', 'Split'),
('Ulica Kneza Trpimira', 23, '31000', 'Osijek'),
('Vukovarska ulica', 269, '10000', 'Zagreb'),
('Obala Hrvatskog narodnog preporoda', 22, '21000', 'Split'),
('Korzo', 28, '51000', 'Rijeka'),
('Ulica Hrvatske Republike', 19, '31000', 'Osijek'),
('Zrinsko Frankopanska ulica', 20, '23000', 'Zadar'),
('Šetalište Franje Tuđmana', 1, '20000', 'Dubrovnik');

INSERT INTO DVORANA (naziv, lokacija_id) VALUES
('Koncertna dvorana Vatroslava Lisinskog', 2),
('Koncertna dvorana Ive Tijardovića', 3),
('Dvorana Gradski vrt', 4);

INSERT INTO DOGADAJ (naziv, kategorija, opis, datum_vrijeme_odrzavanja, dvorana_id) VALUES
('TBF & CROATIAN RADIOTELEVISION JAZZ ORCHESTRA', 'GLAZBA', 'Koncertna suradnja kultnog zagrebačkog sastava TBF i Jazz orkestra Hrvatske radiotelevizije donosi spoj hip hopa, funka i jazz aranžmana u jedinstvenom glazbenom iskustvu.', '2026-06-11 20:00:00', 1),
('MARE #zenamajkaglumica', 'DRAMA', 'Ovaj autorski projekt Marijane Mikulić osobna je priča u kojoj bez uljepšavanja progovara o odnosima muškaraca i žena, braku, majčinstvu te pokušajima da uskladi majčinstvo, posao i sve ono što se dogodi između. Iskrena i autentična, kao i uvijek, Mare u ovoj autobiografskoj priči otvara trenutke u kojima se mnogi prepoznaju, ali ne izgovaraju to često naglas.Predstava je namijenjena publici u dobi od 15+.', '2026-06-25 20:00:00', 1),
('Lucija Dujmović, klarinet', 'GLAZBA', 'Mlada klarinetistica Lucija Dujmović predstavlja večer klasične glazbe uz izvedbe domaćih i stranih skladatelja za klarinet i klavir.', '2026-06-12 20:30:00', 2),
('Koncert "Tragom Olivera"', 'GLAZBA', 'Glazbena priča posvećena Oliveru Dragojeviću. Večer će oživjeti uz pjesme koje poznaju svi: "Moj lipi anđele", "Nadalina", "Nedostaješ mi ti", "Trag u beskraju", "Oprosti mi pape" i mnoge druge. Na pozornicu Gradskog vrta 25. travnja uspinju se Oliverovi Dupini i njegovi dugogodišnji suradnici Zorica Kondža, Tedi Spalato, Goran Karan i Petar Dragojević.', '2026-06-25 21:00:00', 3),
(
 '2Cellos Reunion Tour',
 'GLAZBA',
 'Povratnički koncert dua 2Cellos uz goste iznenađenja.',
 '2026-07-15 20:00:00',
 1
),

(
 'Hamlet',
 'DRAMA',
 'Shakespeareova tragedija u modernoj adaptaciji.',
 '2026-07-20 19:30:00',
 2
),

(
 'Ballet Gala Night',
 'BALET',
 'Večer suvremenog i klasičnog baleta.',
 '2026-08-05 19:00:00',
 1
);

INSERT INTO SEKTOR (naziv, kapacitet, dvorana_id) VALUES
('PARTER', 1900, 1),
('PARTER', 216, 2),
('BALKON', 34, 2),
('ZAPADNA TRIBINA', 1200, 3),
('ISTOČNA TRIBINA', 1200, 3),
('JUŽNA TRIBINA', 600, 3),
('SJEVERNA TRIBINA', 600, 3),
('VIP', 100, 1),
('VIP', 20, 2),
('BALKON', 300, 1);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 20), niz.broj, 1
FROM generate_series(1, 1900) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 12), niz.broj, 2
FROM generate_series(1, 216) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 17), niz.broj, 3
FROM generate_series(1, 34) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 50), niz.broj, 4
FROM generate_series(1, 1200) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 50), niz.broj, 5
FROM generate_series(1, 1200) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 25), niz.broj, 6
FROM generate_series(1, 600) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 25), niz.broj, 7
FROM generate_series(1, 600) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 10), niz.broj, 8
FROM generate_series(1, 100) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 5), niz.broj, 9
FROM generate_series(1, 20) AS niz(broj);

INSERT INTO SJEDALO (red, broj, sektor_id)
SELECT ceil(niz.broj::numeric / 15), niz.broj, 10
FROM generate_series(1, 300) AS niz(broj);

INSERT INTO CJENIK
(cijena, dogadaj_id, sektor_id) VALUES
(30.00, 1, 1),
(26.50, 2, 1),
(15.00, 3, 2),
(10.00, 3, 3),
(30.00, 4, 4),
(30.00, 4, 5),
(25.00, 4, 6),
(25.00, 4, 7),
(45.00, 1, 8),
(35.00, 1, 10),
(40.00, 2, 8),
(18.00, 3, 9),
(40.00, 5, 1),
(70.00, 5, 8),
(30.00, 5, 10),
(22.00, 6, 2),
(35.00, 6, 9),
(28.00, 7, 1),
(50.00, 7, 8),
(20.00, 7, 10);

INSERT INTO REZERVACIJA (datum_vrijeme_isteka, korisnik_id, dogadaj_id, sjedalo_id) VALUES
('2026-03-21 10:10:42', 2, 1, 10),
('2026-03-21 10:13:03', 3, 1, 31),
('2026-03-21 10:15:12', 4, 1, 100),
('2026-03-21 10:15:30', 4, 1, 101),
('2026-03-25 14:31:21', 3, 2, 50),
('2026-03-30 09:12:22', 4, 3, 1901),
('2026-03-31 19:18:10', 3, 3, 1987),
('2026-04-02 12:05:00', 2, 4, 2200),
('2026-04-02 12:05:10', 2, 4, 2201),
('2026-04-02 12:05:20', 2, 4, 2202),
('2026-04-04 15:31:43', 2, 3, 1909),
('2026-04-06 12:02:16', 3, 4, 2531),
('2026-06-10 12:00:00', 2, 5, 50),
('2026-06-10 12:05:00', 3, 5, 60),
('2026-06-15 15:00:00', 4, 6, 1910);

INSERT INTO ULAZNICA (qr_kod, datum_vrijeme_kupnje, korisnik_id, dogadaj_id, sjedalo_id) VALUES
('QR-TBF-0001', '2026-03-21 10:02:24', 2, 1, 10),
('QR-TBF-0002', '2026-03-21 10:05:17', 3, 1, 31),
('QR-TBF-0003', '2026-03-21 10:10:10', 4, 1, 100),
('QR-TBF-0004', '2026-03-21 10:10:59', 4, 1, 101),
('QR-MARE-0001', '2026-03-25 14:20:12', 3, 2, 50),
('QR-LUCIJA-0001', '2026-03-30 09:01:32', 4, 3, 1901), 
('QR-LUCIJA-0002', '2026-03-31 19:11:46', 3, 3, 1987),
('QR-OLIVER-0001', '2026-04-02 12:04:06', 2, 4, 2200),
('QR-OLIVER-0002', '2026-04-02 12:04:20', 2, 4, 2201),
('QR-OLIVER-0003', '2026-04-02 12:04:31', 2, 4, 2202),
('QR-2CELLOS-0001', '2026-06-10 11:00:00', 2, 5, 50),
('QR-2CELLOS-0002', '2026-06-10 11:05:00', 3, 5, 60),
('QR-HAMLET-0001', '2026-06-15 14:00:00', 4, 6, 1910);