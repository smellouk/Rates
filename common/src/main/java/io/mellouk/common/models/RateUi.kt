package io.mellouk.common.models

interface RateUi {
    val currency: String
    val name: String
    val value: String
}

class EditableRateUi(
    override val currency: String,
    override val name: String,
    override var value: String
) : RateUi

class NonEditableRateUi(
    override val currency: String,
    override val name: String,
    override val value: String
) : RateUi