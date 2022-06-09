# JamGame
 
Jam Game is a popular board game amongst younger children. In light of this, I decided to create a GUI version of the game using Java and JavaFX to create UI design. 

The point of the game is to move the RED X labeled car all the way to the right side of the board, but there are other cars in the way preventing such a thing. In order to get the red car to the side of the board, the player must implement problem solving to move the other cars either horizontally or vertically based on the way the are facing. 

In the program, Breadth First Search is constantly being used to find the next shortest game layer to get to a solution. So if the game player is stuck and doesn't know what move to make, he/she can press the 'Hint' button which will update the game by moving a car based on the configuration created from the BFS algorithm. If the puzzle at the current configuration or game board is not solvable, then the user is notified. 

Back to cars, cars either move horizontally or vetically and they cannot jump over other cars in their way to the second selected spot by the user. If the user tries to break these rules, they will not be able to complete them and will be notified. 

In the game another important concept besides Breadth First Search and configuration making, is using the concept of MVC or Model View Controller. Whenever a car was pressed or a load button was clicked, the controller called the model to run the specific function to take care of that action. In doing so the view was then updated for the user interface. For example, if the user wants to change the game puzzle, they would press the load button, in which the controller would notify the model to execute the load function for the user's new puzzle choice. If the puzzle is valid, than the view is updated by displaying the new puzzle.

In order to use the game, the user must have the latest version of JavaFX download and add its' 'lib', library directory to the project structure and they must have the module-info.java file. 


![Hint Picture](https://user-images.githubusercontent.com/71080514/172959998-aeb4f5c9-d6e7-4ccc-b6c5-6337276d1c04.png)
