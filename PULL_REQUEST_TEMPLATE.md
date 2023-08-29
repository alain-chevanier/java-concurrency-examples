## Group Members
<full_name> <email>
<full_name> <email>

## Description
_Insert a description of the feature you are implementing or bug you are fixing. Context about why the change matters is very helpful, as are before and after screenshots._

## Test Plan
_Insert a thorough, step-by-step test plan here. Make sure to provide everything a reviewer will need to verify that your changes actually work._

## Merge Checklist
- [ ] The code has been commented, particularly in hard-to-understand areas.


# Concurrencia VS Paralelismo

## Paralelismo
Ejecutar tareas al mismo tiempo. Necesitamos más un core en nuestra computadora. Puedo ejecutar tantas
tareas en paralelo como el número de cores que yo tenga.

## Concurrencia
Ejecutar varias tareas y asignar un _time slice_ a cada una de ellas.
Supongamos que tenemos 10 hilos y solamente 2 cores. Scheduler.

## Scheduler
Entre la lista de hilos en estado ready/running elige a cual de ellos asignarle algún core.


# Race Conditions

## Read Modify Write
```java
int counter = 0;
Lock lock = new ReentrantLock();

lock.lock();
try {
    counter++;    
} finally {
    lock.unlock();    
}

```

## Check And Do

`T_0`:
```java
synchronized(this) {
    if (value == 0) {
        System.out.println("Value: " + (value + 1));
        // si T_1 se ejecuta concurrentemente esto no va a necesariamente un 1
    }    
}

```

`T_1`:
```java
synchronized(this) {
    for (int i = 0l i < 1000000000; i++) {
        value++;
    }    
}
```