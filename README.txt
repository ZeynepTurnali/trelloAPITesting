!! Kendi key ve token'nınızı General Environment class'ında tanımlamanız gerekmektedir. !!

Board test class'ı sahip olduğu testler:
--------------------
* Board oluşturma ve id'sini board.json içine kaydetme
* Board ismi güncelleme
* Power-upları aktif etme
* Power-up bilgisi çekme
* Board ile beraber default gelen listelerin idlerini çekme ve lists.json dosyası içine kaydetme
* Var olan birinci listeye kart ekleme ve kart id'sini cards.json'a kaydetme
* Var olan ikinci listeye kart ekleme ve kart id'sini cards.json'a kaydetme
* Random olarak iki listeden birinin ismini güncelleme
* Birinci liste altına eklenmiş olan karta yorum yapma
* Birinci listedeki kartı silme
* İkinci listedeki kartı silme
* Oluşturulmuş board'u silme

User test class'ı sahip olduğu testler:
--------------------
// Öncelikli gereklilik: İşlemler için board oluşturulması gerekmektedir.
* Oluşuturulmuş board'dan kullanıcı bilgilerini çekme ve kullanıcı id'sini user.json'a kaydetme
* Kullanıcının emailini çekme
* Kullanıcının dahil olduğu tüm organizasyonların isimlerini çekme
* Kullanıcının sahip olduğu tüm board'ları silme ve admin olmadığı boardlarda console'a uyarı bilgisi basılması
* Kullanıcının sahip olduğu tüm organizasyonları silme


Organizations test class'ı sahip olduğu testler:
--------------------
* Yeni organizasyon oluşturma ve organizasyon.json dosyasına kaydetme
* Oluşturulan organizasyonda board oluşturma
* Oluşturulmuş organizasyonun ismini güncelleme
