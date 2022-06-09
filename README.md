# JamGame
LOOK AT UPPERCASE HEADINGS TO READ CERTAIN CONTENT
 
GAME BREAKDOWN AND SHORT DESCRIPTIONS
 
Jam Game is a popular board game amongst younger children. In light of this, I decided to create a GUI version of the game using Java and JavaFX to create UI design. 

The point of the game is to move the RED X labeled car all the way to the right side of the board, but there are other cars in the way preventing such a thing. In order to get the red car to the side of the board, the player must implement problem solving to move the other cars either horizontally or vertically based on the way the are facing. 

In the program, Breadth First Search is constantly being used to find the next shortest game layer to get to a solution. So if the game player is stuck and doesn't know what move to make, he/she can press the 'Hint' button which will update the game by moving a car based on the configuration created from the BFS algorithm. If the puzzle at the current configuration or game board is not solvable, then the user is notified. 

Back to cars, cars either move horizontally or vetically and they cannot jump over other cars in their way to the second selected spot by the user. If the user tries to break these rules, they will not be able to complete them and will be notified. 

In the game another important concept besides Breadth First Search and configuration making, is using the concept of MVC or Model View Controller. Whenever a car was pressed or a load button was clicked, the controller called the model to run the specific function to take care of that action. In doing so the view was then updated for the user interface. For example, if the user wants to change the game puzzle, they would press the load button, in which the controller would notify the model to execute the load function for the user's new puzzle choice. If the puzzle is valid, than the view is updated by displaying the new puzzle.

If the user wants to reset the puzzle at any point in the game, just press the 'Reset' button on the button of the GUI. 

MUST HAVE TO USER 

In order to use the game, the user must have the latest version of JavaFX download and add its' 'lib', library directory to the project structure and they must have the module-info.java file. 

PICTURES WITH DESCRTIPTION 

![Selected Picture](https://user-images.githubusercontent.com/71080514/172960040-8b8cfa04-b03a-4579-ac28-8979cc25ee57.png)

In the picture above, this is the result when your FIRST select a car. Since a car is present at (3,1) aka row 3, column 1, it is displayed that the user has selected a car and where they selected it. If there was not a car at the spot selected aka a blank square with a '.' inside it, the player would be notified that there isn't a car there. 

![Moved Picture](https://user-images.githubusercontent.com/71080514/172960051-055af643-dc9b-4f8d-9e00-dc6fc943a1ea.png)

In the picture above, this is the result when you SECOND select a spot to move said car. In this case, we decided to move the car 
from row 3, col 1 to row 3, col 0. Since the move is valid that is displayed to the user. In a case where the move is invalid, the user would be notified and the move would not be completed. 

![Hint Picture](https://user-images.githubusercontent.com/71080514/172959998-aeb4f5c9-d6e7-4ccc-b6c5-6337276d1c04.png)

When pressing the hint button, you are given the next best game board that was found via Breadth First Search algorithm. You can see that the algo found in the shortest path to a solution, moving the 'D' car up was first and that car was automatically moved and displayed to the user. 

![Puzzle Picture](https://user-images.githubusercontent.com/71080514/172960104-396c4413-29d4-4de9-9912-a0bf3db73c3f.png)

Above is a picture of the what happens when you press the 'Load' button on the GUI. You are shown the other puzzles that you can choose in which they are loaded onto the game board via MVC concept and the player can continue playing the game. 
