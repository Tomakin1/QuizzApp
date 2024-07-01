package com.example.quizzproject.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzproject.Adapter.LevelAdapter
import com.example.quizzproject.Domain.QuestionModel
import com.example.quizzproject.databinding.ActivityLevelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LevelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )





        binding.levelRv.adapter = LevelAdapter(this) { levelNumber ->
            fetchUserLevelFromFirestore { userLevel ->
                val questionListForLevel = getQuestionsForLevel(levelNumber)
                if (checkIfUserCanProceedToNextLevel(levelNumber, userLevel)) {
                    startQuestionActivity(questionListForLevel)

                    updateUserLevelInFirestore(userLevel)
                } else {
                    Toast.makeText(this, "Önceki seviyeyi bitirin!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.apply {



            backBtn.setOnClickListener {
                val intent = Intent(this@LevelActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun updateUserLevelInFirestore(currentLevel: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = Firebase.firestore
            val userDoc = db.collection("Users").document(user.uid)


            userDoc.update("level", currentLevel + 1)
                .addOnSuccessListener {
                    Log.d(TAG, "User level updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating user level", e)
                }
        }
    }

    private fun fetchUserLevelFromFirestore(callback: (Int) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = Firebase.firestore
            val userDoc = db.collection("Users").document(user.uid)

            userDoc.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userLevel = document.getLong("level")?.toInt() ?: 1
                        callback(userLevel)
                    } else {
                        Log.d(TAG, "No such document")
                        callback(1)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                    callback(1)
                }
        } ?: callback(1)
    }

    private fun checkIfUserCanProceedToNextLevel(level: Int, userLevel: Int): Boolean {
        return userLevel >= level
    }






    private fun startQuestionActivity(questionList: List<QuestionModel>) {
        val intent = Intent(this@LevelActivity, QuestionActivity::class.java)
        intent.putParcelableArrayListExtra("list", ArrayList(questionList))
        startActivity(intent)
    }

    fun questionList(): MutableList<QuestionModel>{
        val question : MutableList<QuestionModel> = mutableListOf()


        question.add(
            QuestionModel(1,
                "Hangi gezegen Güneş Sistemi'ndeki en büyük gezegendir?",
                "A) Mars",
                "B) Jüpiter",
                "C) Venüs",
                "D) Satürn",
                "b",
                5,
                null,
                "Bu gezegen adını bir Roma tanrısından alır.",
                1
            )

        )


        question.add(
            QuestionModel(2,
                "İnsan vücudundaki en büyük organ nedir?",
                "A) Kalp",
                "B) Beyin",
                "C) Karaciğer",
                "D) Deri",
                "d",
                5,
                null,
                "Bu organ, insan cildinin en dış tabakasını oluşturur.",
                1
            )
        )

        question.add(
            QuestionModel(3,
                "Hangisi bir programlama dilidir?",
                "A) HTML",
                "B) JPEG",
                "C) MP3",
                "D) GIF",
                "a",
                5,
                null,
                "Bu dilin adı, yılanın bir türüyle ilişkilidir."
                ,1
            )
        )

        question.add(
            QuestionModel(4,
                " Dünyadaki en yüksek dağ hangisidir?",
                "A) Everest",
                "B) Kilimanjaro",
                "C) Mont Blanc",
                "D) K2",
                "a",
                5,
                null,
                " Bu dağ, Asya'da bulunur",
                1
            )
        )

        question.add(
            QuestionModel(5,
                "Hangisi bir memeli hayvandır?",
                "A) Timsah",
                "B) Yılan",
                "C) Balina",
                "D) Kaplumbağa",
                "c",
                5,
                null,
                "Bu hayvanın adı, büyük kulaklarıyla tanınır."
                ,1
            )
        )

        question.add(
            QuestionModel(6,
                "Hangisi bir ülke değildir?",
                "A) İspanya",
                "B) Brezilya",
                "C) Hindistan",
                "D) İskandinavya",
                "d",
                5,
                null,
                "Bu yer, bir kıta olarak adlandırılır.",
                1
            )
        )

        question.add(
            QuestionModel(7,
                "Hangisi  en büyük kıtadır?",
                "A) Amazonya",
                "B) Arktika",
                "C) Karayipler",
                "D) Atlas",
                "b",
                5,
                null,
                " Bu kıta, \"Asya\" olarak bilinir.",
                1
            )
        )

        question.add(
            QuestionModel(8,
                "Hangisi su içinde yüzer?",
                "A) Tahta",
                "B) Demir",
                "C) Altın",
                "D) Cam",
                "a",
                5,
                null,
                "Yoğunluğu suya yakın olan madde,suda yüzer",
                1)
        )

        question.add(
            QuestionModel(9,
                "Hangisi bir meyve değildir? ",
                "A) Elma",
                "B) Salatalık",
                "C) Portakal",
                "D) Patates",
                "d",
                5,
                null,
                "Hepsi meyve olarak sınıflandırılırken,bir kök sebzesidir.",
                1
            )
        )

        question.add(
            QuestionModel(10,
                "Hangisi bir ünlü ressamdır?",
                "A) Wolfgang Amadeus Mozart",
                "B) Pablo Picasso",
                "C) Albert Einstein",
                "D) William Shakespeare",
                "b",
                5,
                null,
                "Bu ressamın adı, bir hayvan ve renk ile ilişkilendirilebilir.",
                1
            )
        )


        return question
    }

    fun questionList1(): MutableList<QuestionModel>{
        val question : MutableList<QuestionModel> = mutableListOf()




        question.add(
            QuestionModel(1,
                "Hangi ünlü ressamın \"Deli Gökyüzü\" adlı eseri vardır?",
                "A) Vincent van Gogh",
                "B) Leonardo da Vinci",
                "C) Pablo Picasso",
                "D) Michelangelo",
                "a",
                10,

                null,
                "Bu ressamın adı, kulaklarına zarar verdiği için ünlüdür.",
                2
            )
        )

        question.add(
            QuestionModel(2,
                "DNA'nın tam açılımı nedir?",
                "A) Deoksiribonükleik Asit",
                "B) Diksiyon Nükleik Asit",
                "C) Difüzyon Nükleik Asit",
                "D) Desoksijen Nükleik Asit",
                "a",
                10,

                null,
                "Bu terimdeki kelime, DNA'nın yapısındaki özel bir şeker türünü işaret eder.",
                2
            )
        )

        question.add(
            QuestionModel(3,
                "Hangi dizi, \"Yedi Krallık\"ta geçen fantastik bir hikayeyi anlatır?",
                "A) The Walking Dead",
                "B) Breaking Bad",
                "C) Game of Thrones",
                "D) Stranger Things",
                "c",
                10,

                null,
                "Kitapları George R.R. Martin tarafından yazılmıştır.",
                2
            )
        )

        question.add(
            QuestionModel(4,
                "Hangisi bir enzim değildir?***",
                "A) Lipaz",
                "B) Amilaz",
                "C) Siyanobakteriler",
                "D) Proteaz",
                "c",
                10,

                null,
                "Enzimler biyokimyasal reaksiyonlarda katalizör olarak görev yapar.",
                2
            )
        )

        question.add(
            QuestionModel(5,
                "Hangisi bir öykü kitabı yazarıdır?",
                "A) Agatha Christie",
                "B) J.K. Rowling",
                "C) Dan Brown",
                "D) Stephen King",
                "a",
                10,

                null,
                "\"Miss Marple\" ve \"Hercule Poirot\" gibi ünlü karakterlerin yaratıcısıdır.",
                2
            )
        )

        question.add(
            QuestionModel(6,
                "Hangisi bir dans türü değildir?",
                "A) Salsa",
                "B) Rumba",
                "C) Mambo",
                "D) Zumba",
                "d",
                10,

                null,
                "Bu, bir dans türü değil, bir egzersiz programıdır.",
                2
            )
        )

        question.add(
            QuestionModel(7,
                "Hangisi bir mizah dergisi değildir?",
                "A) Penguen",
                "B) Uykusuz",
                "C) Gırgır",
                "D) Naber",
                "d",
                10,

                null,
                "Bu dergi, Türk edebiyatında önemli bir yere sahiptir.",
                2
            )
        )

        question.add(
            QuestionModel(8,
                "Hangisi bir spor malzemesi markasıdır?",
                "A) Nike",
                "B) Rolex",
                "C) Gucci",
                "D) Louis Vuitton",
                "a",
                10,

                null,
                "Bu marka özellikle ayakkabı ve spor giyim ürünleriyle tanınır.",
                2
            )
        )

        question.add(
            QuestionModel(9,
                "Hangi hayvan, kendi kusmuğunu yemek zorunda kalan bir yaratığın efsanevi sembolüdür?",
                "A) Ejderha",
                "B) Phoenix",
                "C) Griffon",
                "D) Hydra",
                "d",
                10,

                null,
                "Bu efsanevi yaratık, bir kafadan daha fazla kafaya sahip olabilir.",
                2
            )
        )


        question.add(
            QuestionModel(10,
                "Hangisi bir müzik enstrümanı değildir?",
                "A) Saksafon",
                "B) Duduk",
                "C) Balalaika",
                "D) Tornado",
                "d",
                10,

                null,
                "Bu, bir doğa olayının adıdır, bir müzik enstrümanı değil.",
                2
            )
        )
        return question
    }

    fun questionList2(): MutableList<QuestionModel>{
        val question : MutableList<QuestionModel> = mutableListOf()




        question.add(
            QuestionModel(1,
                "Hangi element, periyodik tabloda \"H\" sembolüyle gösterilir?",
                "A) Hidrojen",
                "B) Leonardo da Vinci",
                "C) Hafniyum",
                "D) Michelangelo",
                "a",
                15,

                null,
                "evrende en bol bulunan elementlerden biridir ve tek proton içerir.",
                3
            )
        )

        question.add(
            QuestionModel(2,
                "Hangi tarih, Amerika'nın bağımsızlık günü olarak kutlanır?\n",
                "A) 4 Temmuz",
                "B) 14 Şubat",
                "C) 25 Aralık",
                "D) 1 Mayıs",
                "a",
                15,

                null,
                "Bu tarih, 1776'da Amerika Birleşik Devletleri'nin bağımsızlık bildirgesinin imzalandığı tarihtir.",
                3
            )
        )

        question.add(
            QuestionModel(3,
                "Hangi filozof, \"Ben düşünüyorum, öyleyse varım.\" sözüyle tanınır?",
                "A) Aristoteles",
                "B) Sokrates",
                "C) Descartes",
                "D) Platon",
                "c",
                15,

                null,
                "modern felsefenin öncülerindendir ve \"rasyonalizm\" akımının temsilcisidir.",
                3
            )
        )

        question.add(
            QuestionModel(4,
                "Hangi tanrı, Yunan mitolojisinde gökyüzü tanrısıdır?",
                "A) Zeus",
                "B) Poseidon",
                "C) Hades",
                "D) Apollo",
                "a",
                15,

                null,
                "Olimpos Dağı'nın kralı olarak bilinir ve yıldırımı kontrol eder.",
                3
            )
        )

        question.add(
            QuestionModel(5,
                "Hangi şirket, iPhone'un yaratıcısı olarak bilinir?",
                "A) Samsung",
                "B) Microsoft",
                "C) Apple",
                "D) Google",
                "c",
                15,

                null,
                " Steve Jobs tarafından kurulmuştur.",
                3
            )
        )

        question.add(
            QuestionModel(6,
                "angi ülke, Birleşmiş Milletler Güvenlik Konseyi'nin beş daimi üyesinden biridir?",
                "A) Hindistan",
                "B) Almanya",
                "C) Japonya",
                "D) Çin",
                "d",
                15,

                null,
                "Asya kıtasında bulunur.",
                3
            )
        )

        question.add(
            QuestionModel(7,
                "Hangi kıta, kutup noktalarına sahip değildir??",
                "A) Avrupa",
                "B) Afrika",
                "C) Okyanusya",
                "D) Antarktika",
                "a",
                15,

                null,
                "Bu kıta, yüksek enlemlerde bulunmadığı için kutup noktalarına sahip değildir.",
                3
            )
        )

        question.add(
            QuestionModel(8,
                "Hangi film, 1997 yılında en iyi film Oscar'ını kazanmıştır?",
                "A) Forrest Gump",
                "B) The Godfather",
                "C) Titanic",
                "D) Schindler's List",
                "c",
                15,

                null,
                "Çiftleri oldukça etkileyen bir film olmuştur.",
                3
            )
        )

        question.add(
            QuestionModel(9,
                "Hangi spor dalında, Wimbledon en prestijli turnuva olarak bilinir?",
                "A) Futbol",
                "B) Tenis",
                "C) Golf",
                "D) Basketbol",
                "b",
                15,

                null,
                "Raketler ile oynanan bir spor türüdür.",
                3
            )
        )



        question.add(
            QuestionModel(10,
                "Hangi ressam, \"Mona Lisa\" tablosunun yaratıcısı olarak bilinir?",
                "A) Pablo Picasso",
                "B) Leonardo da Vinci",
                "C) Vincent van Gogh",
                "D) Claude Monet",
                "b",
                15,

                null,
                "İlk ismi ünlü bir oyuncunun adıdır.",
                3
            )
        )
        return question
    }

    fun questionList3(): MutableList<QuestionModel>{
        val question : MutableList<QuestionModel> = mutableListOf()




        question.add(
            QuestionModel(1,
                "Hangi fizik kanunu, \"her eyleme karşılık bir tepki vardır\" prensibini açıklar?",
                "A) Newton'un 1. Hareket Yasası",
                "B) Newton'un 2. Hareket Yasası",
                "C) Newton'un 3. Hareket Yasası",
                "D) Archimedes'in Prensibi",
                "c",
                20,

                null,
                "bir cismin başka bir cisme uyguladığı kuvvetin, o cisme etki eden tepki kuvvetine eşit olduğunu ifade eder.",
                4
            )
        )

        question.add(
            QuestionModel(2,
                "Hangi tarih, Berlin Duvarı'nın yıkıldığı gün olarak bilinir?",
                "A) 9 Kasım 1989",
                "B) 7 Ekim 1989",
                "C) 12 Aralık 1989",
                "D) 3 Mart 1990",
                "a",
                20,

                null,
                "Bu tarih, Almanya'nın birleşmesine yol açan önemli bir dönemeçtir.",
                4
            )
        )

        question.add(
            QuestionModel(3,
                "Hangi felsefi akım, \"insanın doğasını araştırmak ve anlamak\" üzerine odaklanır?",
                "A) Epikürcülük",
                "B) Stoacılık",
                "C) Sosyalizm",
                "D) İnsanizm",
                "d",
                20,

                null,
                "İnsanın varoluşsal sorunlarına odaklanır ve insanın doğasını anlamaya çalışır.",
                4
            )
        )

        question.add(
            QuestionModel(4,
                "Hangi tanrı, Yunan mitolojisinde denizlerin tanrısıdır?",
                "A) Zeus",
                "B) Poseidon",
                "C) Hades",
                "D) Apollo",
                "b",
                20,

                null,
                "Kronos ile Rhea'nın ikinci oğlu ve Zeus'un kardeşi.",
                4
            )
        )

        question.add(
            QuestionModel(5,
                "Hangi şirket, \"Windows\" işletim sisteminin yaratıcısı olarak bilinir?",
                "A) Apple",
                "B) Microsoft",
                "C) Google",
                "D) IBM",
                "b",
                20,

                null,
                "Bill Gates tarafından kurulmuştur.",
                4
            )
        )

        question.add(
            QuestionModel(6,
                "Hangi ülke, Birleşmiş Milletler Güvenlik Konseyi'nin daimi üyelerinden biridir, ancak veto yetkisine sahip değildir?",
                "A) Almanya",
                "B) Hindistan",
                "C) Japonya",
                "D) Brezilya",
                "b",
                20,

                null,
                "Bu ülke, dünyanın en kalabalık demokrasisine sahiptir.",
                4
            )
        )

        question.add(
            QuestionModel(7,
                "Hangi şehir, \"Güneşin Doğuşu Ülkesi\" olarak bilinir?",
                "A) Paris",
                "B) Tokyo",
                "C) Roma",
                "D) Sidney",
                "d",
                20,

                null,
                "Avustralya'nın en büyük şehirlerinden biridir ve doğu kıyısında bulunur.",
                4
            )
        )

        question.add(
            QuestionModel(8,
                "Hangi film, 1972 yılında en iyi film Oscar'ını kazanmıştır?",
                "A) The Godfather",
                "B) Star Wars",
                "C) The Sound of Music",
                "D) Forrest Gump",
                "c",
                20,

                null,
                "Bu film, mafya kültürünü konu alan bir başyapıttır",
                4
            )
        )

        question.add(
            QuestionModel(9,
                "Hangi spor dalında, \"Super Bowl\" en önemli şampiyona olarak bilinir?",
                "A) Futbol",
                "B) Beyzbol",
                "C) Amerikan Futbolu",
                "D) Basketbol",
                "c",
                20,

                null,
                "Amerika Birleşik Devletleri'nde büyük bir popülerliğe sahiptir.",
                4
            )
        )


        question.add(
            QuestionModel(10,
                "Hangi ressam, \"Starry Night\" tablosunun yaratıcısı olarak bilinir?",
                "A) Pablo Picasso",
                "B) Leonardo da Vinci",
                "C) Vincent van Gogh",
                "D) Claude Monet",
                "b",
                20,

                null,
                "izlenimci sanat akımının önde gelen temsilcilerindendir",
                4
            )
        )
        return question
    }

    fun questionList4(): MutableList<QuestionModel>{
        val question : MutableList<QuestionModel> = mutableListOf()




        question.add(
            QuestionModel(1,
                "İlk defa 1969 yılında insanlı bir araçla gerçekleştirilen, Ay'a yapılan seyahat programının adı nedir?",
                "A) Voyager Programı",
                "B) Apollo Programı",
                "C) Artemis Programı",
                "D) Gemini Programı",
                "b",
                25,

                null,
                "Uzay keşiflerinde önemli bir dönüm noktasıdır ve \"**** 11\" göreviyle tanınır.",
                5
            )
        )

        question.add(
            QuestionModel(2,
                "Hangi tarih, Birinci Dünya Savaşı'nın sona erdiği gün olarak bilinir?",
                "A) 11 Kasım 1918",
                "B) 28 Haziran 1919",
                "C) 6 Mayıs 1917",
                "D) 9 Ağustos 1914",
                "a",
                25,

                null,
                "Aynı zamanda \"Armistice Day\" olarak da bilinir ve \"Remembrance Day\" olarak anılır.",
                4
            )
        )

        question.add(
            QuestionModel(3,
                "Hangi filozof, \"varlık, düşünce ve dil\" üzerine önemli çalışmalar yapmıştır?",
                "A) Friedrich Nietzsche",
                "B) Ludwig Wittgenstein",
                "C) Immanuel Kant",
                "D) Jean-Paul Sartre",
                "b",
                25,

                null,
                "Dilin felsefi analizi üzerine önemli bir kitap olan \"Tractatus Logico-Philosophicus\"u yazmıştır.",
                5
            )
        )

        question.add(
            QuestionModel(4,
                "Hangi tanrı, Yunan mitolojisinde sanat ve müziğin tanrısıdır?",
                "A) Hermes",
                "B) Dionysos",
                "C) Apollon",
                "D) Ares",
                "c",
                25,

                null,
                "Eşi Kassandra olup Zeus ve Leto'nun oğlu, Artemis'in ikiz kardeşidir.",
                5
            )
        )

        question.add(
            QuestionModel(5,
                "Hangi şirket, Tesla arabalarının üreticisi olarak bilinir??",
                "A) Ford",
                "B) Toyota",
                "C) Tesla, Inc.",
                "D) Volkswagen",
                "c",
                25,

                null,
                "Bu şirket, elektrikli araçlar ve yenilenebilir enerji teknolojileri üzerine odaklanmıştır.",
                5
            )
        )

        question.add(
            QuestionModel(6,
                "Hangi ülke, Avrupa Birliği'nin bir parçası olmasına rağmen Avro para birimini kullanmamaktadır?",
                "A) İngiltere",
                "B) İsveç",
                "C) Danimarka",
                "D) Norveç",
                "c",
                25,

                null,
                "Bu ülkenin başkenti Kopenhag'dır ve İskandinav ülkelerinden biridir.",
                5
            )
        )

        question.add(
            QuestionModel(7,
                "Dünyanın en büyük ada ülkesi hangisidir?",
                "A) Endonezya",
                "B) Japonya",
                "C) Grönland",
                "D) Yeni Zelanda",
                "c",
                25,

                null,
                "Bu ülke, kara parçasının çoğunun buzullarla kaplı olduğu büyük bir adadır.",
                5
            )
        )

        question.add(
            QuestionModel(8,
                "Leonardo DiCaprio, hangi filmde ilk kez En İyi Erkek Oyuncu Oscar'ını kazanmıştır?",
                "A) Titanic",
                "B) The Revenant ",
                "C) Inception",
                "D) The Wolf of Wall Street",
                "b",
                25,

                null,
                "vahşi doğada hayatta kalmaya çalışan bir kaşif olan Hugh Glass'ı canlandırıyor.",
                5
            )
        )

        question.add(
            QuestionModel(9,
                "Hangi ülke, 2018 FIFA Dünya Kupası'nda şampiyon olmuştur?",
                "A) Almanya",
                "B) Fransa",
                "C) Brezilya",
                "D) Arjantin",
                "b",
                25,

                null,
                "Rusya'da düzenlenen ve finalde Hırvatistanın mağlup olduğu turnuva .",
                5
            )
        )


        question.add(
            QuestionModel(10,
                "Hangi yazar, \"Sanat Tarihi\" adlı eseriyle tanınır?",
                "A) Leo Tolstoy",
                "B) Virginia Woolf",
                "C) E. H. Gombrich",
                "D) Marcel Proust",
                "c",
                25,

                null,
                " Bu kitap \"Mona Lisa\" ve \"Sistine Şapeli Tavanı\" gibi ünlü eserlerin detaylı analizlerini içerir",
                5
            )
        )
        return question
    }




    private fun getQuestionsForLevel(level: Int): List<QuestionModel> {
        return when (level) {
            1 -> questionList()
            2 -> questionList1()
            3 -> questionList2()
            4 -> questionList3()
            5 -> questionList4()
            else -> emptyList()
        }
    }







}
