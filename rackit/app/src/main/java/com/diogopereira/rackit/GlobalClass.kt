package com.diogopereira.rackit

import android.app.Application
import com.diogopereira.rackit.classes.Produto

class GlobalClass : Application() {
    var emailUtilizador =""
    var nomeUtilizador = ""
    var uidUtilizador =""
    var currentList = ""
    var currentProduto = Produto()
}