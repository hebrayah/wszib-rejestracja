# Rejestracja użytkowników z zastosowaniem wzorców Proxy i Łańcucha Odpowiedzialności

## Opis zadania
Celem jest stworzenie aplikacji do rejestracji użytkowników, gdzie walidacja danych (np. e-mail, hasło) będzie realizowana za pomocą wzorca Proxy. Wzorzec Łańcuch Odpowiedzialności będzie używany do przeprowadzenia walidacji krok po kroku. Aplikacja powinna obsługiwać wyjątki (np. błędne formaty danych) oraz wykorzystywać typy generyczne do przetwarzania różnych typów danych wejściowych.

Wzorce projektowe:
- Proxy: Walidacja danych wejściowych (e-mail, hasło) przed ich rejestracją.
- Łańcuch Odpowiedzialności: Przeprowadzanie walidacji krok po kroku (np. walidacja e-maila, a potem hasła).

Wejście:
- Dane użytkownika wprowadzone ręcznie z klawiatury lub z pliku JSON:
  JSON: { "username": "jdoe", "email": "jdoe@example.com", "password": "Password123" }
- Klawiatura: Wprowadzanie danych użytkownika.

Wyjście:
- Komunikat o poprawnej rejestracji lub o błędach walidacyjnych.
- Przykład: Rejestracja zakończona sukcesem lub Błąd: niepoprawny e-mail.

Zakres danych:
- Użytkownicy: login, e-mail, hasło.
- Walidacja danych (poprawność e-maila, minimalna długość hasła).

Oczekiwane działanie:
- Aplikacja powinna walidować dane użytkownika za pomocą Proxy oraz Łańcucha Odpowiedzialności.
- Obsługa wyjątków powinna zwracać komunikaty o błędach w przypadku niepoprawnych danych.

Techniki Java:
- Mechanizm wyjątków: Obsługa błędów walidacji.
- Typy generyczne: Przetwarzanie różnych typów danych wejściowych (np. e-mail, hasło).

Zadania rozszerzające:
- Zaawansowana walidacja hasła: Dodanie bardziej złożonych kryteriów walidacyjnych dla hasła (np. obecność cyfr, wielkich liter i znaków specjalnych).
- Wielowątkowość: Wprowadzenie rejestracji wielu użytkowników jednocześnie z wykorzystaniem wielowątkowości.

## Realizacja

Aplikacja:
- W celu naturalnej obsługi wielu użytkowników (rozszerzenie), dane są wprowadzane w formie zapytania REST, zgodnie z zadaną specyfikacją obiektu użytkownika.
- Kontroler REST został zbudowany przy użyciu `spring-boot`.
- Klasy zostały przetestowane jednostkowo, używając Junit 5 oraz Mockito.

Wielowątkowość:
- Wielowątkowość została osiągnięta, poprzez operatora `paralel`, wykonanego na strumieniu, którego źródłem jest wejściowa lista użytkowników.
  (Użycie różnych wątków jest widoczne w logach aplikacji)

Wzorce projektowe:
- *Pośrednik* - użycie tego wzorca podczas walidacji wydaje się pretekstowe, dlatego dodałem takie scenariusze, gdzie zastosowanie go może przynieść jakieś benefity - np. ponieważ hasła są weryfikowane w usłudze sieciowej,
co może być kosztowne, więc wywołujemy ją, dopiero kiedy trywialne reguły walidacji są spełnione. 
    - "Lekka" (Proxy Object) część walidacji, zarówno hasła jak i adresu email została zrealizowana przy użyciu wyrażenia regularnego.    
    - "Ciężka" (Target Object) część w przypadku adresu email, to weryfikacja DNS, w przypadku hasła to użycie usługi sieciowej. 
    - Interfejs (Subject) to Consumer<String>.
- *Łańcuch odpowiedzialności* - Każde pole obiektu `User` jest weryfikowane poprzez osobne ogniwo łańcucha odpowiedzialności. 
    - Każde z ogniw łańcucha deleguje walidację to stosownego obiektu Proxy.

Obsługa wyjątków:
- *Wyjątki* Walidacja danych wydaje się być właściwym użyciem interfejsu funkcyjnego Consumer<> - ponieważ działąnie odbywa się poprzez efekty uboczne (wyjątki).
- W celu demonstracji, Walidacja hasła oraz emaila definiuje swoje klasy wyjątków.
- Wyjątki specyficzne są mapowane to głównego `ValidationException`, a następnie mapowane do odpowiedzi JSON przez error handler kontrolera REST. 

Typy Generyczne:
- Interfejsy funkcyjne UnaryOperator, Function, Consumer są operatorami generycznymi,
- Nie jest to najszczęśliwszy wybór do demonstrowania typów generycznych, ponieważ każda z właściwości obiektu User jest typu String. 

## Użycie

- Przykładowy Request:

    {
      "requestedUsers" : [
        {
          "username" : "costam",
          "email" : "koza@gmail.com",
          "password" : "aaaaDD445$kjhAdfg1"
        }
      ]
    }

- Wymagania nazwy użytkownika:
  - Długość od 3 do 10 znaków,
  - Dowolone duże i małe litery, cyfry oraz znak kropki.
- Wymagania adresu email:
  - Format zgodny z RFC 2822,
  - Domena adresu musi istnieć i mieć zarejestrowany MX server.
- Wymagania hasła
  - długość od 8 do 32 znaków,
  - Wymagane klasy znaków to małe i duże litery, cyfry, znaki specjalne (@#$%^&+=.!).
  - Hasło nie jest notowane w bazach serwisu HaveIBeenPwned. 