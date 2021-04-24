# Refactoring-Instagram
Refactoring-Instagram is designed based on mvvm architecture.
Also, it uses android architecutre components.(viewmodel, livedata, navigation)
<p>
<img src = "https://user-images.githubusercontent.com/34837583/115967955-51aacb80-a570-11eb-9a99-7108c0580537.png" width = "250">
<img src = "https://user-images.githubusercontent.com/34837583/115968024-933b7680-a570-11eb-8fc5-cd4eadc8a443.png" width = "250">
<img src = "https://user-images.githubusercontent.com/34837583/115968278-114c4d00-a572-11eb-8d24-17d4cf647cc2.png" width = "250">
</p>

## Library
- Minimum SDK 16
- Java based
- Firebase (FirebaseAuth, Firebasestorage, FirebaseFirestore, Firebase Cloud Message)
- Facebook : Login 
- Jetpack
  - LiveData : an observable data holder class
  - Navigation : use fragment so easily
  - ViewModel : Designed to store and manage UI-related in a lifecycle.
- Daager : Dependency Injection
- Glide : Image loading
- OkHttp3 : Implement fcm push
- Gson : Convert java object to JSON
- mockito : Unit test
<br>

## MAD Scorecard
![image](https://user-images.githubusercontent.com/34837583/115968508-41482000-a573-11eb-8df1-211831f9100f.png)
![image](https://user-images.githubusercontent.com/34837583/115968538-65a3fc80-a573-11eb-879a-77893d23cc33.png)
<br>

## Architecure
The mvvm architecture basically integrates the local database and remote data using the repository pattern.
Howerver, Firestore provides its own local cache. This means we don't have to use repository pattern.
![image](https://user-images.githubusercontent.com/34837583/115968932-2aa2c880-a575-11eb-9101-5f021f81df82.png)
