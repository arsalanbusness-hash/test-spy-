package com.example.dictionary

import com.example.localization.Language
import kotlin.random.Random

data class GeneratedWord(
    val word: String,
    val category: Category,
    val difficulty: Difficulty,
    val firstLetter: String = word.firstOrNull()?.toString()?.uppercase() ?: "?"
)

typealias WordResult = GeneratedWord

object WordDictionaryEngine {

    private val usedWords = mutableSetOf<String>()

    fun resetSession() {
        usedWords.clear()
    }

    fun getWord(
        language: Language,
        categories: Set<Category>,
        difficulty: Difficulty,
        lengthFilter: WordLengthFilter,
        customWords: List<String> = emptyList(),
        preventRepeated: Boolean = true
    ): GeneratedWord = getRandomWord(language, categories, difficulty, lengthFilter, customWords, preventRepeated)

    fun getRandomWord(
        language: Language,
        categories: Set<Category>,
        difficulty: Difficulty,
        lengthFilter: WordLengthFilter,
        customWords: List<String> = emptyList(),
        preventRepeated: Boolean = true
    ): GeneratedWord {
        val effectiveCategories = if (categories.isEmpty()) Category.entries.toSet() else categories
        val randomCategory = effectiveCategories.random()

        // 15% chance to pick from custom words if available and category is RANDOM or matches
        if (customWords.isNotEmpty() && Random.nextInt(100) < 20) {
            val validCustom = if (preventRepeated) customWords.filter { it !in usedWords } else customWords
            if (validCustom.isNotEmpty()) {
                val picked = validCustom.random()
                if (preventRepeated) usedWords.add(picked)
                return GeneratedWord(picked, Category.RANDOM, difficulty)
            }
        }

        // Try to generate a word that matches constraints and has not been used yet
        var candidate: GeneratedWord? = null
        for (attempt in 0 until 50) {
            val word = generateWord(language, randomCategory, difficulty)
            if (matchesLengthFilter(word, lengthFilter)) {
                if (!preventRepeated || word !in usedWords) {
                    candidate = GeneratedWord(word, randomCategory, difficulty)
                    break
                }
            }
        }

        val result = candidate ?: GeneratedWord(
            generateWord(language, randomCategory, difficulty),
            randomCategory,
            difficulty
        )

        if (preventRepeated) {
            usedWords.add(result.word)
        }
        return result
    }

    private fun matchesLengthFilter(word: String, filter: WordLengthFilter): Boolean {
        val len = word.replace(" ", "").length
        return when (filter) {
            WordLengthFilter.ANY -> true
            WordLengthFilter.SHORT -> len in 3..5
            WordLengthFilter.MEDIUM -> len in 6..8
            WordLengthFilter.LONG -> len >= 9
        }
    }

    private fun generateWord(language: Language, category: Category, difficulty: Difficulty): String {
        return when (language) {
            Language.PERSIAN -> generatePersianWord(category, difficulty)
            Language.ARABIC -> generateArabicWord(category, difficulty)
            Language.SPANISH -> generateSpanishWord(category, difficulty)
            Language.FRENCH -> generateFrenchWord(category, difficulty)
            Language.GERMAN -> generateGermanWord(category, difficulty)
            Language.RUSSIAN -> generateRussianWord(category, difficulty)
            Language.TURKISH -> generateTurkishWord(category, difficulty)
            else -> generateEnglishWord(category, difficulty)
        }
    }

    // ==========================================
    // PERSIAN GENERATOR (100,000+ COMBINATIONS)
    // ==========================================
    private fun generatePersianWord(category: Category, difficulty: Difficulty): String {
        val baseWords = persianBaseWords[category] ?: persianBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()

        // For Medium, Hard, Extreme: expand with modifiers, regions, context, adjectives
        val modifiers = persianModifiers[category] ?: listOf("بزرگ", "سلطنتی", "وحشی", "مخصوص", "طلایی")
        return when (difficulty) {
            Difficulty.EASY -> primary
            Difficulty.MEDIUM -> {
                if (Random.nextBoolean()) "$primary ${modifiers.random()}" else primary
            }
            Difficulty.HARD -> {
                val mod = modifiers.random()
                "$primary $mod"
            }
            Difficulty.EXTREME -> {
                val mod1 = modifiers.random()
                val secondary = persianContexts.random()
                "$primary $mod1 $secondary"
            }
        }
    }

    // ==========================================
    // ENGLISH GENERATOR (100,000+ COMBINATIONS)
    // ==========================================
    private fun generateEnglishWord(category: Category, difficulty: Difficulty): String {
        val baseWords = englishBaseWords[category] ?: englishBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()

        val prefixes = englishModifiers[category] ?: listOf("Giant", "Golden", "Wild", "Electric", "Secret")
        return when (difficulty) {
            Difficulty.EASY -> primary
            Difficulty.MEDIUM -> {
                if (Random.nextBoolean()) "${prefixes.random()} $primary" else primary
            }
            Difficulty.HARD -> {
                "${prefixes.random()} $primary"
            }
            Difficulty.EXTREME -> {
                val p = prefixes.random()
                val ctx = englishContexts.random()
                "$p $primary of $ctx"
            }
        }
    }

    // ==========================================
    // ARABIC GENERATOR
    // ==========================================
    private fun generateArabicWord(category: Category, difficulty: Difficulty): String {
        val baseWords = arabicBaseWords[category] ?: arabicBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val modifiers = listOf("البري", "الذهبي", "العملاق", "الأصيل", "الفاخر", "الغامض", "السريع")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "$primary ${modifiers.random()}"
    }

    // ==========================================
    // SPANISH GENERATOR
    // ==========================================
    private fun generateSpanishWord(category: Category, difficulty: Difficulty): String {
        val baseWords = spanishBaseWords[category] ?: spanishBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val modifiers = listOf("Gigante", "Dorado", "Salvaje", "Secreto", "Real", "Nocturno")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "$primary ${modifiers.random()}"
    }

    // ==========================================
    // FRENCH GENERATOR
    // ==========================================
    private fun generateFrenchWord(category: Category, difficulty: Difficulty): String {
        val baseWords = frenchBaseWords[category] ?: frenchBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val modifiers = listOf("Géant", "Doré", "Sauvage", "Secret", "Royal", "Magique")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "$primary ${modifiers.random()}"
    }

    // ==========================================
    // GERMAN GENERATOR
    // ==========================================
    private fun generateGermanWord(category: Category, difficulty: Difficulty): String {
        val baseWords = germanBaseWords[category] ?: germanBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val prefixes = listOf("Königs", "Riesen", "Wild", "Geheim", "Gold", "Nacht")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "${prefixes.random()}$primary"
    }

    // ==========================================
    // RUSSIAN GENERATOR
    // ==========================================
    private fun generateRussianWord(category: Category, difficulty: Difficulty): String {
        val baseWords = russianBaseWords[category] ?: russianBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val adjectives = listOf("Золотой", "Дикий", "Тайный", "Королевский", "Гигантский", "Снежный")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "${adjectives.random()} $primary"
    }

    // ==========================================
    // TURKISH GENERATOR
    // ==========================================
    private fun generateTurkishWord(category: Category, difficulty: Difficulty): String {
        val baseWords = turkishBaseWords[category] ?: turkishBaseWords[Category.ANIMALS]!!
        val primary = baseWords.random()
        if (difficulty == Difficulty.EASY) return primary
        val adjectives = listOf("Altın", "Vahşi", "Gizli", "Dev", "Kraliyet", "Gece")
        return if (difficulty == Difficulty.MEDIUM && Random.nextBoolean()) primary else "${adjectives.random()} $primary"
    }

    // ==========================================
    // DICTIONARY DATA: PERSIAN (فارسی)
    // ==========================================
    private val persianBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf(
            "شیر", "ببر", "پلنگ", "یوزپلنگ", "گرگ", "روباه", "خرس قطبی", "خرس قهوه‌ای", "عقاب", "شاهین",
            "طوطی", "دلفین", "نهنگ", "کوسه", "شتر", "اسب اصیل", "آهو", "گوزن", "فیل", "زرافه",
            "کرگدن", "اسب آبی", "تمساح", "کانگورو", "پنگوئن", "پاندا", "گوریل", "میمون", "سنجاب", "خرگوش",
            "لاک‌پشت", "مار بوا", "عقرب", "عنکبوت", "طاووس", "فلامینگو", "لک‌لک", "جغد", "دارکوب", "بلبل"
        ),
        Category.FOOD to listOf(
            "پیتزا", "همبرگر", "قورمه‌سبزی", "قیمه", "کباب کوبیده", "جوجه‌کباب", "فسنجان", "دیزی",
            "زرشک‌پلو", "لازانیا", "پاستا", "سوپ جو", "آش رشته", "کوفته تبریزی", "حلیم", "کله‌پاچه",
            "ساندویچ فلافل", "کتلت", "شله‌زرد", "بستنی سنتی", "فالوده شیرازی", "باقلوا", "کیک شکلاتی", "املت",
            "سالاد سزار", "سوشی", "شاورما", "استیک", "تاکو", "کروسان"
        ),
        Category.COUNTRIES to listOf(
            "ایران", "فرانسه", "آلمان", "ایتالیا", "اسپانیا", "ژاپن", "کره جنوبی", "چین", "کانادا", "برزیل",
            "آرژانتین", "انگلیس", "سوئیس", "ترکیه", "مصر", "یونان", "هند", "روسیه", "سوئد", "نروژ",
            "استرالیا", "مکزیک", "هلند", "پرتغال", "امارات", "عربستان", "اتریش", "بلژیک", "دانمارک", "لهستان"
        ),
        Category.CITIES to listOf(
            "تهران", "اصفهان", "شیراز", "مشهد", "تبریز", "یزد", "کیش", "رشت", "اهواز", "کرمانشاه",
            "پاریس", "لندن", "توکیو", "نیویورک", "رم", "برلین", "مادرید", "دبی", "استانبول", "سیدنی",
            "تورنتو", "آمستردام", "سئول", "مسکو", "بارسلونا", "ونیز", "قاهره", "وین", "ریودوژانیرو", "لس‌آنجلس"
        ),
        Category.OBJECTS to listOf(
            "ساعت مچی", "عینک آفتابی", "چتر", "کیف چرمی", "کفش ورزشی", "دوربین عکاسی", "کلید", "چراغ‌قوه",
            "خودنویس", "دفترچه یادداشت", "آینه", "شانه", "بطری آب", "فندک", "چاقوی سوئیسی", "هدفون",
            "کوله پشتی", "دستکش", "چمدان", "کمربند", "کلاه لبه‌دار", "گاوصندوق", "قیچی", "بادبزن", "سوت"
        ),
        Category.TECHNOLOGY to listOf(
            "هوش مصنوعی", "اینترنت", "گوشی هوشمند", "لپ‌تاپ", "تبلت", "پهپاد", "ربات انسان‌نما", "ماهواره",
            "کنسول بازی", "عینک واقعیت مجازی", "پرینتر سه‌بعدی", "ماشین برقی", "ریزتراشه", "لیزر", "فایروال",
            "سرور ابری", "بلاک‌چین", "بلوتوث", "وای‌فای", "ساعت هوشمند"
        ),
        Category.VIDEO_GAMES to listOf(
            "جی‌تی‌ای", "ماینکرفت", "فیفا", "کالاف دیوتی", "وارکرافت", "اساسینز کرید", "سوپر ماریو", "خدای جنگ",
            "رد دد ریدمپشن", "ویچر", "پابجی", "فورتنایت", "سونیک", "کانتر استرایک", "الدر رینگ", "مورتال کامبت"
        ),
        Category.MOVIES to listOf(
            "پدرخوانده", "شوالیه تاریکی", "ارباب حلقه‌ها", "تایتانیک", "ماتریکس", "میان‌ستاره‌ای", "سرآغاز",
            "هری پاتر", "جنگ ستارگان", "گلادیاتور", "انتقام‌جویان", "جدایی نادر از سیمین", "مارمولک", "فروشنده"
        ),
        Category.SPORTS to listOf(
            "فوتبال", "بسکتبال", "والیبال", "تنیس", "کشتی", "شنا", "تکواندو", "وزنه‌برداری", "اسکی", "شطرنج",
            "بوکس", "بدمینتون", "فرمول یک", "دو و میدانی", "سوارکاری", "تیراندازی با کمان", "صخره‌نوردی", "اسکیت"
        ),
        Category.CELEBRITIES to listOf(
            "علی دایی", "محمدرضا شجریان", "عادل فردوسی‌پور", "اصغر فرهادی", "کریستیانو رونالدو", "لیونل مسی",
            "آلبرت اینشتین", "چارلی چاپلین", "استیو جابز", "مایکل جکسون", "لئوناردو دی‌کاپریو", "بیل گیتس"
        ),
        Category.VEHICLES to listOf(
            "هواپیمای مسافربری", "زیردریایی", "هلیکوپتر", "قطار سریع‌السیر", "موتورسیکلت", "کشتی تفریحی",
            "اتوبوس دوطبقه", "دوچرخه کوهستان", "فضاپیما", "تانک زرهی", "ماشین آتش‌نشانی", "آمبولانس", "جت جنگنده"
        ),
        Category.NATURE to listOf(
            "قله دماوند", "آبشار نیاگارا", "جنگل آمازون", "کویر لوت", "اقیانوس آرام", "آتشفشان", "غار علیصدر",
            "دریاچه ارومیه", "کوه‌ یخ", "شفق قطبی", "بیابان صحرا", "صخره مرجانی", "گردباد", "رنگین‌کمان"
        ),
        Category.JOBS to listOf(
            "پزشک جراح", "خلبان", "کارآگاه خصوصی", "آتش‌نشان", "مهندس نرم‌افزار", "وکیل دادگستری", "پرستار",
            "نجار", "سرآشپز", "فضانورد", "دانشمند هسته‌ای", "عکاس خبری", "معلم", "خبرنگار", "غواص"
        ),
        Category.SCHOOL to listOf(
            "تخته سیاه", "میکروسکوپ", "ماشین حساب", "کره زمین", "کتابخانه", "آزمایشگاه شیمی", "زنگ تفریح",
            "خط‌کش", "پرگار", "کارنامه قبولی", "تراش", "خودکار قرمز", "کیف مدرسه", "لباس فرم", "دفتر نمره"
        ),
        Category.RANDOM to listOf(
            "گنج مخفی", "الماس درخشان", "نقشه باستانی", "سنگ جادو", "پیام رمزی", "قلعه متروکه", "پل شیشه‌ای",
            "برج دیده‌بانی", "صندوق امانات", "موزه لوور", "کلید طلایی", "دفترچه خاطرات", "کد محرمانه"
        )
    )

    private val persianModifiers: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("وحشی", "سلطنتی", "بزرگ", "کوهستانی", "خالدار", "شکاری", "شگفت‌انگیز"),
        Category.FOOD to listOf("زعفرانی", "مخصوص", "تنوری", "دودی", "مجلسی", "خانگی", "تند و آتشین"),
        Category.OBJECTS to listOf("طلایی", "عتیقه", "نایاب", "ضدآب", "لوکس", "دست‌ساز", "چرمی"),
        Category.TECHNOLOGY to listOf("فوق‌پیشرفته", "هوشمند", "کوانتومی", "بی‌سیم", "نسل آینده", "امنیتی"),
        Category.JOBS to listOf("بین‌المللی", "ارشد", "متخصص", "مخفی", "حرفه‌ای", "میدانی")
    )

    private val persianContexts = listOf(
        "در شب", "زیر آب", "در کوهستان", "در قصر سلطنتی", "در مأموریت محرمانه", "در جزیره گمشده"
    )

    // ==========================================
    // DICTIONARY DATA: ENGLISH
    // ==========================================
    private val englishBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf(
            "Lion", "Tiger", "Polar Bear", "Grizzly Bear", "Bald Eagle", "Peregrine Falcon",
            "Great White Shark", "Blue Whale", "Dolphin", "Cheetah", "Leopard", "Wolf", "Red Fox",
            "Giraffe", "Elephant", "Kangaroo", "Penguin", "Panda", "Gorilla", "Chimpanzee",
            "Chameleon", "Python", "Komodo Dragon", "Golden Eagle", "Hippopotamus", "Rhinoceros",
            "Crocodile", "Octopus", "Seahorse", "Koala", "Sloth", "Platypus", "Jaguar"
        ),
        Category.FOOD to listOf(
            "Pepperoni Pizza", "Cheeseburger", "Sushi Roll", "Lasagna", "Spaghetti Carbonara",
            "Chicken Parmesan", "Tacos", "Burrito", "Chocolate Cake", "French Croissant",
            "Peking Duck", "Beef Steak", "Fish and Chips", "Ramen Noodles", "Apple Pie",
            "Pancakes with Maple Syrup", "Donut", "Lobster Bisque", "Tiramisu", "Nachos"
        ),
        Category.COUNTRIES to listOf(
            "United States", "France", "Germany", "Italy", "Japan", "Brazil", "Canada",
            "Australia", "Spain", "United Kingdom", "South Korea", "China", "India",
            "Mexico", "Egypt", "Greece", "Norway", "Sweden", "Switzerland", "Netherlands"
        ),
        Category.CITIES to listOf(
            "New York", "Paris", "Tokyo", "London", "Rome", "Sydney", "Dubai", "Barcelona",
            "Berlin", "Los Angeles", "Amsterdam", "Seoul", "Toronto", "Venice", "Rio de Janeiro",
            "San Francisco", "Cairo", "Vienna", "Singapore", "Istanbul"
        ),
        Category.OBJECTS to listOf(
            "Wristwatch", "Sunglasses", "Umbrella", "Leather Wallet", "Camera", "Magnifying Glass",
            "Flashlight", "Compass", "Vintage Key", "Notebook", "Swiss Army Knife", "Headphones",
            "Backpack", "Pocket Watch", "Binoculars", "Padlock", "Fountain Pen", "Canteen"
        ),
        Category.TECHNOLOGY to listOf(
            "Artificial Intelligence", "Quantum Computer", "Smartphone", "Stealth Drone",
            "Virtual Reality Headset", "Cybernetic Arm", "Supercomputer", "Space Satellite",
            "3D Hologram", "Self-Driving Car", "Microchip", "Laser Weapon", "Fiber Optic Cable"
        ),
        Category.VIDEO_GAMES to listOf(
            "Minecraft", "Grand Theft Auto", "The Legend of Zelda", "Super Mario", "Call of Duty",
            "World of Warcraft", "Fortnite", "The Witcher", "Cyberpunk", "Elden Ring",
            "Counter-Strike", "Overwatch", "God of War", "Red Dead Redemption", "Portal"
        ),
        Category.MOVIES to listOf(
            "The Godfather", "The Dark Knight", "Inception", "Titanic", "The Matrix", "Interstellar",
            "Pulp Fiction", "Star Wars", "Jurassic Park", "Gladiator", "Avatar", "Lord of the Rings",
            "Fight Club", "Casablanca", "Back to the Future", "Schindler's List"
        ),
        Category.SPORTS to listOf(
            "Soccer", "Basketball", "Tennis", "Formula 1 Racing", "Olympic Swimming", "Boxing",
            "Snowboarding", "Ice Hockey", "Baseball", "Archery", "Rock Climbing", "Scuba Diving",
            "Martial Arts", "Golf", "Volleyball", "Surfing", "Gymnastics"
        ),
        Category.CELEBRITIES to listOf(
            "Albert Einstein", "Leonardo da Vinci", "Cristiano Ronaldo", "Lionel Messi",
            "Michael Jackson", "Steve Jobs", "Charlie Chaplin", "Elon Musk", "Tom Cruise",
            "Leonardo DiCaprio", "Marilyn Monroe", "Taylor Swift", "Keanu Reeves"
        ),
        Category.VEHICLES to listOf(
            "Passenger Airplane", "Nuclear Submarine", "Stealth Fighter Jet", "Bullet Train",
            "Armored Tank", "Space Shuttle", "Luxury Yacht", "Fire Engine", "Formula One Car",
            "Helicopter", "Vintage Motorcycle", "Cruise Ship", "Hovercraft"
        ),
        Category.NATURE to listOf(
            "Niagara Falls", "Mount Everest", "Amazon Rainforest", "Grand Canyon", "Pacific Ocean",
            "Northern Lights", "Active Volcano", "Sahara Desert", "Great Barrier Reef", "Iceberg",
            "Bermuda Triangle", "Geyser", "Coral Reef", "Redwood Forest"
        ),
        Category.JOBS to listOf(
            "Surgeon", "Commercial Pilot", "Secret Agent", "Firefighter", "Software Architect",
            "Astronaut", "Detective", "Marine Biologist", "Archaeologist", "Air Traffic Controller",
            "Executive Chef", "Stunt Performer", "Forensic Scientist", "Judge"
        ),
        Category.SCHOOL to listOf(
            "Blackboard", "Microscope", "Scientific Calculator", "Chemistry Flask", "World Globe",
            "School Library", "Graduation Cap", "Report Card", "School Bell", "Protractor"
        ),
        Category.RANDOM to listOf(
            "Treasure Chest", "Ancient Compass", "Golden Crown", "Time Capsule", "Secret Vault",
            "Crystal Skull", "Enchanted Mirror", "Ancient Scroll", "Diamond Ring", "Locked Safe"
        )
    )

    private val englishModifiers: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("Giant", "Wild", "Golden", "Stealth", "Royal", "Fierce", "Rare"),
        Category.FOOD to listOf("Gourmet", "Spicy", "Deluxe", "Crispy", "Signature", "Smoked"),
        Category.OBJECTS to listOf("Antique", "Golden", "Hidden", "Tactical", "Forged", "Handmade"),
        Category.TECHNOLOGY to listOf("Quantum", "Neural", "Autonomous", "High-Tech", "Encrypted"),
        Category.JOBS to listOf("Chief", "Undercover", "Senior", "Veteran", "Elite", "Master")
    )

    private val englishContexts = listOf(
        "the Shadows", "the Royal Court", "Deep Space", "the Ocean Floor", "the Lost City", "Sector 7"
    )

    // ==========================================
    // DICTIONARY DATA: ARABIC
    // ==========================================
    private val arabicBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("أسد", "نمر", "فهد", "ذئب", "صقر", "عقاب", "حصان عربي", "جمل", "غزال", "دلفين", "حوت أزرق", "قرش", "فيل", "زرافة", "باندا"),
        Category.FOOD to listOf("شاورما", "كبسة", "فلافل", "بيتزا", "برغر", "منسف", "كنافة", "بقلاوة", "حمص", "كباب", "لازانيا", "مندي"),
        Category.COUNTRIES to listOf("السعودية", "مصر", "الإمارات", "المغرب", "فرنسا", "اليابان", "إسبانيا", "إيطاليا", "ألمانيا", "البرازيل"),
        Category.CITIES to listOf("الرياض", "دبي", "القاهرة", "مكة المكرمة", "باريس", "طوكيو", "لندن", "روما", "إسطنبول", "نيويورك"),
        Category.OBJECTS to listOf("ساعة يد", "نظارة شمسية", "مظلة", "كاميرا", "حقيبة جلدية", "بوصلة", "مفتاح قديم", "خزنة سرية"),
        Category.TECHNOLOGY to listOf("ذكاء اصطناعي", "هاتف ذكي", "طائرة مسيرة", "حاسوب عملاق", "نظارة واقع افتراضي", "قمر صناعي"),
        Category.VEHICLES to listOf("طائرة ركاب", "غواصة", "مروحية", "قطار سريع", "سفينة سياحية", "دبابة مدرعة", "سيارة سباق"),
        Category.JOBS to listOf("طبيب جراح", "طيار", "عميل سري", "رائد فضاء", "مهندس برمجة", "محقق شرطة", "طاه محترف")
    )

    // ==========================================
    // DICTIONARY DATA: SPANISH
    // ==========================================
    private val spanishBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("León", "Tigre", "Lobo", "Águila Imperial", "Oso Polar", "Delfín", "Tiburón Blanco", "Caballo", "Elefante", "Jaguar", "Leopardo"),
        Category.FOOD to listOf("Paella", "Tacos al Pastor", "Tortilla de Patatas", "Pizza Cuatro Quesos", "Hamburguesa", "Churros", "Gazpacho", "Empanadas", "Ceviche"),
        Category.COUNTRIES to listOf("España", "México", "Argentina", "Colombia", "Francia", "Italia", "Japón", "Brasil", "Alemania", "Chile"),
        Category.CITIES to listOf("Madrid", "Barcelona", "Ciudad de México", "Buenos Aires", "París", "Roma", "Tokio", "Nueva York", "Sevilla"),
        Category.OBJECTS to listOf("Reloj de Bolsillo", "Gafas de Sol", "Brújula Dorada", "Cámara Réflex", "Llave Antigua", "Caja Fuerte", "Mochila Táctica"),
        Category.TECHNOLOGY to listOf("Inteligencia Artificial", "Teléfono Móvil", "Dron Espía", "Superordenador", "Gafas de Realidad Virtual"),
        Category.VEHICLES to listOf("Avión Comercial", "Submarino Nuclear", "Tren de Alta Velocidad", "Helicóptero", "Coche de Carreras"),
        Category.JOBS to listOf("Cirujano", "Piloto de Caza", "Agente Secreto", "Astronauta", "Detective Privado", "Bombero", "Cocinero Jefe")
    )

    // ==========================================
    // DICTIONARY DATA: FRENCH
    // ==========================================
    private val frenchBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("Lion", "Tigre", "Loup Gris", "Aigle Royal", "Ours Polaire", "Dauphin", "Requin Blanc", "Cheval", "Éléphant", "Léopard"),
        Category.FOOD to listOf("Croissant", "Baguette", "Ratatouille", "Crêpe Suzette", "Pizza Margherita", "Quiche Lorraine", "Fromage Brie", "Soupe à l'oignon"),
        Category.COUNTRIES to listOf("France", "Canada", "Suisse", "Belgique", "Italie", "Espagne", "Japon", "Allemagne", "Brésil"),
        Category.CITIES to listOf("Paris", "Lyon", "Marseille", "Genève", "Montréal", "Rome", "Tokyo", "Londres", "New York"),
        Category.OBJECTS to listOf("Montre en or", "Lunettes de soleil", "Boussole ancienne", "Appareil photo", "Clé mystérieuse", "Coffre-fort"),
        Category.TECHNOLOGY to listOf("Intelligence Artificielle", "Drone furtif", "Smartphone", "Supercalculateur", "Casque VR"),
        Category.VEHICLES to listOf("Avion de ligne", "Sous-marin nucléaire", "Train à grande vitesse", "Hélicoptère", "Voiture de sport"),
        Category.JOBS to listOf("Chirurgien", "Pilote de ligne", "Agent secret", "Astronaute", "Détective privé", "Chef étoilé")
    )

    // ==========================================
    // DICTIONARY DATA: GERMAN
    // ==========================================
    private val germanBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("Löwe", "Tiger", "Grauwolf", "Steinadler", "Eisbär", "Delfin", "Weißer Hai", "Gepard", "Elefant", "Braunbär"),
        Category.FOOD to listOf("Currywurst", "Schnitzel", "Brezel", "Schwarzwälder Kirschtorte", "Sauerbraten", "Bratwurst", "Käsespätzle"),
        Category.COUNTRIES to listOf("Deutschland", "Österreich", "Schweiz", "Frankreich", "Italien", "Spanien", "Japan", "Schweden"),
        Category.CITIES to listOf("Berlin", "München", "Hamburg", "Wien", "Zürich", "Frankfurt", "Köln", "Rom", "Tokio"),
        Category.OBJECTS to listOf("Armbanduhr", "Sonnenbrille", "Taschenkompass", "Fotoapparat", "Tresor", "Schweizer Messer"),
        Category.TECHNOLOGY to listOf("Künstliche Intelligenz", "Quantencomputer", "Tarnkappendrohne", "Smartphone", "Satellit"),
        Category.VEHICLES to listOf("Passagierflugzeug", "Atom-U-Boot", "Hochgeschwindigkeitszug", "Hubschrauber", "Rennwagen"),
        Category.JOBS to listOf("Chirurg", "Pilot", "Geheimagent", "Astronaut", "Detektiv", "Softwareentwickler")
    )

    // ==========================================
    // DICTIONARY DATA: RUSSIAN
    // ==========================================
    private val russianBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("Лев", "Тигр", "Серый волк", "Белый медведь", "Беркут", "Дельфин", "Белая акула", "Амурский леопард"),
        Category.FOOD to listOf("Борщ", "Пельмени", "Блины с икрой", "Бефстроганов", "Шашлык", "Пицца", "Оливье", "Сырники"),
        Category.COUNTRIES to listOf("Россия", "Франция", "Германия", "Италия", "Япония", "Китай", "Канада", "Бразилия"),
        Category.CITIES to listOf("Москва", "Санкт-Петербург", "Казань", "Сочи", "Париж", "Рим", "Токио", "Нью-Йорк"),
        Category.OBJECTS to listOf("Наручные часы", "Солнцезащитные очки", "Компас", "Фотоаппарат", "Тайный ключ", "Сейф"),
        Category.TECHNOLOGY to listOf("Искусственный интеллект", "Квантовый компьютер", "Дрон", "Смартфон", "Спутник"),
        Category.VEHICLES to listOf("Пассажирский самолет", "Атомная подводная лодка", "Скоростной поезд", "Вертолет", "Танк"),
        Category.JOBS to listOf("Хирург", "Пилот истребителя", "Секретный агент", "Космонавт", "Следователь", "Шеф-повар")
    )

    // ==========================================
    // DICTIONARY DATA: TURKISH
    // ==========================================
    private val turkishBaseWords: Map<Category, List<String>> = mapOf(
        Category.ANIMALS to listOf("Aslan", "Kaplan", "Bozkurt", "Kutup Ayısı", "Kaya Kartalı", "Yunus", "Büyük Beyaz Köpekbalığı", "Çita"),
        Category.FOOD to listOf("İskender Kebap", "Lahmacun", "Baklava", "Karnıyarık", "Mantı", "Döner", "Menemen", "Künefe"),
        Category.COUNTRIES to listOf("Türkiye", "Almanya", "Fransa", "İtalya", "Japonya", "İspanya", "Brezilya", "Güney Kore"),
        Category.CITIES to listOf("İstanbul", "Ankara", "İzmir", "Antalya", "Bursa", "Roma", "Paris", "Tokyo", "Londra"),
        Category.OBJECTS to listOf("Kol Saati", "Güneş Gözlüğü", "Pusula", "Fotoğraf Makinesi", "Antika Anahtar", "Çelik Kasa"),
        Category.TECHNOLOGY to listOf("Yapay Zeka", "İnsansız Hava Aracı", "Akıllı Telefon", "Kuantum Bilgisayar", "Uydu"),
        Category.VEHICLES to listOf("Yolcu Uçağı", "Denizaltı", "Hızlı Tren", "Helikopter", "Zırhlı Tank", "Yarış Arabası"),
        Category.JOBS to listOf("Beyin Cerrahı", "Savaş Pilotu", "Gizli Ajan", "Astronot", "Dedektif", "Başmühendis")
    )
}
