//Name: V.Susanto
//Start to End Date: May 24th - May 30th
//Assignment: BattleShip
//Purpose: This class creates the individual ships which will appear multiple times on each map on battleship 

public class Ships {
    
    private String shipType;
    private int shipValue;
    private int[] shipPos;
    private int otherCoord;
    private boolean isHorizontal;
    //shipType specifies what kind of ship it is and length
    //shipValue shows the ship in integer form within the map array
    //shipPos records the position of each ship on it's axis (e.g the x-coordinates when it's horizontal and vice versa)
    //otherCoord records the singular y/x coordinate that's needed
    //The booleans show if the ship has been hit already or if it's horizontal
    
    public Ships(String shipNew, boolean isHor){
        //Constructor method, initializes the shiptype, if it's horizontal, value, and positions
        shipType = shipNew;
        isHorizontal = isHor;

        if(shipType.equals("A")){
            shipValue = 5;
        }else if(shipType.equals("B")){
            shipValue = 4;
        }else if(shipType.equals("C")){
            shipValue = 3;
        }else if(shipType.equals("D")){
            shipValue = 2;
        }else if(shipType.equals("S")){
            shipValue = 1;
        }
        //Initializes the shipvalue based on what the user enters

        shipPos = new int[shipValue];
    }

    public void insertShip(int currentPos, int count){
        //Setter method, records the position/coordinates of the ship
        shipPos[count] = currentPos;
    }

    public void insertotherCoord(int insertCoord){
        //Setter method, records the other coordinate for the other axis of the ship
        otherCoord = insertCoord;
    }

    public int otherCoord(){
        //Getter method, returns the other coordinate for the map to check
        return otherCoord;
    }

    public int shipValue(){
        //Getter method, returns the value of the ship
        return shipValue;
    }

    public int[] shipPos(){
        //Getter method, returns the array with the ship's coordinates 
        return shipPos;
    }

    public boolean isHorizontal(){
        //Getter method, sees if the ship is horizontal or not
        return isHorizontal;
    }

    public String shipType(){
        //Getter method, sees what kind of ship it is
        return shipType;
    }
}
