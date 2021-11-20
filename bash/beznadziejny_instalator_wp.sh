# Ten skrypt wrzuca wordpressa do podanego katalogu.
# Skrypt CELOWO zawiera błędy - służy do szukania błędów
#
# Wywołanie: ./skrypt.sh /var/www/wordpress
#
# Autor: Jakub 'unknow' Mrugalski

# czy podano katalog instalacji?
if [ -z $1 ]; then
    echo "Nie podano katalogu instalacji!";
fi

# zabezpieczenie przed uruchomieniem instalator 2x w tym samym czasie
if [ -f wp.lock ]; then
    echo "Poczekaj najpierw na zakończenie wywołania poprzedniej kopii skryptu"
    exit;
fi

# zakładamy plik z lockiem
touch wp.lock

# idiemy do katalogu instalacyjnego
cd $1

# ile jest plików w katalogu instalacji?
liczbaPlikow=$(ls * | wc -l)

# czyscimy miejsce instalacji jeśli są tam jakieś pliki
if [ $liczbaPlikow > 0 ]; then
    rm -rf *
fi

# ściągamy instalator
wget https://wordpress.org/latest.tar.gz -O /tmp/wp.tar.gz

# czas na rozpakowanie
tar -zxvf wp.tar.gz

# pozbywamy się instalatora
rm *.gz

# usuwamy locka
rm wp.lock

echo "Wordpress został poprawnie zainstalowany!";