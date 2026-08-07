package com.example.wanderlust.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// Wanderlust — Nature Explorer Palette
// Primary  : Emerald Green  (#059669 family)
// Secondary: Warm Amber     (#F59E0B family)
// Tertiary : Sky Blue       (#0EA5E9 family)  — kept for water/sky accents
// ─────────────────────────────────────────────────────────────────────────────

// ── Seed / Raw tokens ─────────────────────────────────────────────────────────
// Greens
val Emerald900 = Color(0xFF064E3B)
val Emerald800 = Color(0xFF065F46)
val Emerald700 = Color(0xFF047857)
val Emerald600 = Color(0xFF059669)
val Emerald500 = Color(0xFF10B981)
val Emerald400 = Color(0xFF34D399)
val Emerald200 = Color(0xFFA7F3D0)
val Emerald100 = Color(0xFFD1FAE5)
val Emerald50  = Color(0xFFECFDF5)

// Ambers
val Amber600   = Color(0xFFD97706)
val Amber500   = Color(0xFFF59E0B)
val Amber400   = Color(0xFFFBBF24)
val Amber200   = Color(0xFFFDE68A)
val Amber100   = Color(0xFFFEF3C7)

// Sky Blues (tertiary / water accent)
val Sky600     = Color(0xFF0284C7)
val Sky400     = Color(0xFF38BDF8)
val Sky200     = Color(0xFFBAE6FD)

// Map pin semantic colors (used directly in NearbyPlacesExplorer)
val PinHotel   = Color(0xFF2563EB)   // Deep Blue  — Hotels & Stays
val PinShop    = Color(0xFF10B981)   // Emerald    — Shops & Markets
val PinRental  = Color(0xFFF97316)   // Bright Orange — Vehicle Rentals
val PinTour    = Color(0xFF8B5CF6)   // Violet     — Tours & Trips

// Neutrals
val Slate950   = Color(0xFF0F172A)
val Slate900   = Color(0xFF1E293B)
val Slate800   = Color(0xFF1E293B)
val Slate700   = Color(0xFF334155)
val Slate500   = Color(0xFF64748B)
val Slate300   = Color(0xFFCBD5E1)
val Slate200   = Color(0xFFE2E8F0)
val Slate100   = Color(0xFFF1F5F9)
val Slate50    = Color(0xFFF8FAFC)

// Error
val Red700     = Color(0xFFB91C1C)
val Red400     = Color(0xFFF87171)

// ─────────────────────────────────────────────────────────────────────────────
// Dark Mode — Nature Explorer Dark
// ─────────────────────────────────────────────────────────────────────────────
// ─────────────────────────────────────────────────────────────────────────────
// Wanderlust — Modern Coral & Teal Glassmorphic Palette
// Primary  : Warm Coral Orange (#FF6B35)
// Secondary: Vibrant Teal     (#00ABA7)
// Dark Bg  : Deep Obsidian    (#0B0F17)
// ─────────────────────────────────────────────────────────────────────────────

val Coral500  = Color(0xFFFF6B35)   // Warm Coral Primary
val Coral600  = Color(0xFFE5531F)
val Coral400  = Color(0xFFFF8559)
val Coral100  = Color(0xFFFFECE5)

val Teal500   = Color(0xFF00ABA7)   // Vibrant Teal Secondary
val Teal600   = Color(0xFF008985)
val Teal400   = Color(0xFF26C6C2)
val Teal100   = Color(0xFFD6F7F6)

val Obsidian950 = Color(0xFF0B0F17)
val Obsidian900 = Color(0xFF151C28)
val Obsidian800 = Color(0xFF1E2838)
val Obsidian700 = Color(0xFF2B394E)
val Obsidian600 = Color(0xFF3F516B)

object WanderlustDark {
    val Background            = Obsidian950        // #0B0F17 — Deep Obsidian
    val OnBackground          = Color(0xFFF1F5F9)
    val Surface               = Obsidian900        // #151C28 — Sleek dark card surface
    val OnSurface             = Color(0xFFF8FAFC)
    val SurfaceContainer      = Obsidian800        // #1E2838
    val SurfaceContainerHigh  = Obsidian700
    val SurfaceContainerLow   = Obsidian950

    val OnSurfaceVariant      = Color(0xFF94A3B8)

    val Outline               = Coral500
    val OutlineVariant        = Color(0xFF263346)

    val Primary               = Coral500           // #FF6B35 — Warm Coral
    val PrimaryContainer      = Coral600
    val OnPrimaryContainer    = Color(0xFFFFFFFF)

    val Secondary             = Teal500            // #00ABA7 — Vibrant Teal
    val SecondaryContainer    = Teal600
    val OnSecondaryContainer  = Color(0xFFFFFFFF)

    val Tertiary              = Color(0xFFF59E0B)   // Rating Gold
    val TertiaryContainer     = Color(0xFFD97706)
    val OnTertiaryContainer   = Color(0xFFFFFFFF)

    val Error                 = Color(0xFFEF4444)
    val OnError               = Color(0xFFFFFFFF)

    val GlassBg               = Color(0xD9151C28)
    val GhostBorder           = Color(0xFF263346)
}

object WanderlustLight {
    val Background            = Color(0xFFF8FAFC)
    val OnBackground          = Color(0xFF0F172A)
    val Surface               = Color(0xFFFFFFFF)
    val OnSurface             = Color(0xFF0F172A)
    val SurfaceContainer      = Color(0xFFF1F5F9)
    val SurfaceContainerHigh  = Color(0xFFE2E8F0)
    val SurfaceContainerLow   = Color(0xFFF8FAFC)

    val OnSurfaceVariant      = Color(0xFF475569)

    val Outline               = Coral500
    val OutlineVariant        = Color(0xFFCBD5E1)

    val Primary               = Coral600           // #E5531F
    val PrimaryContainer      = Coral500
    val OnPrimaryContainer    = Color(0xFFFFFFFF)

    val Secondary             = Teal600            // #008985
    val SecondaryContainer    = Teal500
    val OnSecondaryContainer  = Color(0xFFFFFFFF)

    val Tertiary              = Color(0xFFD97706)
    val TertiaryContainer     = Color(0xFFF59E0B)
    val OnTertiaryContainer   = Color(0xFFFFFFFF)

    val Error                 = Color(0xFFDC2626)
    val OnError               = Color(0xFFFFFFFF)

    val GlassBg               = Color(0xF7FFFFFF)
    val GhostBorder           = Color(0xFFE2E8F0)
}

