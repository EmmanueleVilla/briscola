package com.shadowings.briscola

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

enum class Suits {
    HEARTS, DIAMONDS, CLUBS, SPADES
}

data class Card(val suit: String, val rank: String)

enum class GameState {
    NOT_STARTED, IN_PROGRESS, FINISHED
}

var gameState = GameState.NOT_STARTED

fun getDeck(): List<Card> {
    return (1..10).map {
        listOf(Suits.HEARTS, Suits.DIAMONDS, Suits.CLUBS, Suits.SPADES).map { suit ->
            Card(suit.name, it.toString())
        }
    }.flatten()
}

class BriscolaWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val panel = JPanel(BorderLayout())

        fun refreshUI() {
            panel.removeAll()

            when (gameState) {
                GameState.NOT_STARTED -> {
                    val wrapper = JPanel()
                    wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)

                    val startButton = JButton("Start Game")
                    startButton.alignmentX = Component.CENTER_ALIGNMENT
                    startButton.addActionListener {
                        getDeck().shuffled()
                        gameState = GameState.IN_PROGRESS

                        SwingUtilities.invokeLater { refreshUI() }
                    }

                    wrapper.add(Box.createVerticalGlue())
                    wrapper.add(startButton)
                    wrapper.add(Box.createVerticalGlue())


                    panel.add(wrapper, BorderLayout.CENTER)
                }

                GameState.IN_PROGRESS -> {
                    val label = JLabel("Game in Progress")
                    label.horizontalAlignment = SwingConstants.CENTER
                    panel.add(label, BorderLayout.CENTER)
                }

                GameState.FINISHED -> {
                    val label = JLabel("Game Finished")
                    label.horizontalAlignment = SwingConstants.CENTER
                    panel.add(label, BorderLayout.CENTER)
                }
            }

            panel.revalidate()
            panel.repaint()
        }

        refreshUI()

        val content = ContentFactory.getInstance()
            .createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}