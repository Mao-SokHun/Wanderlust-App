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
object WanderlustDark {
    // Backgrounds & surfaces
    val Background            = Color(0xFF0D1512)   // Very dark green-tinted black
    val OnBackground          = Color(0xFFD1FAE5)   // Emerald 100
    val Surface               = Color(0xFF121A16)   // Dark green-tinted surface
    val OnSurface             = Color(0xFFE2F5EC)
    val SurfaceContainer      = Color(0xFF1A2820)   // Elevated card bg
    val SurfaceContainerHigh  = Color(0xFF1F3328)
    val SurfaceContainerLow   = Color(0xFF151E19)

    // Text on surface
    val OnSurfaceVariant      = Color(0xFFA7F3D0)   // Emerald 200 — secondary text

    // Outline
    val Outline               = Color(0xFF34D399)   // Emerald 400
    val OutlineVariant        = Color(0xFF047857)   // Emerald 700 — subtle borders

    // Primary — Emerald
    val Primary               = Color(0xFF34D399)   // Emerald 400 — readable on dark
    val PrimaryContainer      = Color(0xFF059669)   // Emerald 600 — filled buttons
    val OnPrimaryContainer    = Color(0xFFFFFFFF)

    // Secondary — Amber
    val Secondary             = Color(0xFFFBBF24)   // Amber 400
    val SecondaryContainer    = Color(0xFFD97706)   // Amber 600
    val OnSecondaryContainer  = Color(0xFF1C1007)

    // Tertiary — Sky Blue
    val Tertiary              = Color(0xFF38BDF8)   // Sky 400
    val TertiaryContainer     = Color(0xFF0284C7)   // Sky 600
    val OnTertiaryContainer   = Color(0xFFFFFFFF)

    // Error
    val Error                 = Color(0xFFF87171)   // Red 400
    val OnError               = Color(0xFF7F1D1D)

    // Glass / ghost
    val GlassBg               = Color(0x991A2820)
    val GhostBorder           = Color.White.copy(alpha = 0.1f)
}

// ─────────────────────────────────────────────────────────────────────────────
// Light Mode — Nature Explorer Light
// ─────────────────────────────────────────────────────────────────────────────
object WanderlustLight {
    // Backgrounds & surfaces
    val Background            = Slate50              // #F8FAFC — Clean Slate White
    val OnBackground          = Slate900             // #1E293B — Charcoal Slate text
    val Surface               = Color(0xFFFFFFFF)    // Pure white cards
    val OnSurface             = Slate900
    val SurfaceContainer      = Color(0xFFF0FDF8)    // Slight green tint on containers
    val SurfaceContainerHigh  = Slate200             // #E2E8F0
    val SurfaceContainerLow   = Color(0xFFF7FDFB)

    // Text on surface
    val OnSurfaceVariant      = Slate700             // #334155 — secondary text

    // Outline
    val Outline               = Color(0xFF6EE7B7)    // Emerald 300
    val OutlineVariant        = Emerald100            // Very subtle green border

    // Primary — Emerald
    val Primary               = Emerald700           // #047857 — readable on white
    val PrimaryContainer      = Emerald600           // #059669 — filled button bg
    val OnPrimaryContainer    = Color(0xFFFFFFFF)

    // Secondary — Amber
    val Secondary             = Amber600             // #D97706
    val SecondaryContainer    = Amber500             // #F59E0B
    val OnSecondaryContainer  = Color(0xFF1C1007)

    // Tertiary — Sky Blue
    val Tertiary              = Sky600               // #0284C7
    val TertiaryContainer     = Sky400               // #38BDF8
    val OnTertiaryContainer   = Color(0xFFFFFFFF)

    // Error
    val Error                 = Red700               // #B91C1C
    val OnError               = Color(0xFFFFFFFF)

    // Glass / ghost
    val GlassBg               = Color(0xCCF0FDF8)
    val GhostBorder            = Emerald700.copy(alpha = 0.15f)
}
