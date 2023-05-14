package com.example.composetodolistapp.navigation

import android.util.Log

sealed class Screen(val route: String) {
    object SignIn: Screen("sign-in")
    object TodoScreen: Screen("todos")
    object TodoDetails: Screen("details")
    object SignUp: Screen("sign-up")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withOptArgs(vararg args: Pair<String?, String?>): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                if (arg.first == null && arg.second == null) {
                    append("?")
                } else if (arg.second != null) {
                    append("${arg.first}=${arg.second}&")
                } else {
                    append("/${arg.first}")
                }
            }
            if (get(lastIndex) == '&') {
                deleteCharAt(lastIndex)
            }
            Log.d("Screen", this.toString())
        }
    }

    fun withMultOptArgs(vararg args: Pair<String, String?>): String {
        return buildString {
            val optionalArgs = args.filter { it.second != null }.joinToString(",") { "${it.first}=${it.second}" }
            append(route)
            if (optionalArgs.isNotBlank()) {
                append("?$optionalArgs")
            }
        }
    }
}