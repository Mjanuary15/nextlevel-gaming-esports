package com.nextlevel.esports.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nextlevel.nextlevelgamingesports.R

// ---------- Design tokens ----------
private object NL {
    val Base = Color(0xFF0E0A24)
    val Surface = Color(0xFF1A1340)
    val SurfaceHigh = Color(0xFF2A1F63)
    val Violet = Color(0xFF9A8CFF)
    val Lime = Color(0xFF6BE81F)      // primary actions only
    val Magenta = Color(0xFFFF2E88)   // live / urgency
    val Gold = Color(0xFFFFC24B)      // member status, VIP
    val TextDim = Color(0xFFB4A9F5)
}

private val DisplayFont: FontFamily = FontFamily.Default

// ---------- Screen ----------
@Composable
fun HomeScreen(
    @DrawableRes heroImage: Int? = R.drawable.football_gameplay_screen,
    tournamentTitle: String = "FC 27 Esports Tournament",
    onRegister: () -> Unit = {},
    onManageSession: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NL.Base)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Header(xpProgress = 1250 / 1500f)
        TournamentHero(heroImage, tournamentTitle, onRegister)
        NextSession(onManage = onManageSession)
        Text("Book your arena", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Categories(onClick = onCategoryClick)
        Spacer(Modifier.height(12.dp))
    }
}

// ---------- Header with XP ring ----------
@Composable
private fun Header(xpProgress: Float) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 4.dp.toPx()
                val inset = stroke / 2
                val arcSize = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
                drawArc(NL.SurfaceHigh, -90f, 360f, false, Offset(inset, inset), arcSize, style = Stroke(stroke))
                drawArc(NL.Lime, -90f, 360f * xpProgress, false, Offset(inset, inset), arcSize,
                    style = Stroke(stroke, cap = StrokeCap.Round))
            }
            Box(
                Modifier.size(36.dp).clip(CircleShape).background(Color(0xFF8B80F9)),
                contentAlignment = Alignment.Center,
            ) { Icon(painterResource(R.drawable.ic_person), contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)) }
        }
        Column(Modifier.weight(1f)) {
            Text("Hello, Gamer!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Level 12 · Gold member", color = NL.Gold, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("1,250 XP", color = NL.Lime, fontFamily = DisplayFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("250 to Level 13", color = NL.TextDim, fontSize = 11.sp)
        }
    }
}

// ---------- Tournament hero ----------
@Composable
private fun TournamentHero(@DrawableRes image: Int?, title: String, onRegister: () -> Unit) {
    val outerShape = CutCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomEnd = 4.dp, bottomStart = 24.dp)
    val displayImage = image ?: R.drawable.football_gameplay_screen
    Column(
        Modifier
            .fillMaxWidth()
            .clip(outerShape)
            .border(1.dp, NL.Lime.copy(alpha = 0.5f), outerShape)
            .background(NL.Surface)
    ) {
        // Top Banner Artwork Section
        Box(
            Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color(0xFF2E1872))
        ) {
            Image(
                painterResource(displayImage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color(0x990E0A24)))
                )
            )
            Box(Modifier.align(Alignment.TopStart).padding(12.dp)) { LiveChip() }
        }

        // Bottom Details Section
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text("Prize pool", color = NL.TextDim, fontSize = 12.sp)
            Text("R10,000", color = NL.Lime, fontFamily = DisplayFont, fontWeight = FontWeight.Black, fontSize = 38.sp)
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = { 14 / 32f },
                    modifier = Modifier.weight(1f).height(5.dp).clip(CircleShape),
                    color = NL.Magenta,
                    trackColor = Color(0xFF2A1F52),
                )
                Text("18 spots left", color = Color(0xFFCFC7FF), fontSize = 11.sp)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NL.Lime, contentColor = NL.Base),
            ) {
                Text("Register now", fontFamily = DisplayFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun LiveChip() {
    val transition = rememberInfiniteTransition(label = "live")
    val dotAlpha by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "dot",
    )
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(NL.Magenta).padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(8.dp)
                .graphicsLayer { alpha = dotAlpha }
                .clip(CircleShape)
                .background(Color.White)
        )
        Spacer(Modifier.width(6.dp))
        Text("LIVE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

// ---------- Next session ----------
@Composable
private fun NextSession(onManage: () -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(NL.Surface)
            .clickable(onClick = onManage).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Next session", color = NL.TextDim, fontSize = 12.sp)
            Text("Sat 11 Oct · 14:00", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text("Squad VIP lounge · 6 players", color = NL.TextDim, fontSize = 13.sp)
        }
        Text("Manage ›", color = NL.Violet, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

// ---------- Category bento ----------
@Composable
private fun Categories(onClick: (String) -> Unit = {}) {
    val gamepadPainter = painterResource(R.drawable.ic_gamepad)
    val groupsPainter = rememberVectorPainter(Icons.Filled.Groups)
    val trophyPainter = painterResource(R.drawable.ic_trophy)

    Row(Modifier.fillMaxWidth().height(230.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryTile(
            icon = gamepadPainter,
            title = "Casual and solo",
            subtitle = "RTX 4080 PCs, PS5 booths, sim racing",
            accent = NL.Lime,
            topGradientColor = Color(0xFF183322),
            shape = CutCornerShape(topEnd = 18.dp, bottomStart = 18.dp),
            modifier = Modifier.weight(1.15f).fillMaxHeight(),
            onClick = { onClick("solo") },
        )
        Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CategoryTile(
                icon = groupsPainter,
                title = "Groups and parties",
                subtitle = null,
                accent = NL.Violet,
                topGradientColor = Color(0xFF2B205C),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                onClick = { onClick("groups") },
            )
            CategoryTile(
                icon = trophyPainter,
                title = "Schools and corporate",
                subtitle = null,
                accent = NL.Gold,
                topGradientColor = Color(0xFF382F18),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                onClick = { onClick("corporate") },
            )
        }
    }
}

@Composable
private fun CategoryTile(
    icon: Painter,
    title: String,
    subtitle: String?,
    accent: Color,
    topGradientColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier
            .clip(shape)
            .background(Brush.verticalGradient(listOf(topGradientColor, NL.Surface)))
            .border(1.dp, accent.copy(alpha = 0.45f), shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(28.dp))
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
        }
        Column {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, color = NL.TextDim, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0A24, heightDp = 900)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
