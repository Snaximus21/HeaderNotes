package com.rg.headernotes.util

/**
 * Можно сделать sealed интерфейсом, стейта же никакого нет у тебя у UiState
 */
sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()

    /**
     * Я бы тут поменял параметр на Throwable,
     * исключения же разные могут быть и поведение разное у системы будет
     */
    data class Failure(val error: String?): UiState<Nothing>()
}