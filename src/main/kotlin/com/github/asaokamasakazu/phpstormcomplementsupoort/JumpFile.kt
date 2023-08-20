package com.github.asaokamasakazu.phpstormcomplementsupoort

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

class JumpFile : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.getData(CommonDataKeys.PROJECT)
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)

        if (editor != null && project != null && virtualFile != null && virtualFile.isValid) {
            val psiFile = e.getData(CommonDataKeys.PSI_FILE)
            if (psiFile is PhpFile) {
                val caretModel = editor.caretModel
                val offset = caretModel.offset
                val element = psiFile.findElementAt(offset)
                if (element is StringLiteralExpression) {
                    val text = element.contents
                    if (text.startsWith("__('") && text.endsWith("')")) {
                        val key = text.substring(4, text.length - 2)
                        val filePath = "lang/ja/exception.php"  // 遷移先ファイルのパス
                        val targetFile = virtualFile.parent?.findFileByRelativePath(filePath)
                        if (targetFile != null && targetFile.isValid && targetFile.exists()) {
                            openFileInEditor(project, targetFile)
                        }
                    }
                }
            }
        }
    }

    private fun openFileInEditor(project: Project, file: VirtualFile) {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val editors = fileEditorManager.openFile(file, true)
        if (editors.isNotEmpty()) {
            // 適切なエディタでファイルが開かれた場合の処理を追加
        }
    }
}
