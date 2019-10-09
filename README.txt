First of all, thanks for providing such a good project, It really taught me a lot when I was developing. I¡¯m using MVVM architecture in this project. The View Layer is Activity. Its responsibility is to talk with ViewModel to trigger some business methods (like fetching pins data from server and display them in a list in this case). The data will be received using Livedata and then activity will use UI-system to render it (like displaying on Google Map and list in this case). 

For DataModel layer, I¡¯m using repository pattern with room for persistence and Retrofit for Rest api. Repository fulfilled a contract interface to achieve inversion of control. And I¡¯m also using Kotlin coroutines in data fetching because it gets better performance than regular threads, and it¡¯s easy to maintain subscription relationship to avoid memory leak.

For ViewModel layer, I¡¯m using a consistent scope to manage all coroutine. So it will also survived configuration change and close automatically  when Viewmodel is destroyed.

In the list part, I¡¯m using CustomView + Recyclerview + ListAdapter+ Databinding.  ListAdapter can calculate the differences between old and new data, so it can be applied animation easily. Databinding makes the code really clean and fit MVVM. The CustomView is called Bottomfly. The idea is driven by great map products like Google Map, Baidu Map. Their list can fly over the map, it¡¯s so cool so i try to implement it in my way. Actually I spend most of the time in this part to develop, debug and adjust.

I spent around half day in total to build this project,and i hope you will enjoy
this project.

Thanks again for providing such a good chance. Feel free to reach me if you have any problems about this project.


