package structures.management;

import model.LecturaSensor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GestorDeRed - Módulo Integrador de Estructuras de Datos
 * VERSION ESTUDIANTE
 *
 * Objetivo: Implementar la gestión de la red utilizando las colecciones de Java (JCF).
 * El éxito de este módulo no es solo que el código compile, sino que elijas
 * la estructura correcta para cada problema.
 */
public class GestorDeRed {
    // 1. Historial de Eventos: Registro lineal de todas las actividades
    private List<String> historialEventos;

    // 2. Navegador de Alertas: Permite moverse adelante y atrás entre alertas
    private Deque<String> navegadorAlertas;
    private String alertaActual;

    // 3. Ciclo de Monitoreo: Gestión de la rotación de sensores a consultar
    private Queue<String> colaMonitoreo;

    // 4. Topología de la Red: Grafo de conexiones entre estaciones (Lista de adyacencia)
    private Map<String, List<String>> topologiaRed;

    // 5. Buffer de Ingesta: Cola de espera para procesar lecturas entrantes
    private Queue<LecturaSensor> bufferIngesta;

    public GestorDeRed() {
        // TODO: Inicializa las colecciones usando las implementaciones más adecuadas de Java
        // (ArrayList, ArrayDeque, HashMap, etc.)
        this.historialEventos = new ArrayList<>();
        this.navegadorAlertas = new ArrayDeque<>();
        this.colaMonitoreo = new ArrayDeque<>();
        this.topologiaRed = new HashMap<>();
        this.bufferIngesta = new ArrayDeque<>();
    }

    // =========================================================================
    // SECCIÓN 1: HISTORIAL Y EVENTOS
    // =========================================================================
    // TODO 1:
    public void agregarEvento(String evento) {
        String registro = "[" + new java.util.Date() + "] " + evento;
        historialEventos.add(registro);
    }

    public List<String> obtenerEventosRecientes(int limite) {
        if (historialEventos.size() <= limite) return new ArrayList<>(historialEventos);
        return historialEventos.subList(historialEventos.size() - limite, historialEventos.size());
    }
    // Todo 10
    public void limpiarHistorialAntiguo(int mantener) {
        while (historialEventos.size() > mantener) {
            historialEventos.remove(0);
        }
    }

    // =========================================================================
    // SECCIÓN 2: NAVEGACIÓN DE ALERTAS
    // =========================================================================

    public void registrarAlerta(String alerta) {
        navegadorAlertas.addLast(alerta);
        alertaActual = alerta;
    }
    // Todo 2
    public String navegarAlSiguiente() {
        String siguiente = navegadorAlertas.pollFirst();

        if (siguiente == null) {
            return null;
        }

        navegadorAlertas.addLast(siguiente);
        alertaActual = siguiente;

        return alertaActual;
    }
    // Todo 3
    public String navegarAlAnterior() {
        String anterior = navegadorAlertas.pollLast();

        if (anterior == null) {
            return null;
        }

        navegadorAlertas.addFirst(anterior);
        alertaActual = anterior;

        return alertaActual;
    }

    public String getAlertaActual() { return alertaActual; }

    // =========================================================================
    // SECCIÓN 3: CICLO DE MONITOREO
    // =========================================================================
    // Todo 4
    public void programarConsulta(String idSensor) {
        if (!colaMonitoreo.contains(idSensor)) {
            colaMonitoreo.offer(idSensor);
        }
    }
    // Todo 5
    public String obtenerSiguienteSensor() {
        String sensor = colaMonitoreo.poll();

        if (sensor != null) {
            colaMonitoreo.offer(sensor);
        }

        return sensor;
    }

    // =========================================================================
    // SECCIÓN 4: TOPOLOGÍA DE RED
    // =========================================================================
    // Todo 6
    public void registrarConexion(String sensorA, String sensorB) {
        topologiaRed.computeIfAbsent(sensorA, k -> new ArrayList<>());
        topologiaRed.computeIfAbsent(sensorB, k -> new ArrayList<>());

        if (!topologiaRed.get(sensorA).contains(sensorB)) {
            topologiaRed.get(sensorA).add(sensorB);
        }

        if (!topologiaRed.get(sensorB).contains(sensorA)) {
            topologiaRed.get(sensorB).add(sensorA);
        }
    }
    // Todo 7
    public List<String> obtenerVecinos(String idSensor) {
        return topologiaRed.getOrDefault(idSensor, new ArrayList<>());
    }
    // Todo 9
    public List<String> detectarEstacionesAisladas() {
        List<String> aisladas = new ArrayList<>();

        for (Map.Entry<String, List<String>> estacion : topologiaRed.entrySet()) {
            if (estacion.getValue().isEmpty()) {
                aisladas.add(estacion.getKey());
            }
        }

        return aisladas;
    }

    // =========================================================================
    // SECCIÓN 5: BUFFER DE INGESTA
    // =========================================================================

    public void recibirLectura(LecturaSensor lectura) {
        bufferIngesta.add(lectura);
    }
    // Todo 8
    public LecturaSensor procesarSiguienteLectura() {
        return bufferIngesta.poll();
    }
    // Todo Extra:
    public List<LecturaSensor> filtrarAlertasCriticas(double limitePm25) {
        return bufferIngesta.stream()
                .filter(lectura -> lectura.getPm25() > limitePm25)
                .collect(Collectors.toList());
    }
}
