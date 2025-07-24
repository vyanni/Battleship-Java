import java.util.Scanner;

//Name: V.Susanto
//Start to End Date: May 24th - May 30th
//Assignment: BattleShip
//Purpose: This program will simulate the game Battleship, which allows players to try and sink all the other player's boats before their's are

public class Main {
    public static boolean gamePlay = true;
    public static Scanner userInput = new Scanner(System.in);
    //The gamePlay boolean is for if the game is running, if it ever becomes false, the game stops
    //The scanner here is used for all the input in the game

    public static MapArray playerMap;
    public static MapArray enemyMap;
    //Sets up the player and enemy maps
    public static void main(String[] args) throws Exception {
        while(gamePlay){
            //The whole game is within a while loop, so that if the player wants to play infinitely, it can continue looping
            
            gameIntro();
            //Calls the game intro method, which essentially shows the main-menu

            if(!gamePlay){
                break;
            }
            //Because the while loop doesn't check the condition until the iteration is done, an if statement is placed here
            //to check whether the player turned it off in the method beforehand

            startGame();
            //Calls the startGame method, which gives the set-up for all the maps before the game  

            //The for-loop for the actually game, the ammo is determined by one quarter of the map-length squared
            //E.g 10x10 map is 25 pieces of ammo, because 10 squared is 100, one-quarter of 100 is 25
            for(int i = 0; i < (playerMap.newMap().length * playerMap.newMap().length)/4; i++){
                playerTurn(enemyMap, i);
                System.out.print("Enemy Map:\n");
                enemyMap.showOpponentMap();
                //Shows the AI map, using the method which hides all the ships

                boolean enemyWin = playerMap.winCheck();
                if(enemyWin){
                    System.out.println("The AI took down all your ships!");
                    break;
                }
                //When the player finishes their turn, checks if they've won by seeing if they took down all the player's ships

                System.out.print("Press Enter to Continue to the AI's Turn: ");
                userInput.nextLine();
                //Waits for the user to go to the next line before continuing the loop
                
                computerTurn(playerMap);
                playerMap.showMap();
                //Uses the computer's turn method to have the computer try and find a ship, and showing the player's updated map after

                boolean playerWin = enemyMap.winCheck();
                if(playerWin){
                    System.out.println("You took down all the AI's ships! You won!");
                    break;
                }
                //When the AI finishes their turn, checks if they've won by seeing if they took down all the AI ships

                System.out.print("Press Enter to Continue: ");
                userInput.nextLine();
                //Waits for the user to press enter before continuing, then restarts the loop
            }

            System.out.println("You've ran out of ammo, game over");
            System.out.print("You had:\n" + enemyMap.successfulHits() + " hits\n" + (((enemyMap.newMap().length * enemyMap.newMap().length)/4) - enemyMap.successfulHits()) + " misses\n\n");
            System.out.print("Press Enter to Continue: ");
            userInput.nextLine();
            //When the game has finished and both sides exhausted their ammo, then finishes the game giving the statistics
            //When the player presses enter, goes back to the beginning of the loop for the player to play a new game
        }
    }

    public static void playerTurn(MapArray enemyArrayMap, int ammo){
        //The method for when the player has to hit a ship on the board

        turnChecker:
        while(true){
            //The whole thing is in a while loop, so that it can keep repeating if the player keeps putting random inputs

            System.out.print("\033[H\033[2J");
            System.out.flush();
            //Clears the console for the player's turn

            System.out.print("Enemy Map:\n");
            enemyMap.showOpponentMap();
            //Shows the opponent's map, with the ships hidden

            System.out.println("\nYou have " + (((playerMap.newMap().length * playerMap.newMap().length)/4) - ammo) + " pieces of ammo left.");
            System.out.print("Enter the coordinates where you want to hit (You cannot enter a coordinate you have already done): \nEnter X-Coordinate: ");
            //Shows the ammount of ammo left, and gives the players instructions on what to do

            try{
                //Try-catch just in case the user inputs anything random

                int xHit = userInput.nextInt();
                //Takes in the user's coordinate for what they want

                while(true){
                    //Repeats if the user messes up their y-coordinate input

                    System.out.print("Enter Y-Coordinate: ");
                    try{
                        int yHit = userInput.nextInt();
                        userInput.nextLine();
                        //Takes in the y coordinate

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        int indication = enemyArrayMap.hitChecker(xHit, yHit);
                        //Inputs the user's coordinates into the method in the map class to see if it's valid

                        if(indication == 1){
                            break turnChecker;
                        }else{
                            break;
                        }
                        //Uses the if statements to see if the coordinate was a valid one, if it's not 1 (which means a hit or miss) then the user must try again

                        //Clears the map, then shows the new updated map with the hit
                    }catch(Exception e){
                        System.out.println("Enter a valid coordinate! ");
                        userInput.nextLine();
                        break;
                        //If it's not a number or some other random input, asks the user to input their y-coordinate again
                        //(Assuming the x-coordinate was already valid)
                    }   
                }  
            }catch(Exception e){
                System.out.println("Enter a valid coordinate! ");
                userInput.nextLine();
                //If the x-coordinate wasn't valid, tries again
            }
        }
    }

    public static void computerTurn(MapArray playerArrayMap){
        //The method for the opponent AI to be able to make a turn 

        System.out.print("\033[H\033[2J");
        System.out.flush();
        //Clears the board before the turn starts

        System.out.println("AI's Turn: \n\nFriendly Map:");
        //Some titles above the baord

        while(true){
            int randomX = (int) (Math.random() * playerMap.newMap().length);
            int randomY = (int) (Math.random() * playerMap.newMap().length);
            //The AI uses random coordinates to try and hit your ship, if it's invalid, the while loop just goes again for the 
            //AI to try again.

            if(playerArrayMap.newMap()[randomX][randomY] > 0){
                int indication = playerArrayMap.hitChecker(randomX, randomY);
                if(indication == 1){
                    break;
                }
            }
            //Enters the coordinates into the method in the map class, sees if it's valid or not
        }
    }

    public static void gameIntro(){
        //The gameintro function, which shows the main menu and choices for starting the game
        
        boolean invalidInput = true;
        //As long as this is true, if the user tries to spam anything that's not an option, it'll continue looping

        while(invalidInput){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            //Clears the console so that you can only see the menu

            System.out.println("Welcome to BattleShip! \nChoose an option \n--------------------");
            System.out.print("1. Play a New Game \n2. Quit\n\nEnter your choice (Make sure to enter a proper input): ");
            //Shows the title screen and gives the choices

            try{
                //Try-catch is needed just in case the suer puts any random characters

                int userChoice = userInput.nextInt();
                if(userChoice == 1){
                    invalidInput = false;
                }else if(userChoice == 2){
                    System.out.println("Thanks for playing!");
                    gamePlay = false;
                    invalidInput = false;
                }
                //Takes the user's input, if it's 1 then stops the main-menu while loop and continues the main method one
                //If it's 2, turns off the gamePlay boolean, which stops the game

            }catch(Exception e){
               userInput.nextLine();
               //If the user places anything weird, skips that line, then resets back with the while loop
            }
        }
    }

    public static void startGame(){
        //Startgame function sets up the game for the player with map size and what-not

        int mapSize;
        //The mapSize, since it'll be a square then only 1 number is needed

        while(true){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            //Clears the console before hand

            System.out.print("How big do you want the map? (Min 5, Max 25, Enter a proper value): ");

            //Try-catch is needed if the user enters anything unconventional
            try{
                mapSize = userInput.nextInt();
                userInput.nextLine();

                //Takes the number which the user puts down and checks if it's from 5-25, numbers larger than that look weird as a map
                if(mapSize >= 5 && mapSize <= 100){

                generateLoop:
                    while(true){
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        //Clears the map again

                        playerMap = new MapArray(mapSize);
                        enemyMap = new MapArray(mapSize);
                        //Passes through the input map size into the map array to create a new object

                        System.out.print("Legend:\n5: Aircraft Carrier (5 tiles long)\n4: Battleship (4 tiles long)\n3: Cruiser (3 tiles long)\n2: Dinghy (2 tiles long)\n1: Submarine (1 tile long)\n\n");
                        //Legend for the ships on the board

                        playerMap.showMap();
                        System.out.print("Re-generate Map? (Enter a valid input, Y/N): ");
                        //Shows the map, allows the user to re-generate their map as many times as they want

                        while(true){
                            String retryInput = userInput.nextLine();
                            if(retryInput.toUpperCase().equals("Y")){
                                break;
                            }else if(retryInput.toUpperCase().equals("N")){
                                break generateLoop;
                            }else{
                                continue;
                            }

                            //Checks if the user actually put down Y/N by turning into capital and then checking it
                            //If not, repeats in an infinite for-loop
                        }
                    }

                //Once the user finally chooses their map by pressing no, breaks the loop for the whole method
                break;
                }
            }catch(Exception e){
                userInput.nextLine();
                //If they input something invalid, justs loops again in a while loop
            }
        }
    }
}