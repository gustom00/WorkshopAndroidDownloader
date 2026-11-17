package top.apricityx.workshop.ui

import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.TextStyle
import top.apricityx.workshop.AppLanguage
import top.apricityx.workshop.UiTranslations

val LocalWorkshopLanguage = staticCompositionLocalOf { AppLanguage.English }

fun localizeUiText(text: String, language: AppLanguage): String =
    UiTranslations.translate(text, language)

@Composable
fun localizedUiText(text: String): String =
    localizeUiText(text, LocalWorkshopLanguage.current)

@Composable
fun LocalizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null,
) {
    MaterialText(
        text = localizedUiText(text),
        modifier = modifier,
        color = color,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow,
        style = style,
        textAlign = textAlign,
    )
}
