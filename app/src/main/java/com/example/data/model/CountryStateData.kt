package com.example.data.model

data class StateInfo(
    val name: String,
    val code: String? = null,
    val capitalOrHub: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)

data class CountryInfo(
    val id: String,
    val name: String,
    val code: String,
    val flagEmoji: String,
    val continent: String,
    val capital: String,
    val states: List<StateInfo>
)

object WorldCountriesDatabase {

    val CONTINENTS = listOf(
        "All Continents",
        "North America",
        "Asia",
        "Europe",
        "South America",
        "Africa",
        "Oceania"
    )

    val COUNTRIES: List<CountryInfo> = listOf(
        // United States (50 States + DC)
        CountryInfo(
            id = "US",
            name = "United States",
            code = "US",
            flagEmoji = "🇺🇸",
            continent = "North America",
            capital = "Washington, D.C.",
            states = listOf(
                StateInfo("Alabama", "AL", "Montgomery", 32.3792, -86.3077, "America/Chicago"),
                StateInfo("Alaska", "AK", "Juneau", 58.3019, -134.4197, "America/Anchorage"),
                StateInfo("Arizona", "AZ", "Phoenix", 33.4484, -112.0740, "America/Phoenix"),
                StateInfo("Arkansas", "AR", "Little Rock", 34.7465, -92.2896, "America/Chicago"),
                StateInfo("California", "CA", "Sacramento", 38.5816, -121.4944, "America/Los_Angeles"),
                StateInfo("Colorado", "CO", "Denver", 39.7392, -104.9903, "America/Denver"),
                StateInfo("Connecticut", "CT", "Hartford", 41.7658, -72.6734, "America/New_York"),
                StateInfo("Delaware", "DE", "Dover", 39.1582, -75.5244, "America/New_York"),
                StateInfo("District of Columbia", "DC", "Washington", 38.9072, -77.0369, "America/New_York"),
                StateInfo("Florida", "FL", "Tallahassee", 30.4383, -84.2807, "America/New_York"),
                StateInfo("Georgia", "GA", "Atlanta", 33.7490, -84.3880, "America/New_York"),
                StateInfo("Hawaii", "HI", "Honolulu", 21.3069, -157.8583, "Pacific/Honolulu"),
                StateInfo("Idaho", "ID", "Boise", 43.6150, -116.2023, "America/Boise"),
                StateInfo("Illinois", "IL", "Springfield", 39.7817, -89.6501, "America/Chicago"),
                StateInfo("Indiana", "IN", "Indianapolis", 39.7684, -86.1581, "America/Indiana/Indianapolis"),
                StateInfo("Iowa", "IA", "Des Moines", 41.5868, -93.6250, "America/Chicago"),
                StateInfo("Kansas", "KS", "Topeka", 39.0473, -95.6752, "America/Chicago"),
                StateInfo("Kentucky", "KY", "Frankfort", 38.2009, -84.8733, "America/New_York"),
                StateInfo("Louisiana", "LA", "Baton Rouge", 30.4515, -91.1871, "America/Chicago"),
                StateInfo("Maine", "ME", "Augusta", 44.3106, -69.7795, "America/New_York"),
                StateInfo("Maryland", "MD", "Annapolis", 38.9784, -76.4922, "America/New_York"),
                StateInfo("Massachusetts", "MA", "Boston", 42.3601, -71.0589, "America/New_York"),
                StateInfo("Michigan", "MI", "Lansing", 42.7325, -84.5555, "America/Detroit"),
                StateInfo("Minnesota", "MN", "Saint Paul", 44.9537, -93.0900, "America/Chicago"),
                StateInfo("Mississippi", "MS", "Jackson", 32.2988, -90.1848, "America/Chicago"),
                StateInfo("Missouri", "MO", "Jefferson City", 38.5767, -92.1735, "America/Chicago"),
                StateInfo("Montana", "MT", "Helena", 46.5891, -112.0391, "America/Denver"),
                StateInfo("Nebraska", "NE", "Lincoln", 40.8136, -96.7026, "America/Chicago"),
                StateInfo("Nevada", "NV", "Carson City", 39.1638, -119.7674, "America/Los_Angeles"),
                StateInfo("New Hampshire", "NH", "Concord", 43.2081, -71.5376, "America/New_York"),
                StateInfo("New Jersey", "NJ", "Trenton", 40.2171, -74.7429, "America/New_York"),
                StateInfo("New Mexico", "NM", "Santa Fe", 35.6870, -105.9378, "America/Denver"),
                StateInfo("New York", "NY", "Albany", 42.6526, -73.7562, "America/New_York"),
                StateInfo("North Carolina", "NC", "Raleigh", 35.7796, -78.6382, "America/New_York"),
                StateInfo("North Dakota", "ND", "Bismarck", 46.8083, -100.7837, "America/Chicago"),
                StateInfo("Ohio", "OH", "Columbus", 39.9612, -82.9988, "America/New_York"),
                StateInfo("Oklahoma", "OK", "Oklahoma City", 35.4676, -97.5164, "America/Chicago"),
                StateInfo("Oregon", "OR", "Salem", 44.9429, -123.0351, "America/Los_Angeles"),
                StateInfo("Pennsylvania", "PA", "Harrisburg", 40.2732, -76.8867, "America/New_York"),
                StateInfo("Rhode Island", "RI", "Providence", 41.8240, -71.4128, "America/New_York"),
                StateInfo("South Carolina", "SC", "Columbia", 34.0007, -81.0348, "America/New_York"),
                StateInfo("South Dakota", "SD", "Pierre", 44.3683, -100.3510, "America/Chicago"),
                StateInfo("Tennessee", "TN", "Nashville", 36.1627, -86.7816, "America/Chicago"),
                StateInfo("Texas", "TX", "Austin", 30.2672, -97.7431, "America/Chicago"),
                StateInfo("Utah", "UT", "Salt Lake City", 40.7608, -111.8910, "America/Denver"),
                StateInfo("Vermont", "VT", "Montpelier", 44.2601, -72.5754, "America/New_York"),
                StateInfo("Virginia", "VA", "Richmond", 37.5407, -77.4360, "America/New_York"),
                StateInfo("Washington", "WA", "Olympia", 47.0379, -122.9007, "America/Los_Angeles"),
                StateInfo("West Virginia", "WV", "Charleston", 38.3498, -81.6326, "America/New_York"),
                StateInfo("Wisconsin", "WI", "Madison", 43.0731, -89.4012, "America/Chicago"),
                StateInfo("Wyoming", "WY", "Cheyenne", 41.1400, -104.8202, "America/Denver")
            )
        ),

        // India (28 States & Major Union Territories)
        CountryInfo(
            id = "IN",
            name = "India",
            code = "IN",
            flagEmoji = "🇮🇳",
            continent = "Asia",
            capital = "New Delhi",
            states = listOf(
                StateInfo("Andhra Pradesh", "AP", "Amaravati", 16.5062, 80.6480, "Asia/Kolkata"),
                StateInfo("Arunachal Pradesh", "AR", "Itanagar", 27.0844, 93.6053, "Asia/Kolkata"),
                StateInfo("Assam", "AS", "Dispur", 26.1445, 91.7362, "Asia/Kolkata"),
                StateInfo("Bihar", "BR", "Patna", 25.5941, 85.1376, "Asia/Kolkata"),
                StateInfo("Chhattisgarh", "CG", "Raipur", 21.2514, 81.6296, "Asia/Kolkata"),
                StateInfo("Delhi (NCT)", "DL", "New Delhi", 28.6139, 77.2090, "Asia/Kolkata"),
                StateInfo("Goa", "GA", "Panaji", 15.4909, 73.8278, "Asia/Kolkata"),
                StateInfo("Gujarat", "GJ", "Gandhinagar", 23.2156, 72.6369, "Asia/Kolkata"),
                StateInfo("Haryana", "HR", "Chandigarh", 30.7333, 76.7794, "Asia/Kolkata"),
                StateInfo("Himachal Pradesh", "HP", "Shimla", 31.1048, 77.1734, "Asia/Kolkata"),
                StateInfo("Jammu & Kashmir", "JK", "Srinagar", 34.0837, 74.7973, "Asia/Kolkata"),
                StateInfo("Jharkhand", "JH", "Ranchi", 23.3441, 85.3096, "Asia/Kolkata"),
                StateInfo("Karnataka", "KA", "Bengaluru", 12.9716, 77.5946, "Asia/Kolkata"),
                StateInfo("Kerala", "KL", "Thiruvananthapuram", 8.5241, 76.9366, "Asia/Kolkata"),
                StateInfo("Ladakh", "LA", "Leh", 34.1526, 77.5771, "Asia/Kolkata"),
                StateInfo("Madhya Pradesh", "MP", "Bhopal", 23.2599, 77.4126, "Asia/Kolkata"),
                StateInfo("Maharashtra", "MH", "Mumbai", 19.0760, 72.8777, "Asia/Kolkata"),
                StateInfo("Manipur", "MN", "Imphal", 24.8170, 93.9368, "Asia/Kolkata"),
                StateInfo("Meghalaya", "ML", "Shillong", 25.5788, 91.8933, "Asia/Kolkata"),
                StateInfo("Mizoram", "MZ", "Aizawl", 23.7271, 92.7176, "Asia/Kolkata"),
                StateInfo("Nagaland", "NL", "Kohima", 25.6751, 94.1086, "Asia/Kolkata"),
                StateInfo("Odisha", "OR", "Bhubaneswar", 20.2961, 85.8245, "Asia/Kolkata"),
                StateInfo("Punjab", "PB", "Chandigarh", 30.7333, 76.7794, "Asia/Kolkata"),
                StateInfo("Rajasthan", "RJ", "Jaipur", 26.9124, 75.7873, "Asia/Kolkata"),
                StateInfo("Sikkim", "SK", "Gangtok", 27.3389, 88.6065, "Asia/Kolkata"),
                StateInfo("Tamil Nadu", "TN", "Chennai", 13.0827, 80.2707, "Asia/Kolkata"),
                StateInfo("Telangana", "TG", "Hyderabad", 17.3850, 78.4867, "Asia/Kolkata"),
                StateInfo("Tripura", "TR", "Agartala", 23.8315, 91.2868, "Asia/Kolkata"),
                StateInfo("Uttar Pradesh", "UP", "Lucknow", 26.8467, 80.9462, "Asia/Kolkata"),
                StateInfo("Uttarakhand", "UK", "Dehradun", 30.3165, 78.0322, "Asia/Kolkata"),
                StateInfo("West Bengal", "WB", "Kolkata", 22.5726, 88.3639, "Asia/Kolkata")
            )
        ),

        // Canada (Provinces & Territories)
        CountryInfo(
            id = "CA",
            name = "Canada",
            code = "CA",
            flagEmoji = "🇨🇦",
            continent = "North America",
            capital = "Ottawa",
            states = listOf(
                StateInfo("Ontario", "ON", "Toronto", 43.6532, -79.3832, "America/Toronto"),
                StateInfo("Quebec", "QC", "Quebec City", 46.8139, -71.2080, "America/Toronto"),
                StateInfo("British Columbia", "BC", "Victoria", 48.4284, -123.3656, "America/Vancouver"),
                StateInfo("Alberta", "AB", "Edmonton", 53.5461, -113.4938, "America/Edmonton"),
                StateInfo("Manitoba", "MB", "Winnipeg", 49.8951, -97.1384, "America/Winnipeg"),
                StateInfo("Saskatchewan", "SK", "Regina", 50.4547, -104.6067, "America/Regina"),
                StateInfo("Nova Scotia", "NS", "Halifax", 44.6488, -63.5752, "America/Halifax"),
                StateInfo("New Brunswick", "NB", "Fredericton", 45.9636, -66.6431, "America/Halifax"),
                StateInfo("Newfoundland & Labrador", "NL", "St. John's", 47.5615, -52.7126, "America/St_Johns"),
                StateInfo("Prince Edward Island", "PE", "Charlottetown", 46.2382, -63.1311, "America/Halifax"),
                StateInfo("Northwest Territories", "NT", "Yellowknife", 62.4540, -114.3718, "America/Yellowknife"),
                StateInfo("Yukon", "YT", "Whitehorse", 60.7212, -135.0568, "America/Whitehorse"),
                StateInfo("Nunavut", "NU", "Iqaluit", 63.7467, -68.5170, "America/Iqaluit")
            )
        ),

        // Australia (States & Territories)
        CountryInfo(
            id = "AU",
            name = "Australia",
            code = "AU",
            flagEmoji = "🇦🇺",
            continent = "Oceania",
            capital = "Canberra",
            states = listOf(
                StateInfo("New South Wales", "NSW", "Sydney", -33.8688, 151.2093, "Australia/Sydney"),
                StateInfo("Victoria", "VIC", "Melbourne", -37.8136, 144.9631, "Australia/Melbourne"),
                StateInfo("Queensland", "QLD", "Brisbane", -27.4698, 153.0251, "Australia/Brisbane"),
                StateInfo("Western Australia", "WA", "Perth", -31.9505, 115.8605, "Australia/Perth"),
                StateInfo("South Australia", "SA", "Adelaide", -34.9285, 138.6007, "Australia/Adelaide"),
                StateInfo("Tasmania", "TAS", "Hobart", -42.8821, 147.3272, "Australia/Hobart"),
                StateInfo("Australian Capital Territory", "ACT", "Canberra", -35.2809, 149.1300, "Australia/Sydney"),
                StateInfo("Northern Territory", "NT", "Darwin", -12.4634, 130.8456, "Australia/Darwin")
            )
        ),

        // Germany (16 Federal States)
        CountryInfo(
            id = "DE",
            name = "Germany",
            code = "DE",
            flagEmoji = "🇩🇪",
            continent = "Europe",
            capital = "Berlin",
            states = listOf(
                StateInfo("Bavaria", "BY", "Munich", 48.1351, 11.5820, "Europe/Berlin"),
                StateInfo("Baden-Württemberg", "BW", "Stuttgart", 48.7758, 9.1829, "Europe/Berlin"),
                StateInfo("North Rhine-Westphalia", "NW", "Düsseldorf", 51.2277, 6.7735, "Europe/Berlin"),
                StateInfo("Hesse", "HE", "Wiesbaden", 50.0782, 8.2398, "Europe/Berlin"),
                StateInfo("Saxony", "SN", "Dresden", 51.0504, 13.7373, "Europe/Berlin"),
                StateInfo("Lower Saxony", "NI", "Hanover", 52.3759, 9.7320, "Europe/Berlin"),
                StateInfo("Berlin", "BE", "Berlin", 52.5200, 13.4050, "Europe/Berlin"),
                StateInfo("Hamburg", "HH", "Hamburg", 53.5511, 9.9937, "Europe/Berlin"),
                StateInfo("Rhineland-Palatinate", "RP", "Mainz", 49.9929, 8.2473, "Europe/Berlin"),
                StateInfo("Brandenburg", "BB", "Potsdam", 52.3906, 13.0645, "Europe/Berlin"),
                StateInfo("Schleswig-Holstein", "SH", "Kiel", 54.3233, 10.1228, "Europe/Berlin"),
                StateInfo("Thuringia", "TH", "Erfurt", 50.9848, 11.0299, "Europe/Berlin"),
                StateInfo("Saxony-Anhalt", "ST", "Magdeburg", 52.1205, 11.6276, "Europe/Berlin"),
                StateInfo("Mecklenburg-Vorpommern", "MV", "Schwerin", 53.6355, 11.4012, "Europe/Berlin"),
                StateInfo("Saarland", "SL", "Saarbrücken", 49.2402, 6.9969, "Europe/Berlin"),
                StateInfo("Bremen", "HB", "Bremen", 53.0793, 8.8017, "Europe/Berlin")
            )
        ),

        // United Kingdom
        CountryInfo(
            id = "GB",
            name = "United Kingdom",
            code = "GB",
            flagEmoji = "🇬🇧",
            continent = "Europe",
            capital = "London",
            states = listOf(
                StateInfo("England", "ENG", "London", 51.5074, -0.1278, "Europe/London"),
                StateInfo("Scotland", "SCT", "Edinburgh", 55.9533, -3.1883, "Europe/London"),
                StateInfo("Wales", "WLS", "Cardiff", 51.4816, -3.1791, "Europe/London"),
                StateInfo("Northern Ireland", "NIR", "Belfast", 54.5973, -5.9301, "Europe/London"),
                StateInfo("Greater Manchester", "GM", "Manchester", 53.4808, -2.2426, "Europe/London"),
                StateInfo("West Midlands", "WM", "Birmingham", 52.4862, -1.8904, "Europe/London"),
                StateInfo("West Yorkshire", "WY", "Leeds", 53.8008, -1.5491, "Europe/London")
            )
        ),

        // Japan (Prefectures & Regions)
        CountryInfo(
            id = "JP",
            name = "Japan",
            code = "JP",
            flagEmoji = "🇯🇵",
            continent = "Asia",
            capital = "Tokyo",
            states = listOf(
                StateInfo("Tokyo", "13", "Tokyo", 35.6762, 139.6503, "Asia/Tokyo"),
                StateInfo("Osaka", "27", "Osaka", 34.6937, 135.5023, "Asia/Tokyo"),
                StateInfo("Kyoto", "26", "Kyoto", 35.0116, 135.7681, "Asia/Tokyo"),
                StateInfo("Kanagawa", "14", "Yokohama", 35.4437, 139.6380, "Asia/Tokyo"),
                StateInfo("Aichi", "23", "Nagoya", 35.1815, 136.9066, "Asia/Tokyo"),
                StateInfo("Fukuoka", "40", "Fukuoka", 33.5904, 130.4017, "Asia/Tokyo"),
                StateInfo("Hokkaido", "01", "Sapporo", 43.0618, 141.3545, "Asia/Tokyo"),
                StateInfo("Hiroshima", "34", "Hiroshima", 34.3853, 132.4553, "Asia/Tokyo"),
                StateInfo("Miyagi", "04", "Sendai", 38.2682, 140.8694, "Asia/Tokyo"),
                StateInfo("Okinawa", "47", "Naha", 26.2124, 127.6809, "Asia/Tokyo"),
                StateInfo("Hyogo", "28", "Kobe", 34.6901, 135.1955, "Asia/Tokyo"),
                StateInfo("Shizuoka", "22", "Shizuoka", 34.9756, 138.3828, "Asia/Tokyo")
            )
        ),

        // Brazil (States)
        CountryInfo(
            id = "BR",
            name = "Brazil",
            code = "BR",
            flagEmoji = "🇧🇷",
            continent = "South America",
            capital = "Brasília",
            states = listOf(
                StateInfo("São Paulo", "SP", "São Paulo", -23.5505, -46.6333, "America/Sao_Paulo"),
                StateInfo("Rio de Janeiro", "RJ", "Rio de Janeiro", -22.9068, -43.1729, "America/Sao_Paulo"),
                StateInfo("Minas Gerais", "MG", "Belo Horizonte", -19.9167, -43.9345, "America/Sao_Paulo"),
                StateInfo("Bahia", "BA", "Salvador", -12.9714, -38.5014, "America/Bahia"),
                StateInfo("Paraná", "PR", "Curitiba", -25.4290, -49.2671, "America/Sao_Paulo"),
                StateInfo("Rio Grande do Sul", "RS", "Porto Alegre", -30.0346, -51.2177, "America/Sao_Paulo"),
                StateInfo("Pernambuco", "PE", "Recife", -8.0476, -34.8770, "America/Recife"),
                StateInfo("Ceará", "CE", "Fortaleza", -3.7172, -38.5433, "America/Fortaleza"),
                StateInfo("Distrito Federal", "DF", "Brasília", -15.7975, -47.8919, "America/Sao_Paulo"),
                StateInfo("Santa Catarina", "SC", "Florianópolis", -27.5954, -48.5480, "America/Sao_Paulo"),
                StateInfo("Amazonas", "AM", "Manaus", -3.1190, -60.0217, "America/Manaus"),
                StateInfo("Goiás", "GO", "Goiânia", -16.6869, -49.2648, "America/Sao_Paulo")
            )
        ),

        // Mexico (States)
        CountryInfo(
            id = "MX",
            name = "Mexico",
            code = "MX",
            flagEmoji = "🇲🇽",
            continent = "North America",
            capital = "Mexico City",
            states = listOf(
                StateInfo("Ciudad de México", "CDMX", "Mexico City", 19.4326, -99.1332, "America/Mexico_City"),
                StateInfo("Jalisco", "JAL", "Guadalajara", 20.6597, -103.3496, "America/Mexico_City"),
                StateInfo("Nuevo León", "NL", "Monterrey", 25.6866, -100.3161, "America/Monterrey"),
                StateInfo("Puebla", "PUE", "Puebla", 19.0414, -98.2063, "America/Mexico_City"),
                StateInfo("Guanajuato", "GTO", "Guanajuato", 21.0190, -101.2574, "America/Mexico_City"),
                StateInfo("Veracruz", "VER", "Xalapa", 19.5438, -96.9102, "America/Mexico_City"),
                StateInfo("Quintana Roo", "QROO", "Cancún", 21.1619, -86.8515, "America/Cancun"),
                StateInfo("Yucatán", "YUC", "Mérida", 20.9674, -89.5926, "America/Merida"),
                StateInfo("Baja California", "BC", "Tijuana", 32.5149, -117.0382, "America/Tijuana"),
                StateInfo("Chihuahua", "CHIH", "Chihuahua", 28.6353, -106.0889, "America/Chihuahua"),
                StateInfo("Oaxaca", "OAX", "Oaxaca", 17.0732, -96.7266, "America/Mexico_City")
            )
        ),

        // France (Regions)
        CountryInfo(
            id = "FR",
            name = "France",
            code = "FR",
            flagEmoji = "🇫🇷",
            continent = "Europe",
            capital = "Paris",
            states = listOf(
                StateInfo("Île-de-France", "IDF", "Paris", 48.8566, 2.3522, "Europe/Paris"),
                StateInfo("Auvergne-Rhône-Alpes", "ARA", "Lyon", 45.7640, 4.8357, "Europe/Paris"),
                StateInfo("Provence-Alpes-Côte d'Azur", "PACA", "Marseille", 43.2965, 5.3698, "Europe/Paris"),
                StateInfo("Nouvelle-Aquitaine", "NAQ", "Bordeaux", 44.8378, -0.5792, "Europe/Paris"),
                StateInfo("Occitanie", "OCC", "Toulouse", 43.6047, 1.4442, "Europe/Paris"),
                StateInfo("Hauts-de-France", "HDF", "Lille", 50.6292, 3.0573, "Europe/Paris"),
                StateInfo("Grand Est", "GES", "Strasbourg", 48.5734, 7.7521, "Europe/Paris"),
                StateInfo("Pays de la Loire", "PDL", "Nantes", 47.2184, -1.5536, "Europe/Paris"),
                StateInfo("Brittany", "BRE", "Rennes", 48.1173, -1.6778, "Europe/Paris"),
                StateInfo("Normandy", "NOR", "Rouen", 49.4432, 1.0999, "Europe/Paris")
            )
        ),

        // Italy (Regions)
        CountryInfo(
            id = "IT",
            name = "Italy",
            code = "IT",
            flagEmoji = "🇮🇹",
            continent = "Europe",
            capital = "Rome",
            states = listOf(
                StateInfo("Lazio", "LAZ", "Rome", 41.9028, 12.4964, "Europe/Rome"),
                StateInfo("Lombardy", "LOM", "Milan", 45.4642, 9.1900, "Europe/Rome"),
                StateInfo("Campania", "CAM", "Naples", 40.8518, 14.2681, "Europe/Rome"),
                StateInfo("Veneto", "VEN", "Venice", 45.4408, 12.3155, "Europe/Rome"),
                StateInfo("Piedmont", "PIE", "Turin", 45.0703, 7.6869, "Europe/Rome"),
                StateInfo("Tuscany", "TOS", "Florence", 43.7696, 11.2558, "Europe/Rome"),
                StateInfo("Sicily", "SIC", "Palermo", 38.1157, 13.3615, "Europe/Rome"),
                StateInfo("Emilia-Romagna", "EMR", "Bologna", 44.4949, 11.3426, "Europe/Rome"),
                StateInfo("Puglia", "PUG", "Bari", 41.1171, 16.8719, "Europe/Rome"),
                StateInfo("Liguria", "LIG", "Genoa", 44.4056, 8.9463, "Europe/Rome")
            )
        ),

        // Spain (Autonomous Communities)
        CountryInfo(
            id = "ES",
            name = "Spain",
            code = "ES",
            flagEmoji = "🇪🇸",
            continent = "Europe",
            capital = "Madrid",
            states = listOf(
                StateInfo("Community of Madrid", "MD", "Madrid", 40.4168, -3.7038, "Europe/Madrid"),
                StateInfo("Catalonia", "CT", "Barcelona", 41.3851, 2.1734, "Europe/Madrid"),
                StateInfo("Andalusia", "AN", "Seville", 37.3891, -5.9845, "Europe/Madrid"),
                StateInfo("Valencian Community", "VC", "Valencia", 39.4699, -0.3763, "Europe/Madrid"),
                StateInfo("Basque Country", "PV", "Bilbao", 43.2630, -2.9350, "Europe/Madrid"),
                StateInfo("Galicia", "GA", "Santiago de Compostela", 42.8782, -8.5448, "Europe/Madrid"),
                StateInfo("Canary Islands", "CN", "Las Palmas", 28.1235, -15.4363, "Atlantic/Canary"),
                StateInfo("Balearic Islands", "IB", "Palma", 39.5696, 2.6502, "Europe/Madrid"),
                StateInfo("Castile and León", "CL", "Valladolid", 41.6523, -4.7245, "Europe/Madrid")
            )
        ),

        // China (Provinces & Municipalities)
        CountryInfo(
            id = "CN",
            name = "China",
            code = "CN",
            flagEmoji = "🇨🇳",
            continent = "Asia",
            capital = "Beijing",
            states = listOf(
                StateInfo("Beijing", "BJ", "Beijing", 39.9042, 116.4074, "Asia/Shanghai"),
                StateInfo("Shanghai", "SH", "Shanghai", 31.2304, 121.4737, "Asia/Shanghai"),
                StateInfo("Guangdong", "GD", "Guangzhou", 23.1291, 113.2644, "Asia/Shanghai"),
                StateInfo("Zhejiang", "ZJ", "Hangzhou", 30.2741, 120.1551, "Asia/Shanghai"),
                StateInfo("Sichuan", "SC", "Chengdu", 30.5728, 104.0668, "Asia/Shanghai"),
                StateInfo("Jiangsu", "JS", "Nanjing", 32.0603, 118.7969, "Asia/Shanghai"),
                StateInfo("Hubei", "HB", "Wuhan", 30.5928, 114.3055, "Asia/Shanghai"),
                StateInfo("Shaanxi", "SN", "Xi'an", 34.3416, 108.9398, "Asia/Shanghai"),
                StateInfo("Hong Kong", "HK", "Hong Kong", 22.3193, 114.1694, "Asia/Hong_Kong"),
                StateInfo("Chongqing", "CQ", "Chongqing", 29.4316, 106.9123, "Asia/Shanghai")
            )
        ),

        // South Africa (Provinces)
        CountryInfo(
            id = "ZA",
            name = "South Africa",
            code = "ZA",
            flagEmoji = "🇿🇦",
            continent = "Africa",
            capital = "Pretoria",
            states = listOf(
                StateInfo("Gauteng", "GP", "Johannesburg", -26.2041, 28.0473, "Africa/Johannesburg"),
                StateInfo("Western Cape", "WC", "Cape Town", -33.9249, 18.4241, "Africa/Johannesburg"),
                StateInfo("KwaZulu-Natal", "KZN", "Durban", -29.8587, 31.0218, "Africa/Johannesburg"),
                StateInfo("Eastern Cape", "EC", "Gqeberha", -33.9608, 25.6022, "Africa/Johannesburg"),
                StateInfo("Free State", "FS", "Bloemfontein", -29.1181, 26.2249, "Africa/Johannesburg"),
                StateInfo("Limpopo", "LP", "Polokwane", -23.9045, 29.4689, "Africa/Johannesburg"),
                StateInfo("Mpumalanga", "MP", "Mbombela", -25.4753, 30.9694, "Africa/Johannesburg"),
                StateInfo("North West", "NW", "Mahikeng", -25.8653, 25.6442, "Africa/Johannesburg"),
                StateInfo("Northern Cape", "NC", "Kimberley", -28.7282, 24.7499, "Africa/Johannesburg")
            )
        ),

        // Nigeria (States)
        CountryInfo(
            id = "NG",
            name = "Nigeria",
            code = "NG",
            flagEmoji = "🇳🇬",
            continent = "Africa",
            capital = "Abuja",
            states = listOf(
                StateInfo("Lagos State", "LA", "Lagos", 6.5244, 3.3792, "Africa/Lagos"),
                StateInfo("Federal Capital Territory", "FCT", "Abuja", 9.0765, 7.3986, "Africa/Lagos"),
                StateInfo("Kano State", "KN", "Kano", 12.0022, 8.5920, "Africa/Lagos"),
                StateInfo("Rivers State", "RI", "Port Harcourt", 4.8156, 7.0498, "Africa/Lagos"),
                StateInfo("Oyo State", "OY", "Ibadan", 7.3775, 3.9470, "Africa/Lagos"),
                StateInfo("Kaduna State", "KD", "Kaduna", 10.5105, 7.4165, "Africa/Lagos"),
                StateInfo("Edo State", "ED", "Benin City", 6.3350, 5.6037, "Africa/Lagos"),
                StateInfo("Anambra State", "AN", "Awka", 6.2209, 7.0723, "Africa/Lagos")
            )
        ),

        // United Arab Emirates (7 Emirates)
        CountryInfo(
            id = "AE",
            name = "United Arab Emirates",
            code = "AE",
            flagEmoji = "🇦🇪",
            continent = "Asia",
            capital = "Abu Dhabi",
            states = listOf(
                StateInfo("Dubai", "DXB", "Dubai", 25.2048, 55.2708, "Asia/Dubai"),
                StateInfo("Abu Dhabi", "AUH", "Abu Dhabi", 24.4539, 54.3773, "Asia/Dubai"),
                StateInfo("Sharjah", "SHJ", "Sharjah", 25.3463, 55.4209, "Asia/Dubai"),
                StateInfo("Ajman", "AJM", "Ajman", 25.4052, 55.5136, "Asia/Dubai"),
                StateInfo("Ras Al Khaimah", "RAK", "Ras Al Khaimah", 25.7895, 55.9432, "Asia/Dubai"),
                StateInfo("Fujairah", "FUJ", "Fujairah", 25.1288, 56.3265, "Asia/Dubai"),
                StateInfo("Umm Al Quwain", "UAQ", "Umm Al Quwain", 25.5653, 55.5533, "Asia/Dubai")
            )
        ),

        // Egypt (Governorates)
        CountryInfo(
            id = "EG",
            name = "Egypt",
            code = "EG",
            flagEmoji = "🇪🇬",
            continent = "Africa",
            capital = "Cairo",
            states = listOf(
                StateInfo("Cairo Governorate", "CAI", "Cairo", 30.0444, 31.2357, "Africa/Cairo"),
                StateInfo("Alexandria Governorate", "ALX", "Alexandria", 31.2001, 29.9187, "Africa/Cairo"),
                StateInfo("Giza Governorate", "GIZ", "Giza", 30.0131, 31.2089, "Africa/Cairo"),
                StateInfo("Luxor Governorate", "LUX", "Luxor", 25.6872, 32.6396, "Africa/Cairo"),
                StateInfo("Aswan Governorate", "ASW", "Aswan", 24.0889, 32.8998, "Africa/Cairo"),
                StateInfo("South Sinai Governorate", "JS", "Sharm El-Sheikh", 27.9158, 34.3299, "Africa/Cairo"),
                StateInfo("Red Sea Governorate", "BA", "Hurghada", 27.2579, 33.8116, "Africa/Cairo")
            )
        ),

        // Argentina (Provinces)
        CountryInfo(
            id = "AR",
            name = "Argentina",
            code = "AR",
            flagEmoji = "🇦🇷",
            continent = "South America",
            capital = "Buenos Aires",
            states = listOf(
                StateInfo("Buenos Aires Autonomous City", "CABA", "Buenos Aires", -34.6037, -58.3816, "America/Argentina/Buenos_Aires"),
                StateInfo("Buenos Aires Province", "BA", "La Plata", -34.9214, -57.9545, "America/Argentina/Buenos_Aires"),
                StateInfo("Córdoba", "CBA", "Córdoba", -31.4201, -64.1888, "America/Argentina/Cordoba"),
                StateInfo("Santa Fe", "SF", "Rosario", -32.9468, -60.6393, "America/Argentina/Cordoba"),
                StateInfo("Mendoza", "MDZ", "Mendoza", -32.8895, -68.8458, "America/Argentina/Mendoza"),
                StateInfo("Salta", "SLA", "Salta", -24.7821, -65.4232, "America/Argentina/Salta"),
                StateInfo("Misiones", "MIS", "Posadas", -27.3621, -55.8969, "America/Argentina/Buenos_Aires")
            )
        )
    )
}
