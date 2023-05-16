package fr.nextgear.mesentretiensmoto.model

/**
 * Created by adrien on 01/11/2018.
 */
enum class StateMaintenance {
        TO_DO {
            override val value: Boolean
            get() = false
        },
        DONE {
            override val value: Boolean
            get() = true
        };

        abstract val value: Boolean
}