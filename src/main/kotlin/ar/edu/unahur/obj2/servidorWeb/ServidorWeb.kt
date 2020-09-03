package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class ServidorWeb {
  val modulos = mutableListOf<Modulo>()
  val analizadores = mutableListOf<Analizador>()

  fun realizarPedido(pedido: Pedido): Respuesta {
    if (!pedido.url.startsWith("http:")) {
      return Respuesta(codigo = CodigoHttp.NOT_IMPLEMENTED, body = "", tiempo = 10, pedido = pedido, modulo = null)
    }

    if (this.algunModuloSoporta(pedido.url)) {
      val moduloSeleccionado = this.modulos.find { it.puedeTrabajarCon(pedido.url) }!!
      return Respuesta(CodigoHttp.OK, moduloSeleccionado.body, moduloSeleccionado.tiempoRespuesta, pedido = pedido, modulo = moduloSeleccionado)
    }

    return Respuesta(codigo = CodigoHttp.NOT_FOUND, body = "", tiempo = 10, pedido = pedido, modulo = null)
  }

  fun algunModuloSoporta(url: String) = this.modulos.any { it.puedeTrabajarCon(url) }

  fun agregarModulo(modulo: Modulo) {
    this.modulos.add(modulo)
  }

  //agregar dinamicamente analizador
  fun agregarAnalizador(analizador: Analizador) {
    analizadores.add(analizador)
  }
  //quitar dinamicamente analizador
  fun quitarAnalizador(analizador: Analizador) {
    analizadores.remove(analizador)
  }
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido, val modulo: Modulo?)

//Nuevo objeto de Pedido para reutilizar en analizador
class Pedido (val ip: String, val url: String, val fechaHora: LocalDateTime)