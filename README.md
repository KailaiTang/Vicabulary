# Vicabulary
An Interactive and Immersive Language Learning App

Designed an enhanced vocabulary book app with video clips for users to learn a language while watching their favorite movies.

Frontend: 
1. Developed the UI with standard widgets such as RecyclerView, Dialog, Menu and tested several flows 
2. Accessed the clipboards and linused Selectable Spannable Textview to capture words and lookup for definition.
3. Utilized Mindorks’s PlaceHolderView and RecyclerView to support swipe gestures for liking/disliking the saved words

Backend: 
1. Incorporated callback function to fetch data from asynchronous Dictionary.com RESTful API call
2. Implemented the bottom bar & page navigation using JetPack navigation component 
3. Used Amazon S3 to store movie information(metadata, time, title, etc.) and used the SQLite database to support local cache and offline models. 
4. Imported ExoPlayer Library, launched multi-thread process to display subtitle and video synchronously in playback fragment

