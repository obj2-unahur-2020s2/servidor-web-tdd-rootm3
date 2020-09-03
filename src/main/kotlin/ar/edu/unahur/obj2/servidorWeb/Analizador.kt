package ar.edu.unahur.obj2.servidorWeb

//Clase general de Analizador
abstract class Analizador() {
    val respuestas = mutableListOf<Respuesta>()
    val modulos = mutableListOf<Modulo>()

    abstract fun analizarweb(modulo: Modulo, respuesta: Respuesta): Int?
}

//Clase de Analizador que detecta demora
class DetectorDeDemora (val demoraMinima: Int): Analizador() {
    var respuestasConDemora = 0
    override fun analizarweb(modulo: Modulo, respuesta: Respuesta): Int {
        if(demoraMinima < respuesta.tiempo){
            respuestasConDemora += 1
        }
        return respuestasConDemora
    }
}

//Clase de Analizador que detecta ipsospechosas
class Ipsospechosa(val ipsospechosas: List<String>): Analizador() {
    val pedidos = mutableListOf<Pedido>()

    override fun analizarweb(modulo: Modulo, respuesta: Respuesta): Int? {
        respuestas.add(respuesta)
        modulos.add(modulo)
        if(ipsospechosas.contains(respuesta.pedido.ip)) {
            pedidos.add(respuesta.pedido)
        }
        return null
    }

    fun cantidadPedidosRealizados(ipSospechosa: String) = pedidos.count { p -> p.ip == ipSospechosa }

    //falta modulo más consultado

    //ips que requirieron la ruta
}

// Falta Estadistica