Coding Challenge: Weather App - 09/29/2023
Author: Jolina Lam

Overview:
I used MVVM + repository architecture with Flows and states to pass data to the View layer. I really
enjoyed building the UI since there was so much data to work with and spent some extra time to make it
look nice. Also updated the app icon and included a view model unit test :)

Please refer to the comments in individual classes to more thoughts related to the code I wrote.
Some final thoughts that I didn't leave in comments in the code is that while I used databinding for
the UI here because I was more familiar with the tool, I was cognizant about writing code that
would make the refactoring effort to migrate to Compose easier, like using flows and having a 
ViewState that would be dictate the current state of the app. Also given more time, 
I would have loved to implement dependency injection.

Thank you for your time!