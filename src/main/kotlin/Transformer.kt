import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.Name

class ShallowSizeIrTransformer(
    private val pluginContext: IrPluginContext,
) : IrElementTransformerVoid() {

    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (declaration.isShallowSizeFunction) {
            val parent = declaration.parentAsClass
            parent.getSizeOfClass().let { size ->
                declaration.body = DeclarationIrBuilder(pluginContext, declaration.symbol).irBlockBody {
                    +
                    irReturn(irInt(size))
                }
            }
        }
        return super.visitFunction(declaration)
    }

    private val IrFunction.isShallowSizeFunction
        get() = parent is IrClass &&
                parentAsClass.isData &&
                name == ShallowSizeIdentifier.name &&
                valueParameters.isEmpty() &&
                returnType == pluginContext.irBuiltIns.intType

    private fun IrClass.getSizeOfClass(): Int {
        return primaryConstructor?.valueParameters?.sumOf { valueParameter ->
            valueParameter.shallowCopySize()
        } ?: 0

    }

    private fun IrValueParameter.shallowCopySize(): Int {
        return type.let {
            when {
                it.isByte() -> Byte.SIZE_BYTES
                it.isUByte() -> UByte.SIZE_BYTES
                it.isShort() -> Short.SIZE_BYTES
                it.isUShort() -> UShort.SIZE_BYTES
                it.isInt() -> Int.SIZE_BYTES
                it.isUInt() -> UInt.SIZE_BYTES
                it.isLong() -> Long.SIZE_BYTES
                it.isULong() -> ULong.SIZE_BYTES
                it.isFloat() -> Float.SIZE_BYTES
                it.isDouble() -> Double.SIZE_BYTES
                it.isChar() -> Char.SIZE_BYTES
                it.isBoolean() -> 1
                else -> 0
            }
        }
    }

}

object ShallowSizeIdentifier {
    val name = Name.identifier("shallowSize")
}
