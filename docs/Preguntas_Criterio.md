# Criterios de Decisión: Estructuras de Datos en la Red de Sensores

Este documento es la parte más importante de tu entrega. No basta con que el código funcione; debes justificar **por qué** elegiste una colección de Java sobre otra.

Responde a las siguientes preguntas basándote en la teoría de nodos y el rendimiento del JCF.

### 1. El dilema del Historial
Para el `historialEventos`, podrías usar un `ArrayList` o un `LinkedList`.
- Si la plataforma registra miles de eventos por segundo y necesitas eliminar los más antiguos frecuentemente para ahorrar memoria, ¿cuál de las dos colecciones es más eficiente y por qué? (Pista: piensa en el costo de eliminar el primer elemento).
- Elegiría LinkedList, porque permite eliminar elementos de una lista con un costo menor cuando ya se tiene ubicada la posición del elemento. En una ArrayList, al eliminar un elemento, los demás deben desplazarse para ocupar el espacio que queda libre, lo que puede generar un costo de O(n). En cambio, una LinkedList solo necesita modificar los enlaces entre los nodos para realizar la eliminación, lo que puede hacerse en O(1) si ya se tiene la referencia al nodo. Por eso, para un sistema que elimina eventos antiguos constantemente, LinkedList puede ser una mejor alternativa.

### 2. Navegación Bidireccional
En el `navegadorAlertas`, utilizamos un `Deque` (Double Ended Queue).
- ¿Qué ventaja ofrece un `Deque` sobre una `Queue` simple para implementar la funcionalidad de "Atrás" y "Adelante"?
- ¿Cómo se relaciona esto con el concepto de un `NodoDoble`?
- La interfaz Queue trabaja principalmente bajo el principio FIFO (First In, First Out), es decir, el primer elemento que ingresa es el primero en salir. Por otro lado, Deque permite insertar y eliminar elementos tanto al inicio como al final de la estructura. Esto se relaciona con NodoDoble, ya que este tipo de nodo tiene dos referencias: una hacia el nodo anterior y otra hacia el siguiente. Gracias a estas conexiones es posible recorrer y modificar la estructura en ambas direcciones, lo que facilita implementar el comportamiento de una lista doblemente enlazada o una deque.

### 3. El Ciclo de Monitoreo
Para la `colaMonitoreo`, implementaste una rotación (sacar del frente y meter al final).
- ¿Por qué una `Queue` es la estructura natural para este problema?
- ¿Qué pasaría con la equidad del sistema si usaras una `Stack` (Pila) en su lugar?
- Una cola es apropiada para el monitoreo de sensores porque permite procesar las lecturas en el mismo orden en que llegan, siguiendo el principio FIFO. Esto garantiza que los eventos más antiguos sean atendidos primero y evita que las lecturas nuevas tengan prioridad sobre las anteriores. Si se utilizara una pila (Stack), se aplicaría el principio LIFO (Last In, First Out), haciendo que las últimas lecturas recibidas se procesen primero. Esto podría provocar que algunos eventos antiguos esperen demasiado tiempo o incluso queden sin procesar si siguen llegando nuevos eventos, afectando la equidad del sistema.

### 4. Topología de Red (El Grafo)
Representaste la red de sensores como un `Map<String, List<String>>`.
- ¿Por qué usamos un `Map` y no un arreglo bidimensional (matriz de adyacencia) considerando que la mayoría de los sensores solo están conectados a 2 o 3 vecinos?
- ¿Cómo se traduce esta estructura de `Map` a la idea de un `NodoGrafo` con una lista de adyacencias?
- La estructura Map<String, List<String>> representa una lista de adyacencia, donde cada elemento del mapa representa un nodo y su lista contiene los nodos con los que está conectado. Su principal ventaja es que utiliza menos memoria cuando el grafo tiene pocas conexiones, ya que solamente almacena las relaciones existentes. En cambio, una matriz de adyacencia utiliza una matriz de tamaño V × V, donde V representa la cantidad de nodos, por lo que puede consumir más memoria cuando existen pocas conexiones. Sin embargo, la matriz permite consultar de manera más rápida si existe una conexión entre dos nodos, mientras que en una lista de adyacencia se debe recorrer la lista correspondiente. Por eso, elegiría Map<String, List<String>> para representar redes de sensores con pocas conexiones entre sus dispositivos.

### 5. Buffer de Ingesta y Rendimiento
El `bufferIngesta` procesa datos en orden FIFO.
- Si el volumen de datos aumenta drásticamente y necesitas procesar las lecturas en paralelo usando múltiples hilos, ¿seguiría siendo adecuado un `ArrayDeque` o deberías cambiar a una `ConcurrentLinkedQueue`? Justifica tu respuesta basándote en la seguridad de hilos (Thread-safety).
- La principal diferencia es que ArrayDeque no es segura para el acceso simultáneo desde varios hilos, mientras que ConcurrentLinkedQueue sí está diseñada para trabajar en escenarios multihilo. El concepto de thread-safety significa que una estructura de datos puede ser utilizada por varios hilos al mismo tiempo sin provocar inconsistencias o errores en sus operaciones. Por esta razón, si varios procesos o sensores necesitan agregar y retirar lecturas simultáneamente, elegiría ConcurrentLinkedQueue, ya que permite realizar estas operaciones de forma segura sin necesidad de sincronizar manualmente cada acceso. Por otro lado, ArrayDeque puede ser una mejor opción cuando se trabaja con un solo hilo, debido a su menor sobrecarga.