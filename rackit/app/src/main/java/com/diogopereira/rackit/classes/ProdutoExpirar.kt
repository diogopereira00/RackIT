package com.diogopereira.rackit.classes

import java.util.*
import kotlin.collections.ArrayList

class ProdutoExpirar(
    var nomeProduto: String? = null,
    var codBarras: String? = null,
    var listaDe: String? = null,
    var adicionadoEm: Long? = null,
    var produtoID: String? = null,
    var imagemProduto: String? = null,
    var precoCompra: String? = null,
    var infoProdutoID: String? = null,
    var dataCompra: String? = null,
    var dataValidade: String? = null,
    var dataValidadeAux: Date? = null,
    var listaInfoProduto: ArrayList<InfoProduto> = ArrayList()
)