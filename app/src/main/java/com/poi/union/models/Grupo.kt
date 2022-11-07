package com.poi.union.models

class Grupo (
    var grupoId: String = "",
    var grupoName: String = "",
    var grupoImagen: String = "",
    var adminId: String = "",
    var adminName: String = "",
    var timestamp: String = "",
    var members: List<MiembroGrupo>? = null
    //var nfts: List<NFT>? = null
)