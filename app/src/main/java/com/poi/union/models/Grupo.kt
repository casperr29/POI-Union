package com.poi.union.models
import java.io.Serializable


class Grupo : Serializable {
    var grupoId: String = ""
    var grupoName: String = ""
    var grupoImagen: String = ""
    var adminId: String = ""
    var adminName: String = ""
    var timestamp: String = ""
    var members: List<MiembroGrupo>? = null
    //var nfts: List<NFT>? = null
}