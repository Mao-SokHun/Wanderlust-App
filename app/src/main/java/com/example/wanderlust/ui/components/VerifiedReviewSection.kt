package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.SampleReviews
import com.example.wanderlust.data.model.TravelerReview
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized

@Composable
fun VerifiedReviewSection(
    averageRating: Double = 4.9,
    reviewCount: Int = 28,
    modifier: Modifier = Modifier,
) {
    var showAddReviewDialog by remember { mutableStateOf(false) }
    val reviews = remember {
        mutableStateListOf<TravelerReview>().apply {
            addAll(SampleReviews.sampleList)
        }
    }

    if (showAddReviewDialog) {
        AddReviewModalDialog(
            onDismiss = { showAddReviewDialog = false },
            onSubmit = { newRev ->
                reviews.add(0, newRev)
                showAddReviewDialog = false
            },
        )
    }

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Rating Breakdown Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "$averageRating",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Text(
                        if (AppLocale.isKhmer) "$reviewCount ការវាយតម្លៃពីអ្នកប្រើយ៉ាងពិតប្រាកដ" else "Based on $reviewCount verified reviews",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Button(
                    onClick = { showAddReviewDialog = true },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        if (AppLocale.isKhmer) "សរសេរការវាយតម្លៃ" else "Write Review",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Rating Breakdown Bars
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                RatingBarRow(5, 0.85f, "85%")
                RatingBarRow(4, 0.10f, "10%")
                RatingBarRow(3, 0.03f, "3%")
                RatingBarRow(2, 0.01f, "1%")
                RatingBarRow(1, 0.01f, "1%")
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Reviews List
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                reviews.forEach { rev ->
                    ReviewCardItem(review = rev)
                }
            }
        }
    }
}

@Composable
private fun RatingBarRow(stars: Int, progress: Float, percentText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("$stars★", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(20.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(999.dp)),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(percentText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ReviewCardItem(review: TravelerReview) {
    var helpfulCount by remember { mutableIntStateOf(review.helpfulCount) }
    var hasLiked by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ProfileAvatar(size = 36.dp, displayName = review.authorName)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(review.authorName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        if (review.isVerifiedTraveler) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                    if (review.isVerifiedTraveler) {
                        Text(
                            if (AppLocale.isKhmer) "អ្នកបានជិះពិតប្រាកដ" else "Verified Traveler",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                (1..5).forEach { star ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (star <= review.rating) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        // Comment Text
        Text(
            review.comment,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        // Review Photos Gallery
        if (review.photoUrls.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                review.photoUrls.forEach { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }

        // Footer Helpful Control
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(review.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (!hasLiked) {
                            helpfulCount++
                            hasLiked = true
                        } else {
                            helpfulCount--
                            hasLiked = false
                        }
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = if (hasLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Helpful ($helpfulCount)",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (hasLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun AddReviewModalDialog(
    onDismiss: () -> Unit,
    onSubmit: (TravelerReview) -> Unit,
) {
    var selectedRating by remember { mutableIntStateOf(5) }
    var commentText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (AppLocale.isKhmer) "សរសេរការវាយតម្លៃ" else "Write a Review",
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    if (AppLocale.isKhmer) "ជ្រើសរើសពិន្ទុផ្កាយ" else "Select Rating:",
                    style = MaterialTheme.typography.labelMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { selectedRating = star }) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= selectedRating) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = {
                        Text(
                            if (AppLocale.isKhmer) "ចែករំលែកបទពិសោធន៍របស់អ្នក..." else "Share details of your experience...",
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rev = TravelerReview(
                        id = System.currentTimeMillis().toString(),
                        authorName = SessionManager.userName ?: "Traveler",
                        rating = selectedRating,
                        date = "Just now",
                        comment = commentText.ifBlank { "Great experience!" },
                        isVerifiedTraveler = true,
                    )
                    onSubmit(rev)
                },
                enabled = commentText.isNotBlank(),
            ) {
                Text(if (AppLocale.isKhmer) "បញ្ជូន" else "Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
