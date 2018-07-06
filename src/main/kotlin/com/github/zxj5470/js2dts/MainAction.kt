package com.github.zxj5470.js2dts

import com.intellij.lang.javascript.structureView.JSStructureViewElement
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

/**
 *
 * @receiver JSStructureViewElement
 * @param int Int
 */
fun JSStructureViewElement.doRec(int: Int) {
	val tab = "\t".repeat(int)
	val tabinc = "\t".repeat(int + 1)
	this.presentation.apply {
		val icon = this.getIcon(false) ?: return@apply
		val typeName = icon.toString().substringAfterLast("/").substringBefore(".")
		var str = when (typeName) {
			JsClass -> "module "
			StaticMark -> "staticMark "
			Field -> ""
			Method -> "function "
			Js -> ""
			Var -> ""
			Property -> ""
			else -> ""
		}
		val text = this.presentableText?.replace("*", "any")
				?: return@doRec
		if (str == "staticMark ") {
//			str = "_S "
			if ('(' in text && ')' in text) {
				println("$tab$CLASS ${text.substringBefore('(')} {")
				val params = text.substringAfter('(').substringBefore(')')
				println("$tabinc$CONSTRUCTOR($params)")
			} else {
				if (this@doRec.children.isNotEmpty()) {
					println("$tab$ENUM $text {")
				} else {
					println("$tab${this@doRec.value.text.replace(": ", " = ")},")
				}
			}
		} else {
			if (str == "function ") {
				str = ""
			}
			print("$tab$str$text")
			if (this@doRec.children.isNotEmpty()) print(" {\n")
			else println()
		}
	}
	this.children.apply {
		if (this.isEmpty()) return@apply
		else {
			forEach {
				(it as JSStructureViewElement).doRec(int + 1)
			}
		}
	}
	if (this@doRec.children.isNotEmpty())
		println("$tab}")
}