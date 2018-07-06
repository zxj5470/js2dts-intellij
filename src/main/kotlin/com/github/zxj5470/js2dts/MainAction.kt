package com.github.zxj5470.js2dts

import com.intellij.lang.javascript.structureView.JSStructureViewElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys

class MainAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent?) {
		if (e == null) return
		val psiFile = e.getData(PlatformDataKeys.PSI_FILE)
		val jstree = JSStructureViewElement(psiFile, true)
		jstree.children.forEach {
			(it as JSStructureViewElement).doRec(0)
		}
	}
}

var i = 0
/**
 *
 * @receiver JSStructureViewElement
 * @param int Int
 */
fun JSStructureViewElement.doRec(int: Int) {
	val tab = "\t".repeat(int)
	val tabPlus = "\t".repeat(int + 1)
	this.presentation.presentationShow(int, this@doRec)
	this.children.apply {
		if (this.isEmpty()) return@apply
		else {
			forEachIndexed { index, it ->
				(it as JSStructureViewElement).doRec(int + 1)
				if (index == this.lastIndex) {
					if (i != 0) {
						i--
						println("$tab}")
					}

				}
			}
		}
	}
}

fun ItemPresentation.presentationShow(int: Int, viewElement: JSStructureViewElement) {
	val tab = "\t".repeat(int)
	val tabPlus = "\t".repeat(int + 1)
	val icon = getIcon(false) ?: return
	val typeName = icon.toString().substringAfterLast("/").substringBefore(".")
	val text = presentableText?.replace("*", "any") ?: return
	val str = when (typeName) {
		JsClass -> "$tab$MODULE $text" + s(viewElement)
		StaticMark -> {
			if ('(' in text && ')' in text) {
				val params = text.substringAfter('(').substringBefore(')')
				i++
				"$tab$CLASS ${text.substringBefore('(')} {\n" +
						"$tabPlus$CONSTRUCTOR($params)"
			} else {
				if (viewElement.children.isNotEmpty()) {
					i++
					"$tab$ENUM $text {"
				} else {
					"$tab${viewElement.value.text.replace(": ", " = ")},"
				}
			}
		}
		Field -> "$tab$text;"
		Method -> "$tab$text"
		Js -> null
		Var -> "$tab$LET $text"
		Property -> null
		else -> null
	}
	if (str != null)
		println(str)

}

private fun s(jsStructureViewElement: JSStructureViewElement) =
		if (jsStructureViewElement.children.isNotEmpty()) {
			i++
			" {"
		} else {
			""
		}
