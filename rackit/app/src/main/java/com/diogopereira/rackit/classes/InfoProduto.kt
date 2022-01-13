package com.diogopereira.rackit.classes

import java.util.*

data class InfoProduto (var dataCompra : String? = null,
                        var dataValidade : String?=null,
                        var dataValidadeAux : Date? = null,
                        var precoCompra : String? = null,
                        var produtoID: String?=null,
                        var infoProdutoID: String?=null)