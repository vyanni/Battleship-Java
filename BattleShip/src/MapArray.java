//Name: V.Susanto
//Start to End Date: May 24th - May 30th
//Assignment: BattleShip
//Purpose: This class creates the individual maps for both the player and the opponent 


public class MapArray {
    
    private int[][] newMap;
    private String[] boatOptions = {"A", "B", "C", "D", "S"};
    private boolean[] horizontalChoose = {true, false};
    private int[] shipBias = {4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 0};
    private Ships[] activeBoats;
    private int successfulHits;
    //newMap is the map itself in a 2D array, boatOptions simulate which boats can be spawned
    //It's an integer array, where 0 means nothing, -1 means a miss, -2 means a hit, and the positive values reflect the ship's value
    //horizontalChoose chooses randomly between whether the boat is horizontal or vertical
    //The class used to make too many large ships, so shipBias makes smaller ships more common using an array of sizes
    //activeBoats is an array of all the boats on the board, sees which ones are active or not
    //successfulhits simply counts up all the hits over the turn, then will use it for data later
    
    public MapArray(int mapSize){
        //This is the constructor for the array, initializes the mapsize from the user inputs

        newMap = new int[mapSize][mapSize];
        activeBoats = new Ships[mapSize];
        //The ship will make as many boats as the size of the map, (e.g 5x5 map has 5 boats, 20x20 map will have 20 boats)

        for(int i = 0; i < mapSize; i++){
            int selectionWeighing = (int)(shipBias[(int)(Math.random() * shipBias.length)]);
            activeBoats[i] = new Ships(boatOptions[selectionWeighing], horizontalChoose[(int)(Math.random()*2)]);
        }
        //Creates all the ships on the board in the array, uses the shipBias array in order to generate more smaller boats than larger ones

        generateMap();
        //Starts to place the boats on the map
    }

    public boolean winCheck(){
        if(successfulHits == activeBoats.length){
            return true;
        }
        return false;

        //Checks if there were as many hits as boats, since hitting a boat will sink the whole thing
    }

    public void generateMap(){
        //The method for placing the boats on the map randomly

        for(int i = 0; i < activeBoats.length; i++){
            //For loop which generates a random spot for each boat in the activeBoats array

            boolean searchingSpot = true;
            //Boolean to know if a ship is still looking for a spot

            while(searchingSpot){
                boolean emptyspace = true;
                //Boolean to know if there's an empty space on the map

                //Checks if the boat will be horiztonal or not
                if(activeBoats[i].isHorizontal()){
                    int randomY = (int) (Math.random()*(newMap.length));
                    int randomX = (int) (Math.random()*(newMap.length - activeBoats[i].shipPos().length));
                    //If it's horizontal, generates a random y-coordinate from the top to bottom
                    //The x-coordinate will go until the map - ship length (e.g in a 15x15, an aircraft carrier with length 5 would find a random x-coordinate from 0-10)
                    //This is because the boats will be generated left to right, and to make sure it doesn't go past the length of the array

                    int squareChecker = randomX;
                        for(int j = 0; j < activeBoats[i].shipPos().length; j++){
                        if(newMap[randomY][squareChecker] == 0){
                            squareChecker++;
                        }else{
                            emptyspace = false;
                            break;
                        }
                    }
                    //Checks if each spot going forward is empty by setting a new integer as the x coordinate, then checking every single
                    //spot from there up to how long the ship is. If there's no empty space, the while-loop will eventually restart

                    if(emptyspace){
                        //If it turns out that all the spots are empty, places down the boat by setting each spot equal to the value of 
                        //the ship. Then it records the ship's position within the array inside the ship class
                        for(int j = 0; j < activeBoats[i].shipPos().length; j++){
                            newMap[randomY][randomX+j] = activeBoats[i].shipValue();
                            activeBoats[i].insertShip(randomX + j, j);
                        }

                        //Inserts the other coordinate on the other axis as a separate variable
                        activeBoats[i].insertotherCoord(randomY);
                        searchingSpot = false;
                        //Finishes the loop for this specific ship
                    }
                }else{
                    int randomY = (int) (Math.random()*(newMap.length - activeBoats[i].shipPos().length));
                    int randomX = (int) (Math.random()*(newMap.length));
                    //Same logic for vertical, where it'll choose any random x-coordinate from left to right
                    //But for y-axis, it'll be the map length minus the ship's length, generated top to bottom

                    int squareChecker = randomY;
                        for(int j = 0; j < activeBoats[i].shipPos().length; j++){
                        if(newMap[squareChecker][randomX] == 0){
                            squareChecker++;
                        }else{
                            emptyspace = false;
                            break;
                        }
                    }
                    //Checks if it's empty vertically, setting the counter to the y-coordinate instead and going down

                    if(emptyspace){
                        //If it's actually empty, then sets the coordinates to the ship's values and records them
                        for(int j = 0; j < activeBoats[i].shipPos().length; j++){
                            newMap[randomY+j][randomX] = activeBoats[i].shipValue();
                            activeBoats[i].insertShip(randomY + j, j);
                        }

                        //Inserts the x-coordinate as the other axis as a separate variable
                        activeBoats[i].insertotherCoord(randomX);
                        searchingSpot = false;
                        //Finishes the loop for this specific ship
                    }
                }

            }
        }
    }

    public int hitChecker(int xCoord, int yCoord){
        //The method for checking if a coordinate hit a ship or not

        if(newMap[yCoord][xCoord] == 0){
            newMap[yCoord][xCoord] = -1;

            System.out.println(xCoord + ", " + yCoord + " was a miss!");
            return 1;
            //First checks if the value is 0 or nothing in that square, if so, return that it was a regular shot and a miss

        }else if(newMap[yCoord][xCoord] > 0){
            //If it's above 0, (e.g there's a ship in that position), goes through each ship in the activeBoats array, and see which 
            //has matching coordinates. If they share the same other axis coordinates, then checks manually through each position
            for(int i = 0; i < activeBoats.length; i++){
                if(activeBoats[i].isHorizontal() == true && activeBoats[i].otherCoord() == yCoord){
                    for(int j = 0; j < activeBoats[i].shipPos().length; j++){

                        if(activeBoats[i].shipPos()[j] == xCoord){
                            //If at some point, both x and y coordinates match, then show that it was a hit
                            System.out.println("(" + xCoord + ", " + yCoord + ")" + " was a hit!");
                            if(newMap[yCoord][xCoord] == 1){
                                System.out.println("A Submarine was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 2){
                                System.out.println("A Dinghy was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 3){
                                System.out.println("A Cruiser was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 4){
                                System.out.println("A Battleship was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 5){
                                System.out.println("An Aircraft Carrier was Sunk!");
                            }
                            //Checks which kind of boat it was, then shows it sunk

                            successfulHits++;
                            //Increments successful hits

                            //Changes each part of the ship to =-2, meaning an already hit ship
                            for(int q = 0; q < activeBoats[i].shipPos().length; q++){
                                newMap[activeBoats[i].otherCoord()][activeBoats[i].shipPos()[q]] = -2;
                            }

                            //Marks the ship as dead in the array, a 1 means that it was a valid coordinate (hit or miss)
                            return 1;
                        }
                    }
                }else if(activeBoats[i].isHorizontal() == false && activeBoats[i].otherCoord() == xCoord){
                    //Same logic for vertical boats, checking each position until a match is found
                    for(int j = 0; j < activeBoats[i].shipPos().length; j++){
                        if(activeBoats[i].shipPos()[j] == yCoord){
                            System.out.println("(" + xCoord + ", " + yCoord + ")" + " was a hit!");
                            if(newMap[yCoord][xCoord] == 1){
                                System.out.println("A Submarine was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 2){
                                System.out.println("A Dinghy was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 3){
                                System.out.println("A Cruiser was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 4){
                                System.out.println("A Battleship was Sunk!");
                            }else if(newMap[yCoord][xCoord] == 5){
                                System.out.println("An Aircraft Carrier was Sunk!");
                            }
                            //When a match is eventually found, records the coordinates and shows the user

                            successfulHits++;
                            for(int q = 0; q < activeBoats[i].shipPos().length; q++){
                                newMap[activeBoats[i].shipPos()[q]][activeBoats[i].otherCoord()] = -2;
                            }
                            //Sets each part of the map to -2 to show that ship was hit

                            return 1;
                            //Marks the ship as dead and returns to the main class
                        }
                    }
                }
            }
        }else if(newMap[yCoord][xCoord] == -1 || newMap[yCoord][xCoord] == -2){
            //If you've already hit the ship or if you've already hit the coordinate, return -1, which means it was an invalid coordinate
            return -1;
        }

        //If there's any errors, it'll return -2
        return -2;
    }

    public void showMap(){
        //The method for showing the map, with boats and such

        System.out.print("      ");
        for(int i = 0; i < newMap.length; i++){
            if(i <= 9){
                System.out.print("|" + (i) + "| ");
            }else if(i > 9){
                System.out.print("|" + (i) + "|");
            }
        }
        //Shows the top axis, with the numbering and lines in between
        //For numbers greater than 9, since they're 2 digits and take up more space, there's different spacing

        System.out.print("\n ");

        //Prints out the map out with the ships, for each new row (e.g j == 0), 
        //then prints how the axis for that first, same logic with double digit numbers as before
        //Then afterwards, if the integer value is 0, or nothing there, then prints out a dash
        //If the integer value is -1, or a miss, print out X
        //If the integer value is -2, or a hit, print out H
        //Other than that, print out the exact values of each ship
        for(int i = 0; i < newMap.length; i++){
            for(int j = 0; j < newMap.length; j++){
                if(j == 0 && i < 10){
                    System.out.print("| " + (i) + "|  ");
                }else if(j == 0 && i >= 10){
                    System.out.print("|" + (i) + "|  ");
                }

                if(newMap[i][j] == 0){
                    System.out.print("-   " );
                }else if(newMap[i][j] == -1){
                    System.out.print("X   ");
                }else if(newMap[i][j] == -2){
                    System.out.print("H   ");
                }else{
                    System.out.print(newMap[i][j] + "   ");
                }
            }

            System.out.print("\n ");
        }
    }

    public void showOpponentMap(){
        //This is the method for showing the map, hidden of the ships
        //Logic follows mostly the same, just without the ship values being shown

        System.out.print("      ");
        for(int i = 0; i < newMap.length; i++){
            if(i <= 9){
                System.out.print("|" + (i) + "| ");
            }else if(i > 9){
                System.out.print("|" + (i) + "|");
            }
        }
        //Shows the axis on the top of the map

        System.out.print("\n ");

        //Prints out if something was a hit, miss, or dash, but just doesn't show the ship value there
        for(int i = 0; i < newMap.length; i++){
            for(int j = 0; j < newMap.length; j++){
                if(j == 0 && i < 10){
                    System.out.print("| " + (i) + "|  ");
                }else if(j == 0 && i >= 10){
                    System.out.print("|" + (i) + "|  ");
                }

                if(newMap[i][j] == -1){
                    System.out.print("X   ");
                }else if(newMap[i][j] == -2){
                    System.out.print("H   ");
                }else{
                    System.out.print("-   " );
                }
            }
            System.out.print("\n ");
        }
    }

    public int[][] newMap(){
        return newMap;
        //Getter methods, as the main class needs to grab the newMap sometimes
    }

    public int successfulHits(){
        return successfulHits;
        //Getter method, successfulHits is needed for the end of the game
    }
}
