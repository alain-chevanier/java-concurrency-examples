# Computación Concurrente - Introducción a Hilos 

## Equipo de enseñanza
* Ricchy Alaín Pérez Chevanier <alain.chevanier@ciencias.unam.mx>


## Desarrollo
En esta práctica trabajarás con una base de código construida con Java 11 y Maven Wrapper, 
también proveemos pruebas unitarias escritas con la biblioteca **Junit 5.7.2** que te 
darán retrospectiva inmediatamente sobre el funcionamiento de tu implementación.

Para ejecutar las pruebas necesitas ejecutar el siguiente comando:

```
$ ./mvnw test
```

Para ejecutar las pruebas contenidas en una única clase de pruebas, utiliza 
un comando como el siguiente:

```
$ ./mvnw -Dtest=MyClassTest test
```

En el código que recibirás la clase **App** tiene un método __main__ que puedes ejecutar 
como cualquier programa escrito en __Java__. Para eso primero tienes que empaquetar 
la aplicación y finalmente ejecutar el jar generado. Utiliza un comando como el que 
sigue:

```
$ ./mvnw package
... o saltando las pruebas unitarias
$ ./mvnw package -DskipTests
...
...
$ ./mvnw exec:java 
```

## Configuración de los git hooks para formatear el código

Antes de empezar a realizar commits que contenga tu solución
tienes que configurar un módulo de git que te ayudará a 
formatear tu código.

```
./mvnw git-code-format:install-hooks
```

## Forma de trabajo

Recomendamos ampliamente utilizar el editor [IntelliJ](https://www.jetbrains.com/help/idea/installation-guide.html)
para realizar el desarrollo de la práctica.
También agrega el plugin de IntelliJ [SonarLint](https://www.sonarsource.com/products/sonarlint/features/jetbrains/).

## Agenda

* Hilos en Java
* Interrumpir hilos
* Bloquear hilos


