package com.hiccup.kidpainting.utilities

class ColorHelper {
    internal var arrays = arrayOf(
            "#e57373", "#f44336", "#d32f2f",// red
            "#ffb74d", "#ff9800", "#f57c00",//orange
            "#fff176", "#ffeb3b", "#fbc02d",//yellow
            "#f06292", "#e91e63", "#c2185b",// pink
            "#ba68c8", "#9c27b0", "#7b1fa2",// purple
            "#9575cd", "#673ab7", "#512da8",// deep purple
            "#64b5f6", "#2196f3", "#1976d2",// blue
            "#81c784", "#4caf50", "#388e3c",//green
            "#a1887f", "#795548", "#5d4037",//brown
            "#e0e0e0", "#9e9e9e", "#616161",//grey
            "#90a4ae", "#607d8b", "#455a64",// blue gray
            "#000000")

    fun getColors(): Array<String> {
        return arrays
    }
}