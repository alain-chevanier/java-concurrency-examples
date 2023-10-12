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

En el paquete `unam.ciencias.computoconcurrente.threadobjects` tenemos ejemplos de los siguientes temas:
* Hilos en Java
* Interrumpir hilos
* Bloquear hilos

En el paquete `unam.ciencias.computoconcurrente.synchronization` tenemos ejemplos de cómo resolver el problema de la 
sección crítica nativamente en java.
* synchronized - intrinsic lock - reentrant lock
* regular lock

En el paquete `unam.ciencias.computoconcurrente.soexamples` tenemos ejemplos relacionados con los slides del capítulo
de _Process Synchronization_ del libro de operating system concepts de Silberschatz.


