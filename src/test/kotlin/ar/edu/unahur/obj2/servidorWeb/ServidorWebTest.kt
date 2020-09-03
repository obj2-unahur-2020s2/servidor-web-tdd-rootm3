package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("TEST GENERAL SOBRE SERVIDOR WEB") {
    val servidor = ServidorWeb()
    val pedido1 = Pedido("208.66.14.1", "https://pepito.com.ar/hola.txt", LocalDateTime.now())
    val pedido1bis = Pedido("208.66.14.1", "http://pepito.com.ar/hola.txt", LocalDateTime.now())
    val pedido2 = Pedido("208.66.14.1", "http://pepito.com.ar/playa.png", LocalDateTime.now())
    val analizador1 = DetectorDeDemora(1)
    val analizador2 = DetectorDeDemora(50)
    val analizador3 = Ipsospechosa(listOf("208.66.14.1"))
    servidor.agregarModulo(
      Modulo(listOf("txt"), "todo bien", 100)
    )
    servidor.agregarModulo(
      Modulo(listOf("jpg", "gif"), "qué linda foto", 100)
    )
    val modulo1 = Modulo(listOf("text"), "todo bien", 100)
    val modulo2 = Modulo(listOf("text"), "todo bien", 1)

    it("devuelve 501 si recibe un pedido que no es HTTP") {
      val respuesta = servidor.realizarPedido(pedido1)
      respuesta.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      respuesta.body.shouldBe("")
    }

    it("devuelve 200 si algún módulo puede trabajar con el pedido") {
      val respuesta = servidor.realizarPedido(pedido1bis)
      respuesta.codigo.shouldBe(CodigoHttp.OK)
      respuesta.body.shouldBe("todo bien")
    }

    it("devuelve 404 si ningún módulo puede trabajar con el pedido por que es png") {
      val respuesta = servidor.realizarPedido(pedido2)
      respuesta.codigo.shouldBe(CodigoHttp.NOT_FOUND)
      respuesta.body.shouldBe("")
    }

    it("test agregar analizador") {
      servidor.agregarAnalizador(analizador1)
      servidor.analizadores.shouldContain(analizador1)
    }

    it("test quitar analizador") {
      servidor.agregarAnalizador(analizador1)
      servidor.quitarAnalizador(analizador1)
      servidor.analizadores.isEmpty()
    }


    it("Hay un pedido con demora") { //pasa
      val respuesta = servidor.realizarPedido(pedido1bis)
      servidor.agregarAnalizador(analizador2)
      servidor.analizadores.isNotEmpty()
      analizador1.analizarweb(modulo2, respuesta)
      analizador1.respuestasConDemora.shouldBe(1)
    }

    it("cantidad de pedidos por IP Sospechosa") {
      val respuesta = servidor.realizarPedido(pedido1bis)
      analizador3.analizarweb(modulo1, respuesta)
      analizador3.cantidadPedidosRealizados("208.66.14.1").shouldBe(1)
    }


  }
})
