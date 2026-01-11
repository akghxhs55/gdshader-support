package kr.jaehoyi.gdshader.model

fun DataType.withPrecision(precision: Precision): DataType {
    if (precision == Precision.DEFAULT) return this
    
    return when (this) {
        is FloatType -> FloatType.of(precision)
        
        is VectorType -> {
            val newElement = (this.elementType as? FloatType) ?.let { FloatType.of(precision) }
            if (newElement != null) {
                VectorType.of(newElement, this.containerSize) ?: this
            } else {
                this
            }
        }
        
        is MatrixType -> {
            val newElement = this.elementType.withPrecision(precision) as? VectorType
            if (newElement != null) {
                MatrixType.of(newElement) ?: this
            } else {
                this
            }
        }
        
        else -> this
    }
}
