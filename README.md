# Backend Java Spring Boot

//TODO(21.12.2022) README.md 

## Kontekst
* [Opis i cel projektu](#opis-i-cel-projektu)
* [Instrukcje dotyczące instalacji i uruchamiania projektu](#instrukcje-dotyczące-instalacji-i-uruchamiania-projektu)
* [Linki do dokumentacji i innych zasobów](#linki-do-dokumentacji-i-innych-zasobów)
* [Informacje o autorach i licencji projektu](#informacje-o-autorach-i-licencji-projektu)

## Opis i cel projektu
Celem projektu jest zrealizowanie funkcjonlaności projektu inżynierskiego - Strona do wsparcia w czasie kryzysu związanego z wojną.
W zakres tych funkcjonlaności wchodzi utworzenie połączenia z siecią IPFS, realizacja endpointów do pozyskiwania, zmieniania i usuwania danych dotyczących informacji o broni, pierwszej pomocy, wskazówek dotyczących przetrwania i funkcjonalności mapy.

## Instrukcje dotyczące instalacji i uruchamiania projektu
Aby uruchomić projekt najpierw należy posiadać zainstalowanego [Dockera](https://www.docker.com/products/docker-desktop/) na swoim komputerze.
Następnie należy skolonować oba repozytoria (front-end i backend aplikacji) i kolejno wywołać komendy:
####a.  w folderze zawierającym, projekt backendu aplikacji:
```bash
docker build -t back-end .
```
#### b.w folderze zawierającym frontend aplikacji należy wpisać:
```bash
docker build -t front-end .
```
Ostatni krok uruchomienia aplikacji polega na wywołaniu polecenia w folderze zawierającym oba projekty:
```bash
docker-compose up -d
```
oraz uruchomieniu aplikacji w przeglądarce pod adresem [localhost:3000](http://localhost:3000/)

## Linki do dokumentacji i innych zasobów
- [Podłączenie do sieci IPFS](https://github.com/ipfs-shipyard/java-ipfs-http-client)
- [Podłączenie backendu aplikacji do PostgreSQL](https://www.baeldung.com/spring-boot-postgresql-docker)

## Informacje o autorach i licencji projektu
Autorami repozytorium są:
- [Mikołaj Noga](https://github.com/MikolajNoga)
- [Zuzanna Borkowska](https://github.com/ZuZa1235012350)
