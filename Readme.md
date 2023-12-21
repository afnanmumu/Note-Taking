#Instructions to Run the Application  
##Prerequisites:  
Make sure you have Java installed on your system.  
##Clone the Repository:  
git clone https://github.com/afnanmumu/Note-Taking.git  
##Running Instructions:  
Open the Project in Intellij and right click on the project folder -> Open Module Settings -> Modules -> Dependecies tab -> click '+' sign -> add folder path to filename\lib\json-20231013.jar  
Open src/Main.java  
press shift+F10 or click the run button   

##Step 1: Adding a New Note  
Click "Add New Note" Button:  

When you click the "Add New Note" button, a new window will pop up.  
Fill in Note Details:  

Inside this window, there are fields for the title and description of your new note. Fill in these details.  
Click "Submit" Button:  

After filling in the details, click the "Submit" button.  
Confirmation Message:  

A message will appear, confirming that your note has been successfully added.  
##Step 2: Fetching Notes from the Dummy API  
Click "Get Notes" Button:  

Click the "Get Notes" button to fetch data from the dummy API.  
Display of Notes:  

The notes you added will now be displayed on the main screen along with an "Edit" and a "Delete" button for each note.  
##Step 3: Deleting a Note  
Click "Delete" Button:  

To delete a note, click the "Delete" button next to the note you want to remove.  
List Refresh:  

The list will be automatically refreshed, and the selected note will be deleted.  
##Step 4: Editing a Note  
Click "Edit" Button:  

To edit a note, click the "Edit" button next to the note you want to modify.  
Edit Window Opens:  

An editing window will appear, allowing you to modify the title and description.  
Click "Update" Button:  

After making changes, click the "Update" button.  
Confirmation Message:  

A confirmation message will appear, indicating that your note has been successfully edited.  
Note:  
The application follows a simple structure where you can add, fetch, edit, and delete notes.   
Each action triggers a specific set of operations, providing a seamless user experience.  
The use of "Add New Note," "Get Notes," "Edit," and "Delete" buttons streamlines the process.  
This user-friendly design allows you to easily manage your notes through a clear and intuitive interface.  


Application Architecture and Libraries Used  
#Architecture:  

The application follows the Model-View-ViewModel (MVVM) architecture, separating concerns between data (Model), presentation (View), and logic (ViewModel).  
#Libraries Used:  

javax.swing: Used for building the graphical user interface (GUI).  
org.json: Used for handling JSON data.  
Development Report  
#Key Decisions:  

##MVVM Architecture:  

Chose MVVM to maintain a clean separation of concerns, making the application more modular and maintainable.  
##Swing for GUI:  

Utilized javax.swing for GUI development, providing a platform-independent way to create graphical interfaces.  
JSON Handling:  

Used org.json library to parse and handle JSON responses from the API.  
#Challenges Faced:  

##Swing Threading:  

Ensured proper threading for Swing components to prevent freezing of the GUI during API calls.  
##API Integration:  

Handled challenges related to integrating and parsing data from a dummy API.  
##User Experience:  

Worked to provide a smooth and intuitive user experience, especially during asynchronous operations like API calls.  