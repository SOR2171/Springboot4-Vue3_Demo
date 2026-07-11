package com.github.sor2171.backend.handler

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WsHandler(val path: String)
