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

fun JSStructureViewElement.doRec(int: Int) {
	this.presentation.apply {
		println("  ".repeat(int * 2) + this.presentableText)
//			println(this.getIcon(true)) // get Type by Icon !
	}
	this.children.forEach {
		(it as JSStructureViewElement).doRec(int + 1)
	}
}