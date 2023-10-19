## Problema

La clase `Rooms` administra una colección de _cuartos_, indexados de `0` hasta `m-1` 
(`m` es el argumento del constructor). Los _threads_ pueden entrar o salir de cualquier 
_cuarto_ en el rango. Cada _cuarto_ puede contener un número arbitrario de _threads_
simultáneamente, pero solamente un _cuarto_ puede estar ocupado al mismo tiempo. 
Por ejemplo, si hay dos cuartos, indexados con `0` y `1` respectivamente, entonces
cualquier número de _threads_ pueden entrar al _cuarto_ `0`, pero ninguno puede entrar
al cuarto `1` mientras el cuarto `0` está ocupado. La clase `unam.ciencias.computoconcurrente.Rooms`
muestra el código base de la clase descrita. A cada _cuarto_ se le puede asignar un `exitHandler`
llamando al método `setExitHandler(i, methodReference)`, donde `i` es el índice del _cuarto_ y 
`methodReference` el `exitHandler` correspondiente. El `exitHandler` es llamado por el último
_thread_ qie sale de un _cuarto_, pero antes de que algún _thread_ subsecuente entre a algún otro
_cuarto_, este método es llamado solamente una vez y mientras está corriendo, no deben de haber 
_threads_ en ningún cuarto.

#### Especificación del programa

1. Si un _thread_ está en el _cuarto_ `i`, entonces ningún thread puede estar en un cuarto `j` si `j!=i`.
2. El último _thread_ que sale de un _cuarto_, llama el _exit handler_ de dicho _cuarto_. Y ningún cuarto
   puede estar ocupado mientras el handler está corriendo.
3. Tu implementación debe de ser justa, es decir, cualquier _thread_ que intenta entrar a un _cuarto_,
   eventualmente tiene éxito. (puedes asumir que todos los _threads_ que entran a algún _cuarto_ eventualmente
   salen)
