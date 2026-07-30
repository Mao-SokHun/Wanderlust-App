package com.example.wanderlust.data.model

data class AudioGuide(
    val id: String,
    val titleEn: String,
    val titleKh: String,
    val landmarkNameEn: String,
    val landmarkNameKh: String,
    val narrativeEn: String,
    val narrativeKh: String,
    val durationSeconds: Int = 180,
    val audioUrl: String = "",
    val coverImageUrl: String = "",
)

object SampleAudioGuides {
    val sampleList = listOf(
        AudioGuide(
            id = "audio-angkor",
            titleEn = "The Secret Architecture of Angkor Wat",
            titleKh = "អាថ៌កំបាំងស្ថាបត្យកម្មប្រាសាទអង្គរវត្ត",
            landmarkNameEn = "Angkor Wat Temple",
            landmarkNameKh = "ប្រាសាទអង្គរវត្ត",
            narrativeEn = "Welcome to Angkor Wat, the heart of Khmer heritage built by King Suryavarman II in the 12th century. Notice the central towers symbolizing Mount Meru...",
            narrativeKh = "សូមស្វាគមន៍មកកាន់ប្រាសាទអង្គរវត្ត ដែលជាដួងព្រលឹងជាតិខ្មែរ កសាងឡើងដោយព្រះបាទសូរ្យវរ្ម័នទី២ នៅសតវត្សរ៍ទី១២...",
            durationSeconds = 210,
            coverImageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=500",
        ),
        AudioGuide(
            id = "audio-royal-palace",
            titleEn = "Royal Palace & Silver Pagoda Secrets",
            titleKh = "ប្រវត្តិនៃព្រះបរមរាជវាំង និងវត្តព្រះកែវមរកត",
            landmarkNameEn = "Royal Palace Phnom Penh",
            landmarkNameKh = "ព្រះបរមរាជវាំង ភ្នំពេញ",
            narrativeEn = "Discover the Throne Hall and the Silver Pagoda, home to the Emerald Buddha and 5,000 silver tiles floor...",
            narrativeKh = "ស្វែងយល់អំពីព្រះទីនាំងទេវាវិនិច្ឆ័យ និងវត្តព្រះកែវមរកត ដែលមានបាតក្រាលដោយការ៉ូប្រាក់ជាង ៥.០០០ ផ្ទាំង...",
            durationSeconds = 165,
            coverImageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=500",
        ),
    )
}
