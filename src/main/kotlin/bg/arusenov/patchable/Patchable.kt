package bg.arusenov.patchable

import java.util.function.Consumer

class Patchable<T> private constructor() {

    /**
     * Returns the underlying value or null if not set.
     */
    var value: T? = null
        private set

    var isSet: Boolean = false
        private set

    val isPresent
        get() = this.value != null

    private constructor(v: T) : this() {
        this.value = v
        this.isSet = true
    }

    fun ifSet(c: Consumer<T>) {
        if (this.isSet) {
            c.accept(this.value as T)
        }
    }

    fun ifSetK(c: (v: T) -> Unit) {
        if (this.isSet) {
            c(this.value as T)
        }
    }

    companion object {

        private val NOT_SET = Patchable<Any>()

        @JvmStatic
        fun <T> notSet(): Patchable<T> {
            return NOT_SET as Patchable<T>
        }

        @JvmStatic
        fun <T> of(value: T): Patchable<T> {
            return Patchable(value)
        }
    }
}