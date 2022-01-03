package com.diogopereira.rackit.classes

<<<<<<< Updated upstream
data class Produto (var nomeProduto : String ?=null,  var codBarras : String?= null, var listaDe : String? = null, val adicionadoEm : Long? = null, val produtoID : String? = null)
=======
data class Produto (var nomeProduto : String ?=null,
                    var codBarras : String?= null,
                    var listaDe : String? = null,
                    var adicionadoEm : Long? = null,
                    var produtoID : String? = null,
                    var imagemProduto: String? = null,
                    var listaInfoProduto: ArrayList<InfoProduto> = ArrayList()
)
{
    fun adicionarInfoProduto(infoProduto: InfoProduto){
        listaInfoProduto.add(infoProduto)
    }
}
>>>>>>> Stashed changes
